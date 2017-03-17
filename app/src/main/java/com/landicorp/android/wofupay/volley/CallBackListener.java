package com.landicorp.android.wofupay.volley;

import com.android.volley.VolleyError;

public interface CallBackListener<T> {

	void onSuccess(T response);
	
	void onFail(VolleyError error);
	
	void onRequestStart();
}
