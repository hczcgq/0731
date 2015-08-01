package com.chen.insurre.http;

import android.content.Context;
import android.util.Log;

import com.chen.insurre.util.CommTools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpHelper {

    public static final int HTTP_POST = 0;
    public static final int HTTP_GET = 1;

    private static final int mTimeoutConnection = 120 * 1000;
    private static final int mTimeoutSocket = 130 * 1000;

    private static String TAG = "HttpHelper";

    /**
     *
     * @param serverUrl
     *            服务器url
     * @param remoteType
     *            HttpHelper.HTTP_POST 或 HttpHelper.HTTP_GET
     * @param hashMap
     *            请求参数的hashmap
     * @return 服务器返回的string
     * @throws Exception
     */
    public static String doRequestForString(Context context, String serverUrl,
                                            int remoteType, Map<String, String> hashMap) throws Exception {
        int timeoutConnection = mTimeoutConnection;
        int timeoutSocket = mTimeoutSocket;

        return doRequestForString(context, serverUrl, remoteType, hashMap,
                timeoutConnection, timeoutSocket);
    }

    /**
     *
     * @param serverUrl
     *            服务器url
     * @param remoteType
     *            HttpHelper.HTTP_POST 或 HttpHelper.HTTP_GET
     * @param hashMap
     *            请求参数的hashmap
     * @param timeoutConnection
     *            建立连接超时时间
     * @param timeoutSocket
     *            等待数据超时时间
     * @return 服务器返回的string
     * @throws Exception
     */
    public static String doRequestForString(Context context, String serverUrl,
                                            int remoteType, Map<String, String> hashMap, int timeoutConnection,
                                            int timeoutSocket) throws Exception {
        String result = "";
        HttpEntity entity = doRequestForEntity(context, serverUrl, remoteType,
                hashMap, timeoutConnection, timeoutSocket);
        if (entity != null)
            result = EntityUtils.toString(entity, "UTF-8");
        Log.v(TAG, result);

        return result;
    }

    /**
     *
     * @param serverUrl
     *            服务器url
     * @param remoteType
     *            HttpHelper.HTTP_POST 或 HttpHelper.HTTP_GET
     * @param hashMap
     *            请求参数的hashmap
     * @return 服务器返回的HttpEntity
     * @throws Exception
     */
    public static HttpEntity doRequestForEntity(Context context,
                                                String serverUrl, int remoteType, Map<String, String> hashMap)
            throws Exception {
        int timeoutConnection = mTimeoutConnection;
        int timeoutSocket = mTimeoutSocket;
        return doRequestForEntity(context, serverUrl, remoteType, hashMap,
                timeoutConnection, timeoutSocket);
    }

    /**
     *
     * @param serverUrl
     *            服务器url
     * @param remoteType
     *            HttpHelper.HTTP_POST 或 HttpHelper.HTTP_GET
     * @param hashMap
     *            请求参数的hashmap
     * @param timeoutConnection
     *            建立连接超时时间
     * @param timeoutSocket
     *            等待数据超时时间
     * @return 服务器返回的HttpEntity
     * @throws Exception
     */
    public static HttpEntity doRequestForEntity(Context context,
                                                String serverUrl, int remoteType, Map<String, String> hashMap,
                                                int timeoutConnection, int timeoutSocket) throws Exception {
        HttpEntity entity = null;
        HttpResponse httpResponse = doRequestForResponse(context, serverUrl,
                remoteType, hashMap, timeoutConnection, timeoutSocket);
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status != 401) {
            entity = httpResponse.getEntity();
        }
        return entity;
    }

    /**
     *
     * @param serverUrl
     *            服务器url
     * @param remoteType
     *            HttpHelper.HTTP_POST 或 HttpHelper.HTTP_GET
     * @param hashMap
     *            请求参数的hashmap
     * @return 服务器返回的HttpResponse
     * @throws Exception
     */
    public static HttpResponse doRequestForResponse(Context context,
                                                    String serverUrl, int remoteType, Map<String, String> hashMap)
            throws Exception {
        int timeoutConnection = mTimeoutConnection;
        int timeoutSocket = mTimeoutSocket;

        return doRequestForResponse(context, serverUrl, remoteType, hashMap,
                timeoutConnection, timeoutSocket);
    }

    /**
     *
     * @param serverUrl
     *            服务器url
     * @param remoteType
     *            HttpHelper.HTTP_POST 或 HttpHelper.HTTP_GET
     * @param hashMap
     *            请求参数的hashmap
     * @param timeoutConnection
     *            建立连接超时时间
     * @param timeoutSocket
     *            等待数据超时时间
     * @return 服务器返回的HttpResponse
     * @throws Exception
     */
    public static HttpResponse doRequestForResponse(Context context,
                                                    String serverUrl, int remoteType, Map<String, String> hashMap,
                                                    int timeoutConnection, int timeoutSocket) throws Exception {
        StringBuilder sb = new StringBuilder(serverUrl);
        if (hashMap != null) {
            for (Entry<String, String> entry : hashMap.entrySet()) {
                if (!sb.toString().contains("?"))
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                else
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
            }
        }
        Log.d(TAG, sb.toString());

        HttpUriRequest request = null;
        if (remoteType == HTTP_POST) {
            if (hashMap != null) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (Entry<String, String> entry : hashMap.entrySet()) {
                    params.add(new BasicNameValuePair(entry.getKey(), entry
                            .getValue()));
                }
                HttpPost post = new HttpPost(serverUrl);
                post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                request = post;
            } else {
                HttpPost post = new HttpPost(serverUrl);
                request = post;
            }
        } else if (remoteType == HTTP_GET) {
            if (hashMap != null) {
                for (Entry<String, String> entry : hashMap.entrySet()) {
                    if (!serverUrl.contains("?"))
                        serverUrl += "?" + entry.getKey() + "="
                                + entry.getValue();
                    else
                        serverUrl += "&" + entry.getKey() + "="
                                + entry.getValue();
                }
            }
            request = new HttpGet(serverUrl);
        }
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

        if (!CommTools.isNetworkWifi(context)) {
            String proxyHost = android.net.Proxy.getDefaultHost();
            if (proxyHost != null) {
                HttpHost proxy = new HttpHost(
                        android.net.Proxy.getDefaultHost(),
                        android.net.Proxy.getDefaultPort());
                httpClient.getParams().setParameter(
                        ConnRouteParams.DEFAULT_PROXY, proxy);
            }
        }

        HttpResponse httpResponse = httpClient.execute(request);
        return httpResponse;
    }

}
