package com.landicorp.android.wofupay.bean;

import java.io.Serializable;

public class DetailBean implements Serializable{
	public String name;
	public String phone;
	public String province;
	public String address;
	public String city;
	public String area;
	@Override
	public String toString() {
		return "DetailBean [name=" + name + ", phone=" + phone + ", province="
				+ province + ", address=" + address + ", city=" + city
				+ ", area=" + area + "]";
	}
	
}
