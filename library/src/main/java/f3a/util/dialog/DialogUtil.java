package f3a.util.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.f3a.f3autil.R;

import java.util.ArrayList;
import java.util.List;

import f3a.util.model.TagInfo;
import wheelpicker.entity.City;
import wheelpicker.entity.County;
import wheelpicker.entity.Province;
import wheelpicker.picker.OptionPicker;
import wheelpicker.picker.TripleLinkagePicker;


/**
 * 对话框工具，用于生成并显示对话框
 *
 * @author Bob Ackles
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class DialogUtil {

    ////////////////////// 单项选择器 ///////////////////////

    /**
     * 单项选择器 (从屏幕底部弹出)
     *
     * @param targetView 选择完成后数据填充控件
     * @param items      数据来源列表
     */
    public static OptionPicker showPickerDialog(Activity mContext, final TextView targetView,
                                                List<? extends TagInfo> items) {
        final List<TagInfo> itemList = new ArrayList<>();
        if (items != null) {
            itemList.addAll(items);
        }
        if (itemList.isEmpty()) {
            itemList.add(new TagInfo("", ""));
        }
        List<String> itemNameList = new ArrayList<>();
        for (TagInfo data : itemList) {
            itemNameList.add(data.getName());
        }
        OptionPicker mPicker = new OptionPicker(mContext, itemNameList);
        mPicker.setSubmitTextSize(16);
        mPicker.setCancelTextSize(16);
        mPicker.setTopLineVisible(false);
        mPicker.setTopHeight(70);
        mPicker.setCanceledOnTouchOutside(true);
        mPicker.setDividerColor(Color.parseColor("#CACACA"));
        mPicker.setCancelTextColor(Color.parseColor("#0075ff"));
        mPicker.setSubmitTextColor(Color.parseColor("#0075ff"));
        mPicker.setTextColor(Color.parseColor("#000000"), Color.parseColor("#CACACA"));
        mPicker.setAnimationStyle(R.style.dialog_anim_bottom);

        String initName = targetView.getText().toString().trim();
        if (TextUtils.isEmpty(initName)) {
            mPicker.setSelectedIndex(0);
        } else {
            mPicker.setSelectedIndex(itemNameList.indexOf(initName));
        }
        mPicker.setCycleDisable(true);
        mPicker.setTextSize(15);
        mPicker.addOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                targetView.setText(item);
                targetView.setTag(itemList.get(index).getValue());
            }
        });
        mPicker.show();
        return mPicker;
    }

    /**
     * 单项选择器 (从屏幕底部弹出)
     *
     * @param initName  初始值
     * @param items     数据来源列表
     */
    public static OptionPicker showPickerDialog(Activity mContext, String initName,
            List<? extends TagInfo> items, OptionPicker.OnOptionPickListener listener) {
        final List<TagInfo> itemList = new ArrayList<>();
        if (items != null) {
            itemList.addAll(items);
        }
        if (itemList.isEmpty()) {
            itemList.add(new TagInfo("", ""));
        }
        List<String> itemNameList = new ArrayList<>();
        for (TagInfo data : itemList) {
            itemNameList.add(data.getName());
        }
        OptionPicker mPicker = new OptionPicker(mContext, itemNameList);
        mPicker.setSubmitTextSize(16);
        mPicker.setCancelTextSize(16);
        mPicker.setTopLineVisible(false);
        mPicker.setTopHeight(70);
        mPicker.setCanceledOnTouchOutside(true);
        mPicker.setDividerColor(Color.parseColor("#CACACA"));
        mPicker.setCancelTextColor(Color.parseColor("#0075ff"));
        mPicker.setSubmitTextColor(Color.parseColor("#0075ff"));
        mPicker.setTextColor(Color.parseColor("#000000"), Color.parseColor("#CACACA"));
        mPicker.setAnimationStyle(R.style.dialog_anim_bottom);

        if (TextUtils.isEmpty(initName)) {
            mPicker.setSelectedIndex(0);
        } else {
            mPicker.setSelectedIndex(itemNameList.indexOf(initName));
        }
        mPicker.setCycleDisable(true);
        mPicker.setTextSize(15);
        mPicker.addOnOptionPickListener(listener);
        mPicker.show();
        return mPicker;
    }



    ////////////////////// 双项选择器 ///////////////////////

    /**
     * 双项选择器 (从屏幕底部弹出)
     *
     * @param names 当前选择的2个数据
     * @param dataList 数据来源列表
     */
    public static void showDoublePickerDialog(Activity mContext, final String[] names,
            List<Province> dataList, TripleLinkagePicker.OnDoublePickListener listener) {
        if (dataList == null) {
            return;
        }
        try {
            TripleLinkagePicker mPicker = new TripleLinkagePicker(mContext, dataList);
            mPicker.setHideCounty(true);
            mPicker.setCanceledOnTouchOutside(true);
            mPicker.setSubmitTextSize(16);
            mPicker.setCancelTextSize(16);
            mPicker.setTopLineVisible(false);
            mPicker.setTopHeight(50);
            mPicker.setDividerColor(Color.parseColor("#CACACA"));
            mPicker.setCancelTextColor(Color.parseColor("#999999"));
            mPicker.setSubmitTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            mPicker.setTextColor(Color.parseColor("#000000"), Color.parseColor("#CACACA"));
            mPicker.setAnimationStyle(R.style.dialog_anim_bottom);
            if (names != null && names.length >= 2) {
                if (!TextUtils.isEmpty(names[0])) {
                    mPicker.setSelectedItem(names[0], names[1]);
                }
            }
            mPicker.setOnDoublePickListener(listener);
            mPicker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    ////////////////////// 三项选择器 ///////////////////////

    /**
     * 三项选择器 (从屏幕底部弹出)
     *
     * @param names 当前选择的3个数据
     * @param values 当前选择的3个数据对应的id
     * @param dataList 数据来源列表
     * @param tvTarget 选择完成后数据填充控件
     */
    public static void showTriplePickerDialog(Activity mContext, final String[] names,
            final String[] values, List<Province> dataList, final TextView tvTarget) {
        if (dataList == null) {
            return;
        }
        try {
            TripleLinkagePicker mPicker = new TripleLinkagePicker(mContext, dataList);
            mPicker.setCanceledOnTouchOutside(true);
            mPicker.setSubmitTextSize(16);
            mPicker.setCancelTextSize(16);
            mPicker.setTopLineVisible(false);
            mPicker.setTopHeight(50);
            mPicker.setDividerColor(Color.parseColor("#CACACA"));
            mPicker.setCancelTextColor(Color.parseColor("#999999"));
            mPicker.setSubmitTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            mPicker.setTextColor(Color.parseColor("#000000"), Color.parseColor("#CACACA"));
            mPicker.setAnimationStyle(R.style.dialog_anim_bottom);
            if (names.length >= 3) {
                if (!TextUtils.isEmpty(names[0])) {
                    mPicker.setSelectedItem(names[0], names[1], names[2]);
                }
            }
            mPicker.setOnTriplePickListener(new TripleLinkagePicker.OnTriplePickListener() {
                @Override
                public void onPicked(Province province, City city, County county) {

                    values[0] = province.getAreaId();
                    values[1] = city.getAreaId();
                    values[2] = county.getAreaId();

                    names[0] = province.getAreaName();
                    names[1] = city.getAreaName();
                    names[2] = county.getAreaName();
                    tvTarget.setText(String.format("%1$s - %2$s - %3$s", names[0], names[1], names[2]));
                }
            });
            mPicker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 三项选择器 (从屏幕底部弹出)
     *
     * @param names 当前选择的3个数据
     * @param dataList 数据来源列表
     */
    public static void showTriplePickerDialog(Activity mContext, final String[] names,
            List<Province> dataList, TripleLinkagePicker.OnTriplePickListener listener) {
        if (dataList == null) {
            return;
        }
        try {
            TripleLinkagePicker mPicker = new TripleLinkagePicker(mContext, dataList);
            mPicker.setCanceledOnTouchOutside(true);
            mPicker.setSubmitTextSize(16);
            mPicker.setCancelTextSize(16);
            mPicker.setTopLineVisible(false);
            mPicker.setTopHeight(50);
            mPicker.setDividerColor(Color.parseColor("#CACACA"));
            mPicker.setCancelTextColor(Color.parseColor("#999999"));
            mPicker.setSubmitTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            mPicker.setTextColor(Color.parseColor("#000000"), Color.parseColor("#CACACA"));
            mPicker.setAnimationStyle(R.style.dialog_anim_bottom);
            if (names.length >= 3) {
                if (!TextUtils.isEmpty(names[0])) {
                    mPicker.setSelectedItem(names[0], names[1], names[2]);
                }
            }
            mPicker.setOnTriplePickListener(listener);
            mPicker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    ////////////////////// 消息对话框 ///////////////////////
    
    public static TitleMsgDialog showMsgDialog(Context context, String msg,
            String btnCancel, String btnOk, DialogInterface.OnClickListener listener) {
        return showMsgDialog(context, null, msg, btnCancel, btnOk, listener);
    }
    
    public static TitleMsgDialog showMsgDialog(Context context, String title, String msg,
            String btnCancel, String btnOk, DialogInterface.OnClickListener listener) {
        return new TitleMsgDialog(context)
                .setData(title, msg, btnCancel, btnOk, listener)
                .show();
    }

    public static class TitleMsgDialog extends BaseDialog {

        TextView tvTitle;

        TextView tvMsg;

        TextView tvCancel;

        TextView tvOk;

        DialogInterface.OnClickListener listener;

        public TitleMsgDialog(@NonNull Context context) {
            super(context);

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_title_msg_2button, null);
            tvTitle = view.findViewById(R.id.title);
            tvMsg = view.findViewById(R.id.msg);
            tvCancel = view.findViewById(R.id.btn_cancel);
            tvOk = view.findViewById(R.id.btn_ok);
            contentView(view).width(0.7F);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(TitleMsgDialog.this.getRaw(), v.getId() == R.id.btn_cancel ? 0 : 1);
                }
            };
            tvCancel.setOnClickListener(onClickListener);
            tvOk.setOnClickListener(onClickListener);
        }

        public TitleMsgDialog setData(String title, String msg, String btnCancel, String btnOk,
                DialogInterface.OnClickListener listener) {
            if (!TextUtils.isEmpty(title)) {
                tvTitle.setText(title);
            } else {
                tvTitle.setVisibility(View.GONE);
            }
            tvMsg.setText(msg);
            if (!TextUtils.isEmpty(btnCancel)) {
                tvCancel.setText(btnCancel);
            } else {
                tvCancel.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(btnOk)) {
                tvOk.setText(btnOk);
            }
            this.listener = listener;
            return this;
        }

        public TitleMsgDialog titleColor(@ColorInt int color) {
            tvTitle.setTextColor(color);
            return this;
        }

        public TitleMsgDialog msgColor(@ColorInt int color) {
            tvMsg.setTextColor(color);
            return this;
        }

        public TitleMsgDialog msgTextSize(int dimenInSp) {
            tvMsg.setTextSize(dimenInSp);
            return this;
        }

        public TitleMsgDialog msgGravity(int gravity) {
            tvMsg.setGravity(gravity);
            return this;
        }
    }



    ////////////////////// 显示屏幕居中自定义对话框 ///////////////////////

    public static BaseDialog showCustomDialog(Context context, View view) {
        return showCustomDialog(context, view, true);
    }

    public static BaseDialog showCustomDialog(Context context, View view, boolean cancelOutside) {
        return showCustomDialog(context, view, cancelOutside, true);
    }

    public static BaseDialog showCustomDialog(Context context, View view, boolean cancelOutside, boolean cancellable) {
        return showCustomDialog(context, view, 0.8f, cancelOutside, cancellable);
    }

    public static BaseDialog showCustomDialog(Context context, View view, float width, boolean cancelOutside, boolean cancellable) {
        return showCustomDialog(context, view, width, 0f, cancelOutside, cancellable);
    }

    public static BaseDialog showCustomDialog(Context context, View view, float width, float height,
                                          boolean cancelOutside, boolean cancellable) {
        return showCustomDialog(context, R.style.dialog_custom, view, width, height, cancelOutside, cancellable);
    }

    public static BaseDialog showCustomDialog(Context context, int style, View view, float width, float height,
                                          boolean cancelOutside, boolean cancellable) {
        return showCustomDialog(context, style, view, width, height, cancelOutside, cancellable, 0, Gravity.CENTER);
    }

    public static BaseDialog showCustomDialog(Context context, int style, View view, float width, float height,
                                          boolean cancelOutside, boolean cancellable, int anims, int gravity) {
        return new BaseDialog(context, style)
                .width(width)
                .height(height)
                .gravity(gravity)
                .animations(anims)
                .cancelable(cancellable)
                .canceledOnTouchOutside(cancelOutside)
                .contentView(view)
                .show();
    }
}
