package com.landicorp.android.wofupay.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GasBean extends BasicBean {

	/**
	 * 卡类型
	 */
	public int type;
	public String data;

	@Override
	public String toString() {
		return "GasBean [type=" + type + ", data=" + data + ", BillNo="
				+ BillNo + ", payAmount=" + payAmount + ", phoneNumber="
				+ phoneNumber + ", function=" + function + ", cardId=" + cardId
				+ "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(BillNo);
		dest.writeString(payAmount);
		dest.writeString(phoneNumber);
		dest.writeInt(function);
		dest.writeString(payAmount);
		dest.writeString(cardId);
		dest.writeString(data);
		dest.writeInt(type);
	}

	/**
	 * 对象的创建器，
	 */
	public static final Creator<GasBean> CREATOR = new Creator<GasBean>() {

		@Override
		public GasBean createFromParcel(Parcel source) {
			GasBean bean = new GasBean();
			bean.BillNo = source.readString();
			bean.payAmount = source.readString();
			bean.phoneNumber = source.readString();
			bean.function = source.readInt();
			bean.payAmount = source.readString();
			bean.cardId= source.readString();
			bean.data= source.readString();
			bean.type = source.readInt(); 
			return bean;
		}

		@Override
		public GasBean[] newArray(int size) {
			return new GasBean[0];
		}
	};

}
