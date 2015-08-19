package com.chen.insurre.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.insurre.MyApplication;
import com.chen.insurre.R;
import com.chen.insurre.bean.ResultInfo;
import com.chen.insurre.bean.TurnItemInfo;
import com.chen.insurre.http.HttpHelper;
import com.chen.insurre.util.CommTools;
import com.chen.insurre.util.Constant;
import com.chen.insurre.util.NetworkUtil;
import com.chen.insurre.util.PreferencesUtils;
import com.chen.insurre.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends TabActivity implements View.OnClickListener {

    private TextView NameTextView, CollectionTextView, TurnInTextView, TurnOutTextView;
    private ImageView TurnInTagImageView;
    private Activity mContext = this;

    private LogOutTask mLogOutTask;

    private int mMode = 0;
    private TabHost mTabHost;

    public static final int MODE_COLLECTION = 0;
    public static final int MODE_TRUNIN = 1;
    public static final int MODE_TRUNOUT = 2;
    private int undeal = 0;

    private MyApplication application;

    private MainBroadCast receiver;
    public static final String BRAODCAST_MAIN = "com.chen.insurre.ui.BroadCast";


    private Timer timer;

    private class MainBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String undeal_count = intent.getStringExtra("undeal");
            int int_undel;
            if (TextUtils.isEmpty(undeal_count)) {
                int_undel = 0;
            } else {
                int_undel = Integer.parseInt(undeal_count);
            }
            Message message = new Message();
            message.obj = int_undel;
            message.what = 100;
            handler.sendMessage(message);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_main);
        application = MyApplication.getInstance();
        // 注册广播
        receiver = new MainBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BRAODCAST_MAIN);
        registerReceiver(receiver, filter);
        undeal = getIntent().getIntExtra("undeal", 0);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        String name = PreferencesUtils.getString(mContext, Constant.SP_USER_NAME);
        if (TextUtils.isEmpty(name)) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void initView() {
        NameTextView = (TextView) findViewById(R.id.NameTextView);
        NameTextView.setText("采集人员：" + PreferencesUtils.getString(mContext, Constant.SP_USER_NAME));

        CollectionTextView = (TextView) findViewById(R.id.CollectionTextView);
        TurnInTextView = (TextView) findViewById(R.id.TurnInTextView);
        TurnOutTextView = (TextView) findViewById(R.id.TurnOutTextView);

        TurnInTagImageView = (ImageView) findViewById(R.id.TurnInTagImageView);

        CollectionTextView.setOnClickListener(this);
        TurnInTextView.setOnClickListener(this);
        TurnOutTextView.setOnClickListener(this);

        if (undeal > 0) {
            TurnInTagImageView.setVisibility(View.VISIBLE);
        } else {
            TurnInTagImageView.setVisibility(View.GONE);
        }

        initTabHost();
        switchMode(mMode);

        timer = new Timer();
        timer.schedule(task, 2000, 1000 * 20);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                super.handleMessage(msg);
                int index = (int) msg.obj;
                if (index > 0) {
                    TurnInTagImageView.setVisibility(View.VISIBLE);
                } else {
                    TurnInTagImageView.setVisibility(View.GONE);
                }
            }
        }
    };


    public void LogOut(View view) {
        if (!NetworkUtil.networkIsAvailable(mContext)) {
            ToastUtil.showToastShort(this, "请检查网络连接状态。");
            return;
        }
        if (mLogOutTask != null
                && mLogOutTask.getStatus() != AsyncTask.Status.FINISHED)
            mLogOutTask.cancel(true);
        mLogOutTask = new LogOutTask();
        mLogOutTask.execute();

    }


    private void initTabHost() {
        if (mTabHost != null) {
            mTabHost.clearAllTabs();
        }
        mTabHost = getTabHost();


        TabHost.TabSpec mineTabSpec = mTabHost.newTabSpec("collection_tab");
        mineTabSpec.setIndicator(getString(R.string.app_name),
                getResources().getDrawable(R.drawable.login_corners_bg));
        mineTabSpec.setContent(new Intent(this, CollectionActivity.class));
        mTabHost.addTab(mineTabSpec);

        TabHost.TabSpec indexTabSpec = mTabHost.newTabSpec("trunin_tab");
        indexTabSpec.setIndicator(getString(R.string.app_name),
                getResources().getDrawable(R.drawable.login_corners_bg));
        indexTabSpec.setContent(new Intent(this, TurnInActivity.class));
        mTabHost.addTab(indexTabSpec);

        TabHost.TabSpec messageTabSpec = mTabHost.newTabSpec("trunout_tab");
        messageTabSpec.setIndicator(getString(R.string.app_name),
                getResources().getDrawable(R.drawable.login_corners_bg));
        messageTabSpec.setContent(new Intent(this, TurnOutActivity.class));
        mTabHost.addTab(messageTabSpec);

        mMode = 0;
    }

    public void switchMode(int mode) {
        if (mTabHost != null) {
            CollectionTextView.setBackgroundColor(0xFFFFFFFF);
            TurnInTextView.setBackgroundColor(0xFFFFFFFF);
            TurnOutTextView.setBackgroundColor(0xFFFFFFFF);

            if (mode == MODE_COLLECTION) {
                mMode = MODE_COLLECTION;
                CollectionTextView.setBackgroundColor(0xFFF2F2F2);
                mTabHost.setCurrentTabByTag("collection_tab");
            } else if (mode == MODE_TRUNIN) {
                mMode = MODE_TRUNIN;
                TurnInTextView.setBackgroundColor(0xFFF2F2F2);
                mTabHost.setCurrentTabByTag("trunin_tab");
            } else if (mode == MODE_TRUNOUT) {
                mMode = MODE_TRUNOUT;
                TurnOutTextView.setBackgroundColor(0xFFF2F2F2);
                mTabHost.setCurrentTabByTag("trunout_tab");
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (mTabHost != null) {
            if (view == CollectionTextView) {
                mMode = MODE_COLLECTION;
            } else if (view == TurnInTextView) {
                mMode = MODE_TRUNIN;
            } else if (view == TurnOutTextView) {
                mMode = MODE_TRUNOUT;
            }
        }
        switchMode(mMode);
    }


    /**
     * 退出登录
     */
    private class LogOutTask extends AsyncTask<String, Void, ResultInfo> {
        private Dialog dialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            try {
                dialog = ProgressDialog.show(mContext, "请稍后",
                        "退出登陆...");
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
            String url = CommTools.getRequestUrl(mContext, R.string.logout_url);
            HashMap<String, String> hashParams = new HashMap<String, String>();
            hashParams.put("regkey", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
            ResultInfo resultInfo = null;
            try {
                String result = HttpHelper.doRequestForString(mContext, url,
                        HttpHelper.HTTP_GET, hashParams);
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
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            } else if (result != null && result.getDescription() != null
                    && !result.getDescription().equals("")) {
                Toast.makeText(mContext, "退出登陆失败，" + result.getDescription(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "退出登陆失败，请稍后再试!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    //定时请求走访转入
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Log.e("----","------------------1111111111111");
            String url = CommTools.getRequestUrl(mContext, R.string.trun_in_url);
            HashMap<String, String> hashParams = new HashMap<String, String>();
            hashParams.put("regkey", PreferencesUtils.getString(mContext, Constant.SP_USER_REGKEY));
            String result = null;
            try {
                result = HttpHelper.doRequestForString(mContext, url,
                        HttpHelper.HTTP_GET, hashParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result != null) {
                int int_undel = 0;
                ResultInfo<TurnItemInfo> Item = new Gson().fromJson(result, new TypeToken<ResultInfo<TurnItemInfo>>() {
                }.getType());
                if (Item != null && Item.getResult() != null
                        && Item.getResult().equals("0")) {
                    TurnItemInfo mTurnItemInfo = ((TurnItemInfo) Item.getBean());
                    String undeal = mTurnItemInfo.getUndeal();
                    if (TextUtils.isEmpty(undeal)) {
                        int_undel = 0;
                    } else {
                        int_undel = Integer.parseInt(undeal);
                    }
                }

                Message message = new Message();
                message.obj = int_undel;
                message.what = 100;
                handler.sendMessage(message);


            }
        }
    };


}
