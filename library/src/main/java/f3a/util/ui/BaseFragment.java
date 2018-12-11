package f3a.util.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f3a.f3autil.BuildConfig;
import com.f3a.f3autil.R;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import f3a.util.text.TextUtil;

/**
 * author: zhengbo
 * date: 2017-11-15.
 */
@SuppressWarnings({"unchecked", "unused", "ConstantConditions", "SameParameterValue"})
public abstract class BaseFragment extends Fragment {
    
    protected Activity mContext;
    
    protected LayoutInflater inflater;
    
    protected Unbinder unbinder = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        inflater = getActivity().getLayoutInflater();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            EventBus.builder().throwSubscriberException(BuildConfig.DEBUG).build();
        }
        return view;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initStatusBar();
        initData();
        initData(view, savedInstanceState);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        inflater = null;
        System.gc();
    }
    
    protected abstract @LayoutRes int getContentViewId();
    
    protected void initData() {
    
    }
    
    protected void initData(View view, Bundle savedInstanceState) {
    
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgReceived(String msg) {
    }
    
    protected <T extends View> T findViewById(@IdRes int id) {
        return getView().findViewById(id);
    }
    
    
    
    ////////////////////////////// 沉浸式状态栏 //////////////////////////////
    
    /**
     * 初始化顶部View的上边留白，防止和系统状态栏重合
     */
    protected void initStatusBar() {
        StatusBarUtil.setTransparentForImageViewInFragment(mContext,
                getView().findViewById(R.id.toolbar));
    }

    
    
    ////////////////////// Resources ///////////////////////

    protected int getDimensionById(int dimensionId) {
        return getResources().getDimensionPixelSize(dimensionId);
    }
    
    protected int getColorById(int colorId) {
        return getResources().getColor(colorId);
    }
    
    protected ColorStateList getColorStateListById(int colorId) {
        return getResources().getColorStateList(colorId);
    }

    public static int getColorById(Context context, int colorId) {
        return context.getResources().getColor(colorId);
    }

    public static Drawable getDrawableById(Context context, int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }

    protected Drawable getDrawableById(int drawableId) {
        return getDrawableById(mContext, drawableId);
    }
    
    public String wrapNone(String text) {
        return TextUtil.wrapNone(text);
    }
    
    
    
    ////////////////////// Child Fragment ///////////////////////
    
    /**
     * add child fragment
     */
    protected void addChildFragment(@IdRes int containerId, Fragment newFragment) {
        getChildFragmentManager()
                .beginTransaction()
                .add(containerId, newFragment)
                .commitNow();
    }
    
    
    
    ////////////////////// Fragment Arguments ///////////////////////
    
    public <T extends BaseFragment> T put(String key, boolean value) {
        Bundle bundle = getArguments() == null ? new Bundle() : getArguments();
        bundle.putBoolean(key, value);
        setArguments(bundle);
        return (T) this;
    }
    
    public <T extends BaseFragment> T put(String key, int value) {
        Bundle bundle = getArguments() == null ? new Bundle() : getArguments();
        bundle.putInt(key, value);
        setArguments(bundle);
        return (T) this;
    }
    
    public <T extends BaseFragment> T put(String key, long value) {
        Bundle bundle = getArguments() == null ? new Bundle() : getArguments();
        bundle.putLong(key, value);
        setArguments(bundle);
        return (T) this;
    }
    
    public <T extends BaseFragment> T put(String key, float value) {
        Bundle bundle = getArguments() == null ? new Bundle() : getArguments();
        bundle.putFloat(key, value);
        setArguments(bundle);
        return (T) this;
    }
    
    public <T extends BaseFragment> T put(String key, double value) {
        Bundle bundle = getArguments() == null ? new Bundle() : getArguments();
        bundle.putDouble(key, value);
        setArguments(bundle);
        return (T) this;
    }
    
    public <T extends BaseFragment> T put(String key, String value) {
        Bundle bundle = getArguments() == null ? new Bundle() : getArguments();
        bundle.putString(key, value);
        setArguments(bundle);
        return (T) this;
    }
    
    public <T extends BaseFragment> T put(String key, Serializable value) {
        Bundle bundle = getArguments() == null ? new Bundle() : getArguments();
        bundle.putSerializable(key, value);
        setArguments(bundle);
        return (T) this;
    }
    
    public <T extends BaseFragment> T put(String key, ArrayList<String> value) {
        Bundle bundle = getArguments() == null ? new Bundle() : getArguments();
        bundle.putStringArrayList(key, value);
        setArguments(bundle);
        return (T) this;
    }
    
    public <T extends BaseFragment> T putIntegerArrayList(String key, ArrayList<Integer> value) {
        Bundle bundle = getArguments() == null ? new Bundle() : getArguments();
        bundle.putIntegerArrayList(key, value);
        setArguments(bundle);
        return (T) this;
    }
    
    
    
    ////////////////////// 线程 ///////////////////////
    
    protected void runOnUiThread(Runnable action) {
        getActivity().runOnUiThread(action);
    }
}
