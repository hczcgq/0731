package com.chen.insurre.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络连接工具类
 *
 * @author 陈国权
 */
public class NetworkUtil {

    public static final String MSG_NO_NETWORK = "";
    public static final int NETTYPE_WIFI = 0x01; // 网络类型为wifi
    public static final int NETTYPE_CMWAP = 0x02; // 网络类型为wap
    public static final int NETTYPE_CMNET = 0x03; // 网络类型为net

    /**
     * 判断网络是否存在 Determine whether the network is available
     *
     * @param context
     * @return
     */
    public static boolean networkIsAvailable(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        if (info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();

            if (!StringUtil.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 检查当前是否为wifi. Determine whether the wifi is available
     *
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
}
