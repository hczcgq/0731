package com.chen.insurre.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.chen.insurre.R;

/**
 * Created by chenguoquan on 8/3/15.
 */
public class TurnInReceiveDialogActivity extends Activity{

    private int mScreenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_turn_in_detail_receive);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
    }

    public void ComfirmClick(View view){

    }

    public void CancelClick(View view){
        finish();
    }
}
