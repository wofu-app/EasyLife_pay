package com.landicorp.android.wofupay.bean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */

public class DataBean extends DataSupport {
    /**
     * 热门城市
     */
    public List<Train_Hot_Bean> hot=new ArrayList<Train_Hot_Bean>();
    /**
     * 城市列表
     */
    public List<Train_City_Bean> list=new ArrayList<Train_City_Bean>();
}
