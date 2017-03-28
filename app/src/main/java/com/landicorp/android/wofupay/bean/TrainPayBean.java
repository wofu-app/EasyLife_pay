package com.landicorp.android.wofupay.bean;

import android.os.Parcel;

/**
 * Created by Administrator on 2017/3/28.
 */

public class TrainPayBean  extends BasicBean   {


    public String reqDateTime;
    public String queryKey;
    public String trainNo;
    public String departStationCode;
    public String arriveStationCode;
    public String departDate;
    public String departTime;
    public String contactName;
    public String contactMobile;
    public String passengers;
    public String seatName;
    public String departStation;
    public String arriveStation;


    public TrainPayBean(Parcel in) {
        reqDateTime = in.readString();
        queryKey = in.readString();
        trainNo = in.readString();
        departStationCode = in.readString();
        arriveStationCode = in.readString();
        departDate = in.readString();
        departTime = in.readString();
        contactName = in.readString();
        contactMobile = in.readString();
        passengers = in.readString();
        BillNo = in.readString();
        payAmount = in.readString();
        phoneNumber = in.readString();
        function = in.readInt();
        cardId = in.readString();
        seatName=in.readString();
        departStation=in.readString();
        arriveStation=in.readString();
    }


    @Override
    public String toString() {
        return "TrainPayBean [reqDateTime=" + reqDateTime + ", queryKey="
                + queryKey + ", trainNo=" + trainNo + ", departStationCode="
                + departStationCode + ", arriveStationCode="
                + arriveStationCode + ", departDate=" + departDate
                + ", departTime=" + departTime + ", contactName=" + contactName
                + ", contactMobile=" + contactMobile + ", passengers="
                + passengers + "]";
    }


    public TrainPayBean() {

    }

    public static final Creator<TrainPayBean> CREATOR = new Creator<TrainPayBean>() {
        @Override
        public TrainPayBean createFromParcel(Parcel in) {
            return new TrainPayBean(in);
        }

        @Override
        public TrainPayBean[] newArray(int size) {
            return new TrainPayBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reqDateTime);
        dest.writeString(queryKey);
        dest.writeString(trainNo);
        dest.writeString(departStationCode);
        dest.writeString(arriveStationCode);
        dest.writeString(departDate);
        dest.writeString(departTime);
        dest.writeString(contactName);
        dest.writeString(contactMobile);
        dest.writeString(passengers);
        dest.writeString(BillNo);
        dest.writeString(payAmount);
        dest.writeString(phoneNumber);
        dest.writeInt(function);
        dest.writeString(cardId);
        dest.writeString(seatName);
        dest.writeString(departStation);
        dest.writeString(arriveStation);

    }
}

