package com.chen.insurre.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chen.insurre.MyApplication;
import com.chen.insurre.R;
import com.chen.insurre.bean.LoginInfo;
import com.chen.insurre.bean.ParamInfo;
import com.chen.insurre.bean.ResultInfo;
import com.chen.insurre.bean.TurnItemInfo;
import com.chen.insurre.http.HttpHelper;
import com.chen.insurre.util.CommTools;
import com.chen.insurre.util.Constant;
import com.chen.insurre.util.NetworkUtil;
import com.chen.insurre.util.PreferencesUtils;
import com.chen.insurre.util.StringUtil;
import com.chen.insurre.util.ToastUtil;
import com.chen.insurre.view.ValidateImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
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

    private TurnInTask mTurnInTask;

    private MyApplication application;

    private String pversion;  //参数文件参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_login);
        PreferencesUtils.PREFERENCE_NAME = "InsureInfo";
        application=MyApplication.getInstance();
        pversion= PreferencesUtils.getString(this,Constant.SP_PVERSION,"2");

        initView();

        new Thread(new Runnable() {
            @Override
            public void run() {
                readJsonDate();
            }
        }).start();
    }




    private String readInputStream(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    /**
     * 读取本地文件
     * @return
     */
    private String readFromFile() {
        if(!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)){//SD卡不存在则不操作
            return null;//返回到程序的被调用处
        }
        String result=null;
        File fileDir=new File(getExternalFilesDir(null)+"/json");
        File file=new File(fileDir,"json.txt");
        if(file.exists()){
            try {
                result=readInputStream(new FileInputStream(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 转换为Object数据
     */
    private void readJsonDate() {
        String result=readFromFile();
        if(result==null){
            return;
        }
        ParamInfo paramInfo = new Gson().fromJson(result, new TypeToken<ParamInfo>() {
        }.getType());
        if (paramInfo != null) {
            application.setCaijiList(paramInfo.getCaiji());
            application.setReasonList(paramInfo.getReason());
            application.setProvsList(paramInfo.getProvs());
            application.setStateList(paramInfo.getState());
            application.setCanbaoList(paramInfo.getCanbao());
        }
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


        if(StringUtil.isEmpty(verify)){
            ToastUtil.showToastShort(this,"验证码不能为空");
            return;
        }

        if(!verify.equals(randomCode)){
            ToastUtil.showToastShort(this,"验证码错误");
            return;
        }


        if (!NetworkUtil.networkIsAvailable(mContext)) {
            ToastUtil.showToastShort(this,"请检查网络连接状态。");
            return ;
        }

        if (mLoginTask != null
                && mLoginTask.getStatus() != AsyncTask.Status.FINISHED)
            mLoginTask.cancel(true);
        mLoginTask = new LoginTask();
        mLoginTask.execute(username, password);

    }


    /**
     * 保存数据到文件
     * @param paramInfo
     */
    private void saveParams(final ParamInfo paramInfo){
        if(paramInfo==null){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                application.setCaijiList(paramInfo.getCaiji());
                application.setReasonList(paramInfo.getReason());
                application.setProvsList(paramInfo.getProvs());
                application.setStateList(paramInfo.getState());
                application.setCanbaoList(paramInfo.getCanbao());
                String result=new Gson().toJson(paramInfo);
                if(!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)){//SD卡不存在则不操作
                    return;//返回到程序的被调用处
                }
                File fileDir=new File(getExternalFilesDir(null)+"/json");
                fileDir.mkdirs();
                File file=new File(fileDir,"json.txt");
                if(!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                PrintStream out = null;
                try {
                    out = new PrintStream(new FileOutputStream(file));
                    out.print(result);//将数据变为字符串后保存
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }finally{
                    if(out!=null){
                        out.close();//关闭输出
                    }
                }
            }
        }).start();
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

            userName = params[0];
            String pwd = params[1];
            String url = CommTools.getRequestUrl(mContext, R.string.login_url);
            HashMap<String, String> hashParams = new HashMap<String, String>();
            hashParams.put("uname", userName);
            hashParams.put("upass", pwd);
            hashParams.put("pversion", pversion);
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
            if (result != null && result.getResult() != null
                    && result.getResult().equals("0")) {
                LoginInfo loginInfo=(LoginInfo)result.getBean();
                if(loginInfo!=null){
                    PreferencesUtils.putString(mContext, Constant.SP_USER_ID,loginInfo.getId());
                    PreferencesUtils.putString(mContext,Constant.SP_USER_PASSWD,loginInfo.getPasswd());
                    PreferencesUtils.putString(mContext,Constant.SP_USER_NAME,loginInfo.getName());
                    PreferencesUtils.putString(mContext,Constant.SP_USER_KEY,loginInfo.getKey());
                    PreferencesUtils.putString(mContext, Constant.SP_USER_REGKEY, loginInfo.getRegkey());
                    if(loginInfo.getParams()!=null) {
                        PreferencesUtils.putString(mContext,Constant.SP_PVERSION,loginInfo.getParams().getPversion());
                        saveParams(loginInfo.getParams());
                    }
                }
                loadDate();
            } else if (result != null && result.getDescription() != null
                    && !result.getDescription().equals("")) {
                if (dialog != null)
                    dialog.dismiss();
                Toast.makeText(mContext, "登录失败，" + result.getDescription(),
                        Toast.LENGTH_SHORT).show();
            } else {
                if (dialog != null)
                    dialog.dismiss();
                Toast.makeText(mContext, "登录失败，请稍后再试!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void loadDate() {
        if (mTurnInTask != null
                && mTurnInTask.getStatus() != AsyncTask.Status.FINISHED)
            mTurnInTask.cancel(true);
        mTurnInTask = new TurnInTask();
        mTurnInTask.execute();
    }
    private class TurnInTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            if (!NetworkUtil.networkIsAvailable(mContext)) {
                return null;
            }

            String url  = CommTools.getRequestUrl(mContext, R.string.trun_in_url);
            HashMap<String, String> hashParams = new HashMap<String, String>();
            hashParams.put("regkey", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
            String result = null;
            try {
                result = HttpHelper.doRequestForString(mContext, url,
                        HttpHelper.HTTP_GET, hashParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int int_undel=0;
            if (dialog != null)
                dialog.dismiss();
            if (result != null) {
                try {
                    ResultInfo<TurnItemInfo> Item = new Gson().fromJson(result, new TypeToken<ResultInfo<TurnItemInfo>>() {
                    }.getType());
                    if (Item != null && Item.getResult() != null
                            && Item.getResult().equals("0")) {
                        TurnItemInfo mTurnItemInfo = ((TurnItemInfo) Item.getBean());
                        String undeal=mTurnItemInfo.getUndeal();
                        if(TextUtils.isEmpty(undeal)){
                            int_undel=0;
                        }else{
                            int_undel=Integer.parseInt(undeal);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            Intent intent=new Intent(mContext,MainActivity.class);
            intent.putExtra("undeal",int_undel);
            startActivity(intent);
            finish();
        }
    }
}
