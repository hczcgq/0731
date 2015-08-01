package com.chen.insurre.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.chen.insurre.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class CommTools {
    public static boolean isNetworkAvailable(Context context) {
        boolean value = false;
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            value = true;
        }
        return value;
    }

    public static SharedPreferences getPreference(Context context) {
        SharedPreferences preference = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preference;
    }

    public static JSONObject getJSONObject(HashMap<String, Object> map) {
        JSONObject reqObject = new JSONObject();
        for (Entry<String, Object> e : map.entrySet()) {
            try {
                reqObject.put(e.getKey(), e.getValue());
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return reqObject;
    }

    public static boolean isValidPhoneNum(String phoneNum) {
        boolean result = false;
        if (phoneNum != null) {
            String regularExp = "^(13|15|18)\\d{9}$";
            result = phoneNum.matches(regularExp);
        }
        return result;
    }

    // 2-10个中文或 2-30个英文字母
    public static boolean isValidUserRealName(String name) {
        boolean result = false;
        if (name != null) {
            String regularExp = "(^[\u4e00-\u9fa5]{2,10}$)|(^[a-zA-z][a-zA-z\\s\\/]{0,28}[a-zA-z]$)";
            result = name.matches(regularExp);
        }
        return result;
    }



    public static int dip2px(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 监测网络是否存在wifi状态
     *
     * @param context
     * @return
     */
    public static boolean isNetworkWifi(Context context) {
        boolean flag = false;
        ConnectivityManager cwjManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cwjManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()
                .name().equals("CONNECTED")) {
            flag = true;
        }
        return flag;
    }

    public static String getAppName(Context context) {
        String appName = context.getPackageManager()
                .getApplicationLabel(context.getApplicationInfo()).toString();
        return appName;
    }

    public static String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = sdf.format(date);
        return timeStamp;
    }

    public static String getVersionName(Context context) {
        String currentVersionName = null;
        PackageManager pkgManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = pkgManager.getPackageInfo(
                    context.getPackageName(), 0);
            currentVersionName = packageInfo.versionName + "";
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return currentVersionName;
    }

    public static String getMetaData(Context context, String metaDataName) {
        String metaData = null;
        PackageManager pkgManager = context.getPackageManager();
        try {
            ApplicationInfo packageInfo = pkgManager.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaDataBundle = packageInfo.metaData;
            if (metaDataBundle != null)
                metaData = metaDataBundle.getString(metaDataName);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return metaData;
    }

    public static Bitmap BitmapCompression(File file, int maxWidth)
            throws FileNotFoundException {
        Bitmap destBm = null;
        if (maxWidth != 0) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, opts);
            int srcWidth = opts.outWidth;
            int srcHeight = opts.outHeight;
            int destWidth = 0;
            int destHeight = 0;
            double ratio = srcWidth / maxWidth;
            destWidth = maxWidth;
            destHeight = (int) (srcHeight / ratio);
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inSampleSize = (int) ratio + 1;
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            destBm = BitmapFactory.decodeStream(new FileInputStream(file),
                    null, newOpts);
        } else {
            destBm = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        return destBm;
    }

    public static JSONObject getJSONObjectString(HashMap<String, String> map) {
        JSONObject reqObject = new JSONObject();
        for (Entry<String, String> e : map.entrySet()) {
            try {
                reqObject.put(e.getKey(), e.getValue());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return reqObject;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static byte[] getBytesFromInputStream(InputStream is, int bufsiz)
            throws IOException {
        int total = 0;
        byte[] bytes = new byte[4096];
        ByteBuffer bb = ByteBuffer.allocate(bufsiz);

        while (true) {
            int read = is.read(bytes);
            if (read == -1)
                break;
            bb.put(bytes, 0, read);
            total += read;
        }

        byte[] content = new byte[total];
        bb.flip();
        bb.get(content, 0, total);

        return content;
    }

    public static Bitmap BitmapCompression(byte[] data, int maxWidth) {
        Bitmap destBm = null;
        int ratio = 0;
        int srcWidth = 0;
        int srcHeight = 0;
        if (maxWidth != 0) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            srcWidth = opts.outWidth;
            srcHeight = opts.outHeight;
            ratio = srcWidth / maxWidth;
        }
        if (ratio > 1) {
            int destWidth = 0;
            int destHeight = 0;
            destWidth = maxWidth;
            destHeight = srcHeight / ratio;
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inSampleSize = ratio + 1;
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            destBm = BitmapFactory.decodeByteArray(data, 0, data.length,
                    newOpts);
        } else {
            destBm = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        return destBm;
    }

    public static byte[] getBytesFromBitmap(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }

    public static boolean isGpsEnabled(Context context) {
        boolean result = false;
        LocationManager lm = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        result = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return result;
    }

    public static String getRequestUrl(Context context, int urlRes) {
        String baseUrl = context.getString(R.string.base_url);
        String apiPrefix = context.getString(R.string.api_prefix_url);
        String url = baseUrl + apiPrefix + context.getString(urlRes);
        return url;
    }

    public static Bitmap drawTextToBitmap(Context mContext, int resourceId,
                                          String mText) {
        try {
            Resources resources = mContext.getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);

            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.rgb(110, 110, 110));
            // text size in pixels
            paint.setTextSize(CommTools.dip2px(mContext, 20));
            // text shadow
            // paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width());
            int y = (bitmap.getHeight() - bounds.height());

            canvas.drawText(mText, x / 2f, bitmap.getHeight() - y / 2f, paint);

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String MD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    public static String changHtmlTo(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        String html = new String(str);
        html = html.replaceAll("&amp;", "&");
        html = html.replaceAll("&lt;", "<");
        html = html.replaceAll("&gt;", ">");
        html = html.replaceAll("&apos;", "'");
        html = html.replaceAll("&nbsp;", " ");
        return html;
    }

    public static String[] split(String msg, int num) {
        int len = msg.length();
        if (len <= num)
            return new String[] { msg };

        int count = len / (num - 1);
        count += len > (num - 1) * count ? 1 : 0; // 这里应该值得注意

        String[] result = new String[count];

        int pos = 0;
        int splitLen = num - 1;
        for (int i = 0; i < count; i++) {
            if (i == count - 1)
                splitLen = len - pos;

            result[i] = msg.substring(pos, pos + splitLen);
            pos += splitLen;

        }

        return result;
    }

}
