package f3a.util.contact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 拨号工具
 *
 * @author Bob Ackles
 */
public class CallUtil {

    /**
     * 直接打电话
     *
     * @param phoneno 电话号码
     */
    @SuppressLint("MissingPermission")
    public static void call(Context context, String phoneno) {
        if (context == null || phoneno == null || phoneno.length() == 0) {
            throw new IllegalArgumentException();
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:" + phoneno));
        context.startActivity(intent);
    }

    /**
     * 只拨号
     *
     * @param phoneno 电话号码
     */
    public static void dial(Context context, String phoneno) {
        if (context == null || phoneno == null || phoneno.length() == 0) {
            throw new IllegalArgumentException();
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:" + phoneno));
        context.startActivity(intent);
    }
}
