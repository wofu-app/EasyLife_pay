package com.landicorp.android.wofupay.utils;

import com.landicorp.android.wofupay.bean.GoodsBean;

import java.util.ArrayList;
import java.util.List;

public class CmConstants {

	//购物车商品集合
    private static List<GoodsBean> goodsDatas;
    
    //订单商品集合
    private static List<GoodsBean> orderGoodsDatas;
    
    public static List<GoodsBean> getGoodsDatas() {
    	if(goodsDatas == null){
    		goodsDatas = new ArrayList<GoodsBean>();
    	}
		return goodsDatas;
	}

	public static void setGoodsDatas(List<GoodsBean> goodsDatas) {
		CmConstants.goodsDatas = goodsDatas;
	}
	
	public static void romoveGoodsDatas() {
		if(goodsDatas!=null){
			goodsDatas.clear();
		}
	}

	public static List<GoodsBean> getOrderGoodsDatas() {
		if(orderGoodsDatas == null){
			orderGoodsDatas = new ArrayList<GoodsBean>();
    	}
		return orderGoodsDatas;
	}

	public static void setOrderGoodsDatas(List<GoodsBean> orderGoodsDatas) {
		CmConstants.orderGoodsDatas = orderGoodsDatas;
	}
	
	public static void romoveOrderGoodsDatas() {
		if(orderGoodsDatas!=null){
			orderGoodsDatas.clear();
		}
	}
}
