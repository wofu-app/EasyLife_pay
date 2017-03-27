package com.landicorp.android.wofupay.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;
import android.util.Xml;

public class XmlUtils {

	private static XmlUtils xmlUtils = null;
	private String reason="启用";

	private XmlUtils() {
	};

	public static XmlUtils getInstance() {
		if (xmlUtils == null) {
			xmlUtils = new XmlUtils();
		}
		return xmlUtils;
	}

	/**
	 * 获取xml文件中指定的值
	 * @param xmlString
	 * @param name
	 * @return
	 */
	public static String getSpecialValue(String xmlString,String name){
		String value = null;
		
		if (TextUtils.isEmpty(xmlString)) {
			return null;
		}
		
		XmlPullParser pullParser = Xml.newPullParser();
	
		try {
			pullParser.setInput(new ByteArrayInputStream(xmlString.getBytes("UTF-8")), "UTF-8");
			int event = pullParser.getEventType();// 触发第一个事件
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
					case XmlPullParser.START_TAG:
						if (name.equals(pullParser.getName())) {
							value = pullParser.nextText();
						}
						break;
				}
				event = pullParser.next();

			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 将map集合转换成Xml
	 * @param map
	 * @return
	 */
	public static String mapToHeardXml(Map<String, String> map){
		StringBuffer sb  = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><message>");
		Set<String> keySet = map.keySet();
		sb.append("<head>");
		for (String key : keySet) {
			if (key.equals("body")) {
				continue;
			}
			
			if (!TextUtils.isEmpty(key)) {
				sb.append("<"+key+">");
				sb.append(map.get(key));
				sb.append("</"+key+">");
			}
		}
		sb.append("</head>");
		sb.append("<body>").append(map.get("body")).append("</body>");
		sb.append("</message>");
		return sb.toString();
	}
	
	/**
	 * 将map集合转换成Xml
	 * @param map
	 * @return
	 */
	public static String mapToBodyXml(Map<String, String> map){
		StringBuffer sb  = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><body>");
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			if (!TextUtils.isEmpty(key)) {
				sb.append("<"+key+">");
				sb.append(map.get(key));
				sb.append("</"+key+">");
			}
		}
		sb.append("</body>");
		return sb.toString();
	}
	

}
