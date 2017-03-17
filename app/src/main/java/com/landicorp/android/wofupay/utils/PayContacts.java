package com.landicorp.android.wofupay.utils;

/**
 * Created by Administrator on 2017/3/17.
 */

public class PayContacts {
    public static String version  = "2.2.7.v2";//版本号
    public static String verDate = "2016年2月28日";//版本号
 //   public static String terminalcode =  DeviceUtils.getDevicePort();//终端号

    /************************测试环境*****************************/
//	public static boolean isDebug = true;
//	public static String base_url = "http://120.210.205.29:8066/";//正式环境
//	public static String WOFU_MERID = "29002000209"; // 测试环境商户号
//	public static  String LOTTER="http://120.210.205.29:8067/"; //测试环境
//	public static String YIN ="0";//测试地址
//	public static String YIN_FACTORYID = "0000";//测试
//	public static String YIN_KEY = "000000";//测试
//	public static String WOFU_URL = "http://120.210.205.29:8067/ChinaLottery/ChinaLotteryServer.ashx";//测试
//	public static String WOFU_TYPE = "0";
//	public static String LOTTERIES = "http://119.57.104.170:7070/billservice/sltAPI";
//	public static String LOTTERIES_AGENTID = "800205";
//	public static String LOTTERIES_AGENTPWD = "wzltest";

    public  static String NEWLOTTERIES="http://121.33.73.19:8005";
    public  static String NEWLOTTERIES_ID="100033";
    public  static String NEWLOTTERIES_KEY="A134951829CB53232A67C39D";




    ///////////////////////////////////////////正式环境/////////////////////////////////////////////////
    public static boolean isDebug = false;
    public static String WOFU_TYPE = "1";
    public static String WOFU_URL = "http://120.210.205.29:8066/ChinaLottery/ChinaLotteryServer.ashx";// 正式
    public static String WOFU = "http://122.144.133.212:8047/"; // 彩票正式环境
    public static String base_url = "http://120.210.205.29:8066/";// 正式环境
    public static String WOFU_MERID = "29002000126"; // 彩票正式环境商户号
    public static String LOTTER = "http://120.210.205.29:8066/"; // 正式环境
    public static String YIN = "1";// 正式地址
    /** 企业编号 factoryID */
    // public static String YIN_FACTORYID = "hp37";//正式
    public static String YIN_FACTORYID = "WFMS";// 正式
    /** 签到密码 key */
    // public static String YIN_KEY = "hprj76";//正式
    public static String YIN_KEY = "061116";// 正式
    public static String LOTTERIES = "http://116.213.75.179:8080/billservice/sltAPI";
    public static String LOTTERIES_AGENTID = "800194";
    public static String LOTTERIES_AGENTPWD = "15wofu08m";




    /*******************************微信数据*****************************************/
    public static String weixin_notify_url = base_url+"BillPay/WeixinPayXZ.ashx";//微信回调地址
    public static String weixin_mch_create_ip = "127.0.0.1";//微信订单生成机器的Ip
    public static String weixin_service = "pay.weixin.native";
    public static String ali_service = "pay.alipay.native";

    public static String weixin_query_seriver_url="unified.trade.query";
    public static String weixin_url="https://pay.swiftpass.cn/pay/gateway";//微信支付地址
    //	public static String weixin_key="9d101c97133837e13dde2d32a5054abb";//微信支付的key值测试
//	public static String weixin_mch_id = "7551000001";//微信商户号测试
    public static String weixin_key="5cfb907241d25a4bae82241565871d01";//微信支付的key值
    public static String weixin_mch_id = "101500000819";//微信商户号


    /**************************服务器配置************************************/
    public static String Server_url="120.210.205.29";//检查是否更新

    public static String hear_url=base_url+"SystemMange/SysServer.ashx";//心跳地址?action=Heartbeat"

    public static String unionpay_url=base_url+"BillPay/BillPayXZ.ashx?action=UpdateBillStatus";//银联支付销账


    public static String updata_DownLoad_URL=base_url;//有更新下载文件地址


    public static  String URL=base_url+"gateway.ashx";//账单号正式地址


    public static  String UPDATA_URL=base_url+"WebServer.ashx"; //广告 流量等数据上传地址
    public static  String AD_URL=base_url+"ADFiles/"; //广告下载地址
    public static  String LogURL=base_url+"HandlerUpLoad.ashx"; //log日志上传地址


    public static  String URL_YIN=base_url+"ysapi/ysApiRequest.ashx"; //银盛服务器转接地址

