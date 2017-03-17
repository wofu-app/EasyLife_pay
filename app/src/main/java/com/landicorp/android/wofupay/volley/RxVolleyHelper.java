package com.landicorp.android.wofupay.volley;

import java.util.Map;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.landicorp.android.wofupay.base.BaseApplication;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.JLog;

public class RxVolleyHelper {

	private VolleyHttp http;

	private RequestQueue queue = BaseApplication.queue;
	private int timeOut = 60 * 1000;//超时时间
	private int retryTimes = 1; //重试次数
	private String url;//请求地址

	private int method = Method.POST;

	/**
	 * 创建默认的请求方式(post)
	 * @param url
	 * @param
	 */
	public RxVolleyHelper(String url) {
		this.url = url;
	}

	/**
	 * 创建指定的get或者post请求
	 * @param method
	 * @param url
	 * @param
	 */
	public RxVolleyHelper(int method,String url) {
		this.url = url;
		this.method  = method;
	}

	/**
	 * 上传字符串
	 * @param body
	 * @return
	 */
	public Observable<String> postBody(final String body){
		JLog.i(url + "?" + body);
		return Observable.create(new OnSubscribe<String>() {

			@Override
			public void call(final Subscriber<? super String> t) {
				http = new VolleyHttp(method, url, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						t.onError(error);

					}
				}, new VolleyHttp.SuccessListener() {
					@Override
					public void onSuccess(String response) {
						t.onNext(response);
						t.onCompleted();
					}
				});
				http.setRetryPolicy(new DefaultRetryPolicy(timeOut, retryTimes, 1.0f));
				http.addBody(body);;
				JLog.i(url+"?"+body);
				t.onStart();
				queue.add(http);
			}
		}).subscribeOn(AndroidSchedulers.mainThread());
		 
	}	
	/**
	 * 提交键值对
	 */
	public Observable<String> postParams(final Map<String, String> params) {
		return Observable.create(new OnSubscribe<String>() {

			@Override
			public void call(final Subscriber<? super String> t) {
				http = new VolleyHttp(method, url, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						t.onError(error);

					}
				}, new VolleyHttp.SuccessListener() {
					@Override
					public void onSuccess(String response) {
						t.onNext(response);
						t.onCompleted();
					}
				});
				http.setRetryPolicy(new DefaultRetryPolicy(timeOut, retryTimes, 1.0f));
				http.addParmars(params);
				JLog.i(url+"?"+ AppUtils.encodeParameters(params));
				t.onStart();
				queue.add(http);
			}
		}).subscribeOn(AndroidSchedulers.mainThread());
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
	public void cancle(Object tag) {
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
