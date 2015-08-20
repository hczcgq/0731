package com.chen.insurre.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.chen.insurre.MyApplication;
import com.chen.insurre.R;
import com.chen.insurre.adapter.CityAdapter;
import com.chen.insurre.adapter.ItemAdapter;
import com.chen.insurre.adapter.ProvinceAdapter;
import com.chen.insurre.adapter.TownAdapter;
import com.chen.insurre.bean.CanbaoInfo;
import com.chen.insurre.bean.CityInfo;
import com.chen.insurre.bean.CollectionInfo;
import com.chen.insurre.bean.ItemInfo;
import com.chen.insurre.bean.PersonInfo;
import com.chen.insurre.bean.ProvinceInfo;
import com.chen.insurre.bean.ResultInfo;
import com.chen.insurre.bean.TownInfo;
import com.chen.insurre.bean.WaidiCanbaoInfo;
import com.chen.insurre.bean.WeiCanbaoInfo;
import com.chen.insurre.http.HttpHelper;
import com.chen.insurre.util.CommTools;
import com.chen.insurre.util.Constant;
import com.chen.insurre.util.NetworkUtil;
import com.chen.insurre.util.PreferencesUtils;
import com.chen.insurre.util.StringUtil;
import com.chen.insurre.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenguoquan on 7/30/15.
 */
public class CollectionActivity extends Activity {

    private List<ItemInfo> caijiList;
    private List<ItemInfo> reasonList;
    private List<ProvinceInfo> provsList;
    private List<CityInfo> cityList;
    private List<TownInfo> townList;
    private List<ItemInfo> stateList;
    private List<ItemInfo> canbaoList;
    private List<ProvinceInfo> OrgprovsList;
    private List<CityInfo> OrgcityList;
    private List<TownInfo> OrgtownList;
    private List<ProvinceInfo> WdcbprovsList;
    private List<CityInfo> WdcbcityList;

    private Activity mContext = this;

    private CollectionTask mCollectionTask;

    private EditText IDCardEdittext;

    private LinearLayout ContentLayout;

    private TextView NameTextView, SexTextView, BirthTextView, AgeTextView, RoonNoTextView, ContractTextView, RegionTextView, AreaTextView, PropTextView, LocationTextView;

    //参保
    private EditText JHRNameEditText, JHRIDCardEditText, JHRMobileEditText, MobileEditText, DescribeEditText;
    private Spinner CJSpinner, CBSpinner;
    //未参保
    private Spinner WCBprovSpinner, WCBcitySpinner, WCBtownSpinner, WCBStatusSpinner, WCBReasonSpinner, WCBorgProvSpinner, WCBorgCitySpinner, WCBorgTownSpinner;
    private EditText WCBaddressEditText, WCBorgNameEditText, WCBorgAddrEditText;
    private CheckBox WCBCheckBox;
    //外地参保
    private Spinner WDCBprovSpinner, WDCBcitySpinner;
    private CheckBox WDCBzzJfCheckBox, WDCBltDyCheckBox, WDCBjnJfCheckBox, WDCBjnDyCheckBox, WDCBqtJfCheckBox, WDCBqtDyCheckBox, WDCBzgYbCheckBox, WDCBjnYbCheckBox, WDCBqtYbCheckBox;

    private View canbaoView, weicanboaView, waidicanbaoView;

    private Button saveButton, resetButton;

    private ScrollView myScrollView;

    private String name, cardno;

    private static final int REQUEST_SEARCH = 0;
    private static final int REQUEST_SAVE = 1;


    //采集信息
    private String cbstatus, cbstate;

    //未参保
    private String prov, city, town, status, reason, orgProv, orgCity, orgTown, wdcbPro, WdcbCity;


