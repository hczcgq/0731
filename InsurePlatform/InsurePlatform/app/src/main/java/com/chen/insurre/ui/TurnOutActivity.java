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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.chen.insurre.R;
import com.chen.insurre.adapter.TurnAdapter;
import com.chen.insurre.bean.PersonInfo;
import com.chen.insurre.bean.ResultInfo;
import com.chen.insurre.bean.TurnItemInfo;
import com.chen.insurre.bean.TurnListItem;
import com.chen.insurre.bean.TurnOutDetailInfo;
import com.chen.insurre.bean.TurnOutInfo;
import com.chen.insurre.http.HttpHelper;
import com.chen.insurre.util.CommTools;
import com.chen.insurre.util.Constant;
import com.chen.insurre.util.NetworkUtil;
import com.chen.insurre.util.PreferencesUtils;
import com.chen.insurre.util.ToastUtil;
import com.chen.listview.library.PullToRefreshLayout;
import com.chen.listview.library.PullableListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenguoquan on 7/30/15.
 */
public class TurnOutActivity extends Activity implements View.OnClickListener {

    private Activity mContext = this;

    private TextView UndealTextview, ReceiveTextview, RejectTextview;

    private TextView TurnListNameTextView, TurnDetailNameTextView;

//    private ListView TurnListView;

    private PullableListView mListView;

    private PullToRefreshLayout mPullToRefreshLayout;

    private Button ListViewButton, DetailButton;

    private View DetailUndealView, DetailReceiveView, DetailRejectView;

    private ViewFlipper viewFlipper;

    private TextView TurnNameTextView, TurnSexTextView, TurnBirthTextView, TurnAgeTextView, TurnRoonNoTextView, TurnContractTextView, TurnRegionTextView,
            TurnAreaTextView, TurnPropTextView, TurnLocationTextView;

    private TextView TurnOutApplyTimeTextView, TurnOutRoadTextView, TurnOutAreaTextView, TurnOutReasonTextView;

    private TextView TurnOutRejectTimeTextView, TurnOutRejectReasonTextView;

    private TurnInTask mTurnInTask;

    private TurnAdapter adapter;

    private List<TurnListItem> datas;

    private TurnItemInfo mTurnItemInfo;

    private String cardno;

    private static final int TRUN_OUT = 0;
    private static final int TRUN_OUT_UNDEAL = 1;
    private static final int TRUN_OUT_RECEIVE = 2;
    private static final int TRUN_OUT_REJECT = 3;
    private static final int TRUN_OUT_DETAIL = 4;

    private int Tag = TRUN_OUT_UNDEAL;


    private boolean isLoadMore = false; //是否下啦刷新

    private int pagesize = 15;

    private int offset = 1;

