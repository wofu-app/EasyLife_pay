package com.landicorp.android.wofupay.volley;

import java.util.Map;

import android.R.integer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.landicorp.android.wofupay.base.BaseApplication;
import com.landicorp.android.wofupay.utils.JLog;


public class VolleyHelper {

	private CallBackListener<String> callback;
	private VolleyHttp http;

	private static RequestQueue queue = BaseApplication.queue;
	private int timeOut = 60 * 1000;//超时时间
	private int retryTimes = 1; //重试次数
	private String url;//请求地址

	/**
	 * 创建默认的请求方式(post)
	 * @param url
	 * @param callback
	 */
	public VolleyHelper(String url, CallBackListener<String> callback) {
		this.callback = callback;
		this.url = url;
		http = new VolleyHttp(Method.POST, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyHelper.this.callback.onFail(error);
				error.printStackTrace();

			}
		}, new VolleyHttp.SuccessListener() {
			@Override
			public void onSuccess(String response) {
				VolleyHelper.this.callback.onSuccess(response);
			}
		});
		http.setRetryPolicy(new DefaultRetryPolicy(timeOut, retryTimes, 1.0f));

	}

	/**
	 * 创建指定的get或者post请求
	 * @param method
	 * @param url
	 * @param callback
	 */
	public VolleyHelper(int method,String url, 
			CallBackListener<String> callback) {
		this.callback = callback;
		this.url = url;
		http = new VolleyHttp(method, url, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyHelper.this.callback.onFail(error);
				error.printStackTrace();

			}
		}, new VolleyHttp.SuccessListener() {
			@Override
			public void onSuccess(String response) {
				VolleyHelper.this.callback.onSuccess(response);
			}
		});
		http.setRetryPolicy(new DefaultRetryPolicy(timeOut, retryTimes, 1.0f));

	}

	/**
	 * 上传字符串
	 * @param body
	 */
	public void postBody(String body){
		JLog.i(url + "?" + body);
		http.addBody(body);
		callback.onRequestStart();
		queue.add(http);
	}	
	/**
	 * 提交键值对
	 */
	public void postParams(Map<String, String> params) {
		//JLog.i(url + "?" + Utils.encodeParameters(params));
		http.addParmars(params);
		callback.onRequestStart();
		queue.add(http);
	}

	/**
	 * 设置本次http请求的tag,用来取消请求的时候调用
	 * 
	 * @param tag
	 */
	public void satTag(Object tag) {
		http.setTag(tag);
	}

	/**
	 * 根据设置的tag来取消请求
	 * 
	 * @param tag
	 */
	public static void cancle(Object tag) {
		queue.cancelAll(tag);
	}
	
	/**
	 * 设置超时时间和重试次数
	 * 
	 * @param timeout
	 *            超时时间默认60秒
	 * @param retrytimes
	 *            请求次数,默认1
	 */
	public void configRequest(int timeout, int retrytimes) {
		this.timeOut = timeout;
		this.retryTimes = retrytimes;
		http.setRetryPolicy(new DefaultRetryPolicy(timeOut, retryTimes, 1.0f));

	}

}
