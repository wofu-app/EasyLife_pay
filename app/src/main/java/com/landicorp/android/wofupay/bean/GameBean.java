package com.landicorp.android.wofupay.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GameBean extends BasicBean {

	/**
	 * 是否是QB充值
	 */
	public boolean isQB;
	/**
	 * 是否是是DNF充值
	 */
	public boolean isDNF;
	
	/**
	 * 充值总数
	 */
	public int chargeCount=1;
	
	/**
	 * 支付时间
	 */
	public String payData;
	/**
	 * qq号码
	 */
	public String userNumber;
	/**
	 * 充值类型
	 */
	public String type;

	/**
	 * 确认qq号码
	 */
	public String qQNumber;
	
	@Override
	public String toString() {
		return "GameBean [isQB=" + isQB + ", isDNF=" + isDNF + ", chargeCount="
				+ chargeCount + ", payData=" + payData + ", userNumber="
				+ userNumber + ", type=" + type + ", BillNo=" + BillNo
				+ ", payAmount=" + payAmount + ", phoneNumber=" + phoneNumber
				+ ", function=" + function + ", cardId=" + cardId + "]";
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
		dest.writeInt(chargeCount);
		dest.writeString(payAmount);
		dest.writeString(userNumber);
		dest.writeString(type);
		dest.writeString(payData);
		
		

	}

	/**
	 * 对象的创建器，
	 */
	public static final Creator<GameBean> CREATOR = new Creator<GameBean>() {

		@Override
		public GameBean createFromParcel(Parcel source) {
			GameBean bean = new GameBean();
			bean.BillNo = source.readString();
			bean.payAmount = source.readString();
			bean.phoneNumber = source.readString();
			bean.function = source.readInt();
			bean.chargeCount = source.readInt();
			bean.payAmount = source.readString();
			bean.userNumber = source.readString();
			bean.type = source.readString();
			bean.payData = source.readString();
			return bean;
		}

		@Override
		public GameBean[] newArray(int size) {
			return new GameBean[0];
		}
	};

}
