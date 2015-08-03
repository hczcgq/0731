package com.chen.insurre.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.chen.insurre.R;
import com.chen.insurre.bean.LoginInfo;
import com.chen.insurre.bean.ResultInfo;
import com.chen.insurre.http.HttpHelper;
import com.chen.insurre.http.InsureClient;
import com.chen.insurre.util.CommTools;
import com.chen.insurre.util.Constant;
import com.chen.insurre.util.NetworkUtil;
import com.chen.insurre.util.PreferencesUtils;
import com.chen.insurre.util.StringUtil;
import com.chen.insurre.util.ToastUtil;
import com.chen.insurre.view.ValidateImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by chenguoquan on 7/29/15.
 */
public class LoginActivity extends Activity {

    private EditText UsernameEditext, PasswordEditext, VerifyEditText;
    private LinearLayout VerifyLayout;

    private ValidateImageView mValidateImageView = null;
    private String[] responseArray = null; // 验证码数组

    private Activity mContext=this;

    private LoginTask mLoginTask;

    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_login);
        PreferencesUtils.PREFERENCE_NAME = "InsureInfo";

        initView();
    }

    private void initView() {
        UsernameEditext = (EditText) findViewById(R.id.UsernameEditext);
        PasswordEditext = (EditText) findViewById(R.id.PasswordEditext);
        VerifyEditText = (EditText) findViewById(R.id.VerifyEditText);
        VerifyLayout = (LinearLayout) findViewById(R.id.VerifyLayout);

        mValidateImageView = new ValidateImageView(this);
        VerifyLayout.addView(mValidateImageView);
        responseArray = mValidateImageView.getValidataAndSetImage(getRandomInteger());

        mValidateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                responseArray = mValidateImageView.getValidataAndSetImage(getRandomInteger());
            }
        });

        VerifyEditText.setText("");
        responseArray = mValidateImageView.getValidataAndSetImage(getRandomInteger());
    }

    // 为数组赋值1~9的随机数
    private String[] getRandomInteger() {
        String[] reuestArray = new String[4];
        for (int i = 0; i < 4; i++) {
            reuestArray[i] = String.valueOf((int) (Math.random() * 9 + 1));
        }
        return reuestArray;
    }

    // 获取返回的数组
    private String getResponseStr(String[] response) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : response) {
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    public void Login(View view){
        String username=UsernameEditext.getText().toString();
        String password=PasswordEditext.getText().toString();
        String verify=VerifyEditText.getText().toString();
        String randomCode= getResponseStr(responseArray).trim();

        if(StringUtil.isEmpty(username)){
            ToastUtil.showToastShort(this,"用户名不能为空");
            return;
        }

        if(StringUtil.isEmpty(password)){
            ToastUtil.showToastShort(this,"密码不能为空");
            return;
        }


//        if(StringUtil.isEmpty(verify)){
//            ToastUtil.showToastShort(this,"验证码不能为空");
//            return;
//        }
//
//        if(!verify.equals(randomCode)){
//            ToastUtil.showToastShort(this,"验证码错误");
//            return;
//        }



        if (mLoginTask != null
                && mLoginTask.getStatus() != AsyncTask.Status.FINISHED)
            mLoginTask.cancel(true);
        mLoginTask = new LoginTask();
        mLoginTask.execute(username, password);



//        loginServer(username,password);

    }


    private class LoginTask extends AsyncTask<String, Void, ResultInfo> {
        private String userName;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                dialog = ProgressDialog.show(LoginActivity.this, "请稍后",
                        "登录中...");
                dialog.setCancelable(true);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        @Override
        protected ResultInfo doInBackground(String... params) {
            if (!NetworkUtil.networkIsAvailable(mContext)) {
                return null;
            }
            userName = params[0];
            String pwd = params[1];
            String url = CommTools.getRequestUrl(mContext, R.string.login_url);
            HashMap<String, String> hashParams = new HashMap<String, String>();
            hashParams.put("uname", userName);
            hashParams.put("upass", pwd);
            ResultInfo resultInfo = null;
            try {
                String result = HttpHelper.doRequestForString(mContext, url,
                        HttpHelper.HTTP_GET, hashParams);

                resultInfo = new Gson().fromJson(result ,new TypeToken<ResultInfo<LoginInfo>>(){}.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultInfo;
        }

        @Override
        protected void onPostExecute(ResultInfo result) {
            super.onPostExecute(result);
            if (dialog != null)
                dialog.dismiss();
            if (result != null && result.getResult() != null
                    && result.getResult().equals("0")) {
                LoginInfo loginInfo=(LoginInfo)result.getBean();
                if(loginInfo!=null){
                    PreferencesUtils.putString(mContext, Constant.SP_USER_ID,loginInfo.getId());
                    PreferencesUtils.putString(mContext,Constant.SP_USER_PASSWD,loginInfo.getPasswd());
                    PreferencesUtils.putString(mContext,Constant.SP_USER_NAME,loginInfo.getName());
                    PreferencesUtils.putString(mContext,Constant.SP_USER_KEY,loginInfo.getKey());
                    PreferencesUtils.putString(mContext, Constant.SP_USER_REGKEY, loginInfo.getRegkey());
                }
                Intent intent=new Intent(mContext,MainActivity.class);
                startActivity(intent);
                finish();
            } else if (result != null && result.getDescription() != null
                    && !result.getDescription().equals("")) {
                Toast.makeText(mContext, "登录失败，" + result.getDescription(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "登录失败，请稍后再试!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }





    private void loginServer(String username,String password){
        String url = CommTools.getRequestUrl(mContext, R.string.login_url);
        HashMap<String, String> hashParams = new HashMap<String, String>();
        hashParams.put("uname", username);
        hashParams.put("upass", password);
        RequestParams params = new RequestParams(hashParams);
        InsureClient.get(url,params,new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
                try {
                    dialog = ProgressDialog.show(LoginActivity.this, "请稍后",
                            "登录中...");
                    dialog.setCancelable(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (dialog != null)
                    dialog.dismiss();
                Log.d("chen1",response.toString());
                processResult(response.toString());
            }
        });
    }

    private void processResult(String result){
        ResultInfo resultInfo=new Gson().fromJson(result ,new TypeToken<ResultInfo<LoginInfo>>(){}.getType());
        if (resultInfo != null && resultInfo.getResult() != null
                && resultInfo.getResult().equals("0")) {
            LoginInfo loginInfo=(LoginInfo)resultInfo.getBean();
            if(loginInfo!=null){
                PreferencesUtils.putString(mContext, Constant.SP_USER_ID,loginInfo.getId());
                PreferencesUtils.putString(mContext,Constant.SP_USER_PASSWD,loginInfo.getPasswd());
                PreferencesUtils.putString(mContext,Constant.SP_USER_NAME,loginInfo.getName());
                PreferencesUtils.putString(mContext,Constant.SP_USER_KEY,loginInfo.getKey());
                PreferencesUtils.putString(mContext, Constant.SP_USER_REGKEY, loginInfo.getRegkey());
            }
            Intent intent=new Intent(mContext,MainActivity.class);
            startActivity(intent);
            finish();
        } else if (resultInfo != null && resultInfo.getDescription() != null
                && !resultInfo.getDescription().equals("")) {
            Toast.makeText(mContext, "登录失败，" + resultInfo.getDescription(),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "登录失败，请稍后再试!", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