    private CollectionInfo tempCollectionInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_collection);

        initView();
        initEvent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hidenKeyboard();
    }

    private void initView() {
        IDCardEdittext = (EditText) findViewById(R.id.IDCardEdittext);
        ContentLayout = (LinearLayout) findViewById(R.id.ContentLayout);
        NameTextView = (TextView) findViewById(R.id.NameTextView);
        SexTextView = (TextView) findViewById(R.id.SexTextView);
        BirthTextView = (TextView) findViewById(R.id.BirthTextView);
        AgeTextView = (TextView) findViewById(R.id.AgeTextView);
        RoonNoTextView = (TextView) findViewById(R.id.RoonNoTextView);
        ContractTextView = (TextView) findViewById(R.id.ContractTextView);
        RegionTextView = (TextView) findViewById(R.id.RegionTextView);
        AreaTextView = (TextView) findViewById(R.id.AreaTextView);
        PropTextView = (TextView) findViewById(R.id.PropTextView);
        LocationTextView = (TextView) findViewById(R.id.LocationTextView);

        JHRNameEditText = (EditText) findViewById(R.id.JHRNameEditText);
        JHRIDCardEditText = (EditText) findViewById(R.id.JHRIDCardEditText);
        JHRMobileEditText = (EditText) findViewById(R.id.JHRMobileEditText);
        MobileEditText = (EditText) findViewById(R.id.MobileEditText);
        DescribeEditText = (EditText) findViewById(R.id.DescribeEditText);
        CJSpinner = (Spinner) findViewById(R.id.CJSpinner);
        CBSpinner = (Spinner) findViewById(R.id.CBSpinner);

        WCBprovSpinner = (Spinner) findViewById(R.id.WCBprovSpinner);
        WCBcitySpinner = (Spinner) findViewById(R.id.WCBcitySpinner);
        WCBtownSpinner = (Spinner) findViewById(R.id.WCBtownSpinner);
        WCBStatusSpinner = (Spinner) findViewById(R.id.WCBStatusSpinner);
        WCBReasonSpinner = (Spinner) findViewById(R.id.WCBReasonSpinner);
        WCBorgProvSpinner = (Spinner) findViewById(R.id.WCBorgProvSpinner);
        WCBorgCitySpinner = (Spinner) findViewById(R.id.WCBorgCitySpinner);
        WCBorgTownSpinner = (Spinner) findViewById(R.id.WCBorgTownSpinner);
        WCBaddressEditText = (EditText) findViewById(R.id.WCBaddressEditText);
        WCBorgNameEditText = (EditText) findViewById(R.id.WCBorgNameEditText);
        WCBorgAddrEditText = (EditText) findViewById(R.id.WCBorgAddrEditText);
        WCBCheckBox = (CheckBox) findViewById(R.id.WCBCheckBox);

        WDCBprovSpinner = (Spinner) findViewById(R.id.WDCBprovSpinner);
        WDCBcitySpinner = (Spinner) findViewById(R.id.WDCBcitySpinner);
        WDCBzzJfCheckBox = (CheckBox) findViewById(R.id.WDCBzzJfCheckBox);
        WDCBltDyCheckBox = (CheckBox) findViewById(R.id.WDCBltDyCheckBox);
        WDCBjnJfCheckBox = (CheckBox) findViewById(R.id.WDCBjnJfCheckBox);
        WDCBjnDyCheckBox = (CheckBox) findViewById(R.id.WDCBjnDyCheckBox);
        WDCBqtJfCheckBox = (CheckBox) findViewById(R.id.WDCBqtJfCheckBox);
        WDCBqtDyCheckBox = (CheckBox) findViewById(R.id.WDCBqtDyCheckBox);
        WDCBzgYbCheckBox = (CheckBox) findViewById(R.id.WDCBzgYbCheckBox);
        WDCBjnYbCheckBox = (CheckBox) findViewById(R.id.WDCBjnYbCheckBox);
        WDCBqtYbCheckBox = (CheckBox) findViewById(R.id.WDCBqtYbCheckBox);

        canbaoView = findViewById(R.id.canbaoView);
        weicanboaView = findViewById(R.id.weicanboaView);
        waidicanbaoView = findViewById(R.id.waidicanbaoView);

        myScrollView = (ScrollView) findViewById(R.id.myScrollView);

        saveButton = (Button) findViewById(R.id.saveButton);
        resetButton = (Button) findViewById(R.id.resetButton);
    }

    private void initEvent() {
        //采集
        CJSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (caijiList == null || caijiList.size() == 0) {
                    return;
                }
                cbstatus = caijiList.get(position).getId();
                if(canbaoList!=null&&canbaoList.size()>0) {
                    setSpinnerData(CBSpinner, canbaoList.get(0).getId());
                }
                if (cbstatus.equals("1")) {
                    if (cbstate.equals("3")) {
                        weicanboaView.setVisibility(View.GONE);
                        waidicanbaoView.setVisibility(View.VISIBLE);
                    } else {
                        weicanboaView.setVisibility(View.VISIBLE);
                        waidicanbaoView.setVisibility(View.GONE);
                    }
                } else {
                    setSpinnerData(CBSpinner, "");
                    weicanboaView.setVisibility(View.GONE);
                    waidicanbaoView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //参保
        CBSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (canbaoList == null || canbaoList.size() == 0) {
                    return;
                }
                cbstate = canbaoList.get(position).getId();
                if (cbstatus.equals("1")) {
                    if (canbaoList.get(position).getName().contains("在外地参保")) {
                        weicanboaView.setVisibility(View.GONE);
                        waidicanbaoView.setVisibility(View.VISIBLE);
                    } else {
                        weicanboaView.setVisibility(View.VISIBLE);
                        waidicanbaoView.setVisibility(View.GONE);
                    }
                }else{
                    weicanboaView.setVisibility(View.GONE);
                    waidicanbaoView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //常住地省
        WCBprovSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (provsList == null || provsList.size() == 0) {
                    return;
                }
                cityList = provsList.get(position).getChild();
                if (cityList == null) {
                    cityList = new ArrayList<CityInfo>();
                    city = "";
                    townList = new ArrayList<TownInfo>();
                    WCBtownSpinner.setAdapter(new TownAdapter(mContext, townList));
                    town = "";
                }
                WCBcitySpinner.setAdapter(new CityAdapter(mContext, cityList));
                setSpinnerData(WCBcitySpinner, city);
                prov = provsList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //常住地市
        WCBcitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (cityList == null || cityList.size() == 0) {
                    return;
                }
                townList = cityList.get(position).getChild();
                if (townList == null) {
                    townList = new ArrayList<TownInfo>();
                    town = "";
                }
                WCBtownSpinner.setAdapter(new TownAdapter(mContext, townList));
                setSpinnerData(WCBtownSpinner, town);
                city = cityList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //常住地区县
        WCBtownSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (townList == null || townList.size() == 0) {
                    return;
                }
                town = townList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //未参保状态
        WCBStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (stateList == null || stateList.size() == 0) {
                    return;
                }
                status = stateList.get(position).getId();

                if(TextUtils.isEmpty(tempCollectionInfo.getWeiCanbaoInfo().getStatus())) {
                    if (status.equals("2") || status.equals("3")) {
                        orgProv = "320000";
                        orgCity = "320500";
                        orgTown = "";

                    } else {
                        orgProv = "";
                        orgCity = "";
                        orgTown = "";
                    }
                    setSpinnerData(WCBorgProvSpinner, orgProv);
                    setSpinnerData(WCBorgCitySpinner, orgCity);
                    setSpinnerData(WCBorgTownSpinner, orgTown);
                }else{
                    if (status.equals("2") || status.equals("3")) {
                        orgProv = tempCollectionInfo.getWeiCanbaoInfo().getOrgProv();
                        orgCity = tempCollectionInfo.getWeiCanbaoInfo().getOrgCity();
                        orgTown = tempCollectionInfo.getWeiCanbaoInfo().getOrgTown();

                    } else {
                        orgProv = "";
                        orgCity = "";
                        orgTown = "";
                    }
                    setSpinnerData(WCBorgProvSpinner, orgProv);
                    setSpinnerData(WCBorgCitySpinner, orgCity);
                    setSpinnerData(WCBorgTownSpinner, orgTown);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //未参保原因
        WCBReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (reasonList == null || reasonList.size() == 0) {
                    return;
                }
                reason = reasonList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //学校省
        WCBorgProvSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (OrgprovsList == null || OrgprovsList.size() == 0) {
                    return;
                }
                OrgcityList = OrgprovsList.get(position).getChild();
                if (OrgcityList == null) {
                    OrgcityList = new ArrayList<CityInfo>();
                    orgCity = "";
                    OrgtownList = new ArrayList<TownInfo>();
                    WCBorgTownSpinner.setAdapter(new TownAdapter(mContext, OrgtownList));
                    orgTown = "";
                }
                WCBorgCitySpinner.setAdapter(new CityAdapter(mContext, OrgcityList));
                setSpinnerData(WCBorgCitySpinner, orgCity);
                orgProv = OrgprovsList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //学校市
        WCBorgCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (OrgcityList == null || OrgcityList.size() == 0) {
                    return;
                }
                OrgtownList = OrgcityList.get(position).getChild();
                if (OrgtownList == null) {
                    OrgtownList = new ArrayList<TownInfo>();
                    orgTown = "";
                }
                WCBorgTownSpinner.setAdapter(new TownAdapter(mContext, OrgtownList));
                setSpinnerData(WCBorgTownSpinner, orgTown);
                orgCity = OrgcityList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //学校区县
        WCBorgTownSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (OrgtownList == null || OrgtownList.size() == 0) {
                    return;
                }
                orgTown = OrgtownList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //外地参保
        WDCBprovSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (WdcbprovsList == null || WdcbprovsList.size() == 0) {
                    return;
                }
                WdcbcityList = WdcbprovsList.get(position).getChild();
                if (WdcbcityList == null) {
                    WdcbcityList = new ArrayList<CityInfo>();
                    WdcbCity = "";
                }
                WDCBcitySpinner.setAdapter(new CityAdapter(mContext, WdcbcityList));
                setSpinnerData(WDCBcitySpinner, WdcbCity);
                wdcbPro = WdcbprovsList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        WDCBcitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (WdcbcityList == null || WdcbcityList.size() == 0) {
                    return;
                }
                WdcbCity = WdcbcityList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void hidenKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(IDCardEdittext.getWindowToken(), 0); //隐藏
    }


    public void Search(View view) {


        String cardName = IDCardEdittext.getText().toString();
        if (StringUtil.isEmpty(cardName)) {
            ToastUtil.showToastShort(mContext, "身份证不能为空");
            return;
        }
        HashMap<String, String> hashParams = new HashMap<String, String>();
        hashParams.put("cardno", cardName);
        hidenKeyboard();
        loadData(REQUEST_SEARCH, hashParams);

    }

    private void loadData(int requestCode, HashMap<String, String> hashParams) {
        if (!NetworkUtil.networkIsAvailable(mContext)) {
            ToastUtil.showToastShort(this, "请检查网络连接状态。");
            return;
        }

        if (mCollectionTask != null
                && mCollectionTask.getStatus() != AsyncTask.Status.FINISHED)
            mCollectionTask.cancel(true);
        mCollectionTask = new CollectionTask(requestCode, hashParams);
        mCollectionTask.execute();
    }

    public void SaveClick(View view) {
        HashMap<String, String> hashParams = new HashMap<String, String>();
        String jhname = JHRNameEditText.getText().toString();
        String jhcardno = JHRIDCardEditText.getText().toString();
        String jhtelephone = JHRMobileEditText.getText().toString();
        String telephone = MobileEditText.getText().toString();
        String comment = DescribeEditText.getText().toString();
        hashParams.put("cardno", cardno);
        hashParams.put("name", name);
        hashParams.put("jhname", jhname);
        hashParams.put("jhcardno", jhcardno);
        hashParams.put("jhtelephone", jhtelephone);
        hashParams.put("cbstatus", cbstatus);
        hashParams.put("cbstate", cbstate);
        hashParams.put("telephone", telephone);
        hashParams.put("comment", comment);
        if (cbstatus.equals("1")) {
            if (cbstate.equals("3")) {
                String zzJf, ltDy, jnJf, jnDy, qtJf, qtDy, zgYb, jnYb, qtYb;
                if (WDCBzzJfCheckBox.isChecked()) {
                    zzJf = "1";
                } else {
                    zzJf = "0";
                }
                if (WDCBltDyCheckBox.isChecked()) {
                    ltDy = "1";
                } else {
                    ltDy = "0";
                }
                if (WDCBjnJfCheckBox.isChecked()) {
                    jnJf = "1";
                } else {
                    jnJf = "0";
                }
                if (WDCBjnDyCheckBox.isChecked()) {
                    jnDy = "1";
                } else {
                    jnDy = "0";
                }
                if (WDCBqtJfCheckBox.isChecked()) {
                    qtJf = "1";
                } else {
                    qtJf = "0";
                }
                if (WDCBqtDyCheckBox.isChecked()) {
                    qtDy = "1";
                } else {
                    qtDy = "0";
                }
                if (WDCBzgYbCheckBox.isChecked()) {
                    zgYb = "1";
                } else {
                    zgYb = "0";
                }
                if (WDCBjnYbCheckBox.isChecked()) {
                    jnYb = "1";
                } else {
                    jnYb = "0";
                }

                if (WDCBqtYbCheckBox.isChecked()) {
                    qtYb = "1";
                } else {
                    qtYb = "0";
                }

                hashParams.put("prov", wdcbPro);
                hashParams.put("city", WdcbCity);
                hashParams.put("zzJf", zzJf);
                hashParams.put("ltDy", ltDy);
                hashParams.put("jnJf", jnJf);
                hashParams.put("jnDy", jnDy);
                hashParams.put("qtJf", qtJf);
                hashParams.put("qtDy", qtDy);
                hashParams.put("zgYb", zgYb);
                hashParams.put("jnYb", jnYb);
                hashParams.put("qtYb", qtYb);
            } else {
                String addr = WCBaddressEditText.getText().toString();
                String orgName = WCBorgNameEditText.getText().toString();
                String orgAddr = WCBorgAddrEditText.getText().toString();
                String canbao;
                if (WCBCheckBox.isChecked()) {
                    canbao = "Y";
                } else {
                    canbao = "N";
                }
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
        }

        if (!NetworkUtil.networkIsAvailable(mContext)) {
            ToastUtil.showToastShort(this, "请检查网络连接状态。");
            return;
        }

        loadData(REQUEST_SAVE, hashParams);
    }

    public void ResetClick(View view) {
        ShowContentInfo(tempCollectionInfo);
    }

    private class CollectionTask extends AsyncTask<String, Void, ResultInfo> {
        private Dialog dialog;

        private int requestCode;

        HashMap<String, String> hashParams;

        public CollectionTask(int requestCode, HashMap<String, String> hashParams) {
            this.requestCode = requestCode;
            this.hashParams = hashParams;
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
            int remoteType = HttpHelper.HTTP_GET;
            if (requestCode == REQUEST_SEARCH) {
                remoteType = HttpHelper.HTTP_GET;
            } else {
                remoteType = HttpHelper.HTTP_POST;
            }
            try {
                String result = HttpHelper.doRequestForString(mContext, url,
                        remoteType, hashParams);

                resultInfo = new Gson().fromJson(result, new TypeToken<ResultInfo<CollectionInfo>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultInfo;
        }

        @Override
        protected void onPostExecute(ResultInfo result) {
            super.onPostExecute(result);
            if (requestCode == REQUEST_SEARCH) {

                caijiList = MyApplication.getInstance().getCaijiList();
                reasonList = MyApplication.getInstance().getReasonList();
                provsList = MyApplication.getInstance().getProvsList();
                stateList = MyApplication.getInstance().getStateList();
                canbaoList = MyApplication.getInstance().getCanbaoList();
                OrgprovsList = provsList;
                WdcbprovsList = provsList;
                if (caijiList != null) {
                    CJSpinner.setAdapter(new ItemAdapter(mContext, caijiList));
                }
                if (canbaoList != null) {
                    CBSpinner.setAdapter(new ItemAdapter(mContext, canbaoList));
                }
                if (stateList != null) {
                    WCBStatusSpinner.setAdapter(new ItemAdapter(mContext, stateList));
                }
                if (reasonList != null) {
                    WCBReasonSpinner.setAdapter(new ItemAdapter(mContext, reasonList));
                }
                if (provsList != null) {
                    WCBprovSpinner.setAdapter(new ProvinceAdapter(mContext, provsList));
                    WCBorgProvSpinner.setAdapter(new ProvinceAdapter(mContext, OrgprovsList));
                    WDCBprovSpinner.setAdapter(new ProvinceAdapter(mContext, WdcbprovsList));
                }
            }

            if (dialog != null)
                dialog.dismiss();
            if (result != null && result.getResult() != null
                    && result.getResult().equals("0")) {
                if (requestCode == REQUEST_SEARCH) {
                    CollectionInfo info = (CollectionInfo) result.getBean();
                    if (info != null) {
                        ContentLayout.setVisibility(View.VISIBLE);
                        tempCollectionInfo = info;
                        ShowContentInfo(info);
                    }
                } else {
                    Toast.makeText(mContext, result.getDescription(),
                            Toast.LENGTH_SHORT).show();
                }

            } else if (result != null && result.getDescription() != null
                    && !result.getDescription().equals("")) {
                Toast.makeText(mContext, "请求失败，" + result.getDescription(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "请求失败，请稍后再试!", Toast.LENGTH_SHORT)
                        .show();
            }

            if (requestCode == REQUEST_SEARCH) {
                myScrollView.fullScroll(ScrollView.FOCUS_UP);
                NameTextView.setFocusable(true);
                NameTextView.setFocusableInTouchMode(true);
                NameTextView.requestFocus();
            }
        }
    }


    private void ShowContentInfo(CollectionInfo info) {
        //户籍信息
        PersonInfo personInfo = info.getPersonInfo();
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

        cardno = IDCardEdittext.getText().toString();
        name = NameTextView.getText().toString();

        //采集
        CanbaoInfo canbaoInfo = info.getCanbaoInfo();
        JHRNameEditText.setText(canbaoInfo.getJhname());
        JHRIDCardEditText.setText(canbaoInfo.getJhcardno());
        JHRMobileEditText.setText(canbaoInfo.getJhtelephone());
        MobileEditText.setText(canbaoInfo.getTelephone());
        DescribeEditText.setText(canbaoInfo.getComment());

        cbstate = canbaoInfo.getCbstate();
        cbstatus = canbaoInfo.getCbstatus();
        setSpinnerData(CJSpinner, cbstate);
        if(cbstate.equals("1")){
            setSpinnerData(CBSpinner, cbstatus);
        }else{
            setSpinnerData(CBSpinner, "");
        }


        //外地参保
        WaidiCanbaoInfo waidiCanbaoInfo = info.getWaidiCanbaoInfo();
        showWaidiCanbao(waidiCanbaoInfo);

        //未参保
        WeiCanbaoInfo weiCanbaoInfo = info.getWeiCanbaoInfo();
        showWeicanbao(weiCanbaoInfo);


        if (cbstatus.equals("1")) {
            if (cbstate.equals("3")) {
                weicanboaView.setVisibility(View.GONE);
                waidicanbaoView.setVisibility(View.VISIBLE);
            } else {
                weicanboaView.setVisibility(View.VISIBLE);
                waidicanbaoView.setVisibility(View.GONE);
            }

        } else {
            weicanboaView.setVisibility(View.GONE);
            waidicanbaoView.setVisibility(View.GONE);
        }

        if (info.getCanEdit().equals("0")) {
            JHRNameEditText.setEnabled(false);
            JHRNameEditText.setEnabled(false);
            JHRIDCardEditText.setEnabled(false);
            JHRMobileEditText.setEnabled(false);
            CJSpinner.setEnabled(false);
            CBSpinner.setEnabled(false);
            MobileEditText.setEnabled(false);

            WCBorgNameEditText.setEnabled(false);
            WCBaddressEditText.setEnabled(false);
            WCBorgAddrEditText.setEnabled(false);
            WCBCheckBox.setEnabled(false);
            WCBprovSpinner.setEnabled(false);
            WCBcitySpinner.setEnabled(false);
            WCBtownSpinner.setEnabled(false);
            WCBStatusSpinner.setEnabled(false);
            WCBReasonSpinner.setEnabled(false);
            WCBorgProvSpinner.setEnabled(false);
            WCBorgCitySpinner.setEnabled(false);
            WCBorgTownSpinner.setEnabled(false);

            WDCBprovSpinner.setEnabled(false);
            WDCBcitySpinner.setEnabled(false);
            WDCBzzJfCheckBox.setEnabled(false);
            WDCBltDyCheckBox.setEnabled(false);
            WDCBjnJfCheckBox.setEnabled(false);
            WDCBjnDyCheckBox.setEnabled(false);
            WDCBqtJfCheckBox.setEnabled(false);
            WDCBqtDyCheckBox.setEnabled(false);
            WDCBzgYbCheckBox.setEnabled(false);
            WDCBjnYbCheckBox.setEnabled(false);
            WDCBqtYbCheckBox.setEnabled(false);

            saveButton.setEnabled(false);
            resetButton.setEnabled(false);
        } else {
            JHRNameEditText.setEnabled(true);
            JHRNameEditText.setEnabled(true);
            JHRIDCardEditText.setEnabled(true);
            JHRMobileEditText.setEnabled(true);
            CJSpinner.setEnabled(true);
            CBSpinner.setEnabled(true);
            MobileEditText.setEnabled(true);

            WCBorgNameEditText.setEnabled(true);
            WCBaddressEditText.setEnabled(true);
            WCBorgAddrEditText.setEnabled(true);
            WCBCheckBox.setEnabled(true);
            WCBprovSpinner.setEnabled(true);
            WCBcitySpinner.setEnabled(true);
            WCBtownSpinner.setEnabled(true);
            WCBStatusSpinner.setEnabled(true);
            WCBReasonSpinner.setEnabled(true);
            WCBorgProvSpinner.setEnabled(true);
            WCBorgCitySpinner.setEnabled(true);
            WCBorgTownSpinner.setEnabled(true);

            WDCBprovSpinner.setEnabled(true);
            WDCBcitySpinner.setEnabled(true);
            WDCBzzJfCheckBox.setEnabled(true);
            WDCBltDyCheckBox.setEnabled(true);
            WDCBjnJfCheckBox.setEnabled(true);
            WDCBjnDyCheckBox.setEnabled(true);
            WDCBqtJfCheckBox.setEnabled(true);
            WDCBqtDyCheckBox.setEnabled(true);
            WDCBzgYbCheckBox.setEnabled(true);
            WDCBjnYbCheckBox.setEnabled(true);
            WDCBqtYbCheckBox.setEnabled(true);

            saveButton.setEnabled(true);
            resetButton.setEnabled(true);
        }
    }

    private void showWaidiCanbao(WaidiCanbaoInfo waidiCanbaoInfo) {

        wdcbPro = waidiCanbaoInfo.getProv();
        WdcbCity = waidiCanbaoInfo.getCity();

        if (TextUtils.isEmpty(wdcbPro)) {
            wdcbPro = "320000";
        }
        if (TextUtils.isEmpty(WdcbCity)) {
            WdcbCity = "320500";
        }
        setSpinnerData(WDCBprovSpinner, wdcbPro);
        setSpinnerData(WDCBcitySpinner, WdcbCity);


        setCheckBoxtDate(WDCBzzJfCheckBox, waidiCanbaoInfo.getZzJf());
        setCheckBoxtDate(WDCBltDyCheckBox, waidiCanbaoInfo.getLtDy());
        setCheckBoxtDate(WDCBjnJfCheckBox, waidiCanbaoInfo.getJnJf());
        setCheckBoxtDate(WDCBjnDyCheckBox, waidiCanbaoInfo.getJnDy());
        setCheckBoxtDate(WDCBqtJfCheckBox, waidiCanbaoInfo.getQtJf());
        setCheckBoxtDate(WDCBqtDyCheckBox, waidiCanbaoInfo.getQtDy());
        setCheckBoxtDate(WDCBzgYbCheckBox, waidiCanbaoInfo.getZgYb());
        setCheckBoxtDate(WDCBjnYbCheckBox, waidiCanbaoInfo.getJnYb());
        setCheckBoxtDate(WDCBqtYbCheckBox, waidiCanbaoInfo.getQtYb());
    }

    private void showWeicanbao(WeiCanbaoInfo weiCanbaoInfo) {
        WCBorgNameEditText.setText(weiCanbaoInfo.getOrgName());
        WCBaddressEditText.setText(weiCanbaoInfo.getAddr());
        WCBorgAddrEditText.setText(weiCanbaoInfo.getOrgAddr());

        if (weiCanbaoInfo.getCanbao().equals("Y")) {
            WCBCheckBox.setChecked(true);
        } else {
            WCBCheckBox.setChecked(false);
        }


        prov = weiCanbaoInfo.getProv();
        city = weiCanbaoInfo.getCity();
        town = weiCanbaoInfo.getTown();

        if (TextUtils.isEmpty(prov)) {
            prov = "320000";
        }
        if (TextUtils.isEmpty(city)) {
            city = "320500";
        }
        if (TextUtils.isEmpty(town)) {
            town = "";
        }
        setSpinnerData(WCBprovSpinner, prov);
        setSpinnerData(WCBcitySpinner, city);
        setSpinnerData(WCBtownSpinner, town);


        setSpinnerData(WCBStatusSpinner, weiCanbaoInfo.getStatus());
        setSpinnerData(WCBReasonSpinner, weiCanbaoInfo.getReason());

        orgProv = weiCanbaoInfo.getOrgProv();
        orgCity = weiCanbaoInfo.getOrgCity();
        orgTown = weiCanbaoInfo.getOrgTown();
        if (TextUtils.isEmpty(orgProv)) {
            if (weiCanbaoInfo.getStatus().equals("2") || weiCanbaoInfo.getStatus().equals("3")) {
                orgProv = "320000";
            } else {
                orgCity = "";
            }
        }
        if (TextUtils.isEmpty(orgCity)) {
            if (weiCanbaoInfo.getStatus().equals("2") || weiCanbaoInfo.getStatus().equals("3")) {
                orgCity = "320500";
            } else {
                orgCity = "";
            }
        }
        if (TextUtils.isEmpty(orgTown)) {
            if (weiCanbaoInfo.getStatus().equals("2") || weiCanbaoInfo.getStatus().equals("3")) {
                orgTown = "";
            } else {
                orgTown = "";
            }

        }
        setSpinnerData(WCBorgProvSpinner, orgProv);
        setSpinnerData(WCBorgCitySpinner, orgCity);
        setSpinnerData(WCBorgTownSpinner, orgTown);

    }

    private void setCheckBoxtDate(CheckBox checkBox, String index) {
        if (index.equals("1")) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    public void setSpinnerData(Spinner spinner, String text) {
        if (spinner == CJSpinner) {
            if (caijiList == null || caijiList.size() == 0) {
                return;
            }
            for (int i = 0; i < caijiList.size(); i++) {
                String item = caijiList.get(i).getId();
                if (item.equals(text)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        } else if (spinner == CBSpinner) {
            if (canbaoList == null || canbaoList.size() == 0) {
                return;
            }
            if(TextUtils.isEmpty(text)){
                if(isListNUll("item",canbaoList)) {
                    canbaoList.add(new ItemInfo("-1", ""));
                }
                spinner.setSelection(canbaoList.size()-1);
                return;
            }
            for (int i = 0; i < canbaoList.size(); i++) {
                String item = canbaoList.get(i).getId();
                if (item.equals(text)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        } else if (spinner == WDCBprovSpinner) {
            if (WdcbprovsList == null || WdcbprovsList.size() == 0) {
                return;
            }
            for (int i = 0; i < WdcbprovsList.size(); i++) {
                String item = WdcbprovsList.get(i).getId();

                if (item.equals(text)) {

                    spinner.setSelection(i);
                    WdcbcityList = WdcbprovsList.get(i).getChild();
                    break;
                }
            }
        } else if (spinner == WDCBcitySpinner) {
            if (WdcbcityList == null || WdcbcityList.size() == 0) {
                return;
            }
            for (int i = 0; i < WdcbcityList.size(); i++) {
                String item = WdcbcityList.get(i).getId();
                if (item.equals(text)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        } else if (spinner == WCBprovSpinner) {
            if (provsList == null || provsList.size() == 0) {
                return;
            }
            for (int i = 0; i < provsList.size(); i++) {
                String item = provsList.get(i).getId();
                if (item.equals(text)) {
                    spinner.setSelection(i);
                    cityList = provsList.get(i).getChild();
                    break;
                }
            }
        } else if (spinner == WCBcitySpinner) {
            if (cityList == null || cityList.size() == 0) {
                return;
            }
            for (int i = 0; i < cityList.size(); i++) {
                String item = cityList.get(i).getId();
                if (item.equals(text)) {
                    spinner.setSelection(i);
                    townList = cityList.get(i).getChild();
                    break;
                }
            }
        } else if (spinner == WCBtownSpinner) {
            if (townList == null || townList.size() == 0) {
                return;
            }
            if (TextUtils.isEmpty(text)) {
                if(isListNUll("town",townList)) {
                    townList.add(new TownInfo("-1", ""));
                }
                spinner.setSelection(townList.size() - 1);
                return;
            }
            for (int i = 0; i < townList.size(); i++) {
                String item = townList.get(i).getId();
                if (item.equals(text)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        } else if (spinner == WCBorgProvSpinner) {
            if (OrgprovsList == null || OrgprovsList.size() == 0) {
                return;
            }

            if (TextUtils.isEmpty(text)) {
                if(isListNUll("pro",OrgprovsList)) {
                    OrgprovsList.add(new ProvinceInfo("-1", ""));
                }
                spinner.setSelection(OrgprovsList.size() - 1);
                return;
            }
            for (int i = 0; i < OrgprovsList.size(); i++) {
                String item = OrgprovsList.get(i).getId();
                if (item.equals(text)) {
                    spinner.setSelection(i);
                    OrgcityList = OrgprovsList.get(i).getChild();
                    break;
                }
            }
        } else if (spinner == WCBorgCitySpinner) {
            if (OrgcityList == null || OrgcityList.size() == 0) {
                return;
            }
            if (TextUtils.isEmpty(text)) {
                if(isListNUll("city",OrgcityList)) {
                    OrgcityList.add(new CityInfo("-1", ""));
                }
                spinner.setSelection(OrgcityList.size() - 1);
                return;
            }
            for (int i = 0; i < OrgcityList.size(); i++) {
                String item = OrgcityList.get(i).getId();
                if (item.equals(text)) {
                    spinner.setSelection(i);
                    OrgtownList = OrgcityList.get(i).getChild();
                    break;
                }
            }
        } else if (spinner == WCBorgTownSpinner) {
            if (OrgtownList == null || OrgtownList.size() == 0) {
                return;
            }

            if (TextUtils.isEmpty(text)) {
                if(isListNUll("town",OrgtownList)) {
                    OrgtownList.add(new TownInfo("-1", ""));
                }
                spinner.setSelection(OrgtownList.size() - 1);
                return;
            }
            for (int i = 0; i < OrgtownList.size(); i++) {
                String item = OrgtownList.get(i).getId();
                if (item.equals(text)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        } else if (spinner == WCBStatusSpinner) {
            if (stateList == null || stateList.size() == 0) {
                return;
            }
            for (int i = 0; i < stateList.size(); i++) {
                String item = stateList.get(i).getId();
                if (item.equals(text)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        } else if (spinner == WCBReasonSpinner) {
            if (reasonList == null || reasonList.size() == 0) {
                return;
            }
            for (int i = 0; i < reasonList.size(); i++) {
                String item = reasonList.get(i).getId();
                if (item.equals(text)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private boolean isListNUll(String str ,List<?> orgtownList) {
        int len=orgtownList.size();
        boolean isnull=true;
        if(str.equals("pro")){
            for(int i=0;i<len;i++){
                ProvinceInfo info=(ProvinceInfo)orgtownList.get(i);
                if(info.getId().equals("-1")){
                    isnull=false;
                    return  isnull;
                }
            }
        }else if(str.equals("city")){
            for(int i=0;i<len;i++){
                CityInfo info=(CityInfo)orgtownList.get(i);
                if(info.getId().equals("-1")){
                    isnull=false;
                    return  isnull;
                }
            }
        }else if(str.equals("town")){
            for(int i=0;i<len;i++){
                TownInfo info=(TownInfo)orgtownList.get(i);
                if(info.getId().equals("-1")){
                    isnull=false;
                    return  isnull;
                }
            }
        }else if(str.equals("item")){
            for(int i=0;i<len;i++){
                ItemInfo info=(ItemInfo)orgtownList.get(i);
                if(info.getId().equals("-1")){
                    isnull=false;
                    return  isnull;
                }
            }
        }
        return isnull;
    }


}