    public static String life_recharge=base_url+"/BillPay/BillPayServer.ashx";

    /**
     * 支付宝支付网址
     */
    public static String PAY_URl="http://pay.haiposoft.com/alipayapi.php";
    /**
     * 公交卡充值结果
     */
    public static String Pay_REsult="http://pay.haiposoft.com/rechargeResult.php";

    /**
     * 功能远程控制
     */
    public static String functions= base_url+"SystemMange/SysServer.ashx";

    /**
     * 更新收单程序
     */
    public static String upPay= base_url+"SystemMange/SysServer.ashx";


    /**
     * 商城列表
     */
    public static  String Market_URL = "http://mall.ahwofu.com/goods_terminal.php";
    /**
     * 商城下单测试接口
     */
    public static String MARKET = "http://mall.ahwofu.com/goods_terminal.php"; //

    /**
     * 查询地址测试接口
     */
    public static final String SEARCH_address = "http://120.210.205.29:8066/ShopServer/ShopServer.ashx";

    /**
     * 获取BillNum测试接口
     */
    public static  String URL_getBillnum="http://120.210.205.29:8066/"+"gateway.ashx";

    /**
     * 商城取消订单测试接口
     */
    public static String MARKET_delete = "http://mall.ahwofu.com/goods_terminal.php";


    /*************************同求网数据*************************************/
//
//	/**
//	 * 用户ID
//	 */
//	public static String ID = "160428094542838";
//
//	/**
//	 * 数字签名
//	 */
//	public static String SING="82659050ba777e2877898c88f81df064";
//	/**
//	 * 同求网基本数据
//	 */
//	public static String TONG_URL = "http://www.tqcard.com/queryProductsAPI";
//	/**
//	 * 水电煤网址
//	 */
//	public static String TONG_WATER="http://www.tqcard.com/queryWaterBill";
//
//	/**
//	 * 手机查询
//	 */
//	public static String TONG_PHONE="http://www.tqcard.com/queryCZProductsAPI";
//
//	/**
//	 * 水煤电订单查询接口
//	 */
//	public static String WARTER_ORDER="http://www.tqcard.com/sendWaterCoalOrder";
//
//	/**
//	 * 订单发送接口
//	 */
//	public static String ORDER_SEND="http://www.tqcard.com/sendOrderAPI";
//	/**
//	 *  查询订单接口
//	 */
//	public static String ORDER_QUERY="http://www.tqcard.com/queryOrderInfoAPI";

    /*****************************彩票相关接口*********************************************/

    public static  String LOTTER_INFO_URL=LOTTER+"ChinaLottery/ChinaLotteryServer.ashx?action=SetLotteryRecord";
    // 获取彩票的订单号，校验手机号是否注册
    public static  String LOTTER_URL=LOTTER+"ChinaLottery/ChinaLotteryServer.ashx";
    // 彩种查询接口
    public static String WOFU_LOTTERYCODEQUERY = "lotteryCodeQuery";
    // 彩票期数查询
    public static String WOFU_QUERYPERIODINFO = "queryPeriodInfo";
    // 投注彩票号码验证
    public static String WOFU_BETCHECK = "betCheck";
    // 投注
    public static String WOFU_BET = "bet";

    public static String WOFU_KEY = "AUTOLOTTERY"; //测试环境的key，正式环境也是
    public static String wofu_urlstring=base_url+"ChinaLottery/ChinaLotteryServer.ashx";//彩票个人信息查询接口

    /********************************************银盛数据***************************************************/

    public static String PHONE=base_url+ "PhoneSever/PhoneSever.ashx";

    /**
     * 终端号 terminalid
     */
    public static String YIN_TERMINALID = "20154301";
//	public static String YIN_TERMINALID_TTAN = "20154301";

    /**
     * 手机充值地址
     */
    public static String YIN_GAMES ="qqRecharge.cgi";
    /**
     * 公交卡充值地址
     */
    public static String YIN_GASCARD ="oilCardRecharge.cgi";

    /**
     * 服务器转接地址
     */
    public static String TRANSFER_URL =base_url+"ysapi/ysApiRequest.ashx";
//	public static String tran=Train_BaseUrl+"BookTicketService.cgi";

    public static String YIN_TRAIN="BookTicketService.cgi";
    public static String YIN_TRAIN_STATION="StationListService.cgi";
    public static String YIN_TRAIN_CITY="TrainListService.cgi";
    public static String YIN_TRAIN_BASIC = base_url+"TrainTickets/TrainTickets.ashx";

}

