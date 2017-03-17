package com.landicorp.android.wofupay.volley;

import com.android.volley.VolleyError;

/**
 * 下载文件的监听类
 * @author ping
 *
 * @param <T>
 */
public class DownLoadListener<T> implements CallBackListener<T> {

	@Override
	public void onSuccess(T response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFail(VolleyError error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestStart() {
		// TODO Auto-generated method stub
		
	}
	
	public void onProgress(long current ,long totle){
		
	}

}
