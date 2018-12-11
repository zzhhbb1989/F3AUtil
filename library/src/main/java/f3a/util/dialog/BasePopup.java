package f3a.util.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import f3a.util.resource.DimenUtil;

/**
 * 基础弹出框，默认界面为列表展示，可选传入自定义的View实现具体界面
 *
 * @author Bob
 * @since 2018-08-27
 */
@SuppressWarnings({"ConstantConditions", "unchecked"})
public class BasePopup {
    
    Context mContext;
    PopupWindow popup;
    View contentView;
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    BaseAdapter adapter;
    List<CharSequence> itemList = new ArrayList<>();
    AdapterView.OnItemClickListener onItemClickListener;
    
    public BasePopup(Context context) {
        mContext = context;
        popup = new PopupWindow(context);
        width(0.5F).height(-2).focusable(true);
    }
    
    public <T extends BasePopup> T width(float width) {
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
    
    public <T extends BasePopup> T height(float height) {
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
    
    public <T extends BasePopup> T contentView(View contentView) {
        this.contentView = contentView;
        return (T) this;
    }
    
    public <T extends BasePopup> T focusable(boolean focusable) {
        popup.setFocusable(focusable);
        return (T) this;
    }
    
    public <T extends BasePopup> T list(List<CharSequence> list) {
        itemList.clear();
        if (list != null) {
            itemList.addAll(list);
        }
        return (T) this;
    }
    
    public <T extends BasePopup> T list(CharSequence... list) {
        itemList.clear();
        if (list != null) {
            Collections.addAll(itemList, list);
        }
        return (T) this;
    }
    
    public <T extends BasePopup> T onDismissListener(PopupWindow.OnDismissListener listener) {
        popup.setOnDismissListener(listener);
        return (T) this;
    }
    
    public <T extends BasePopup> T onItemClickListener(AdapterView.OnItemClickListener listener) {
        onItemClickListener = listener;
        return (T) this;
    }
    
    public <T extends BasePopup> T showAsDropDown(View anchor) {
        show();
        popup.showAsDropDown(anchor);
        return (T) this;
    }
    
    public <T extends BasePopup> T showAsDropDown(View anchor, int x, int y) {
        show();
        popup.showAsDropDown(anchor, x, y);
        return (T) this;
    }
    
    public <T extends BasePopup> T showAtLocation(View parent, int gravity, int x, int y) {
        show();
        popup.showAtLocation(parent, gravity, x, y);
        return (T) this;
    }
    
    private <T extends BasePopup> T show() {
        if (contentView == null) {
            ListView listView = new ListView(mContext);
            listView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
            listView.setAdapter(adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return itemList.size();
                }
                
                @Override
                public Object getItem(int position) {
                    return position;
                }
                
                @Override
                public long getItemId(int position) {
                    return position;
                }
                
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView textView = new TextView(mContext);
                    textView.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
                    int padding = DimenUtil.dp2px(mContext, 12);
                    textView.setPadding(padding, padding, padding, padding);
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(14);
                    textView.setText(itemList.get(position));
                    convertView = textView;
                    return convertView;
                }
            });
            // 回调这样设置可以规避初始化时机的问题
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(parent, view, position, id);
                    }
                }
            });
            contentView = listView;
        }
        popup.setContentView(contentView);
        popup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popup.setWidth(lp.width);
        popup.setHeight(lp.height);
        return (T) this;
    }
    
    public <T extends BasePopup> T dismiss() {
        popup.dismiss();
        return (T) this;
    }
}
