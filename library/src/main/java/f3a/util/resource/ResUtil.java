package f3a.util.resource;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * 获取res文件夹下的各种资源
 *
 * @author zhengbo
 * @since 2018-01-31
 */

public class ResUtil {
    
    private static Resources getRes(Context context) {
        return context.getResources();
    }
    
    @NonNull
    public static String getString(Context context, @StringRes int resId) {
        return getRes(context).getString(resId);
    }
    
    @NonNull
    public static String[] getStringArray(Context context, @ArrayRes int resId) {
        return getRes(context).getStringArray(resId);
    }
    
    public static @ColorInt int getColor(Context context, @ColorRes int resId) {
        return getRes(context).getColor(resId);
    }
    
    public static int getDimen(Context context, @DimenRes int resId) {
        return getRes(context).getDimensionPixelSize(resId);
    }
    
    public static Drawable getDrawable(Context context, @DrawableRes int resId) {
        Drawable drawable = getRes(context).getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }
}
