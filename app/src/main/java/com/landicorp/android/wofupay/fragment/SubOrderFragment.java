package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.adapter.SubOrderAdapter;
import com.landicorp.android.wofupay.bean.DetailBean;
import com.landicorp.android.wofupay.bean.GoodsBean;
import com.landicorp.android.wofupay.bean.GoodsOrderBean;
import com.landicorp.android.wofupay.bean.MarketPayBean;
import com.landicorp.android.wofupay.utils.CmConstants;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.yanzhenjie.fragment.NoFragment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * 确认订单Fragment
 * Created by Administrator on 2017/3/30 0030.
 */
public class SubOrderFragment extends NoFragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    private List<GoodsBean> goodsBean;
    private DetailBean detailBean;
    private String orderId;
    private String phoneNum;

    private ListView mListView;
    private SubOrderAdapter mAdapter;

    private Button mCancelBtn;
    private Button mOkBtn;

    private TextView mOrderNum;
    private TextView mName;
    private TextView mPhone;
    private TextView mAddress;
    private TextView mAmountMoney;

    private Double amount;

    public static SubOrderFragment newInstance(List<GoodsBean> goodsBean, DetailBean detailBean, String data, String phoneNum) {
        SubOrderFragment fragment = new SubOrderFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) goodsBean);
        args.putSerializable(ARG_PARAM2, detailBean);
        args.putString(ARG_PARAM3, data);
        args.putString(ARG_PARAM4, phoneNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            goodsBean = (List<GoodsBean>) getArguments().getSerializable(ARG_PARAM1);
            detailBean = (DetailBean) getArguments().getSerializable(ARG_PARAM2);
            orderId = getArguments().getString(ARG_PARAM3);
            phoneNum = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_order, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.subOrderUI_listView);
        View header = LayoutInflater.from(getContext()).inflate(R.layout.sub_order_header, null);
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.sub_order_footer, null);
        mListView.addHeaderView(header, null, false);
        mListView.addFooterView(footer, null, false);

        mOrderNum = (TextView) header.findViewById(R.id.orderHeadUI_orderNum);
        mName = (TextView) header.findViewById(R.id.orderHeadUI_name);
        mPhone = (TextView) header.findViewById(R.id.orderHeadUI_phone);
        mAddress = (TextView) header.findViewById(R.id.orderHeadUI_address);
        mAmountMoney = (TextView) header.findViewById(R.id.orderFootUI_amountMoney);

        mCancelBtn = (Button) view.findViewById(R.id.subOrderUI_cancelBtn);
        mCancelBtn.setOnClickListener(this);
        mOkBtn = (Button) view.findViewById(R.id.subOrderUI_okBtn);
        mOkBtn.setOnClickListener(this);

        mAdapter = new SubOrderAdapter(getActivity(), goodsBean);
        mListView.setAdapter(mAdapter);
    }

    private void initData() {
        mOrderNum.setText("订单号:" + orderId);
        Double price2 = 0.0;
        int goodsnum = 0;
        for (int i = 0; i < goodsBean.size(); i++) {
            GoodsBean goodsItem = goodsBean.get(i);
            int pnum = Integer.valueOf(goodsItem.goodsNum);
            Double Price = Double.valueOf(goodsItem.marketprice);
            goodsnum += pnum;
            if (goodsItem.issendfree == 1) {
                price2 += pnum * Price;
            }else if (goodsItem.issendfree == 0) {
                price2 += pnum * Price + goodsItem.dispatchprice;
            }
        }
        mAmountMoney.setText("合计:"+"  "+"¥"+price2);
        amount = price2;
        mName.setText(detailBean.name);
        mPhone.setText(detailBean.phone);
        mAddress.setText(detailBean.city + detailBean.area + detailBean.address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.subOrderUI_cancelBtn:
                cancelOrder();
                break;
            case R.id.subOrderUI_okBtn:
                //向服务器请求流水号
                getBillNo();
                break;
        }
    }

    private void getBillNo() {
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.URL_getBillnum);
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "GetHPBillNo");
        params.put("rechargeAccount", "");
        params.put("terminalcode", "");
        params.put("mobile", phoneNum);
        params.put("amount", amount+"");
        params.put("BN_type", "shop");
        params.put("BN_memo", "商城");
        params.put("wshopid", orderId);
        Observable<String> observable = helper.postParams(params );
        observable.subscribe(new Subscriber<Object>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "获取订单号失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Object t) {
                MarketPayBean payBean = new MarketPayBean();
                payBean.BillNo = (String) t;
                payBean.payAmount = (int)(amount*100)+"";
                payBean.phoneNumber = phoneNum;
                payBean.function = 8;

                startFragment(PayTypeFragment.newInstance(null, null, amount*100+""));
//                Intent payIntent = AppUtils.getPayIntent(OrderInfoActivity.this, payBean, amount*100+"");
//                startActivity(payIntent);
//                finish();
            }
        });

    }

    private void cancelOrder() {
        //点击取消订单 ,向服务器发送信息  修改订单状态
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.MARKET_delete);
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "delete");
        params.put("ordersn", orderId);
        Observable<String> observable = helper.postParams(params);
        observable.subscribe(new Subscriber<Object>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "订单取消失败,是否重试?", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Object t) {
                GoodsOrderBean orderBean = new Gson().fromJson((String) t, GoodsOrderBean.class);
                if (orderBean.code == 1) {
                    CmConstants.romoveGoodsDatas();
                    startFragment(MarketFragment.newInstance("", ""));
                    finish();
                }else {
                    Toast.makeText(getContext(), "订单取消失败,是否重试?", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
