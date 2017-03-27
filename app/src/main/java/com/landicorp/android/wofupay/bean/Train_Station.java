package com.landicorp.android.wofupay.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */

public class Train_Station implements Serializable {
    /**
     *  srvDateTime  : 20151028162422
     *  status  : 0000
     * data : {" insureID ":"1","queryKey":"6d31300d58be4d239af4039c71ddc313","trainList":[{"arriveStation":" 南京南 ","arriveStationCode":"nanjingnan","arriveTime":"11:14","departStation":" 北京北 ","departStationCode":"beijingbei","departTime":"07:00","duration":"254","isCanBook":1,"isEnd":1,"isStart":0,"seats":[{"isCanBook":0,"seatName":" 商务座 ","seatNum":50,"seatPrice":1403,"seatType":9},{"isCanBook":1,"seatName":" 一等座 ","seatNum":8,"seatPrice":748,"seatType":5}],"trainNo":"G101"}]}
     * msg : 交易成功
     * sign  :  XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
     */

    public String srvDateTime;
    public String status="";
    /**
     *  insureID  : 1
     * queryKey : 6d31300d58be4d239af4039c71ddc313
     * trainList : [{"arriveStation":" 南京南 ","arriveStationCode":"nanjingnan","arriveTime":"11:14","departStation":" 北京北 ","departStationCode":"beijingbei","departTime":"07:00","duration":"254","isCanBook":1,"isEnd":1,"isStart":0,"seats":[{"isCanBook":0,"seatName":" 商务座 ","seatNum":50,"seatPrice":1403,"seatType":9},{"isCanBook":1,"seatName":" 一等座 ","seatNum":8,"seatPrice":748,"seatType":5}],"trainNo":"G101"}]
     */

    public StationBean data;
    public String msg;
    public String sign;

    @Override
    public String toString() {
        return "Train_Station{" +
                "srvDateTime='" + srvDateTime + '\'' +
                ", status='" + status + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public static class StationBean {
        public String insureID;//保险ID
        public String queryKey;//查询码


        /**
         * arriveStation :  南京南
         * arriveStationCode : nanjingnan
         * arriveTime : 11:14
         * departStation :  北京北
         * departStationCode : beijingbei
         * departTime : 07:00
         * duration : 254
         * isCanBook : 1
         * isEnd : 1
         * isStart : 0
         * seats : [{"isCanBook":0,"seatName":" 商务座 ","seatNum":50,"seatPrice":1403,"seatType":9},{"isCanBook":1,"seatName":" 一等座 ","seatNum":8,"seatPrice":748,"seatType":5}]
         * trainNo : G101
         */

        public List<TrainStationBean> trainList;

        public static class TrainStationBean  {
            public String arriveStation; //达到车站
            public String arriveStationCode;
            public String arriveTime; //到达时间
            public String departStation;
            public String departStationCode;
            public String departTime;//发车时间
            public String duration;//历时 分钟
            public int    isCanBook;//是否可预定 1 是 0不是
            public int    isEnd;//是否是终点站 1 是 0不是
            public int    isStart;//是否是起始站 1 是 0不是
            public String trainNo; //车次号
            //public List<SeatsBean> seats; //座位信息
            @Override
            public String toString() {
                return "TrainStationBean [arriveStation=" + arriveStation
                        + ", arriveStationCode=" + arriveStationCode
                        + ", arriveTime=" + arriveTime + ", departStation="
                        + departStation + ", departStationCode="
                        + departStationCode + ", departTime=" + departTime
                        + ", duration=" + duration + ", isCanBook=" + isCanBook
                        + ", isEnd=" + isEnd + ", isStart=" + isStart
                        + ", trainNo=" + trainNo + ", seats=" + seats + "]";
            }

            /**
             * isCanBook : 0
             * seatName :  商务座
             * seatNum : 50
             * seatPrice : 1403.0
             * seatType : 9
             */

            public List<SeatsBean> seats;

            public static class SeatsBean {
                public int    isCanBook;//0 不可预定 1可预定
                public String seatName;//座位名称
                public int    seatNum;//余票数量
                public double seatPrice;//票价
                public int    seatType;//座位类型
                @Override
                public String toString() {
                    return "SeatsBean [isCanBook=" + isCanBook + ", seatName="
                            + seatName + ", seatNum=" + seatNum
                            + ", seatPrice=" + seatPrice + ", seatType="
                            + seatType + "]";
                }
            }
        }
    }

}

