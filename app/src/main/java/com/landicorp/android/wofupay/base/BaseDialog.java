package com.landicorp.android.wofupay.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.widget.TimeoutShow;

/**
 * Created by Administrator on 2017/3/22.
 */

public class BaseDialog extends Dialog {

    private Context context;
    public TimeoutShow timeoutShow = null;
    public Activity activity;

    public BaseDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;

    }

    public BaseDialog(Context context, Activity activity) {
        super(context, R.style.dialog);
        this.context = context;
        this.activity = activity;

    }

    public void resertTimer() {
        try {
            timeoutShow.resetTimer();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            timeoutShow = new TimeoutShow((Activity) context);
            timeoutShow.startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetachedFromWindow() {

        try {
            timeoutShow.closeTimer();
        } catch (Exception e) {

            e.printStackTrace();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_MOVE) {
            timeoutShow.resetTimer();
        }
        return super.dispatchTouchEvent(event);
    }

}

