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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.chen.insurre.R;
import com.chen.insurre.adapter.TurnAdapter;
import com.chen.insurre.bean.ResultInfo;
import com.chen.insurre.bean.TurnItemInfo;
import com.chen.insurre.bean.TurnListItem;
import com.chen.insurre.http.HttpHelper;
import com.chen.insurre.util.CommTools;
import com.chen.insurre.util.Constant;
import com.chen.insurre.util.NetworkUtil;
import com.chen.insurre.util.PreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenguoquan on 7/30/15.
 */
public class TurnInActivity extends Activity implements View.OnClickListener{

    private Activity mContext = this;

    private TextView UndealTextview, ReceiveTextview, RejectTextview;

    private TextView TurnListNameTextView,TurnDetailNameTextView;

    private ListView TurnListView;

    private Button ListViewButton,DetailButton;

    private View DetailUndealView,DetailReceiveView,DetailRejectView;

    private ViewFlipper viewFlipper;

    private TurnInTask mTurnInTask;

    private TurnAdapter adapter;

    private List<TurnListItem> datas;

    private TurnItemInfo mTurnItemInfo;

    private String cardno;


    private static final int TRUN_IN=0;
    private static final int TRUN_IN_UNDEAL=1;
    private static final int TRUN_IN_RECEIVE=2;
    private static final int TRUN_IN_REJECT=3;
    private static final int TRUN_IN_DETAIL=4;


