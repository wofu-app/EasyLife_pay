package com.landicorp.android.wofupay.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/28.
 */

public class LastPassenger implements Serializable {
    public int type;//乘客类型 1 成人
    public String name;
    public int cardType;//证件类型 1身份证
    public String cardNo;//身份证号码
    public String birth;//1990-08-26
    public int seatType;//座位类型
    public double ticketPrice;//座位价格
    public String phonenum;

    @Override
    public String toString() {
        return "LastPassenger [type=" + type + ", name=" + name + ", cardType="
                + cardType + ", cradNo=" + cardNo + ", birth=" + birth
                + ", seatType=" + seatType + ", ticketPrice=" + ticketPrice
                + ", phonenum=" + phonenum + "]";
    }



}
