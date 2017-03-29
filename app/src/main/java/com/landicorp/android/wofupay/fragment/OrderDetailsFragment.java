package com.landicorp.android.wofupay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.adapter.OrderDetailsAdapter;
import com.landicorp.android.wofupay.bean.GoodsBean;
import com.yanzhenjie.fragment.NoFragment;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详情添加地址Fragment
 * Created by Administrator on 2017/3/29 0029.
 */
public class OrderDetailsFragment extends NoFragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<GoodsBean> goodsBean;
    private String mParam2;

    private ImageButton mAddressBtn;
    private TextView mName;
    private TextView mPhone;
    private TextView mAddress;
    private ListView mListView;
    private TextView mAmountGoods;
    private TextView mAmountMoney;
    private TextView mPrice;
    private Button mOkBtn;

    private Double price;
    private Double price2;

    private boolean hasVerify;
    private String phoneNum;

    private OrderDetailsAdapter mAdapter;

    public OrderDetailsFragment() {
    }

    public static OrderDetailsFragment newInstance(List<GoodsBean> goodsBean, String param2) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) goodsBean);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            goodsBean = (List<GoodsBean>) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_details, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        mAddressBtn = (ImageButton) view.findViewById(R.id.orderDetailUI_addressBtn);
        mAddressBtn.setOnClickListener(this);
        mName = (TextView) view.findViewById(R.id.orderDetailUI_name);
        mPhone = (TextView) view.findViewById(R.id.orderDetailUI_phone);
        mAddress = (TextView) view.findViewById(R.id.orderDetailUI_address);
        mAmountGoods = (TextView) view.findViewById(R.id.orderDetailUI_amountGoods);
        mAmountMoney = (TextView) view.findViewById(R.id.orderDetailUI_amountMoney);
        mPrice = (TextView) view.findViewById(R.id.orderDetailUI_price);
        mOkBtn = (Button) view.findViewById(R.id.orderDetailUI_okBtn);
        mOkBtn.setOnClickListener(this);

        mListView = (ListView) view.findViewById(R.id.orderDetailUI_listView);
        mAdapter = new OrderDetailsAdapter(getActivity());
        mAdapter.setDatas(goodsBean);
        mListView.setAdapter(mAdapter);
    }

    private void initData() {
        price2 = 0.0;
        int goodsnum = 0;
        for (int i = 0; i < goodsBean.size(); i++) {
            GoodsBean goodsItem = goodsBean.get(i);
            int pnum = Integer.valueOf(goodsItem.goodsNum);
            price = Double.valueOf(goodsItem.marketprice);
            goodsnum += pnum;
            if (goodsItem.issendfree == 1) {
                price2 += pnum * price;
            }else if (goodsItem.issendfree == 0) {
                price2 += pnum * price + goodsItem.dispatchprice;
            }
        }
        mAmountGoods.setText("共"+goodsnum+"件商品"+"  "+"合计:");
        mAmountMoney.setText("¥:" + price2);
        mPrice.setText("¥:" + price2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.orderDetailUI_addressBtn:

                // 如果是第一次点击,进入短信验证界面. 如果短信验证过了 直接进入选择地址界面
                if (hasVerify) {
                    //进入选择地址界面
                    startFragmentForResquest(SelectAddressFragment.newInstance(phoneNum, ""), 111);
                }else {
                    //进入短信验证界面
                    startFragmentForResquest(NoteVerifyFragment.newInstance("", ""), 222);
                }

                break;
            case R.id.orderDetailUI_okBtn:



                break;
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle result) {
        super.onFragmentResult(requestCode, resultCode, result);


    }
}
