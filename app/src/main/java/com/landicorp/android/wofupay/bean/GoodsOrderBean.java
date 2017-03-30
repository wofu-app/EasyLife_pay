package com.landicorp.android.wofupay.bean;

import java.io.Serializable;

public class GoodsOrderBean implements Serializable{
	public String data;
	public int code;
	@Override
	public String toString() {
		return "GoodsOrderBean [data=" + data + ", code=" + code + "]";
	}
	
}
