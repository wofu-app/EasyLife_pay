package com.landicorp.android.wofupay.adapter;

import com.landicorp.android.wofupay.bean.GoodsBean;

/**
 * Created by Administrator on 2017/3/29 0029.
 */
public interface ShopCarListener {
    /**
     * @param type
     * @param position
     * @param data
     */
    void onClick(int type, int position, GoodsBean data);
}