    private int Tag=TRUN_IN_UNDEAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_turn);

        initView();
        loadDate(TRUN_IN);
    }

    private void initView() {
        viewFlipper=(ViewFlipper)findViewById(R.id.viewFlipper);
        ReceiveTextview = (TextView) findViewById(R.id.ReceiveTextview);
        UndealTextview = (TextView) findViewById(R.id.UndealTextview);
        RejectTextview = (TextView) findViewById(R.id.RejectTextview);

        TurnListNameTextView= (TextView) findViewById(R.id.TurnListNameTextView);
        TurnDetailNameTextView= (TextView) findViewById(R.id.TurnDetailNameTextView);

        ListViewButton=(Button)findViewById(R.id.ListViewButton);
        DetailButton=(Button)findViewById(R.id.DetailButton);

        TurnListView= (ListView) findViewById(R.id.TurnListView);

        DetailUndealView=findViewById(R.id.DetailUndealView);
        DetailReceiveView=findViewById(R.id.DetailReceiveView);
        DetailRejectView=findViewById(R.id.DetailRejectView);

        ReceiveTextview.setOnClickListener(this);
        UndealTextview.setOnClickListener(this);
        RejectTextview.setOnClickListener(this);

        ListViewButton.setOnClickListener(this);
        DetailButton.setOnClickListener(this);

        TurnListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (viewFlipper.getDisplayedChild() != 0) {
//                    viewFlipper.showNext();
//                }
                cardno=datas.get(position).getCardno();
                loadDate(TRUN_IN_DETAIL);
            }
        });

    }

    private void loadDate(int requestType) {
        if (mTurnInTask != null
                && mTurnInTask.getStatus() != AsyncTask.Status.FINISHED)
            mTurnInTask.cancel(true);
        mTurnInTask = new TurnInTask(requestType);
        mTurnInTask.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ReceiveTextview :
                if (viewFlipper.getDisplayedChild() == 0) {
                    viewFlipper.showNext();
                    loadDate(TRUN_IN_RECEIVE);
                    Tag=TRUN_IN_RECEIVE;
                }
                break;
            case R.id.RejectTextview:
                if (viewFlipper.getDisplayedChild() == 0) {
                    viewFlipper.showNext();
                    loadDate(TRUN_IN_REJECT);
                    Tag=TRUN_IN_REJECT;
                }
                break;
            case R.id.UndealTextview:
                if (viewFlipper.getDisplayedChild() == 0) {
                    viewFlipper.showNext();
                    loadDate(TRUN_IN_UNDEAL);
                    Tag=TRUN_IN_UNDEAL;
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


    /**
     * 第一页面title
     * @param info
     */
    private void showItemDate(TurnItemInfo info){
        CharSequence receiceMsg= Html.fromHtml("转入人员<font color=\"#1026FB\">（已接收" + info.getReceive() + "人）</font>");
        ReceiveTextview.setText(receiceMsg);

        CharSequence undealMsg= Html.fromHtml("转入人员<font color=\"#FB1E27\">（未处理" + info.getUndeal() + "人）</font>");
        UndealTextview.setText(undealMsg);

        CharSequence rejectMsg= Html.fromHtml("转入人员<font color=\"#D07D57\">（已拒绝" + info.getReject() + "人）</font>");
        RejectTextview.setText(rejectMsg);
    }
    /**
     * 第二页面title
     * @param requestType
     */
    private void showTurnListName(int requestType,String index){
        if(requestType==TRUN_IN_RECEIVE) {
            CharSequence receiceMsg= Html.fromHtml("转入人员<font color=\"#1026FB\">（已接收" + index + "人）</font>");
            TurnListNameTextView.setText(receiceMsg);
        }else if(requestType==TRUN_IN_REJECT) {
            CharSequence receiceMsg= Html.fromHtml("转入人员<font color=\"#D07D57\">（已拒绝" +index + "人）</font>");
            TurnListNameTextView.setText(receiceMsg);
        }else if(requestType==TRUN_IN_UNDEAL) {
            CharSequence receiceMsg= Html.fromHtml("转入人员<font color=\"#FB1E27\">（未处理" +index + "人）</font>");
            TurnListNameTextView.setText(receiceMsg);
        }

    }
    /**
     * 第三页面title
     * @param requestType
     */
    private void showTurnDetailName(int requestType,String index){
        if(requestType==TRUN_IN_RECEIVE) {
            CharSequence receiceMsg= Html.fromHtml("转入人员<font color=\"#1026FB\">（已接收" + index + "人）</font>");
            TurnListNameTextView.setText(receiceMsg);
        }else if(requestType==TRUN_IN_REJECT) {
            CharSequence receiceMsg= Html.fromHtml("转入人员<font color=\"#D07D57\">（已拒绝" +index + "人）</font>");
            TurnListNameTextView.setText(receiceMsg);
        }else if(requestType==TRUN_IN_UNDEAL) {
            CharSequence receiceMsg= Html.fromHtml("转入人员<font color=\"#FB1E27\">（未处理" +index + "人）</font>");
            TurnListNameTextView.setText(receiceMsg);
        }
    }


    /**
     * 显示列表
     * @param list
     */
    private void showList(List<TurnListItem> list){
        if(datas==null){
            datas=new ArrayList<TurnListItem>();
        }
        datas.clear();
        datas.addAll(list);
        if(adapter==null){
            adapter=new TurnAdapter(this,datas);
            TurnListView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }


    private class TurnInTask extends AsyncTask<String, Void, String> {
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
        public TurnInTask(int requestType){
            this.requestType=requestType;
        }

        @Override
        protected String doInBackground(String... params) {
            if (!NetworkUtil.networkIsAvailable(mContext)) {
                return null;
            }
            String url=null;
            if(requestType==TRUN_IN) {
               url = CommTools.getRequestUrl(mContext, R.string.trun_in_url);
            }else if(requestType==TRUN_IN_RECEIVE) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_in_receive_url);
            }else if(requestType==TRUN_IN_REJECT) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_in_reject_url);
            }else if(requestType==TRUN_IN_UNDEAL) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_in_undeal_url);
            }else if(requestType==TRUN_IN_DETAIL) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_in_detail_url);
            }
            HashMap<String, String> hashParams = new HashMap<String, String>();
            Log.e("e", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
            hashParams.put("regkey", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
            if(requestType==TRUN_IN_DETAIL){
                hashParams.put("cardno", cardno);
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
                if(requestType==TRUN_IN) {
                    ResultInfo<TurnItemInfo> Item=new Gson().fromJson(result,new TypeToken<ResultInfo<TurnItemInfo>>(){}.getType());
                    if (Item != null && Item.getResult() != null
                            && Item.getResult().equals("0")) {
                        Log.d("ccc",Item.getDescription()+"---"+Item.getBean());
                        mTurnItemInfo=((TurnItemInfo) Item.getBean());
                        showItemDate(mTurnItemInfo);
                    } else{
                        showFaik(Item);
                    }
                }else if(requestType==TRUN_IN_RECEIVE) {
                    showTurnListName(TRUN_IN_RECEIVE,mTurnItemInfo.getReceive());
                    ResultInfo<List<TurnListItem>> Item=new Gson().fromJson(result,new TypeToken<ResultInfo<List<TurnListItem>>>(){}.getType());
                    if (Item != null && Item.getResult() != null
                            && Item.getResult().equals("0")) {
                        List<TurnListItem> list=Item.getBean();
                        showList(list);
                    } else{
                        showFaik(Item);
                    }

                }else if(requestType==TRUN_IN_REJECT) {
                    showTurnListName(TRUN_IN_REJECT,mTurnItemInfo.getReject());
                    ResultInfo<List<TurnListItem>> Item=new Gson().fromJson(result,new TypeToken<ResultInfo<List<TurnListItem>>>(){}.getType());
                    if (Item != null && Item.getResult() != null
                            && Item.getResult().equals("0")) {
                        List<TurnListItem> list=Item.getBean();
                        showList(list);
                    } else{
                        showFaik(Item);
                    }
                }else if(requestType==TRUN_IN_UNDEAL) {
                    showTurnListName(TRUN_IN_UNDEAL,mTurnItemInfo.getUndeal());
                    ResultInfo<List<TurnListItem>> Item=new Gson().fromJson(result,new TypeToken<ResultInfo<List<TurnListItem>>>(){}.getType());
                    if (Item != null && Item.getResult() != null
                            && Item.getResult().equals("0")) {
                        List<TurnListItem> list=Item.getBean();
                        showList(list);
                    } else{
                        showFaik(Item);
                    }
                }else if(requestType==TRUN_IN_DETAIL) {
                    if (viewFlipper.getDisplayedChild() != 0) {
                        viewFlipper.showNext();
                        if(Tag==TRUN_IN_RECEIVE){
                            DetailReceiveView.setVisibility(View.VISIBLE);
                            DetailRejectView.setVisibility(View.GONE);
                            DetailUndealView.setVisibility(View.GONE);
                        }else if(Tag==TRUN_IN_REJECT){
                            DetailReceiveView.setVisibility(View.GONE);
                            DetailRejectView.setVisibility(View.VISIBLE);
                            DetailUndealView.setVisibility(View.GONE);
                        }else if(Tag==TRUN_IN_UNDEAL){
                            DetailReceiveView.setVisibility(View.GONE);
                            DetailRejectView.setVisibility(View.GONE);
                            DetailUndealView.setVisibility(View.VISIBLE);
                        }
                    }
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
