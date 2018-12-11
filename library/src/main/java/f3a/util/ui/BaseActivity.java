package f3a.util.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.f3a.f3autil.BuildConfig;
import com.f3a.f3autil.R;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import f3a.util.dialog.BaseDialog;
import f3a.util.resource.DimenUtil;
import f3a.util.resource.ImageUtil;
import f3a.util.text.TextUtil;
import f3a.util.thread.ActionLoop;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * author: zhengbo
 * date: 2017-11-15.
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public abstract class BaseActivity extends SupportActivity {
    
    protected BaseActivity mContext;
    
    protected LayoutInflater inflater;
    
    protected Unbinder unbinder;
    
    protected Toolbar toolbar;
    protected TextView tvToolbarTitle;
    
    protected View mRootView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mContext = this;
        inflater = getLayoutInflater();
        unbinder = ButterKnife.bind(this);
        mRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            EventBus.builder().throwSubscriberException(BuildConfig.DEBUG).build();
        }
        initToolbar();
        initStatusBar();
        initData();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        inflater = null;
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (loop != null) {
            loop.cancel();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        System.gc();
    }
    
    protected abstract @LayoutRes int getContentViewId();
    
    protected abstract void initData();
    
    @Subscribe
    public void onMsgReceived(String msg) {
    }
    
    
    
    ////////////////////////////// Toolbar //////////////////////////////
    
    public void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            CharSequence title = getIntent().getStringExtra("title");
            setTitle(title == null ? getTitle() : title);
        }
    }
    
    public Toolbar getToolbar() {
        return toolbar;
    }
    
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (toolbar != null) {
            tvToolbarTitle = toolbar.findViewById(R.id.toolbar_title);
            if (tvToolbarTitle != null) {
                tvToolbarTitle.setText(title);
            } else {
                toolbar.setTitle(title);
            }
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    
    ////////////////////////////// 沉浸式状态栏 //////////////////////////////
    
    /**
     * 初始化顶部View的上边留白，防止和系统状态栏重合
     */
    protected void initStatusBar() {
        StatusBarUtil.setTransparentForImageView(mContext, toolbar);
    }
    
    
    
    ////////////////////////////// 加载对话框 //////////////////////////////
    
    private static final Object PROGRESS_DIALOG_LOCK = new Object();
    private LoadingDialog loadingDialog = null;
    private int loadingDialogShowCount = 0;
    
    public void showProgressDialog(String msg) {
        synchronized (PROGRESS_DIALOG_LOCK) {
            loadingDialogShowCount++;
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(mContext);
            }
            loadingDialog.setMsg(TextUtils.isEmpty(msg) ? "正在加载" : msg);
            if (!loadingDialog.isShowing()) {
                loadingDialog.show();
            }
        }
    }
    
    public void dismissProgressDialog() {
        synchronized (PROGRESS_DIALOG_LOCK) {
            loadingDialogShowCount--;
            if (loadingDialog != null && loadingDialog.isShowing() && loadingDialogShowCount == 0) {
                loadingDialog.dismiss();
            }
        }
    }
    
    class LoadingDialog extends BaseDialog {
        
        TextView tvMsg;
    
        public LoadingDialog(@NonNull Context context) {
            super(context);
            
            View view = inflater.inflate(R.layout.dialog_loading, null);
            tvMsg = view.findViewById(R.id.msg);
            width(0F).cancelable(false).contentView(view);
        }
        
        public LoadingDialog setMsg(String msg) {
            return this;
        }
    }
    
    
    
    ////////////////////// 资源 ///////////////////////
    
    public int getDimensionById(int dimensionId) {
        return getResources().getDimensionPixelSize(dimensionId);
    }
    
    public int getColorById(int colorId) {
        return getResources().getColor(colorId);
    }
    
    public static int getColorById(Context context, int colorId) {
        return context.getResources().getColor(colorId);
    }
    
    public static Drawable getDrawableById(Context context, int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }
    
    public Drawable getDrawableById(int drawableId) {
        return getDrawableById(mContext, drawableId);
    }
    
    protected Drawable getDrawableByIdMultiplyColor(int drawableId, int color) {
        Drawable drawable = getDrawableById(drawableId).mutate();
        return ImageUtil.setDrawableColor(drawable, color);
    }
    
    public String[] getStringArrayById(int arrayId) {
        return mContext.getResources().getStringArray(arrayId);
    }
    
    public String wrapNone(String text) {
        return TextUtil.wrapNone(text);
    }
    
    public int dp2px(int dp) {
        return DimenUtil.dp2px(mContext, dp);
    }
    
    public int sp2px(int sp) {
        return DimenUtil.sp2px(mContext, sp);
    }
    
    
    
    ////////////////////// Fragment ///////////////////////
    
    /**
     * add child fragment
     */
    protected void addChildFragment(@IdRes int containerId, Fragment newFragment) {
        addChildFragment(containerId, newFragment, true);
    }
    
    /**
     * add child fragment
     */
    protected void addChildFragment(@IdRes int containerId, Fragment newFragment, boolean now) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        // 加入新页面
        ft.add(containerId, newFragment);
        // 显示新页面
        ft.show(newFragment);
        // 提交页面改变
        if (now) {
            ft.commitNow();
        } else {
            ft.commit();
        }
    }
    
    
    
    ////////////////////// 验证码 ///////////////////////
    
    ActionLoop loop;
    
    protected void startAuthCodeCountDown(final TextView tvSend) {
        tvSend.setEnabled(false);
        new Thread(loop = new ActionLoop(1000) {
    
            int timeLeft;
            int timeLeftTemp;
    
            @Override
            protected void preAction() {
                timeLeft = 60;
            }
    
            @Override
            protected void action() {
                timeLeftTemp = timeLeft;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvSend.setText(timeLeftTemp <= 0 ? "发送验证码" : String.format("%sS后重新获取", timeLeftTemp));
                        tvSend.setEnabled(timeLeftTemp <= 0);
                    }
                });
                if (timeLeft <= 0) {
                    cancel();
                }
                timeLeft--;
            }
        }).start();
    }
}
