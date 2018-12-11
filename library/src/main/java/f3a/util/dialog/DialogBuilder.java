package f3a.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.WindowManager;

import com.f3a.f3autil.R;

import f3a.util.resource.DimenUtil;

/**
 * 基础对话框，需要传入自定义的View实现具体界面
 *
 * @author Bob
 * @since 2018-08-21
 */
@SuppressWarnings({"ConstantConditions", "unchecked"})
public class DialogBuilder {
    
    Context mContext;
    Dialog dialog;
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    View customView;
    @LayoutRes int customViewId = 0;
    
    public DialogBuilder(@NonNull Context context) {
        this(context, R.style.dialog_base);
    }
    
    public DialogBuilder(@NonNull Context context, int themeResId) {
        mContext = context;
        dialog = new Dialog(context, themeResId);
        cancelable(true).canceledOnTouchOutside(true).width(0.8F).height(0);
    }
    
    public <T extends DialogBuilder> T contentView(View view) {
        customView = view;
        return (T) this;
    }
    
    public <T extends DialogBuilder> T contentView(@LayoutRes int id) {
        customViewId = id;
        return (T) this;
    }
    
    public <T extends DialogBuilder> T width(float width) {
        if (width == 0F) {
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        } else if (width > 0) {
            if (width > 1F) {
                lp.width = (int) width;
            } else {
                lp.width = (int) (DimenUtil.getScreenWidth(mContext) * width);
            }
        } else {
            lp.width = (int) width;
        }
        return (T) this;
    }
    
    public <T extends DialogBuilder> T height(float height) {
        if (height == 0F) {
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else if (height > 0) {
            if (height > 1f) {
                lp.height = (int) height;
            } else {
                lp.height = (int) (DimenUtil.getScreenHeight(mContext) * height);
            }
        } else {
            lp.height = (int) height;
        }
        return (T) this;
    }
    
    public <T extends DialogBuilder> T gravity(int gravity) {
        if (gravity >= 0) {
            lp.gravity = gravity;
        }
        return (T) this;
    }
    
    public <T extends DialogBuilder> T animations(@StyleRes int id) {
        if (id != 0) {
            lp.windowAnimations = id;
        }
        return (T) this;
    }
    
    public <T extends DialogBuilder> T cancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return (T) this;
    }
    
    public <T extends DialogBuilder> T canceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return (T) this;
    }
    
    public <T extends DialogBuilder> T show() {
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        if (lp.width != 0) {
            params.width = lp.width;
        }
        if (lp.height != 0) {
            params.height = lp.height;
        }
        if (lp.gravity != 0) {
            params.gravity = lp.gravity;
        }
        if (lp.windowAnimations != 0) {
            params.windowAnimations = lp.windowAnimations;
        }
        dialog.getWindow().setAttributes(params);
        if (customView != null) {
            dialog.setContentView(customView);
        }
        if (customViewId != 0) {
            dialog.setContentView(customViewId);
        }
        return (T) this;
    }
    
    public boolean isShowing() {
        return dialog.isShowing();
    }
    
    public <T extends DialogBuilder> T dismiss() {
        dialog.dismiss();
        return (T) this;
    }
    
    public <T extends DialogBuilder> T cancel() {
        dialog.cancel();
        return (T) this;
    }
    
    public <T extends DialogBuilder> T onShowListener(DialogInterface.OnShowListener listener) {
        dialog.setOnShowListener(listener);
        return (T) this;
    }
    
    public <T extends DialogBuilder> T onDismissListener(DialogInterface.OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
        return (T) this;
    }
    
    public <T extends DialogBuilder> T onCancelListener(DialogInterface.OnCancelListener listener) {
        dialog.setOnCancelListener(listener);
        return (T) this;
    }
    
    public <T extends DialogBuilder> T onKeyListener(DialogInterface.OnKeyListener listener) {
        dialog.setOnKeyListener(listener);
        return (T) this;
    }
    
    public Dialog getRaw() {
        return dialog;
    }
}
