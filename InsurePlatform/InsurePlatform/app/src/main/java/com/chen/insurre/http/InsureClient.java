package com.chen.insurre.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by hm-soft on 2015/8/3.
 */
public class InsureClient {

    private static AsyncHttpClient client=new AsyncHttpClient();
    public static void get(String url,RequestParams params,AsyncHttpResponseHandler responseHandler){
        client.get(url,params,responseHandler);
    }
}
