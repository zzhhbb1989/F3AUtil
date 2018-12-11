package f3a.util.model;

import java.util.List;

/**
 * 列表选择数据
 * author: zhengbo
 * date: 2017-11-22.
 */
public class TagInfo {
    
    /** 名称 **/
    String name;
    /** 对应值 **/
    String value;
    
    public TagInfo() {
    }
    
    public TagInfo(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    ////////////////////// 原始 ///////////////////////
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    
    
    ////////////////////// 操作 ///////////////////////
    
    public static String getNameByValue(List<TagInfo> list, String value) {
        if (list != null) {
            for (TagInfo tagInfo : list) {
                if (tagInfo.getValue().equals(value)) {
                    return tagInfo.getName();
                }
            }
        }
        return null;
    }
    
    public static String getValueByName(List<TagInfo> list, String name) {
        if (list != null) {
            for (TagInfo tagInfo : list) {
                if (tagInfo.getName().equals(name)) {
                    return tagInfo.getValue();
                }
            }
        }
        return null;
    }
    
    
    
    ////////////////////// 获取数据 ///////////////////////
    
    public interface OnGetDataCallback {
        void onGetData();
    }
}
