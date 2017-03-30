package com.landicorp.android.wofupay.bean;


import java.io.Serializable;
import java.util.List;

public class SearchAddressBean implements Serializable{
	public String code;
	public String Msg;
	public List<AddressInfo> Data;
	public static class AddressInfo {
		public String LinkName;
		public String LinkPhone;
		public String Province;
		public String City;
		public String County;
		public String Address;
		public boolean isSelect = false;
		@Override
		public String toString() {
			return "AddressInfo [LinkName=" + LinkName + ", LinkPhone="
					+ LinkPhone + ", Province=" + Province + ", City=" + City
					+ ", County=" + County + ", Address=" + Address + "]";
		}
		
	}
	@Override
	public String toString() {
		return "SearchAddressBean [code=" + code + ", Msg=" + Msg + ", Data="
				+ Data + "]";
	}
	
	
}
