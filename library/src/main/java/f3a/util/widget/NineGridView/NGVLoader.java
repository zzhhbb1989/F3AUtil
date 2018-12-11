package f3a.util.widget.NineGridView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import f3a.util.resource.DimenUtil;

/**
 * 加载器，用于加载各种属性 Not finished yet..
 *
 * @author Bob
 * @since 2018-09-03
 */
public abstract class NGVLoader {
    
    /**
     * 加载图片
     */
    public abstract void loadImage(ImageView imageView, String url);
    
    /**
     * 加载图片控件
     */
    public ImageView loadImageView(Context context, ImageView convertView, int imageCount) {
        if (convertView == null) {
            convertView = new ImageView(context);
        }
        convertView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        convertView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return convertView;
    }
    
    /**
     * 加载图片间距
     */
    public int loadGap(Context context, boolean isHorizontal, int imageCount) {
        return (int) (context.getResources().getDisplayMetrics().density*2);
    }
    
    public int[] loadSingleImageSize() {
        return new int[]{-2, -2};
    }
}
