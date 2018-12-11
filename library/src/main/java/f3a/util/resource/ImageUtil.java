package f3a.util.resource;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * 位图工具类
 * 
 * @author Bob Ackles
 * 
 */
@SuppressWarnings({ "deprecation", "unused" })
public class ImageUtil {

	private static final String TAG = ImageUtil.class.getSimpleName();
	
	
	
	////////////////////// Bitmap ///////////////////////

	/**
	 * @return 根据资源id读取图片
	 */
	public static Bitmap getBitmapByResId(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
	}

	public static Bitmap getBitmapByResIdInSampleSize(Context context, int resId,
			int inSampleSize) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.RGB_565;
		opt.inDither = true;
		opt.inPurgeable = true;
		opt.inInputShareable = false;
		opt.inSampleSize = inSampleSize;
        return BitmapFactory.decodeResource(context.getResources(), resId, opt);
	}

	/**
	 * @param data 原数据
	 * @return 根据字节数组读取图片
	 */
	public static Bitmap getBitmapByBytes(byte[] data) {
		return data == null ? null : BitmapFactory.decodeByteArray(data, 0, data.length);
	}

	/**
	 * @param path 图像的路径
	 * @return 图片
	 */
	public static Bitmap getBitmapByPath(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		File dir = new File(path);
		if (!dir.exists()) {
			return null;
		}
		return BitmapFactory.decodeFile(path);
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param path 图像的路径
	 * @param width 指定输出图像的宽度
	 * @return 生成的缩略图
	 */
	public static Bitmap getBitmapByPath(String path, int width) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int w = options.outWidth;
		int h = options.outHeight;
		if (w == 0 || h == 0) {
			return null;
		}
		if (width <= 0) {
			width = w;
		}
		int height = width*h/w;
		if (w <= width) {
			return BitmapFactory.decodeFile(path, options);
		}
		int beWidth = w / width;
		int beHeight = h / height;
		int be;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ImageUtil.adjustWidth(bitmap, width);
		return bitmap;
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param path 图像的路径
	 * @param width 指定输出图像的宽度
	 * @param height 指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static Bitmap getBitmapByPath(String path, int width, int height) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param inputStream 图像的路径
	 * @param width 指定输出图像的宽度
	 * @return 生成的缩略图
	 */
	public static Bitmap getBitmapByStream(InputStream inputStream, int width) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		BitmapFactory.decodeStream(inputStream, null, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int w = options.outWidth;
		int h = options.outHeight;
		if (w == 0 || h == 0) {
			return null;
		}
		if (width <= 0) {
			width = w;
		}
		int height = width*h/w;
		if (w <= width) {
			return BitmapFactory.decodeStream(inputStream, null, options);
		}
		int beWidth = w / width;
		int beHeight = h / height;
		int be;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ImageUtil.adjustWidth(bitmap, width);
		return bitmap;
	}
	
	public static String getImagePathByUri(ContentResolver cr, Uri uri) {
		String path = null;
		Cursor cursor = MediaStore.Images.Media.query(cr, uri, new String[]{MediaStore.Images.Media.DATA});
		if (cursor.moveToNext()) {
			path = cursor.getString(0);
		}
		cursor.close();
		return path;
	}

	/**
	 * 旋转图片
	 * 
	 * @param src 原始图片
	 * @param degrees 旋转角度，沿x轴顺时针旋转，小于0逆时针旋转
	 * @return bitmap 旋转后的图片
	 */
	public static Bitmap rotate(Bitmap src, float degrees) {
		Bitmap bmp = null;
		if (src != null) {
			Matrix matrix = new Matrix();
			matrix.setRotate(degrees);
			try {
				bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(),
						src.getHeight(), matrix, true);
				src.recycle();
			} catch (OutOfMemoryError e) {
				Log.i(TAG, e.toString());
			}
		}
		return bmp;
	}

	/**
	 * 缩放图片
	 * 
	 */
	public static Bitmap scale(Bitmap src, float xFactor, float yFactor) {
		Bitmap bmp = null;
		if (src != null) {
			Matrix matrix = new Matrix();
			matrix.setScale(xFactor, yFactor);
			try {
				bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(),
						src.getHeight(), matrix, true);
			} catch (OutOfMemoryError e) {
				Log.i(TAG, e.toString());
			}
		}
		return bmp;
	}
	
	public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		float scaleWidth = ((float) w) / width;
		float scaleHeight = ((float) h) / height;
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap   
		// matrix.postRotate(45);   
		return Bitmap.createBitmap(bitmap, 0, 0, width,
				height, matrix, true);
	}
	
	public static Bitmap scale(Bitmap src, int width, int height) {
		Bitmap bmp = null;
		if (src != null) {
			try {
				bmp = Bitmap.createScaledBitmap(src, width, height, false);
			} catch (OutOfMemoryError e) {
				Log.i(TAG, e.toString());
			}
		}
		return bmp;
	}

