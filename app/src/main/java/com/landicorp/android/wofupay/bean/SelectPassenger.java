package com.landicorp.android.wofupay.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/25.
 */

public class SelectPassenger {
    /**
     * Data : [{"Id":1,"IdCard":"412823199403095421","PId":0,"Phone":"15518393785","UserName":"陈真"},{"Id":2,"IdCard":"412825199206078512","PId":1,"Phone":"15737178506","UserName":"逸涵"}]
     * Msg : 成功
     * code : 000
     */

    public String Msg;
    public String code;
    /**
     * Id : 1
     * IdCard : 412823199403095421
     * PId : 0
     * Phone : 15518393785
     * UserName : 陈真
     */

    public List<SelectedPassenger> Data;



    public static class SelectedPassenger {
        public int    Id; //编号
        public String IdCard;//身份证号码
        public int    PId;//判断主从关系的标志
        public String Phone;//电话
        public String UserName;//姓名
        public boolean isSelect = false;
        @Override
        public String toString() {
            return "SelectedPassenger [Id=" + Id + ", IdCard=" + IdCard
                    + ", PId=" + PId + ", Phone=" + Phone + ", UserName="
                    + UserName + "]";
        }


    }


}

