package f3a.util.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 线性or网格分割线
 *
 * Created by yjwfn on 15-9-14.
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    
    Paint mPaint;
    int mPaddingLeft = 0;
    int mPaddingRight = 0;
    int mLineWidthH = 1;
    int mLineWidthV = 1;
    Rect drawRect = new Rect();
    
    public LinearItemDecoration() {
        this(0);
    }
    
    public LinearItemDecoration(@ColorInt int color) {
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }
    
    /**
     * 设置分割线左边留白，当{@link LinearLayoutManager}时生效
     */
    public LinearItemDecoration paddingLeft(int padding) {
        mPaddingLeft = padding;
        return this;
    }
    
    /**
     * 设置分割线左边留白，当{@link LinearLayoutManager}时生效
     */
    public LinearItemDecoration paddingRight(int padding) {
        mPaddingRight = padding;
        return this;
    }
    
    /**
     * 设置分割线左边留白，当{@link LinearLayoutManager}时生效
     */
    public LinearItemDecoration paddingHorizontal(int padding) {
        mPaddingLeft = mPaddingRight = padding;
        return this;
    }
    
    /**
     * 设置分隔线宽度
     */
    public LinearItemDecoration lineWidth(int px) {
        mLineWidthH = mLineWidthV = px;
        return this;
    }
    
    /**
     * 设置分隔线宽度
     */
    public LinearItemDecoration lineWidthH(int px) {
        mLineWidthH = px;
        return this;
    }
    
    /**
     * 设置分隔线宽度
     */
    public LinearItemDecoration lineWidthV(int px) {
        mLineWidthV = px;
        return this;
    }
    
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int lineWidthH = getLineWidthH();
        int lineWidthV = getLineWidthV();
        // 网格，要放在前面，GridLayoutManager继承自LinearLayoutManager...#$&@Q$&#*&*&$*#&*#
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            View childView;
            RecyclerView.LayoutParams layoutParams;
            int childCount = layoutManager.getChildCount();
            int span = layoutManager.getSpanCount();
            int top, left, right, bottom;
            int totalCount = layoutManager.getItemCount();
            
            // item在LayoutManager的位置(当前手机屏幕中可见的位置)
            for (int childLayoutManagerPosition = 0; childLayoutManagerPosition < childCount - 1; childLayoutManagerPosition++) {
                childView = layoutManager.getChildAt(childLayoutManagerPosition);
                // 当前item在Adapter的位置(即所有数据中的位置)
                int childAdapterPosition = parent.getChildAdapterPosition(childView);
                layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
                
                // 横线：绘制当前Item下面的横线，最底部的Item不绘制
                if (childAdapterPosition < span * ((totalCount + span - 1)/span - 1)) {
                    left = childView.getLeft();
                    right = childView.getRight() + (childAdapterPosition%span < span - 1 ? lineWidthH : 0);
                    top = childView.getBottom() + layoutParams.bottomMargin;
                    bottom = top + lineWidthH;
                    drawRect.set(left, top, right, bottom);
                    c.drawRect(drawRect, mPaint);
                }
                
                // 竖线：绘制当前Item右边的竖线，最右边的Item不绘制
                if (childAdapterPosition % span < span - 1) {
                    left = childView.getRight() + layoutParams.rightMargin;
                    right = left + lineWidthV;
                    top = childView.getTop();
                    bottom = childView.getBottom();
                    drawRect.set(left, top, right, bottom);
                    c.drawRect(drawRect, mPaint);
                }
            }
        }
        // 线性
        else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            View childView;
            RecyclerView.LayoutParams layoutParams;
            int childCount = layoutManager.getChildCount();
            int top, left, right, bottom;
            boolean isVertical = ((LinearLayoutManager) layoutManager).getOrientation() == RecyclerView.VERTICAL;
            
            // item在LayoutManager的位置(当前手机屏幕中可见的位置)
            for (int childLayoutManagerPosition = 0; childLayoutManagerPosition < childCount - 1; childLayoutManagerPosition++) {
                childView = layoutManager.getChildAt(childLayoutManagerPosition);
                // 当前item在Adapter的位置(即所有数据中的位置)
                int childAdapterPosition = parent.getChildAdapterPosition(childView);
                if (!isDraw(childAdapterPosition)) {
                    continue;
                }
                if (childAdapterPosition >= parent.getAdapter().getItemCount() - 1) {
                    break;
                }
                layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
                if (isVertical) {// 横线
                    left = childView.getLeft() + getPaddingLeft(childAdapterPosition);
                    right = childView.getRight() - getPaddingRight(childAdapterPosition);
                    top = childView.getBottom() + layoutParams.bottomMargin;
                    bottom = top + lineWidthH;
                } else {// 竖线
                    left = childView.getRight() + layoutParams.rightMargin;
                    right = left + lineWidthV;
                    top = childView.getTop();
                    bottom = childView.getBottom();
                }
                drawRect.set(left, top, right, bottom);
                c.drawRect(drawRect, mPaint);
            }
        }
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        // 网格
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            int span = layoutManager.getSpanCount();
            int totalCount = layoutManager.getItemCount();
            // 横线位置
            if (position < span * ((totalCount + span - 1)/span - 1)) {
                outRect.bottom = getLineWidthH();
            }
            // 竖线位置
            if (position % span < span - 1) {
                outRect.right = getLineWidthV();
            }
        }
        // 线性
        else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            if (position < parent.getAdapter().getItemCount() - 1) {
                if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == RecyclerView.VERTICAL) {// 横线
                    outRect.set(0, 0, 0, getLineWidthH());
                } else {// 竖线
                    outRect.set(0, 0, getLineWidthV(), 0);
                }
            }
        }
    }
    
    /**
     * 得到分隔线(横线)宽度
     */
    private int getLineWidthH() {
        return mLineWidthH;
    }
    
    /**
     * 得到分隔线(竖线)宽度
     */
    private int getLineWidthV() {
        return mLineWidthV;
    }
    
    /**
     * 得到padding left
     */
    public int getPaddingLeft(int position) {
        return mPaddingLeft;
    }
    
    /**
     * 得到padding right
     */
    public int getPaddingRight(int position) {
        return mPaddingRight;
    }
    
    /**
     * 是否绘制当前分割线
     */
    public boolean isDraw(int position) {
        return true;
    }
}
