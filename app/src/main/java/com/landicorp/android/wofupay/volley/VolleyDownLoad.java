package com.landicorp.android.wofupay.volley;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;

public class VolleyDownLoad extends Request<String>{

	private String mDownLoadPath;
	
	public VolleyDownLoad(int method, String url, ErrorListener listener) {
		super(method, url, listener);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		  byte[] data = response.data;
          //convert array of bytes into file
          FileOutputStream fileOuputStream;
		try {
			fileOuputStream = new FileOutputStream(mDownLoadPath);
			 fileOuputStream.write(data);
	          fileOuputStream.close();
	          String parsed = mDownLoadPath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
         
          
		return null;
	}

	@Override
	protected void deliverResponse(String response) {
		
	}

	
	
	
	
	
	
	
	
	
	
	
}
