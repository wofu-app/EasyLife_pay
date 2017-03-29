package com.landicorp.android.wofupay.bean;

import java.io.Serializable;

public class GoodsBean implements Serializable{
	public String goodsId;
	public String goodsNum;//数量
	public String title;//名称
	public String thumb;//图片
	public String marketprice;//单价
	
	public String content;
	public String productprice;
	public String keywords;
	public String isrecommand;
	public String ishot;
	public String total;
	public String sales;
	public HumbBean thumb_url;
	public String istime;
	public String timestart;
	public String timeend;
	public int issendfree;
	public Double dispatchprice;
	public boolean isSelect = false;
	public static class HumbBean implements Serializable{
		public String url_1;
		public String url_2;
		public String url_3;
		public String url_4;
	}
	@Override
	public String toString() {
		return "GoodsBean [goodsId=" + goodsId + ", goodsNum=" + goodsNum
				+ ", title=" + title + ", thumb=" + thumb + ", marketprice="
				+ marketprice + ", content=" + content + ", productprice="
				+ productprice + ", keywords=" + keywords + ", isrecommand="
				+ isrecommand + ", ishot=" + ishot + ", total=" + total
				+ ", sales=" + sales + ", thumb_url=" + thumb_url + ", istime="
				+ istime + ", timestart=" + timestart + ", timeend=" + timeend
				+ ", isSelect=" + isSelect + "]";
	}

	
	

}
