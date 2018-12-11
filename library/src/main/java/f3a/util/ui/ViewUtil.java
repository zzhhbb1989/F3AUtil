package f3a.util.ui;

import android.view.View;

/**
 * @author Bob
 * @since 2018-08-01
 */
public class ViewUtil {
    
    public static void setPadding(View view, int padding) {
        view.setPadding(padding, padding, padding, padding);
    }
    
    public static void setPaddingHorizontal(View view, int paddingHorizontal) {
        view.setPadding(paddingHorizontal, view.getPaddingTop(), paddingHorizontal, view.getPaddingBottom());
    }
    
    public static void setPaddingVertical(View view, int paddingVertical) {
        view.setPadding(view.getPaddingLeft(), paddingVertical, view.getPaddingRight(), paddingVertical);
    }
    
    public static void setPaddingLeft(View view, int px) {
        view.setPadding(px, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }
    
    public static void setPaddingRight(View view, int px) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), px, view.getPaddingBottom());
    }
    
    public static void setPaddingTop(View view, int px) {
        view.setPadding(view.getPaddingLeft(), px, view.getPaddingRight(), view.getPaddingBottom());
    }
    
    public static void setPaddingBottom(View view, int px) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), px);
    }
}
