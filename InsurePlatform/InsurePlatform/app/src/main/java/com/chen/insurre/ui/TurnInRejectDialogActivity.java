package com.chen.insurre.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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
public class TurnInRejectDialogActivity extends Activity{

    private int mScreenHeight;

    private String name,cardno;

    private TextView decribeTextView;

    private Activity mContext=this;

    private RadioGroup ReasonRadioGroup;

    private EditText OtherEdittext;

    private String reason="0";

    private RejectTask mRejectTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_turn_in_detail_dialog_reject);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;

        name=getIntent().getExtras().getString("name");
        cardno=getIntent().getExtras().getString("cardno");

        OtherEdittext= (EditText) findViewById(R.id.OtherEdittext);
        decribeTextView= (TextView) findViewById(R.id.decribeTextView);
        decribeTextView.setText("您将拒绝"+name+"的转入，请选择或输入拒绝原因");
        ReasonRadioGroup= (RadioGroup) findViewById(R.id.ReasonRadioGroup);
        ReasonRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.RadioButton1){
                    reason="非本区人员";
                }else if(checkedId==R.id.RadioButton2){
                    reason="查无此人";
                }else if(checkedId==R.id.RadioButton3){
                    reason=OtherEdittext.getText().toString();
                }
            }
        });
    }

    public void ComfirmClick(View view){

        if (!NetworkUtil.networkIsAvailable(mContext)) {
            ToastUtil.showToastShort(this, "请检查网络连接状态。");
            return ;
        }

        if (mRejectTask != null
                && mRejectTask.getStatus() != AsyncTask.Status.FINISHED)
            mRejectTask.cancel(true);
        mRejectTask = new RejectTask();
        mRejectTask.execute();
    }

    public void CancelClick(View view){
        finish();
    }


    private class RejectTask extends AsyncTask<String, Void, ResultInfo> {
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
            hashParams.put("deal", "reject");
            hashParams.put("cardno",cardno);
            hashParams.put("reason",reason);
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
                Toast.makeText(mContext,result.getDescription(),
                        Toast.LENGTH_SHORT).show();
                finish();
            } else if (result != null && result.getDescription() != null
                    && !result.getDescription().equals("")) {
                Toast.makeText(mContext, "拒绝失败，" + result.getDescription(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "拒绝失败，请稍后再试!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
