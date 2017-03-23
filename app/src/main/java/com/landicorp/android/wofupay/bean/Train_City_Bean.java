package com.landicorp.android.wofupay.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/3/22.
 */

public class Train_City_Bean extends DataSupport {
    /**
     * 城市拼音 beijing
     */
    public String code;
    /**
     * 城市名字 北京
     */
    public String name;
    /**
     * 缩写
     */
    public String shortSpell;
    /**
     * 拼音
     */
    public String spell;
    @Override
    public String toString() {
        return "Train_City_Bean [code=" + code + ", name=" + name
                + ", shortSpell=" + shortSpell + ", spell=" + spell + "]";
    }



}
