package com.learnopengles.android.lesson_OpenGL_ES_2;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * @Desc:
 * @Author: liubaozhu
 * @Date: 2021/11/20 7:56 PM
 */
public class TriangleActivity extends Activity {

    private MyTDView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mView = new MyTDView(this);
        mView.requestFocus();
        mView.setFocusableInTouchMode(true);
        setContentView(mView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.onPause();
    }
}
