package com.chen.insurre.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast消息提示统一管理类
 *
 * @author 陈国权
 */
public class ToastUtil {

    /**
     * 短消息
     *
     * @param context
     * @param string
     */
    public static void showToastShort(Context context, String string) {
        Toast toast = Toast.makeText(context, string,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastShort(Context context, int id) {
        Toast toast = Toast.makeText(context, context.getString(id),
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 长消息
     *
     * @param context
     * @param string
     */
    public static void showToastLong(Context context, String string) {
        Toast toast = Toast.makeText(context, string,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastLong(Context context, int id) {
        Toast toast = Toast.makeText(context, context.getString(id),
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
