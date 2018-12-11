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
    
    ////////////////////// 病种 ///////////////////////
    
    public void setDisease_species_id(String value) {
        setValue(value);
    }
    
    public void setDisease_species_name(String name) {
        setName(name);
    }
    
    ////////////////////// 医院 ///////////////////////
    
    public void setHospital_id(String value) {
        setValue(value);
    }
    
    public void setHospital_name(String name) {
        setName(name);
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
    
    
    
    ////////////////////// 选项数据 ///////////////////////
    
    public static List<TagInfo> typeList;
    
    private static String typeValue;
    
    public static void setTypeValue(String value) {
        typeValue = value;
//        SharedPreferencesUtil.put("type", typeValue);
    }
    
    public static String getTypeValue() {
        return typeValue;
    }
    
    public static String getTypeName() {
        return getNameByValue(typeList, typeValue);
    }
    
    public static List<TagInfo> hospitalList;
    
    public static void init() {
//        typeValue = SharedPreferencesUtil.get("type");
    }
    
    public static void clear() {
        typeList = null;
        typeValue = null;
        hospitalList = null;
//        SharedPreferencesUtil.remove("type");
    }
    
    ////////////////////// 获取数据 ///////////////////////
    
    
    public interface OnGetDataCallback {
        void onGetData();
    }
}
