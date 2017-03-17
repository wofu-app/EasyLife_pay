package com.landicorp.android.wofupay.volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;


public class VolleyHttp extends Request<String>{

	private SuccessListener mListener;
	
	private Map<String, String> params;
	
	private boolean isBody=false;

	/**
	 * 请求体
	 */
	private String bodys;
	
	
	public VolleyHttp(int method, String url, ErrorListener listener,SuccessListener mListener) {
		super(method, url, listener);
		this.mListener = mListener;
	}
	public VolleyHttp(String url, ErrorListener listener,SuccessListener mListener) {
		super(Method.POST, url, listener);
		this.mListener = mListener;
	}

	/**
	 * 添加参数
	 * @param params
	 * 
	 */
	public void addParmars(Map< String, String> params) {
		this.params=params;
		isBody = false;
	}
	
	public void addBody(String body){
		isBody = true;
		this.bodys = body;
	}
	
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
	    String parsed;  
	      try {  
	          parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));  
	      } catch (UnsupportedEncodingException e) {  
	          parsed = new String(response.data);  
	      }  
	      return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));  
	} 
	
	@Override
	protected void deliverResponse(String response) {
		mListener.onSuccess(response);
		
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		
		if (isBody) {

			try {
				return bodys.getBytes(getParamsEncoding());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new RuntimeException("Encoding not supported: " + getParamsEncoding());
			}
		}else {
			return super.getBody();
		}
	}
	
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;
	}
	
	public interface SuccessListener{
		void onSuccess(String response);
	}
}
