package com.landicorp.android.wofupay.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */

public class Yin_FlowQueryBean {

    /**
     * msg : ok
     * status : 0000
     * termTransID : 0000flow20160523092419
     */

    private BaseBean base;
    /**
     * faceValue : 1
     * flowDesc : 河南乐信
     * flowNum : 5MB
     * optCode : 2
     * proCode : 6
     * retailPrices : 1
     * salePrice : .99
     * saleTariff :  0.9940
     * templetId : 158|6|2|5MB|0|*
     * templetName : ohj1
     * userArea : 全国[所有城市]
     */

    private java.util.List<RecordsBean> records;

    public BaseBean getBase() {
        return base;
    }

    public void setBase(BaseBean base) {
        this.base = base;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class BaseBean {
        private String msg;
        private String status;
        private String termTransID;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTermTransID() {
            return termTransID;
        }

        public void setTermTransID(String termTransID) {
            this.termTransID = termTransID;
        }
    }

    public static class RecordsBean {
        private String faceValue;
        private String flowDesc;
        private String flowNum;
        private String optCode;
        private String proCode;
        private String retailPrices;
        private String salePrice;
        private String saleTariff;
        private String templetId;
        private String templetName;
        private String userArea;

        public String getFaceValue() {
            return faceValue;
        }

        public void setFaceValue(String faceValue) {
            this.faceValue = faceValue;
        }

        public String getFlowDesc() {
            return flowDesc;
        }

        public void setFlowDesc(String flowDesc) {
            this.flowDesc = flowDesc;
        }

        public String getFlowNum() {
            return flowNum;
        }

        public void setFlowNum(String flowNum) {
            this.flowNum = flowNum;
        }

        public String getOptCode() {
            return optCode;
        }

        public void setOptCode(String optCode) {
            this.optCode = optCode;
        }

        public String getProCode() {
            return proCode;
        }

        public void setProCode(String proCode) {
            this.proCode = proCode;
        }

        public String getRetailPrices() {
            return retailPrices;
        }

        public void setRetailPrices(String retailPrices) {
            this.retailPrices = retailPrices;
        }

        public String getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(String salePrice) {
            this.salePrice = salePrice;
        }

        public String getSaleTariff() {
            return saleTariff;
        }

        public void setSaleTariff(String saleTariff) {
            this.saleTariff = saleTariff;
        }

        public String getTempletId() {
            return templetId;
        }

        public void setTempletId(String templetId) {
            this.templetId = templetId;
        }

        public String getTempletName() {
            return templetName;
        }

        public void setTempletName(String templetName) {
            this.templetName = templetName;
        }

        public String getUserArea() {
            return userArea;
        }

        public void setUserArea(String userArea) {
            this.userArea = userArea;
        }
    }
}