//	public static Bitmap blur(Context context, Bitmap bitmap, float radius) {
//		if (radius <= 0 || radius > 25) {
//			return bitmap;
//		}
//
//		// 用需要创建高斯模糊bitmap创建一个空的bitmap
//		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),
//				bitmap.getHeight(), Config.ARGB_8888);
//
//		// 初始化Renderscript，这个类提供了RenderScript
//		// context，在创建其他RS类之前必须要先创建这个类，他控制RenderScript的初始化，资源管理，释放
//		RenderScript rs = RenderScript.create(context);
//
//		// 创建高斯模糊对象
//		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs,
//				Element.U8_4(rs));
//
//		// 创建Allocations，此类是将数据传递给RenderScript内核的主要方法，并制定一个后备类型存储给定类型
//		Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
//		Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
//
//		// 设定模糊度
//		blurScript.setRadius(radius);
//
//		// Perform the Renderscript
//		blurScript.setInput(allIn);
//		blurScript.forEach(allOut);
//
//		// Copy the final bitmap created by the out Allocation to the outBitmap
//		allOut.copyTo(outBitmap);
//
//		// recycle the original bitmap
//		bitmap.recycle();
//
//		// After finishing everything, we destroy the Renderscript.
//		rs.destroy();
//		blurScript.destroy();
//
//		return outBitmap;
//	}

	/**
	 * 柔化效果(高斯模糊)
	 * 
	 */
	public static Bitmap blurImageAmeliorate(Bitmap src) {
		long start = System.currentTimeMillis();
		// 高斯矩阵
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };

		int width = src.getWidth();
		int height = src.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.RGB_565);

		int pixR;
		int pixG;
		int pixB;

		int pixColor;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int delta = 16; // 值越小图片会越亮，越大则越暗

		int idx;
		int[] pixels = new int[width * height];
		src.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + pixR * gauss[idx];
						newG = newG + pixG * gauss[idx];
						newB = newB + pixB * gauss[idx];
						idx++;
					}
				}

				newR /= delta;
				newG /= delta;
				newB /= delta;

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("may", "used time=" + (end - start));
		return bitmap;
	}

	/**
	 * 回收bitmap
	 * 
	 */
	public static void recycle(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}

	/**
	 * 获取图片的字节数
	 * 
	 */
	public static int getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null) {
			return 0;
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	public static Bitmap drawable2Bitmap(Drawable src) {
//		if (src instanceof BitmapDrawable) {
//			return ((BitmapDrawable)src).getBitmap();
//		}
		int width = src.getIntrinsicWidth();
		int height = src.getIntrinsicHeight();
		src.setBounds(0, 0, width, height);
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		src.draw(canvas);
		canvas.setBitmap(null);
		return bitmap;
	}

	public static Drawable bitmap2Drawable(Bitmap src) {
		return new BitmapDrawable(src);
	}
	
	public static byte[] bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
	
	public static Bitmap adjustWidth(Bitmap src, int dstWidth) {
		if (src.getWidth() == dstWidth) {
			return src;
		}
		return Bitmap.createScaledBitmap(src, dstWidth, dstWidth*src.getHeight()/src.getWidth(), false);
	}
	
	public static Bitmap adjustHeight(Bitmap src, int dstHeight) {
		if (src.getHeight() == dstHeight) {
			return src;
		}
		return Bitmap.createScaledBitmap(src, 
				dstHeight*src.getWidth()/src.getHeight(), dstHeight, false);
	}
	
	public static Bitmap clipSquareBitmap(Bitmap src) {
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		if (srcWidth == srcHeight) {
			return src;
		}
		if (srcWidth > srcHeight) {
			return Bitmap.createBitmap(src, (srcWidth - srcHeight)/2, 0, srcHeight, srcHeight);
		} else {
			return Bitmap.createBitmap(src, 0, (srcHeight - srcWidth)/2, srcWidth, srcWidth);
		}
	}
	
	public static Bitmap clipSquareBitmap(Bitmap src, int dstLength) {
		return Bitmap.createScaledBitmap(clipSquareBitmap(src), dstLength, dstLength, false);
	}
	
	public static Bitmap clipSquareBitmap(Bitmap src, int width, int height) {
		int imageWidth = src.getWidth();
		int imageHeight = src.getHeight();
		if (imageWidth == width && imageHeight == height) {
			return src;
		}
		if (1f*imageHeight/imageWidth == 1f*height/width) {
			return adjustWidth(src, width);
		}
		if (1f*imageHeight/imageWidth > 1f*height/width) {
			src = adjustWidth(src, width);
			return Bitmap.createBitmap(src, 0, (src.getHeight() - height)/2, width, height);
		}
		src = adjustHeight(src, height);
		return Bitmap.createBitmap(src, (src.getWidth() - width)/2, 0, width, height);
	}

	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public static Bitmap clipRoundBitmap(Bitmap src, int x, int y, int diameter) {
		if (x < 0 || x + diameter > src.getWidth() || y < 0
				|| y + diameter > src.getHeight()) {
			throw new IllegalArgumentException("circle out of bitmap bounds");
		}
        Paint paint = new Paint();
        paint.setAntiAlias(true);
		Bitmap dest = Bitmap.createBitmap(diameter, diameter, Config.ARGB_8888);
		Canvas canvas = new Canvas(dest);
		Path path = new Path();
		path.addCircle(diameter / 2, diameter / 2, diameter / 2, Direction.CW);
		canvas.clipPath(path);
		canvas.drawBitmap(src, -x, -y, paint);
		canvas.setBitmap(null);
		return dest;
	}
    
    public static Bitmap createCircleImage(Bitmap source, int diameter) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(diameter, diameter, Config.ARGB_8888);
        // 产生一个同样大小的画布
        Canvas canvas = new Canvas(target);
        // 首先绘制圆形
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
        // 使用SRC_IN
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // 绘制图片
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
    
    public static Bitmap clipRoundRectBitmap(Bitmap bitmap, float radius) {
//		try {
//			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
//					bitmap.getHeight(), Config.ARGB_8888);
//			
//			Paint paint = new Paint();
//			paint.setAntiAlias(true);
//			paint.setColor(Color.WHITE);
//
//			Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//			RectF rectF = new RectF(src);
//			
//			Canvas canvas = new Canvas(output);
//			canvas.drawARGB(0, 0, 0, 0);
//			canvas.drawRoundRect(rectF, radius, radius, paint);
//			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//
//			Rect rect = new Rect(src);
//			canvas.drawBitmap(bitmap, src, rect, paint);
//			return output;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return bitmap;
//		}
		BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(bitmapShader);
		
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), radius, radius, paint);
		canvas.setBitmap(null);
		return output;
	}
	
	public static Bitmap view2Bitmap(View view) {
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				Config.ARGB_8888);
		// 利用bitmap生成画布
		Canvas canvas = new Canvas(bitmap);

		// 把view中的内容绘制在画布上
		view.draw(canvas);

		return bitmap;
	}

	public static Bitmap compressImage(Bitmap image, Bitmap.CompressFormat format, long maxImageSize) {
		if (getSizeInBytes(image) <= maxImageSize) {
			return image;
		}
		int quality = 100;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		image.compress(format == null ? Bitmap.CompressFormat.JPEG : format, quality, baos);
		// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
		Log.i(TAG, "compressImage() before compress image size = " + baos.toByteArray().length + " bytes");
		int count = 0;
		while (baos.toByteArray().length > maxImageSize) {
			quality -= 10;
			// 重置baos即清空baos
			baos.reset();
			// 这里压缩options%，把压缩后的数据存放到baos中
			image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			count++;
			Log.i(TAG, "compressImage() compress time " + count + " image size = " + baos.toByteArray().length + " bytes");
			// 最多压缩5次
			if (count > 5) {
				break;
			}
		}
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		// 把ByteArrayInputStream数据生成图片
		return BitmapFactory.decodeStream(isBm, null, null);
	}
	
	
	
	////////////////////// ImageView ///////////////////////

	/**
	 * 回收ImageView
	 * 
	 */
	public static void recycleImageView(ImageView img) {
		Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}
	
	
	
	////////////////////// Drawable ///////////////////////
	
	/**
	 * 该方法能改变图片颜色，但是只对纯白色图片有效
	 */
	public static Drawable setDrawableColor(Drawable drawable, int color) {
		drawable.setColorFilter(color, Mode.MULTIPLY);
		return drawable;
	}
}
