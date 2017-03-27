package com.landicorp.android.wofupay.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/25.
 */

public class Passenger implements Serializable{
    public boolean isSelected;
    public String name;
    public String idcard;
    public String zuoweixibie;
    public String phoneNum;
    @Override
    public String toString() {
        return "Passenger [isSelected=" + isSelected + ", name=" + name
                + ", idcard=" + idcard + ", zuoweixibie=" + zuoweixibie + "]";
    }


}
