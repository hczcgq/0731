package com.chen.insurre.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.chen.insurre.R;
import com.chen.insurre.bean.ResultInfo;
import com.chen.insurre.bean.TurnItemInfo;
import com.chen.insurre.http.HttpHelper;
import com.chen.insurre.util.CommTools;
import com.chen.insurre.util.Constant;
import com.chen.insurre.util.NetworkUtil;
import com.chen.insurre.util.PreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * Created by chenguoquan on 7/30/15.
 */
public class TrunOutActivity extends Activity implements View.OnClickListener{

    private Activity mContext = this;

    private TextView UndealTextview, ReceiveTextview, RejectTextview;

    private ListView TrunListView;

    private Button ListViewButton,DetailButton;

    private ViewFlipper viewFlipper;

    private TrunInTask mTrunInTask;


    private static final int TRUN_OUT=0;
    private static final int TRUN_OUT_UNDEAL=1;
    private static final int TRUN_OUT_RECEIVE=2;
    private static final int TRUN_OUT_REJECT=3;
    private static final int TRUN_OUT_DETAIL=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_turn_out);


        initView();

        loadDate(TRUN_OUT);
    }

    private void initView() {
        viewFlipper=(ViewFlipper)findViewById(R.id.viewFlipper);
        ReceiveTextview = (TextView) findViewById(R.id.ReceiveTextview);
        UndealTextview = (TextView) findViewById(R.id.UndealTextview);
        RejectTextview = (TextView) findViewById(R.id.RejectTextview);

        ListViewButton=(Button)findViewById(R.id.ListViewButton);
        DetailButton=(Button)findViewById(R.id.DetailButton);

        TrunListView= (ListView) findViewById(R.id.TrunListView);

        ReceiveTextview.setOnClickListener(this);
        UndealTextview.setOnClickListener(this);
        RejectTextview.setOnClickListener(this);

        ListViewButton.setOnClickListener(this);
        DetailButton.setOnClickListener(this);

    }

    private void loadDate(int requestType) {
        if (mTrunInTask != null
                && mTrunInTask.getStatus() != AsyncTask.Status.FINISHED)
            mTrunInTask.cancel(true);
        mTrunInTask = new TrunInTask(requestType);
        mTrunInTask.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ReceiveTextview :
                if (viewFlipper.getDisplayedChild() == 0) {
                    viewFlipper.showNext();
                    loadDate(TRUN_OUT_RECEIVE);
                }
                break;
            case R.id.RejectTextview:
                if (viewFlipper.getDisplayedChild() == 0) {
                    viewFlipper.showNext();
                    loadDate(TRUN_OUT_REJECT);
                }
                break;
            case R.id.UndealTextview:
                if (viewFlipper.getDisplayedChild() == 0) {
                    viewFlipper.showNext();
                    loadDate(TRUN_OUT_UNDEAL);
                }
                break;
            case R.id.ListViewButton:
                if(viewFlipper.getDisplayedChild()!=0){
                    viewFlipper.showPrevious();
                }
                break;
            case R.id.DetailButton:
                if(viewFlipper.getDisplayedChild()!=0){
                    viewFlipper.showPrevious();
                }
                break;
        }

    }

    private void showItemDate(TurnItemInfo info){
        CharSequence receiceMsg= Html.fromHtml("转出人员<font color=\"#FB1E27\">（已接收" + info.getReceive() + "人）</font>");
        ReceiveTextview.setText(receiceMsg);

        CharSequence undealMsg= Html.fromHtml("转出人员<font color=\"#1026FB\">（未处理" + info.getUndeal() + "人）</font>");
        UndealTextview.setText(undealMsg);

        CharSequence rejectMsg= Html.fromHtml("转出人员<font color=\"#D07D57\">（已拒绝" + info.getReject() + "人）</font>");
        RejectTextview.setText(rejectMsg);
    }


    private class TrunInTask extends AsyncTask<String, Void, String> {
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

        private int requestType;
        public TrunInTask(int requestType){
            this.requestType=requestType;
        }

        @Override
        protected String doInBackground(String... params) {
            if (!NetworkUtil.networkIsAvailable(mContext)) {
                return null;
            }
            String url=null;
            if(requestType==TRUN_OUT) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_out_url);
            }else if(requestType==TRUN_OUT_RECEIVE) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_out_receive_url);
            }else if(requestType==TRUN_OUT_REJECT) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_out_reject_url);
            }else if(requestType==TRUN_OUT_UNDEAL) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_out_undeal_url);
            }else if(requestType==TRUN_OUT_DETAIL) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_out_detail_url);
            }
            HashMap<String, String> hashParams = new HashMap<String, String>();
            Log.e("e", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
            hashParams.put("regkey", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
            if(requestType==TRUN_OUT_DETAIL){
                hashParams.put("cardno", "cardno");
            }
            String result = null;
            try {
                result = HttpHelper.doRequestForString(mContext, url,
                        HttpHelper.HTTP_GET, hashParams);
                Log.d("Tag", result);
                //resultInfo = new Gson().fromJson(result, ResultInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog != null)
                dialog.dismiss();

            if(result!=null){
                System.out.println("result:"+result);
                if(requestType==TRUN_OUT) {
                    ResultInfo<TurnItemInfo> Item=new Gson().fromJson(result,new TypeToken<ResultInfo<TurnItemInfo>>(){}.getType());
                    if (Item != null && Item.getResult() != null
                            && Item.getResult().equals("0")) {
                        Log.d("ccc",Item.getDescription()+"---"+Item.getBean());
                        showItemDate(((TurnItemInfo) Item.getBean()));
                    } else{
                        showFaik(Item);
                    }
                }else if(requestType==TRUN_OUT_RECEIVE) {

                }else if(requestType==TRUN_OUT_REJECT) {

                }else if(requestType==TRUN_OUT_UNDEAL) {

                }else if(requestType==TRUN_OUT_DETAIL) {

                }
            }
        }
    }



    private void showFaik(ResultInfo<?> items){
        if (items != null && items.getDescription() != null
                && !items.getDescription().equals("")) {
            Toast.makeText(mContext, "请求失败，" + items.getDescription(),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "请求失败，请稍后再试!", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}