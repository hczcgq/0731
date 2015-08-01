package com.chen.insurre.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.insurre.R;
import com.chen.insurre.bean.CanbaoInfo;
import com.chen.insurre.bean.CollectionInfo;
import com.chen.insurre.bean.PersonInfo;
import com.chen.insurre.bean.ResultInfo;
import com.chen.insurre.http.HttpHelper;
import com.chen.insurre.util.CommTools;
import com.chen.insurre.util.Constant;
import com.chen.insurre.util.NetworkUtil;
import com.chen.insurre.util.PreferencesUtils;
import com.chen.insurre.util.StringUtil;
import com.chen.insurre.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * Created by chenguoquan on 7/30/15.
 */
public class CollectionActivity extends Activity{

    private Activity mContext=this;

    private CollectionTask mCollectionTask;

    private EditText IDCardEdittext;

    private LinearLayout ContentLayout;

    private TextView NameTextView,SexTextView,BirthTextView,AgeTextView,RoonNoTextView
                     ,ContractTextView,RegionTextView,AreaTextView,PropTextView,LocationTextView;

    //参保
    private EditText JHRNameEditText,JHRIDCardEditText,JHRMobileEditText,MobileEditText;
    private Spinner CJSpinner,CBSpinner;
    //未参保
    private Spinner WCBprovSpinner,WCBcitySpinner,WCBtownSpinner,WCBStatusSpinner,WCBReasonSpinner
                    ,WCBorgProvSpinner,WCBorgCitySpinner,WCBorgTownSpinner;
    private EditText WCBaddressEditText,WCBorgNameEditText,WCBorgAddrEditText;
    private CheckBox WCBCheckBox;
    //外地参保
    private Spinner WDCBprovSpinner,WDCBcitySpinner;
    private CheckBox WDCBzzJfCheckBox,WDCBltDyCheckBox,WDCBjnJfCheckBox,WDCBjnDyCheckBox,WDCBqtJfCheckBox
                     ,WDCBqtDyCheckBox,WDCBzgYbCheckBox,WDCBjnYbCheckBox,WDCBqtYbCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_collection);

        initView();
    }

    private void initView() {
        IDCardEdittext= (EditText) findViewById(R.id.IDCardEdittext);
        ContentLayout= (LinearLayout) findViewById(R.id.ContentLayout);
        NameTextView= (TextView) findViewById(R.id.NameTextView);
        SexTextView= (TextView) findViewById(R.id.SexTextView);
        BirthTextView= (TextView) findViewById(R.id.BirthTextView);
        AgeTextView= (TextView) findViewById(R.id.AgeTextView);
        RoonNoTextView= (TextView) findViewById(R.id.RoonNoTextView);
        ContractTextView= (TextView) findViewById(R.id.ContractTextView);
        RegionTextView= (TextView) findViewById(R.id.RegionTextView);
        AreaTextView= (TextView) findViewById(R.id.AreaTextView);
        PropTextView= (TextView) findViewById(R.id.PropTextView);
        LocationTextView= (TextView) findViewById(R.id.LocationTextView);

        JHRNameEditText= (EditText) findViewById(R.id.JHRNameEditText);
        JHRIDCardEditText= (EditText) findViewById(R.id.JHRIDCardEditText);
        JHRMobileEditText= (EditText) findViewById(R.id.JHRMobileEditText);
        MobileEditText= (EditText) findViewById(R.id.MobileEditText);
        CJSpinner= (Spinner) findViewById(R.id.CJSpinner);
        CBSpinner= (Spinner) findViewById(R.id.CBSpinner);

        WCBprovSpinner= (Spinner) findViewById(R.id.WCBprovSpinner);
        WCBcitySpinner= (Spinner) findViewById(R.id.WCBcitySpinner);
        WCBtownSpinner= (Spinner) findViewById(R.id.WCBtownSpinner);
        WCBStatusSpinner= (Spinner) findViewById(R.id.WCBStatusSpinner);
        WCBReasonSpinner= (Spinner) findViewById(R.id.WCBReasonSpinner);
        WCBorgProvSpinner= (Spinner) findViewById(R.id.WCBorgProvSpinner);
        WCBorgCitySpinner= (Spinner) findViewById(R.id.WCBorgCitySpinner);
        WCBorgTownSpinner= (Spinner) findViewById(R.id.WCBorgTownSpinner);
        WCBaddressEditText= (EditText) findViewById(R.id.WCBaddressEditText);
        WCBorgNameEditText= (EditText) findViewById(R.id.WCBorgNameEditText);
        WCBorgAddrEditText= (EditText) findViewById(R.id.WCBorgAddrEditText);
        WCBCheckBox= (CheckBox) findViewById(R.id.WCBCheckBox);

        WDCBprovSpinner= (Spinner) findViewById(R.id.WDCBprovSpinner);
        WDCBcitySpinner= (Spinner) findViewById(R.id.WDCBcitySpinner);
        WDCBzzJfCheckBox= (CheckBox) findViewById(R.id.WDCBzzJfCheckBox);
        WDCBltDyCheckBox= (CheckBox) findViewById(R.id.WDCBltDyCheckBox);
        WDCBjnJfCheckBox= (CheckBox) findViewById(R.id.WDCBjnJfCheckBox);
        WDCBjnDyCheckBox= (CheckBox) findViewById(R.id.WDCBjnDyCheckBox);
        WDCBqtJfCheckBox= (CheckBox) findViewById(R.id.WDCBqtJfCheckBox);
        WDCBqtDyCheckBox= (CheckBox) findViewById(R.id.WDCBqtDyCheckBox);
        WDCBzgYbCheckBox= (CheckBox) findViewById(R.id.WDCBzgYbCheckBox);
        WDCBjnYbCheckBox= (CheckBox) findViewById(R.id.WDCBjnYbCheckBox);
        WDCBqtYbCheckBox= (CheckBox) findViewById(R.id.WDCBqtYbCheckBox);
    }


    public void Search(View view){
        String cardName=IDCardEdittext.getText().toString();
        if(StringUtil.isEmpty(cardName)){
            ToastUtil.showToastShort(mContext,"身份证不能为空");
            return;
        }

        if (mCollectionTask != null
                && mCollectionTask.getStatus() != AsyncTask.Status.FINISHED)
            mCollectionTask.cancel(true);
        mCollectionTask = new CollectionTask();
        mCollectionTask.execute(cardName);

    }

    private class CollectionTask extends AsyncTask<String, Void, ResultInfo> {
        private Dialog dialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            try {
                dialog = ProgressDialog.show(mContext, "请稍后",
                        "正在加载...");
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
            String cardid = params[0];
            String url = CommTools.getRequestUrl(mContext, R.string.conllection_url);
            HashMap<String, String> hashParams = new HashMap<String, String>();
            hashParams.put("regkey", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
            hashParams.put("cardno", cardid);
            ResultInfo resultInfo = null;
            try {
                String result = HttpHelper.doRequestForString(mContext, url,
                        HttpHelper.HTTP_GET, hashParams);

                resultInfo = new Gson().fromJson(result ,new TypeToken<ResultInfo<CollectionInfo>>(){}.getType());
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
                CollectionInfo info= (CollectionInfo) result.getBean();
                if(info!=null){
                    ContentLayout.setVisibility(View.VISIBLE);
                    ShowContentInfo(info);
                }
            } else if (result != null && result.getDescription() != null
                    && !result.getDescription().equals("")) {
                Toast.makeText(mContext, "请求失败，" + result.getDescription(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "请求失败，请稍后再试!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void ShowContentInfo(CollectionInfo info) {
        Log.e("33",info.getPersonInfo().getLocation());
        //户籍信息
        PersonInfo personInfo=info.getPersonInfo();
        NameTextView.setText(personInfo.getName());
        SexTextView.setText(personInfo.getSex());
        BirthTextView.setText(personInfo.getBirthday());
        AgeTextView.setText(personInfo.getAge());
        RoonNoTextView.setText(personInfo.getRoomno());
        ContractTextView.setText(personInfo.getContract());
        RegionTextView.setText(personInfo.getRegion());
        AreaTextView.setText(personInfo.getArea());
        PropTextView.setText(personInfo.getProp());
        LocationTextView.setText(personInfo.getLocation());

        CanbaoInfo canbaoInfo=info.getCanbaoInfo();
        JHRNameEditText.setText(canbaoInfo.getJhname());
        JHRIDCardEditText.setText(canbaoInfo.getJhcardno());
        JHRMobileEditText.setText(canbaoInfo.getJhtelephone());
        setSpinnerData(CJSpinner,canbaoInfo.getCbstatus());
        setSpinnerData(CBSpinner,canbaoInfo.getCbstate());
        MobileEditText.setText(canbaoInfo.getTelephone());

    }

    public void setSpinnerData(Spinner spinner, String text) {
        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            String item = spinner.getAdapter().getItem(i).toString();
            if (item.equals(text)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public String getSpinnerData(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }
}
