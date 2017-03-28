package com.landicorp.android.wofupay.utils;

import android.annotation.SuppressLint;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.base.BaseApplication;
import com.landicorp.android.wofupay.model.FileBean;
import com.landicorp.android.wofupay.model.FunctionBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ping on 2016/4/29.
 */
@SuppressLint("SimpleDateFormat")
public class AppUtils {
	private static Toast toastShow = null;
	/**
	 * 根据功能类型的id获取相应的名称
	 * 
	 * @param id
	 * @return
	 */
	public static String getFunctionByID(int id) {
		String function = null;
		switch (id) {
		case 0:
			function = "芯片卡充值";
			break;
		case 1:
			function = "公交卡充值";
			break;
		case 2:
			function = "加油卡充值";
			break;
		case 3:
			function = "手机充值";
			break;
		case 4:
			function = "游戏充值";
			break;
		case 5:
			function = "彩票购买";
			break;
		case 6:
			function = "水电煤充值";
			break;
		case 7:
			function="火车票";
			break;
		case 8:
			function = "商城";
			break;
		default:
			break;
		}

		return function;
	}

	/**
	 * 读取功能模块的相关信息
	 * 
	 * @param path
	 *            父文件夹路径
	 * @param fileName
	 *            文件名称
	 * @return 是否读取成功
	 */
	public static FunctionBean readFromFile(String path, String fileName) {
		FunctionBean bean = null;
		File file = new File(path, FileBean.FUNCTIONNANE);
		if (!file.exists() || file.isDirectory()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			int len = 0;
			while ((len = fis.read(bytes)) > 0) {
				bos.write(bytes, 0, len);
			}
			String json = bos.toString();
			bean = new Gson().fromJson(json, FunctionBean.class);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;

	}

	public static void upFucntion(FunctionBean function) {
		try {
			BaseApplication mApplication = ((BaseApplication) BaseApplication.getInstance());

			if (function != null) {
				List<FunctionBean.DataBean> data = function.getData();
				Map<Integer, FunctionBean.DataBean> map = new HashMap<Integer, FunctionBean.DataBean>();
				for (FunctionBean.DataBean dataBean : data) {
					map.put(dataBean.getMenuid(), dataBean);
				}
				mApplication.setFunction(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据Id获取功能状态是否正常
	 * 
	 * @param id
	 *            1:公交卡充值 2.cpu卡充值 3.pboc卡充值 4.余额查询 5.加油卡充值 6.手机充值 7.话费充值 8.流量充值
	 *            9.游戏充值 10.彩票 11福彩 12.体彩 13.助农取款 14.公益 15.水电煤充值 16.电费 17水费
	 *            18.燃气费
	 * @return 0正常,1异常,2隐藏
	 */
	public static int getFunctionState(int id) {
		BaseApplication mApplication = ((BaseApplication) BaseApplication.getInstance());
		Map<Integer, FunctionBean.DataBean> function = mApplication.getFunction();
		if (function == null) {
			return 0;
		}
		int result = 0;
		FunctionBean.DataBean dataBean = function.get(id);

		if (dataBean == null || dataBean.getMenustatus() == null) {
			JLog.i("没有数据");
			return 0;
		}
		// JLog.i(dataBean.toString());
		if (TextUtils.equals("00", dataBean.getMenustatus())) {//正常
			result = 0;
		} else if (TextUtils.equals("01", dataBean.getMenustatus())) {
			result = 1;
		} else if (TextUtils.equals("02", dataBean.getMenustatus())) {
			result = 2;
		} else {
			result = 0;
		}
		return result;
	}

	/**
	 * 根据id获取相应的信息
	 * 
	 * @param id
	 *            1:公交卡充值 2.cpu卡充值 3.pboc卡充值 4.余额查询 5.加油卡充值 6.手机充值 7.话费充值 8.流量充值
	 *            9.游戏充值 10.彩票 11福彩 12.体彩 13.助农取款 14.公益 15.水电煤充值 16.电费 17水费
	 *            18.燃气费
	 * @return
	 */
	public static String getMsg(int id) {
		String msg = "该功能正在升级,请使用其他功能";
		int functionState = getFunctionState(id);
		if (functionState == 1 || functionState == 2) {
			BaseApplication mApplication = ((BaseApplication) BaseApplication.getInstance());
			Map<Integer, FunctionBean.DataBean> function = mApplication.getFunction();
			msg = function.get(id).getMsg();
		}
		return msg;
	}
	/**
	 * 格式化手机号
	 *
	 * @param phone
	 * @return
	 */
	public static String formatPhone(String phone) {
		String result = phone;
		StringWriter sw = new StringWriter();
		char[] charArray = phone.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			sw.append(charArray[i]);
			if (i == 2 || i == 6) {
				sw.append("-");
			}
		}
		result = sw.toString();
		return result;
	}
	/**
	 * 判断是否为手机
	 * 移动号段：134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,
	 * 187,188,147,178,1705
	 *
	 * 联通号段: 130,131,132,155,156,185,186,145,176,1709
	 *
	 * 电信号段:133,153,180,181,189,177,1700
	 *
	 * @param phone
	 * @return
	 */
	public static boolean isPhoneNumber(String phone) {
		if (phone == null)
			return false;
		String str = "^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(phone);
		return m.matches();
	}
	/**
	 * 判断手机号类型 移动号段:
	 * 134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,
	 * 187,188,147,178,1705 联通号段: 130,131,132,155,156,185,186,145,176,1709 电信号段:
	 * 133,153,180,181,189,177,1700
	 *
	 * @param phone
	 * @return 0移动 1联通 2电信 3为虚拟号段 否则为其他号码
	 */
	public static int PhoneNumberType(String phone) {

		int type = -1;

		// 移动
		String CMCC = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)";
		// 联通
		String WCDMA = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)";
		// 电信
		String CTWAP = "(^1(33|53|77|8[019])\\d{8}$)";

		String XU = "(^170\\d{8}$)";

		// 移动
		Pattern p1 = Pattern.compile(CMCC);
		Matcher m1 = p1.matcher(phone);

		// 联通
		Pattern p2 = Pattern.compile(WCDMA);
		Matcher m2 = p2.matcher(phone);

		// 电信
		Pattern p3 = Pattern.compile(CTWAP);
		Matcher m3 = p3.matcher(phone);

		// 虚拟号段
		Pattern p4 = Pattern.compile(XU);
		Matcher m4 = p4.matcher(phone);

		// 移动
		if (m1.matches()) {
			type = 0;
			// 联通
		} else if (m2.matches()) {
			type = 1;
			// 电信
		} else if (m3.matches()) {
			type = 2;
			// 虚拟号段
		} else if (m4.matches()) {
			type = 3;

			// 未知号码
		} else {
			type = 4;
		}

		return type;
	}
	/**
	 * 获取当前时间
	 *
	 * @param type
	 *            时间格式化的格式
	 * @return 格式化的字符串
	 */
	public static String getStringDate(String type) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(type);
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	/**
	 * 描述：MD5加密.
	 *
	 * @param str
	 *            要加密的字符串
	 */
	public final static String MD5(String str) {
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			byte[] strTemp = str.getBytes("utf-8");
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte tmp[] = mdTemp.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char strs[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				strs[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				strs[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			return new String(strs).toLowerCase(); // 换后的结果转换为字符串
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 将Map集合转换成字符串
	 *
	 * @param params
	 * @return
	 */
	public static String encodeParameters(Map<String, String> params) {
		StringBuilder encodedParams = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			encodedParams.append(URLEncoder.encode(entry.getKey()));
			encodedParams.append('=');
			// URLEncoder.encode(entry.getValue())
			encodedParams.append(entry.getValue());
			encodedParams.append('&');
		}
		return encodedParams.toString();
	}
	/**
	 * 以toast方式显示提示信息
	 *
	 * @param context
	 * @param message
	 */
	public static void showMessage(Context context, String message) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastRoot = inflater.inflate(R.layout.lout_toast_style, null);
		TextView textView = (TextView) toastRoot
				.findViewById(R.id.tv_Toast_Property);
		textView.setText(message);

		if (toastShow == null) {
			toastShow = new Toast(context);
		} else {
			toastShow.cancel();
		}
		toastShow.setDuration(Toast.LENGTH_SHORT);
		toastShow.setGravity(Gravity.BOTTOM, 0, 10);
		toastShow.setView(toastRoot);
		toastShow.show();
	}
	/*
	 * 把中文转换位Unicode格式
	 */
	public static String stringToUnicode(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			if (ch > 255)
				str += "\\u" + Integer.toHexString(ch);
			else
				str += "\\" + Integer.toHexString(ch);
		}
		return str;
	}
	/*
	 * Unicode转中文
	 */
	public static String decodeUnicode(String theString) {

		char aChar;

		int len = theString.length();

		StringBuffer outBuffer = new StringBuffer(len);

		for (int x = 0; x < len;) {

			aChar = theString.charAt(x++);

			if (aChar == '\\') {

				aChar = theString.charAt(x++);

				if (aChar == 'u') {

					// Read the xxxx

					int value = 0;

					for (int i = 0; i < 4; i++) {

						aChar = theString.charAt(x++);

						switch (aChar) {

							case '0':

							case '1':

							case '2':

							case '3':

							case '4':

							case '5':

							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException(
										"Malformed   \\uxxxx   encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';

					else if (aChar == 'n')

						aChar = '\n';

					else if (aChar == 'f')

						aChar = '\f';

					outBuffer.append(aChar);

				}

			} else

				outBuffer.append(aChar);

		}

		return outBuffer.toString();

	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	/**
	 * 获取两位小数
	 *
	 * @param data
	 * @return
	 */
	public static float get2Double(double data) {
		BigDecimal b = new BigDecimal(data);
		// return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		// b.setScale(2, BigDecimal.ROUND_HALF_UP) 表明四舍五入，保留两位小数
		float v = (float) Double.parseDouble(String.format("%.2f", data));

		return v;
	}
	/**
	 * dp 2 px
	 *
	 * @param dpVal
	 */
	public static int dp2px(int dpVal, Context c) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, c.getResources().getDisplayMetrics());
	}
}
