package f3a.util.storage;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 操作文件工具类
 *
 * @author Bob Ackles
 *
 */
public class FileUtil {
    
    private static final String TAG = FileUtil.class.getSimpleName();
    
    ////////////////////////////// Android文件 //////////////////////////////
    
    public static File getImageFileByUri(Context context, Uri uri) {
        String path = uri.getPath();
        File file = new File(path);
        if (file.length() == 0L) {
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = new CursorLoader(context, uri, proj, null,
                    null, null).loadInBackground();
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
                return new File(path);
            }
            cursor.close();
        } else {
            return file;
        }
        return null;
    }
    
    ////////////////////////////// 文件/文件夹 //////////////////////////////
    
    public static boolean isFileExists(String path) {
        if (path == null || path.length() == 0) {
            return false;
        }
        File file = new File(path);
        return file.exists();
    }
    
    public static boolean createDirsIfNotExists(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        File dir = new File(path);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return false;
    }
    
    /**
     * 新建文件（即使已存在）
     * @param path 路径
     * @return {@code true} if succeed, {@code false} otherwise
     */
    public static File createFile(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        try {
            File file = new File(path);
            boolean succeed = file.createNewFile();
            return succeed ? file : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 新建文件（当不存在时）
     * @param path 路径
     * @return {@code true} if succeed, {@code false} otherwise
     */
    public static File createFileIfNotExists(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    
    /**
     * Copy File
     *
     * @param srcPath Source path
     * @param dstPath Destination path
     * @return {@code true} if succeed, {@code false} otherwise
     */
    public static boolean copyFile(String srcPath, String dstPath) {
        if (srcPath == null || srcPath.isEmpty() || dstPath == null || dstPath.isEmpty()) {
            return false;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        File oldFile = new File(srcPath);
        if (!oldFile.exists()) {
            Log.e(TAG, "Source file not exists.");
            return false;
        }
        try {
            int bytesRead;
            fis = new FileInputStream(srcPath);
            fos = new FileOutputStream(dstPath);
            byte[] buffer = new byte[1444];
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.flush();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Copy file failed.");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Copy Directory
     *
     * @param srcPath Source path
     * @param dstPath Destination path
     * @return {@code true} if succeed, {@code false} otherwise
     */
    public static boolean copyDir(String srcPath, String dstPath) {
        if (srcPath == null || srcPath.isEmpty() || dstPath == null || dstPath.isEmpty()) {
            return false;
        }
        
        srcPath = addFileSeparator2Path(srcPath);
        dstPath = addFileSeparator2Path(dstPath);
        
        File srcDir = new File(srcPath);
        if (!srcDir.exists() || srcDir.isFile()) {
            return false;
        }
        
        createDirsIfNotExists(dstPath);
        
        File[] files = srcDir.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                String filename = files[i].getName();
                String currSrcPath = srcPath + filename;
                String currDstPath = dstPath + filename;
                // If current is File
                if (files[i].isFile()) {
                    copyFile(currSrcPath, currDstPath);
//					if (!isFileExists(dstPath + filename)) {
//						return false;
//					}
                }
                // If current is Directory
                else if (files[i].isDirectory()) {
                    copyDir(currSrcPath, currDstPath);
                }
            }
        }
        return true;
    }
    
    /**
     * Delete <b>File</b> or <b>Directory</b> and its subs
     *
     * @param path Destination path
     * @return {@code true} if succeed
     */
    public static boolean deleteFileOrDir(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        File root = new File(path);
        if (root.isDirectory()) {
            path = addFileSeparator2Path(path);
            String[] filenames = root.list();
            if (filenames != null && filenames.length > 0) {
                for (int i = 0; i < filenames.length; i++) {
                    String currPath = path + filenames[i];
                    if (!deleteFileOrDir(currPath)) {
                        Log.e(TAG, "Delete file failed. Current path is " + currPath);
                        return false;
                    }
                }
            }
        }
        return root.delete();
    }
    
    /**
     * 移动文件
     * @param srcPath 源路径
     * @param dstPath 目标路径
     * @return {@code true} if succeed, {@code false} otherwise
     */
    public static boolean moveFile(String srcPath, String dstPath) {
        return copyFile(srcPath, dstPath) && deleteFileOrDir(srcPath);
    }
    
    public static boolean saveFile2Path(byte[] data, String path) {
        if (data == null || path == null || path.isEmpty()) {
            return false;
        }
        createDirsIfNotExists(path.substring(0, path.lastIndexOf(File.separator)));
        BufferedOutputStream bos = null;
        try {
            File file = new File(path);
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(data);
            bos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    
    /**
     * Clear all subs of the dir
     *
     * @param path dir's path
     * @return {@code true} if succeed, {@code false} otherwise
     */
    public static boolean clearDir(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        File root = new File(path);
        if (!root.exists()) {
            return true;
        }
        if (!root.isDirectory()) {
            Log.e(TAG, "Path(" + path + ") is not a directory");
            return false;
        }
        
        path = addFileSeparator2Path(path);
        
        String[] filenames = root.list();
        if (filenames != null && filenames.length > 0) {
            for (int i = 0; i < filenames.length; i++) {
                if (!deleteFileOrDir(path + filenames[i])) {
                    return false;
                }
            }
        }
        return true;
    }
    
    ////////////////////////////// 文件名/路径 //////////////////////////////
    
    /**
     * @param path 文件路径
     * @return filename
     */
    public static String getFilename(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        if (path.contains("\\")) {
            return path.substring(path.lastIndexOf("\\") + 1);
        }
        return path.substring(path.lastIndexOf("/") + 1);
    }
    
    /**
     * 文件重命名
     * @param srcPath 源文件路径
     * @param dstName 新文件名称
     * @return 是否成功
     */
    public static boolean rename(String srcPath, String dstName) {
        if (srcPath == null || srcPath.isEmpty()) {
            return false;
        }
        File file = new File(srcPath);
        if (!file.exists()) {
            return false;
        }
        String dstPath = srcPath.substring(0, srcPath.lastIndexOf(File.separator) + 1) + dstName;
        return file.renameTo(new File(dstPath));
    }
    
    public static String addFileSeparator2Path(String path) {
        if (!path.substring(path.length() - 1).equals(File.separator)) {
            path += File.separator;
        }
        return path;
    }
    
    ////////////////////////////// 文件内容 //////////////////////////////
    
    /**
     * 读取文件内容
     * @param file 源文件
     * @return 文件文字内容
     */
    public static String getFileContentText(File file) {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
                content.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return content.toString();
    }
}
