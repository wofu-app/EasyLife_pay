package com.landicorp.android.wofupay.base;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.landicorp.android.wofupay.common.AppManager;
import com.landicorp.android.wofupay.widget.TimeoutShow;

public abstract class BaseActivity extends AppCompatActivity {

	private int screenwidth;
	private int screenHeight;
	private TimeoutShow timeoutShow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 竖屏锁定
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		AppManager.getAppManager().addActivity(this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenwidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (timeoutShow != null) {
			timeoutShow.closeTimer();
			timeoutShow = null;
		}
		timeoutShow = new TimeoutShow(this);
		timeoutShow.startTimer();
	}
	@Override
	protected void onStop() {
		super.onStop();
		if (timeoutShow != null) {
			timeoutShow.closeTimer();
			timeoutShow = null;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timeoutShow != null) {
			timeoutShow.closeTimer();
			timeoutShow = null;
		}
	}

	/** 获取屏幕的宽度*/
	public int getSreenWidth(){
		return screenwidth;
	}

	/** 获取屏幕的高度*/
	public int getSreenHeight(){
		return screenHeight;
	}

	/**
	 * 提示
	 * @param msg
	 */
	public void ToastMsg(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			if (ev.getAction() == MotionEvent.ACTION_DOWN
					|| ev.getAction() == MotionEvent.ACTION_UP
					|| ev.getAction() == MotionEvent.ACTION_MOVE) {
				timeoutShow.resetTimer();
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);

	}

	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null ) {
			if (v instanceof EditText) {
				int[] leftTop = { 0, 0 };
				//获取输入框当前的location位置
				v.getLocationInWindow(leftTop);
				int left = leftTop[0];
				int top = leftTop[1];
				int bottom = top + v.getHeight();
				int right = left + v.getWidth();
				if (event.getX() > left && event.getX() < right
						&& event.getY() > top && event.getY() < bottom) {
					// 点击的是输入框区域，保留点击EditText的事件
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		timeoutShow.resetTimer();
		if(keyCode == KeyEvent.KEYCODE_BACK)
			finishMine();

		if (keyCode == KeyEvent.KEYCODE_HOME) {

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void gotoAct(Class<?> clazz){
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
	}

	public void finishMine() {
		AppManager.getAppManager().finishActivity(this);
	}

	public void exitApp() {
		AppManager.getAppManager().appExit(this);
	}

}
