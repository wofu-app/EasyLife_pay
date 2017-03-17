package com.landicorp.android.wofupay.bean;

/**
 * Created by Administrator on 2017/3/17.
 */

public class PosADList {
    public String SNcode;
    public String QMFCode;
    public String ADFileName;
    public String ADStatus;
    public String Memo;
    public int ApplyNo;
    // 本机广告
    public String ADType;

    public Object posversion;
    public int No;
    public Object BeginTime;
    public Object EndTime;
    public Object ShopName;
    public Object Address;

    /** 第一张图片 */
    public String Imgsrc1;

    /** 第二张图片 **/
    public String Imgsrc2;

    /** 第三章图片 */
    public String Imgsrc3;

    /** 第四章图片 */
    public String Imgsrc4;

    @Override
    public String toString() {
        return "PosADList [SNcode=" + SNcode + ", QMFCode=" + QMFCode
                + ", ADFileName=" + ADFileName + ", ADStatus=" + ADStatus
                + ", Memo=" + Memo + ", ApplyNo=" + ApplyNo + ", ADType="
                + ADType + ", posversion=" + posversion + ", No=" + No
                + ", BeginTime=" + BeginTime + ", EndTime=" + EndTime
                + ", ShopName=" + ShopName + ", Address=" + Address
                + ", Imgsrc1=" + Imgsrc1 + ", Imgsrc2=" + Imgsrc2
                + ", Imgsrc3=" + Imgsrc3 + ", Imgsrc4=" + Imgsrc4 + "]";
    }
}
