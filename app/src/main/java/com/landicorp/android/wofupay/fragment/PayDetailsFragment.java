package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.landicorp.android.wofupay.bean.BasicBean;
import com.landicorp.android.wofupay.volley.CallBackListener;

/**
 * 支付详情Fragment
 * Created by Administrator on 2017/3/23 0023.
 */
public class PayDetailsFragment extends BaseFragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private BasicBean basicBean;
    private String payway;

    private String dateTime;

    private ImageView mPayTitle;
    private ImageView mPayImage;
    private TextView mGoodsName;
    private TextView mDealMoney;
    private TextView mShopName;
    private TextView mOrderNum;

    private Button mCancelBtn;
    private Button mCompleteBtn;

    public PayDetailsFragment() {
    }

    public static PayDetailsFragment newInstance(BasicBean param1, String param2) {
        PayDetailsFragment fragment = new PayDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            basicBean = getArguments().getParcelable(ARG_PARAM1);
            payway = getArguments().getString(ARG_PARAM2);
        }
    }




}
