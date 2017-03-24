package com.landicorp.android.wofupay.bean;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class FailBean extends BasicBean implements Parcelable {

	public String errorCode;
	public String errorMsg;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorCode);
		dest.writeString(errorMsg);
		dest.writeInt(function);
	}
	
	@Override
	public String toString() {
		return "FailBean [errorCode=" + errorCode + ", errorMsg=" + errorMsg
				+ ", function=" + function + "]";
	}
	/**
	 * 对象的创建器，
	 */
	public static final Creator<FailBean> CREATOR = new Creator<FailBean>() {

		@Override
		public FailBean createFromParcel(Parcel source) {
			FailBean bean = new FailBean();
			bean.errorCode = source.readString();
			bean.errorMsg = source.readString(); 
			bean.function = source.readInt();
			return bean;
		}

		@Override
		public FailBean[] newArray(int size) {
			return new FailBean[0];
		}
	};
	
}
