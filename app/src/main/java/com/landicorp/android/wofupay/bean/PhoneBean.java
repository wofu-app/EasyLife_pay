package com.landicorp.android.wofupay.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/17.
 */

public class PhoneBean extends BasicBean {

    /**
     * 标记
     */
    public String serFlag;

    /**
     * 运营商
     */
    public int operator;

    /**
     * 充值类型 1流量 2话费
     */
    public String type;

    /**
     * 订单ID
     */
    public String templetId;

    /**
     * 支付时间
     */
    public String payData;

    /**
     * 默认币种
     */
    public String curType="001";

    /**
     * 是否下发短信,默认为Y
     */
    public String msgFlag="Y";



    /**
     * 相应时间
     */
    public String srvDateTime="";



    /**
     *原请求流水
     */
    public String termTransID="";



    /**
     *相应流水
     */
    public String transID="Y";





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
        dest.writeInt(operator);
        dest.writeString(type);
        dest.writeString(templetId);
        dest.writeString(payData);
        dest.writeString(curType);
        dest.writeString(serFlag);

    }

    /**
     * 对象的创建器，
     */
    public static final Parcelable.Creator<PhoneBean> CREATOR = new Creator<PhoneBean>() {

        @Override
        public PhoneBean createFromParcel(Parcel source) {
            PhoneBean bean = new PhoneBean();
            bean.BillNo = source.readString();
            bean.payAmount = source.readString();
            bean.phoneNumber = source.readString();
            bean.function = source.readInt();
            bean.operator = source.readInt();
            bean.type = source.readString();
            bean.templetId = source.readString();
            bean.payData = source.readString();
            bean.curType = source.readString();
            bean.serFlag = source.readString();
            return bean;
        }

        @Override
        public PhoneBean[] newArray(int size) {
            return new PhoneBean[0];
        }
    };

}

