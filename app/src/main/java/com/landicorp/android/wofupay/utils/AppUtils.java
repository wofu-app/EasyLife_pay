package com.landicorp.android.wofupay.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.finance.app.FinanceApplication;
import com.landicorp.android.finance.transaction.DefaultTransactionContext;
import com.landicorp.android.finance.transaction.Transaction;
import com.landicorp.android.finance.transaction.Transaction.OnTransactionFinishListener;
import com.landicorp.android.wofupay.activity.MainActivity;
import com.landicorp.android.wofupay.base.BaseApplication;
import com.landicorp.android.wofupay.model.FileBean;
import com.landicorp.android.wofupay.model.FunctionBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ping on 2016/4/29.
 */
@SuppressLint("SimpleDateFormat")
public class AppUtils {

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
}
