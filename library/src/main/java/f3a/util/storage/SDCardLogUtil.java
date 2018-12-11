package f3a.util.storage;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import f3a.util.time.TimeUtil;

/**
 * 使用SD卡记录日志工具
 * @author Bob
 * @since 2018-08-09
 */
public class SDCardLogUtil {
    
    public static String BASE_PATH;
    
    public static void init(String rootName) {
        BASE_PATH = Environment.getExternalStorageDirectory().getPath() + "/" + rootName + "/";
    }
    
    public static String getFilePathCrash() {
        return BASE_PATH + "Crash/";
    }
    
    public static String getFilenameHour() {
        return TimeUtil.long2String("yyyy_MM_dd_HH", System.currentTimeMillis()) + ".txt";
    }
    
    public static String getFormattedContent(String content) {
        return TimeUtil.long2String("HH:mm:ss.SSS", System.currentTimeMillis()) + " >>> " + content  + " <<<\n";
    }
    
    /**
     * @param filePath 文件位置
     * @param fileName 文件名称
     * @param content 写入内容
     */
    public static void log(String filePath, String fileName, String content) {
//        System.out.println(filePath);
//        System.out.println(fileName);
//        System.out.println(content);
        final File path = new File(filePath);
        if (!path.exists()) {
            if (!path.mkdirs()) {
                return;
            }
        }
        
        final File saveFile = new File(filePath + fileName);
        if (!saveFile.exists()) {
            try {
                if (!saveFile.createNewFile()) {
                    return;
                }
            } catch (IOException e) {
                return;
            }
        }
        
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(saveFile, "rw");
            raf.seek(saveFile.length());
            raf.write(content.getBytes());
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    public static void formattedLogCrash(String content) {
        log(getFilePathCrash(), getFilenameHour(), getFormattedContent(content));
    }
}
