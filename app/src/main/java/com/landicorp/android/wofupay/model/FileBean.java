package com.landicorp.android.wofupay.model;


import java.io.File;

import android.app.Activity;
import android.os.Environment;
import com.landicorp.android.wofupay.base.BaseApplication;

/**
 * 对文件信息处理的类
 * @author Administrator
 *
 */
public class FileBean {
	/**log日志储存路径*/
	public static final String FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/AllPay/crash/";

	/**配置文件储存路径*/
	public static final String CONFIGPATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/AllPay/config/";

	/**广告下载流量统计文件储存路径*/
	public static final String ADPATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";

	/**280轮播图 广告显示储存路径*/
	public static final String SCREENPATH = BaseApplication.getContext().getDir("LBAD", 0).getAbsolutePath()+"/lunbo/";

	/**本机界面储存位置界面广告*/
	public static final String JIEMIANPATH = BaseApplication.getContext().getDir("LocalAD", 0).getAbsolutePath()+"/jiemian/";

	/**本机界面广告*/
	public static final String JIEMIAADNPATH = BaseApplication.getContext().getDir("LocalAD", 0).getAbsolutePath()+"/jiemian/image.jpg";
	
	/**更新APk储存路径*/
	public static final String UPLOAD = BaseApplication.getContext().getDir("LocalAD", 0).getAbsolutePath()+"/upload/";

	/**
	 * so库加载路径
	 */
	public static final String SOPath = new File(BaseApplication.getContext().getDir("jniLibs", Activity.MODE_PRIVATE).getAbsolutePath()+ File.separator + "libNativeDown.so").getAbsolutePath();
	
	/**
	 * 功能控制模块储存路径
	 */
	public static final String FUNCTION =  BaseApplication.getContext().getDir("function", 0).getAbsolutePath();
	
	/**
	 * 功能控制模块文件名称
	 */
	public static final String FUNCTIONNANE = "function.json";
	
	/**火车站*/
	public static final String TRAIN_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/AllPay/train/";
	/**火车站*/
	public static final String TRAIN_NAME = "trains";


}
