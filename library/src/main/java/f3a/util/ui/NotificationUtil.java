package f3a.util.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * @author Bob
 * @since 2018-12-4
 */
public class NotificationUtil {
    
    private static int currId = 1;
    
    private static String channelId = "1";
    
    private static NotificationChannel channel;
    
    public static long notifi(Context context, NotificationCompat.Builder builder) {
        int id = currId++;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (channel == null) {
                channel = new NotificationChannel(channelId, "name", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }
            builder.setChannelId(channelId);
        }
        manager.notify(id, builder.build());
        return id;
    }
}
