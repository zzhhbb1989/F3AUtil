package f3a.util.widget.NineGridView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import f3a.util.manager.RecyclerContainer;

/**
 * Not finished yet..
 * @author Bob
 * @since 2018-09-03
 */
public class NineGridView extends ViewGroup {
    
    int spanCount = 3;
    NGVLoader mLoader;
    List<ImageView> imageViewList = new ArrayList<>();
    List<String> urlList = new ArrayList<>();
    int imageCount;
    int hGap, vGap;
    int imageLength;
    int[] singleImageSize = new int[2];
    
    public NineGridView(Context context) {
        this(context, null);
    }
    
    public NineGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public NineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (imageCount == 1) {
        
        }
    }
    
    public void setLoader(NGVLoader loader) {
        mLoader = loader;
    }
    
    public void setDataList(List<String> urls) {
        checkLoader();
        urlList.clear();
        if (urls != null) {
            urlList.addAll(urls);
        }
        imageCount = urlList.size();
        if (imageCount == 1) {
            singleImageSize = mLoader.loadSingleImageSize();
        } else if (imageCount > 1) {
            hGap = mLoader.loadGap(getContext(), true, imageCount);
            vGap = mLoader.loadGap(getContext(), false, imageCount);
            imageLength = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - (spanCount - 1)*hGap)/spanCount;
        }
        for (int i = 0; i < imageCount; i++) {
            ImageView imageView = getImageView(i);
            if (imageView.getParent() == null) {
                addView(imageView);
            }
        }
        for (int i = imageCount; i < imageViewList.size(); i++) {
            removeView(imageViewList.get(i));
        }
        postInvalidate();
    }
    
    private ImageView getImageView(int position) {
        if (position < imageViewList.size()) {
            return mLoader.loadImageView(getContext(), imageViewList.get(position), imageCount);
        } else {
            for (int i = imageViewList.size(); i <= position; i++) {
                imageViewList.add(mLoader.loadImageView(getContext(), null, imageCount));
            }
            return imageViewList.get(position);
        }
    }
    
    private void checkLoader() {
        if (mLoader == null) {
            throw new IllegalStateException("U must initiate NGVLoader first!");
        }
    }
}
