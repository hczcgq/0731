package com.chen.insurre.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.insurre.R;
import com.chen.insurre.adapter.CityAdapter;
import com.chen.insurre.adapter.ItemAdapter;
import com.chen.insurre.adapter.ProvinceAdapter;
import com.chen.insurre.adapter.TownAdapter;
import com.chen.insurre.bean.CanbaoInfo;
import com.chen.insurre.bean.CityInfo;
import com.chen.insurre.bean.CollectionInfo;
import com.chen.insurre.bean.ItemInfo;
import com.chen.insurre.bean.ParamInfo;
import com.chen.insurre.bean.PersonInfo;
import com.chen.insurre.bean.ProvinceInfo;
import com.chen.insurre.bean.ResultInfo;
import com.chen.insurre.bean.TownInfo;
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
import java.util.List;

/**
 * Created by chenguoquan on 7/30/15.
 */
public class CollectionActivity extends Activity{

    private List<ItemInfo> caijiList;
    private List<ItemInfo> reasonList;
    private List<ProvinceInfo> provsList;
    private List<CityInfo> cityList;
    private List<ItemInfo> stateList;
    private List<ItemInfo> canbaoList;

    private Activity mContext=this;

    private CollectionTask mCollectionTask;

    private ParamTask mParamTask;

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

    private View weicanboaView,waidicanbaoView;

    private String Cbstate; //参保状态

    private String name,cardno;

