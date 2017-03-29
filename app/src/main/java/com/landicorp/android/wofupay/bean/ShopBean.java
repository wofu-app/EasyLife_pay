package com.landicorp.android.wofupay.bean;


public class ShopBean {
	public String id; // 商品id
	public String title;// 商品名
	public String thumb;// 封面图
	public String content;// 介绍
	public String marketprice;// 价格
	public String total;// 库存
	public String sales;// 销量
	public String keywords;// 关键字
	public String isrecommand;
	public String ishot;
	@Override
	public String toString() {
		return "ShopBean [id=" + id + ", title=" + title + ", thumb=" + thumb
				+ ", content=" + content + ", marketprice=" + marketprice
				+ ", total=" + total + ", sales=" + sales + ", keywords="
				+ keywords + ", isrecommand=" + isrecommand + ", ishot="
				+ ishot + "]";
	}

}
