package f3a.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * 网络相关工具
 * @author Bob Ackles
 *
 */
public class NetUtil {
	
	/**
	 * @return 网络连接是否可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		if (conMgr != null) {
            NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
	}
	
	/**
	 * @return 当前连接的wifi的ssid
	 */
	public static String getCurrentSSID(Context context) {
		WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
		if (wifiManager != null && wifiManager.isWifiEnabled()) {
            if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    String ssid = wifiInfo.getSSID();
                    if (ssid.contains("\"")) {
                        ssid = ssid.replaceAll("\"", "");
                    }
                    return ssid;
                }
            }
        }
		return null;
	}
	
	/**
	 * @return 当前连接的wifi的安全性
	 */
	public static int getCurrentSecurity(Context context) {
		WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
		if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                // 得到配置好的网络连接
                List<WifiConfiguration> wifiConfigList = wifiManager.getConfiguredNetworks();
                
                if (wifiConfigList != null) {
                    for (WifiConfiguration wifiConfiguration : wifiConfigList) {
                        // 配置过的SSID
                        String configSSid = wifiConfiguration.SSID;
                        configSSid = configSSid.replace("\"", "");
                        
                        // 当前连接SSID
                        String currentSSid = wifiInfo.getSSID();
                        currentSSid = currentSSid.replace("\"", "");
                        
                        // 比较networkId，防止配置网络保存相同的SSID
                        if (currentSSid.equals(configSSid) && wifiInfo.getNetworkId() == wifiConfiguration.networkId) {
                            return getSecurity(wifiConfiguration);
                        }
                    }
                }
            }
        }
		return -1;
	}

	public static final String[] SECURITY_TEXTS = { "NONE", "WEP", "WPA/WPA2 PSK", "802.1x EAP" };
	public static final int[] SECURITIES = { 0, 1, 2, 3 };
	
	private static int getSecurity(WifiConfiguration config) {
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
			return SECURITIES[2];
		}
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP)
				|| config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
			return SECURITIES[3];
		}
		return (config.wepKeys[0] != null) ? SECURITIES[1] : SECURITIES[0];
	}

	public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && wifiManager.isWifiEnabled() &&
                wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
    }
	
	/**
	 * @return 程序当前接受的总字节数
	 */
	public static long getCurrentReceivedBytesTotal(Context context) {
		return TrafficStats.getUidRxBytes(context.getApplicationInfo().uid);
	}
}