    private static final int REQUEST_SEARCH=0;
    private static final int REQUEST_SAVE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_collection);

        initView();
        loadParam();
        initEvent();
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

        weicanboaView=findViewById(R.id.weicanboaView);
        waidicanbaoView=findViewById(R.id.waidicanbaoView);
    }

    private void initEvent() {
        WCBprovSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityList=provsList.get(position).getChild();
                WCBcitySpinner.setAdapter(new CityAdapter(mContext,cityList));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        WCBcitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<TownInfo> townList=cityList.get(position).getChild();
                WCBtownSpinner.setAdapter(new TownAdapter(mContext,townList));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        WCBorgProvSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityList = provsList.get(position).getChild();
                WCBorgCitySpinner.setAdapter(new CityAdapter(mContext, cityList));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        WCBorgCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<TownInfo> townList=cityList.get(position).getChild();
                WCBorgTownSpinner.setAdapter(new TownAdapter(mContext,townList));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        WDCBprovSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityList = provsList.get(position).getChild();
                WDCBcitySpinner.setAdapter(new CityAdapter(mContext,cityList));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




    public void Search(View view){
        String cardName=IDCardEdittext.getText().toString();
        if(StringUtil.isEmpty(cardName)){
            ToastUtil.showToastShort(mContext,"身份证不能为空");
            return;
        }
        HashMap<String, String> hashParams = new HashMap<String, String>();
        hashParams.put("cardno", cardName);


        loadData(REQUEST_SEARCH,hashParams);

    }

    private void loadData(int requestCode,HashMap<String, String> hashParams){
        if (mCollectionTask != null
                && mCollectionTask.getStatus() != AsyncTask.Status.FINISHED)
            mCollectionTask.cancel(true);
        mCollectionTask = new CollectionTask(requestCode,hashParams);
        mCollectionTask.execute();
    }

    private void loadParam(){
        if (mParamTask != null
                && mParamTask.getStatus() != AsyncTask.Status.FINISHED)
            mParamTask.cancel(true);
        mParamTask = new ParamTask();
        mParamTask.execute();
    }

    public void SaveClick(View view){
        HashMap<String, String> hashParams = new HashMap<String, String>();
        String jhname=JHRNameEditText.getText().toString();
        String jhcardno=JHRIDCardEditText.getText().toString();
        String jhtelephone=JHRMobileEditText.getText().toString();
        String cbstatus=getSpinnerData(CJSpinner);
        String cbstate=getSpinnerData(CBSpinner);
        String telephone=MobileEditText.getText().toString();
        if(Cbstate.equals("3")){
            String zzJf,ltDy,jnJf,jnDy,qtJf,qtDy,zgYb,jnYb,qtYb;
            String prov=getSpinnerData(WDCBprovSpinner);
            String city=getSpinnerData(WDCBcitySpinner);
            if(WDCBzzJfCheckBox.isChecked()){
                zzJf="1";
            }else{
                zzJf="0";
            }
            if(WDCBltDyCheckBox.isChecked()){
                ltDy="1";
            }else{
                ltDy="0";
            }
            if(WDCBjnJfCheckBox.isChecked()){
                jnJf="1";
            }else{
                jnJf="0";
            }
            if(WDCBjnDyCheckBox.isChecked()){
                jnDy="1";
            }else{
                jnDy="0";
            }
            if(WDCBqtJfCheckBox.isChecked()){
                qtJf="1";
            }else{
                qtJf="0";
            }
            if(WDCBqtDyCheckBox.isChecked()){
                qtDy="1";
            }else{
                qtDy="0";
            }
            if(WDCBzgYbCheckBox.isChecked()){
                zgYb="1";
            }else{
                zgYb="0";
            }
            if(WDCBjnYbCheckBox.isChecked()){
                jnYb="1";
            }else{
                jnYb="0";
            }

            if(WDCBqtYbCheckBox.isChecked()){
                qtYb="1";
            }else{
                qtYb="0";
            }

            hashParams.put("cardno", cardno);
            hashParams.put("name", name);
            hashParams.put("jhname", jhname);
            hashParams.put("jhcardno", jhcardno);
            hashParams.put("jhtelephone", jhtelephone);
            hashParams.put("cbstatus", cbstatus);
            hashParams.put("cbstate", cbstate);
            hashParams.put("telephone", telephone);
            hashParams.put("prov", prov);
            hashParams.put("city", city);
            hashParams.put("zzJf", zzJf);
            hashParams.put("ltDy", ltDy);
            hashParams.put("jnJf", jnJf);
            hashParams.put("jnDy", jnDy);
            hashParams.put("qtJf", qtJf);
            hashParams.put("qtDy", qtDy);
            hashParams.put("zgYb", zgYb);
            hashParams.put("jnYb", jnYb);
            hashParams.put("qtYb", qtYb);
        }else{
            String prov=getSpinnerData(WCBprovSpinner);
            String city=getSpinnerData(WCBcitySpinner);
            String town=getSpinnerData(WCBtownSpinner);
            String addr=WCBaddressEditText.getText().toString();
            String status=getSpinnerData(WCBStatusSpinner);
            String reason=getSpinnerData(WCBReasonSpinner);
            String orgName=WCBorgNameEditText.getText().toString();
            String orgProv=getSpinnerData(WCBorgProvSpinner);
            String orgCity=getSpinnerData(WCBorgCitySpinner);
            String orgTown=getSpinnerData(WCBorgTownSpinner);
            String orgAddr=WCBorgAddrEditText.getText().toString();
            String canbao;
            if(WCBCheckBox.isChecked()){
                canbao="Y";
            }else{
                canbao="N";
            }

            hashParams.put("cardno", cardno);
            hashParams.put("name", name);
            hashParams.put("jhname", jhname);
            hashParams.put("jhcardno", jhcardno);
            hashParams.put("jhtelephone", jhtelephone);
            hashParams.put("cbstatus", cbstatus);
            hashParams.put("cbstate", cbstate);
            hashParams.put("telephone", telephone);
            hashParams.put("prov", prov);
            hashParams.put("city", city);
            hashParams.put("town", town);
            hashParams.put("addr", addr);
            hashParams.put("status", status);
            hashParams.put("reason", reason);
            hashParams.put("orgName", orgName);
            hashParams.put("orgProv", orgProv);
            hashParams.put("orgCity", orgCity);
            hashParams.put("orgTown", orgTown);
            hashParams.put("orgAddr", orgAddr);
            hashParams.put("canbao", canbao);
        }

        loadData(REQUEST_SAVE,hashParams);
    }

    public void ResetClick(View view){

    }

    private class CollectionTask extends AsyncTask<String, Void, ResultInfo> {
        private Dialog dialog;

        private int requestCode;

        HashMap<String, String> hashParams;

        public CollectionTask(int requestCode, HashMap<String, String> hashParams) {
            this.requestCode=requestCode;
            this.hashParams=hashParams;
        }

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

            String url = CommTools.getRequestUrl(mContext, R.string.conllection_url);
            hashParams.put("regkey", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
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
                if(requestCode==REQUEST_SEARCH) {
                    CollectionInfo info = (CollectionInfo) result.getBean();
                    if (info != null) {
                        ContentLayout.setVisibility(View.VISIBLE);
                        ShowContentInfo(info);
                    }
                }else{
                    Log.d("chen","save---------------------");
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


    private class ParamTask extends AsyncTask<String, Void, ResultInfo> {


        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ResultInfo doInBackground(String... params) {
            if (!NetworkUtil.networkIsAvailable(mContext)) {
                return null;
            }
            HashMap<String, String> hashParams = new HashMap<String, String>();
            String url = CommTools.getRequestUrl(mContext, R.string.param_url);
            hashParams.put("regkey", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
            hashParams.put("r","Y");
            ResultInfo resultInfo = null;
            try {
                String result = HttpHelper.doRequestForString(mContext, url,
                        HttpHelper.HTTP_GET, hashParams);
                resultInfo = new Gson().fromJson(result ,new TypeToken<ResultInfo<ParamInfo>>(){}.getType());
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
                ParamInfo paramInfo= (ParamInfo) result.getBean();
                caijiList=paramInfo.getCaiji();
                reasonList=paramInfo.getReason();
                provsList=paramInfo.getProvs();
                stateList=paramInfo.getState();
                canbaoList=paramInfo.getCanbao();

                CJSpinner.setAdapter(new ItemAdapter(mContext,caijiList));
                CBSpinner.setAdapter(new ItemAdapter(mContext,canbaoList));


                WCBprovSpinner.setAdapter(new ProvinceAdapter(mContext,provsList));
                WCBStatusSpinner.setAdapter(new ItemAdapter(mContext,stateList));
                WCBReasonSpinner.setAdapter(new ItemAdapter(mContext,reasonList));

                WCBorgProvSpinner.setAdapter(new ProvinceAdapter(mContext,provsList));

                WDCBprovSpinner.setAdapter(new ProvinceAdapter(mContext,provsList));

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

        cardno=IDCardEdittext.getText().toString();
        name=NameTextView.getText().toString();


        CanbaoInfo canbaoInfo=info.getCanbaoInfo();
        JHRNameEditText.setText(canbaoInfo.getJhname());
        JHRIDCardEditText.setText(canbaoInfo.getJhcardno());
        JHRMobileEditText.setText(canbaoInfo.getJhtelephone());
        setSpinnerData(CJSpinner,canbaoInfo.getCbstatus());
        setSpinnerData(CBSpinner,canbaoInfo.getCbstate());
        MobileEditText.setText(canbaoInfo.getTelephone());

        Cbstate=canbaoInfo.getCbstate();

        if(Cbstate.equals("3")){
            weicanboaView.setVisibility(View.GONE);
            waidicanbaoView.setVisibility(View.VISIBLE);
        }else{
            weicanboaView.setVisibility(View.VISIBLE);
            waidicanbaoView.setVisibility(View.GONE);
        }

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
