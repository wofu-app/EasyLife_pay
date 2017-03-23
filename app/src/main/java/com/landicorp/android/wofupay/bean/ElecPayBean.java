package com.landicorp.android.wofupay.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/21.
 */

public class ElecPayBean extends  BasicBean{
    /**
     * 账单号
     */
    public String type="";




    @Override
    public String toString() {
        return "ElecPayBean [type=" + type + ", BillNo=" + BillNo
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
        dest.writeString(payAmount);
        dest.writeString(cardId);
        dest.writeString(type);



    }

    /**
     * 对象的创建器，
     */
    public static final Parcelable.Creator<ElecPayBean> CREATOR = new Creator<ElecPayBean>() {

        @Override
        public ElecPayBean createFromParcel(Parcel source) {
            ElecPayBean bean = new ElecPayBean();
            bean.BillNo = source.readString();
            bean.payAmount = source.readString();
            bean.phoneNumber = source.readString();
            bean.function = source.readInt();
            bean.payAmount = source.readString();
            bean.cardId= source.readString();
            bean.type = source.readString();
            return bean;
        }

        @Override
        public ElecPayBean[] newArray(int size) {
            return new ElecPayBean[0];
        }
    };
}
