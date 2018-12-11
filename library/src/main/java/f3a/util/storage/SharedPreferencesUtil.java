package f3a.util.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

/**
 * SharedPreference工具类，以键值对的形式实现数据持久化
 *
 * @author Bob
 * @since 2018-05-18
 *
 */

@SuppressWarnings({"unused", "unchecked", "WeakerAccess", "SameParameterValue"})
public class SharedPreferencesUtil {
    
    private static SharedPreferences sp;
    private static Editor editor;
    
    /**
     * 初始化，最好放在{@link android.app.Application}的{@link android.app.Application#onCreate}方法里执行
     */
    public static void init(Context context) {
        init(context, "main_sp");
    }
    
    /**
     * 初始化，最好放在{@link android.app.Application}的{@link android.app.Application#onCreate}方法里执行
     *
     * @param filename 存储数据用的文件名称
     */
    public static void init(Context context, String filename) {
        init(context, filename, Context.MODE_PRIVATE);
    }
    
    /**
     * 初始化，最好放在{@link android.app.Application}的{@link android.app.Application#onCreate}方法里执行
     *
     * @param filename 存储数据用的文件名称
     * @param mode 存储模式
     */
    public static void init(Context context, String filename, int mode) {
        if (sp == null) {
            sp = context.getSharedPreferences(filename, mode);
            editor = sp.edit();
            editor.apply();
        }
    }
    
    public static void recycle() {
        sp = null;
        editor = null;
    }
    
    /**
     * @return 是否包含某个键为key的键值对
     */
    public static boolean contains(String key) {
        return sp.contains(key);
    }
    
    /**
     * 移除键为key的键值对
     */
    public static void remove(String key) {
        editor.remove(key);
        editor.commit();
    }
    
    /**
     * 清除所有键值对
     */
    public static void clear() {
        editor.clear();
        editor.commit();
    }
    
    /**
     * 得到某个键为key的值
     */
    public static <T> T get(String key) {
        Object result = sp.getAll().get(key);
        return (T) result;
    }
    
    
    /**
     * 得到某个键为key的值，没找到则返回默认值defaultValue
     */
    public static <T> T get(String key, T defaultValue) {
        Object result = sp.getAll().get(key);
        if (result == null) {
            result = defaultValue;
        }
        return (T) result;
    }
    
    public static void put(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }
    
    public static void put(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }
    
    public static void put(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }
    
    public static void put(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }
    
    public static void put(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }
    
    public static void put(String key, Set<String> value) {
        editor.putStringSet(key, value);
        editor.commit();
    }
}
