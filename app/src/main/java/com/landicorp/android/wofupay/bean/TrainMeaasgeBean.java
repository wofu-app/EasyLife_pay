package com.landicorp.android.wofupay.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/24.
 */

public class TrainMeaasgeBean implements Serializable {
    public String startStation;//出发站
    public String departTime;//当前时间
    public String trainNo; //车次号
    public String duration;//历时
    public String arriveStation;// 到达站
    public String arriveTime; //到达时间
    public String shangwuzuo;//商务座余票
    public Double shangwuzuoPrice; //商务座价钱
    public String tedengzuo;
    public Double tedengzuoPrice;
    public String yidengzuo;
    public Double yidengzuoPrice;
    public String erdengzuo;
    public Double erdengzuoPrice;
    public String gaojiruanwo;
    public Double gaojiruanwoPrice;
    public String ruanwoshang;
    public Double ruanwoshangPrice;
    public String ruanwoxia;
    public Double ruanwoxiaPrice;
    public String yingwoshang;
    public Double yingwoshangPrice;
    public String yingwozhong;
    public Double yingwozhongPrice;
    public String yingwoxia;
    public Double yingwoxiaPrice;
    public String ruanzuo;
    public Double ruanzuoPrice;
    public String yingzuo;
    public Double yingzuoPrice;
    public String wuzuo;
    public Double wuzuoPrice;
    public String godata; //出发时间
    public String queryKey; //查询码
    public String departStationCode;
    public String arriveStationCode;

    @Override
    public String toString() {
        return "TrainMeaasgeBean{" +
                "startStation='" + startStation + '\'' +
                ", departTime='" + departTime + '\'' +
                ", trainNo='" + trainNo + '\'' +
                ", duration='" + duration + '\'' +
                ", arriveStation='" + arriveStation + '\'' +
                ", arriveTime='" + arriveTime + '\'' +
                ", shangwuzuo='" + shangwuzuo + '\'' +
                ", shangwuzuoPrice=" + shangwuzuoPrice +
                ", tedengzuo='" + tedengzuo + '\'' +
                ", tedengzuoPrice=" + tedengzuoPrice +
                ", yidengzuo='" + yidengzuo + '\'' +
                ", yidengzuoPrice=" + yidengzuoPrice +
                ", erdengzuo='" + erdengzuo + '\'' +
                ", erdengzuoPrice=" + erdengzuoPrice +
                ", gaojiruanwo='" + gaojiruanwo + '\'' +
                ", gaojiruanwoPrice=" + gaojiruanwoPrice +
                ", ruanwoshang='" + ruanwoshang + '\'' +
                ", ruanwoshangPrice=" + ruanwoshangPrice +
                ", ruanwoxia='" + ruanwoxia + '\'' +
                ", ruanwoxiaPrice=" + ruanwoxiaPrice +
                ", yingwoshang='" + yingwoshang + '\'' +
                ", yingwoshangPrice=" + yingwoshangPrice +
                ", yingwozhong='" + yingwozhong + '\'' +
                ", yingwozhongPrice=" + yingwozhongPrice +
                ", yingwoxia='" + yingwoxia + '\'' +
                ", yingwoxiaPrice=" + yingwoxiaPrice +
                ", ruanzuo='" + ruanzuo + '\'' +
                ", ruanzuoPrice=" + ruanzuoPrice +
                ", yingzuo='" + yingzuo + '\'' +
                ", yingzuoPrice=" + yingzuoPrice +
                ", wuzuo='" + wuzuo + '\'' +
                ", wuzuoPrice=" + wuzuoPrice +
                ", godata='" + godata + '\'' +
                ", queryKey='" + queryKey + '\'' +
                ", departStationCode='" + departStationCode + '\'' +
                ", arriveStationCode='" + arriveStationCode + '\'' +
                '}';
    }
}
