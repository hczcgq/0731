package com.chen.insurre.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.insurre.R;
import com.chen.insurre.bean.ResultInfo;
import com.chen.insurre.http.HttpHelper;
import com.chen.insurre.util.CommTools;
import com.chen.insurre.util.Constant;
import com.chen.insurre.util.NetworkUtil;
import com.chen.insurre.util.PreferencesUtils;
import com.chen.insurre.util.ToastUtil;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by chenguoquan on 8/3/15.
 */
public class TurnInReceiveDialogActivity extends Activity{

    private int mScreenHeight;

    private String name,cardno;

    private TextView decribeTextView;

    private Activity mContext=this;

    private ReceiveTask mReceiveTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_turn_in_detail_dialog_receive);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;

        name=getIntent().getExtras().getString("name");
        cardno=getIntent().getExtras().getString("cardno");

        decribeTextView= (TextView) findViewById(R.id.decribeTextView);
        decribeTextView.setText("您是否确认接收"+name+"的转入");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void ComfirmClick(View view){
        if (!NetworkUtil.networkIsAvailable(mContext)) {
            ToastUtil.showToastShort(this, "请检查网络连接状态。");
            return ;
        }

        if (mReceiveTask != null
                && mReceiveTask.getStatus() != AsyncTask.Status.FINISHED)
            mReceiveTask.cancel(true);
        mReceiveTask = new ReceiveTask();
        mReceiveTask.execute();
    }

    public void CancelClick(View view){
        TurnInActivity.fromDialog=true;
        finish();
    }

    private class ReceiveTask extends AsyncTask<String, Void, ResultInfo> {
        private Dialog dialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            try {
                dialog = ProgressDialog.show(mContext, "请稍后",
                        "正在请求...");
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
            String url = CommTools.getRequestUrl(mContext, R.string.trun_in_url);
            HashMap<String, String> hashParams = new HashMap<String, String>();
            hashParams.put("regkey", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
            hashParams.put("deal", "receive");
            hashParams.put("cardno",cardno);
            ResultInfo resultInfo = null;
            try {
                String result = HttpHelper.doRequestForString(mContext, url,
                        HttpHelper.HTTP_POST, hashParams);
                resultInfo = new Gson().fromJson(result, ResultInfo.class);
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
                Toast.makeText(mContext, result.getDescription(),
                        Toast.LENGTH_SHORT).show();
                TurnInActivity.fromDialog=false;
                finish();
            } else if (result != null && result.getDescription() != null
                    && !result.getDescription().equals("")) {
                Toast.makeText(mContext, result.getDescription(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "接收失败，请稍后再试!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
