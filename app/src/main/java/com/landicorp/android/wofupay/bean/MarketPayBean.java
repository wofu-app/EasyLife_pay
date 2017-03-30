package com.landicorp.android.wofupay.bean;

import android.os.Parcel;

public class MarketPayBean extends BasicBean{
	public MarketPayBean(Parcel in){
		BillNo = in.readString();
		payAmount = in.readString();
		phoneNumber = in.readString();
		function = in.readInt();
		cardId = in.readString();
	}
	public MarketPayBean() {
		
	}
	public static final Creator<MarketPayBean> CREATOR = new Creator<MarketPayBean>() {
        @Override
        public MarketPayBean createFromParcel(Parcel in) {
            return new MarketPayBean(in);
        }

        @Override
        public MarketPayBean[] newArray(int size) {
            return new MarketPayBean[size];
        }
    };
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
        dest.writeString(cardId);
		
	}

}