    private View viewLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_turn_out);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewFlipper != null) {
            viewFlipper.setDisplayedChild(0);
        }
        loadDate(TRUN_OUT);

    }

    private void initView() {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        ReceiveTextview = (TextView) findViewById(R.id.ReceiveTextview);
        UndealTextview = (TextView) findViewById(R.id.UndealTextview);
        RejectTextview = (TextView) findViewById(R.id.RejectTextview);

        TurnListNameTextView = (TextView) findViewById(R.id.TurnListNameTextView);
        TurnDetailNameTextView = (TextView) findViewById(R.id.TurnDetailNameTextView);

        ListViewButton = (Button) findViewById(R.id.ListViewButton);
        DetailButton = (Button) findViewById(R.id.DetailButton);

//        TurnListView = (ListView) findViewById(R.id.TurnListView);

        DetailUndealView = findViewById(R.id.DetailUndealView);
        DetailReceiveView = findViewById(R.id.DetailReceiveView);
        DetailRejectView = findViewById(R.id.DetailRejectView);


        //转入转入详情公共部分
        TurnNameTextView = (TextView) findViewById(R.id.TurnNameTextView);
        TurnSexTextView = (TextView) findViewById(R.id.TurnSexTextView);
        TurnBirthTextView = (TextView) findViewById(R.id.TurnBirthTextView);
        TurnAgeTextView = (TextView) findViewById(R.id.TurnAgeTextView);
        TurnRoonNoTextView = (TextView) findViewById(R.id.TurnRoonNoTextView);
        TurnContractTextView = (TextView) findViewById(R.id.TurnContractTextView);
        TurnRegionTextView = (TextView) findViewById(R.id.TurnRegionTextView);
        TurnAreaTextView = (TextView) findViewById(R.id.TurnAreaTextView);
        TurnPropTextView = (TextView) findViewById(R.id.TurnPropTextView);
        TurnLocationTextView = (TextView) findViewById(R.id.TurnLocationTextView);

        TurnOutApplyTimeTextView = (TextView) findViewById(R.id.TurnOutApplyTimeTextView);
        TurnOutRoadTextView = (TextView) findViewById(R.id.TurnOutRoadTextView);
        TurnOutAreaTextView = (TextView) findViewById(R.id.TurnOutAreaTextView);
        TurnOutReasonTextView = (TextView) findViewById(R.id.TurnOutReasonTextView);

        TurnOutRejectTimeTextView = (TextView) findViewById(R.id.TurnOutRejectTimeTextView);
        TurnOutRejectReasonTextView = (TextView) findViewById(R.id.TurnOutRejectReasonTextView);

        ReceiveTextview.setOnClickListener(this);
        UndealTextview.setOnClickListener(this);
        RejectTextview.setOnClickListener(this);

        ListViewButton.setOnClickListener(this);
        DetailButton.setOnClickListener(this);


        viewLoadMore = findViewById(R.id.viewLoadMore);

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_refresh_layout);
        mPullToRefreshLayout
                .setRefreshMode(PullToRefreshLayout.PULL_DOWN);
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                viewLoadMore.setVisibility(View.VISIBLE);
                isLoadMore = true;
                loadDate(Tag);
            }
        });
        mListView = (PullableListView) findViewById(R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isLoadMore = false;
                cardno = datas.get(position).getCardno();
                loadDate(TRUN_OUT_DETAIL);
            }
        });

    }

    /**
     * 显示列表
     *
     * @param list
     */
    private void showList(List<TurnListItem> list) {
        if (list.size() < pagesize) {
            viewLoadMore.setVisibility(View.GONE);
            mPullToRefreshLayout
                    .setRefreshMode(PullToRefreshLayout.PULL_NONE);
        } else {
            viewLoadMore.setVisibility(View.VISIBLE);
            mPullToRefreshLayout
                    .setRefreshMode(PullToRefreshLayout.PULL_DOWN);
        }
        if (datas == null) {
            datas = new ArrayList<TurnListItem>();
        }
        if (!isLoadMore) {
            datas.clear();
        }
        datas.addAll(list);

        if (adapter == null) {
            adapter = new TurnAdapter(this, datas);
            mListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void showPrevious() {
        if (viewFlipper.getDisplayedChild() != 0) {
            viewFlipper.showPrevious();
        }
    }

    private void showNext() {
        if (isLoadMore) {
            return;
        }
        viewFlipper.showNext();
    }

    private void loadDate(int requestType) {
        if (!NetworkUtil.networkIsAvailable(mContext)) {
            ToastUtil.showToastShort(this, "请检查网络连接状态。");
            return;
        }

        if (isLoadMore) {
            offset += 1;
        } else {
            offset = 1;
        }


        if (mTurnInTask != null
                && mTurnInTask.getStatus() != AsyncTask.Status.FINISHED)
            mTurnInTask.cancel(true);
        mTurnInTask = new TurnInTask(requestType);
        mTurnInTask.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ReceiveTextview:
                Tag = TRUN_OUT_RECEIVE;
                isLoadMore = false;
                loadDate(TRUN_OUT_RECEIVE);
                break;
            case R.id.RejectTextview:
                Tag = TRUN_OUT_REJECT;
                isLoadMore = false;
                loadDate(TRUN_OUT_REJECT);
                break;
            case R.id.UndealTextview:
                Tag = TRUN_OUT_UNDEAL;
                isLoadMore = false;
                loadDate(TRUN_OUT_UNDEAL);
                break;
            case R.id.ListViewButton:
                showPrevious();
                break;
            case R.id.DetailButton:
                showPrevious();
                break;
        }

    }

    /**
     * 第一页面title
     *
     * @param info
     */
    private void showItemDate(TurnItemInfo info) {
        CharSequence receiceMsg = Html.fromHtml("转出人员<font color=\"#1026FB\">(已接收" + info.getReceive() + "人)</font>");
        ReceiveTextview.setText(receiceMsg);

        CharSequence undealMsg = Html.fromHtml("转出人员<font color=\"#FB1E27\">(未处理" + info.getUndeal() + "人)</font>");
        UndealTextview.setText(undealMsg);

        CharSequence rejectMsg = Html.fromHtml("转出人员<font color=\"#D07D57\">(已拒绝" + info.getReject() + "人)</font>");
        RejectTextview.setText(rejectMsg);
    }

    /**
     * 第二页面title
     *
     * @param requestType
     */
    private void showTurnListName(int requestType, String index) {
        if (requestType == TRUN_OUT_RECEIVE) {
            CharSequence receiceMsg = Html.fromHtml("转出人员<font color=\"#1026FB\">(已接收" + index + "人)</font>");
            TurnListNameTextView.setText(receiceMsg);
        } else if (requestType == TRUN_OUT_REJECT) {
            CharSequence receiceMsg = Html.fromHtml("转出人员<font color=\"#D07D57\">(已拒绝" + index + "人)</font>");
            TurnListNameTextView.setText(receiceMsg);
        } else if (requestType == TRUN_OUT_UNDEAL) {
            CharSequence receiceMsg = Html.fromHtml("转出人员<font color=\"#FB1E27\">(未处理" + index + "人)</font>");
            TurnListNameTextView.setText(receiceMsg);
        }

    }

    /**
     * 第三页面title
     *
     * @param requestType
     */
    private void showTurnDetailName(int requestType) {
        if (requestType == TRUN_OUT_RECEIVE) {
            CharSequence receiceMsg = Html.fromHtml("转出人员<font color=\"#1026FB\">（已接收）</font>");
            TurnDetailNameTextView.setText(receiceMsg);
        } else if (requestType == TRUN_OUT_REJECT) {
            CharSequence receiceMsg = Html.fromHtml("转出人员<font color=\"#D07D57\">（已拒绝）</font>");
            TurnDetailNameTextView.setText(receiceMsg);
        } else if (requestType == TRUN_OUT_UNDEAL) {
            CharSequence receiceMsg = Html.fromHtml("转出人员<font color=\"#FB1E27\">（未处理）</font>");
            TurnDetailNameTextView.setText(receiceMsg);
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

        public TurnInTask(int requestType) {
            this.requestType = requestType;
        }

        @Override
        protected String doInBackground(String... params) {
            if (!NetworkUtil.networkIsAvailable(mContext)) {
                return null;
            }
            String url = null;
            if (requestType == TRUN_OUT) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_out_url);
            } else if (requestType == TRUN_OUT_RECEIVE) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_out_receive_url);
            } else if (requestType == TRUN_OUT_REJECT) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_out_reject_url);
            } else if (requestType == TRUN_OUT_UNDEAL) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_out_undeal_url);
            } else if (requestType == TRUN_OUT_DETAIL) {
                url = CommTools.getRequestUrl(mContext, R.string.trun_out_detail_url);
            }
            HashMap<String, String> hashParams = new HashMap<String, String>();
            Log.e("e", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
            hashParams.put("regkey", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
            if (requestType == TRUN_OUT_DETAIL) {
                hashParams.put("cardno", cardno);
            }
            if (requestType == TRUN_OUT_RECEIVE || requestType == TRUN_OUT_REJECT || requestType == TRUN_OUT_UNDEAL) {
                hashParams.put("pageno", String.valueOf(offset));
                hashParams.put("pagesize", String.valueOf(pagesize));
            }
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
            if (dialog != null)
                dialog.dismiss();
            if (isLoadMore) {
                mPullToRefreshLayout
                        .loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }

            if (result != null) {
                try {
                    if (requestType == TRUN_OUT) {
                        ResultInfo<TurnItemInfo> Item = new Gson().fromJson(result, new TypeToken<ResultInfo<TurnItemInfo>>() {
                        }.getType());
                        if (Item != null && Item.getResult() != null
                                && Item.getResult().equals("0")) {
                            Log.d("ccc", Item.getDescription() + "---" + Item.getBean());
                            mTurnItemInfo = ((TurnItemInfo) Item.getBean());
                            showItemDate(mTurnItemInfo);
                        } else {
                            showFaik(Item);
                        }
                    } else if (requestType == TRUN_OUT_RECEIVE) {
                        showTurnListName(TRUN_OUT_RECEIVE, mTurnItemInfo.getReceive());

                        ResultInfo<List<TurnListItem>> Item = new Gson().fromJson(result, new TypeToken<ResultInfo<List<TurnListItem>>>() {
                        }.getType());
                        if (Item != null && Item.getResult() != null
                                && Item.getResult().equals("0")) {
                            List<TurnListItem> list = Item.getBean();
                            showList(list);
                            showNext();
                        } else {
                            showFaik(Item);
                        }
                    } else if (requestType == TRUN_OUT_REJECT) {
                        showTurnListName(TRUN_OUT_REJECT, mTurnItemInfo.getReject());
                        ResultInfo<List<TurnListItem>> Item = new Gson().fromJson(result, new TypeToken<ResultInfo<List<TurnListItem>>>() {
                        }.getType());
                        if (Item != null && Item.getResult() != null
                                && Item.getResult().equals("0")) {
                            List<TurnListItem> list = Item.getBean();
                            showList(list);
                            showNext();
                        } else {
                            showFaik(Item);
                        }
                    } else if (requestType == TRUN_OUT_UNDEAL) {
                        showTurnListName(TRUN_OUT_UNDEAL, mTurnItemInfo.getUndeal());
                        ResultInfo<List<TurnListItem>> Item = new Gson().fromJson(result, new TypeToken<ResultInfo<List<TurnListItem>>>() {
                        }.getType());
                        if (Item != null && Item.getResult() != null
                                && Item.getResult().equals("0")) {
                            List<TurnListItem> list = Item.getBean();
                            showList(list);
                            showNext();
                        } else {
                            showFaik(Item);
                        }
                    } else if (requestType == TRUN_OUT_DETAIL) {
                        showTurnDetailName(Tag);
                        ResultInfo<TurnOutDetailInfo> Item = new Gson().fromJson(result, new TypeToken<ResultInfo<TurnOutDetailInfo>>() {
                        }.getType());
                        if (Item != null && Item.getResult() != null
                                && Item.getResult().equals("0")) {
                            TurnOutDetailInfo detailInfo = Item.getBean();
                            if (Tag == TRUN_OUT_RECEIVE) {
                                DetailReceiveView.setVisibility(View.VISIBLE);
                                DetailRejectView.setVisibility(View.GONE);
                                DetailUndealView.setVisibility(View.VISIBLE);
                            } else if (Tag == TRUN_OUT_REJECT) {
                                DetailReceiveView.setVisibility(View.VISIBLE);
                                DetailRejectView.setVisibility(View.VISIBLE);
                                DetailUndealView.setVisibility(View.VISIBLE);
                            } else if (Tag == TRUN_OUT_UNDEAL) {
                                DetailReceiveView.setVisibility(View.VISIBLE);
                                DetailRejectView.setVisibility(View.GONE);
                                DetailUndealView.setVisibility(View.VISIBLE);
                            }
                            showGlobalDetail(detailInfo, Tag);
                            viewFlipper.showNext();
                        } else {
                            showFaik(Item);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showGlobalDetail(TurnOutDetailInfo item, int index) {
        PersonInfo personInfo = item.getPersonInfo();
        TurnNameTextView.setText(personInfo.getName());
        TurnSexTextView.setText(personInfo.getSex());
        TurnBirthTextView.setText(personInfo.getBirthday());
        TurnAgeTextView.setText(personInfo.getAge());
        TurnRoonNoTextView.setText(personInfo.getRoomno());
        TurnContractTextView.setText(personInfo.getContract());
        TurnRegionTextView.setText(personInfo.getRegion());
        TurnAreaTextView.setText(personInfo.getArea());
        TurnPropTextView.setText(personInfo.getProp());
        TurnLocationTextView.setText(personInfo.getLocation());

        TurnOutInfo turnOutInfo = item.getInoutInfo();

        TurnOutApplyTimeTextView.setText(turnOutInfo.getSqDate());
        TurnOutRoadTextView.setText(turnOutInfo.getInStreet());
        TurnOutAreaTextView.setText(turnOutInfo.getInArea());
        TurnOutReasonTextView.setText(turnOutInfo.getReason());
        if (index == TRUN_OUT_REJECT) {
            TurnOutRejectTimeTextView.setText(turnOutInfo.getRejectDate());
            TurnOutRejectReasonTextView.setText(turnOutInfo.getRejectReason());
        }
    }

    /**
     * 加载失败
     *
     * @param items
     */
    private void showFaik(ResultInfo<?> items) {
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
