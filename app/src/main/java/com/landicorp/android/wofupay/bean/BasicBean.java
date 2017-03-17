package com.landicorp.android.wofupay.bean;

import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/17.
 */

public abstract class BasicBean  implements Parcelable {
    /**
     * 账单号
     */
    public String BillNo="";

    /**
     * 支付金额 单位:分
     */
    public String payAmount = "";

    /**
     * 手机号
     */
    public String phoneNumber = "";

    /**
     *功能模块
     *  芯片卡充值 :0    公交卡充值:1  ,加油卡充值 :2  ,手机充值:3 ,游戏充值:4  ,彩票:5,  水电煤充值:6,火车票7, 商城8
     */
    public int function=-1;
    /**
     * 卡号
     */
    public String cardId="";


    @Override
    public String toString() {
        return "BasicBean [BillNo=" + BillNo + ", payAmount=" + payAmount
                + ", phoneNumber=" + phoneNumber + ", function=" + function
                + "]";
    }





}

