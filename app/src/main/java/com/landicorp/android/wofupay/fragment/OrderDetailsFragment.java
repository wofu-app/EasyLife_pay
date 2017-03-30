package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.adapter.OrderDetailsAdapter;
import com.landicorp.android.wofupay.bean.DetailBean;
import com.landicorp.android.wofupay.bean.GoodsBean;
import com.landicorp.android.wofupay.bean.GoodsOrderBean;
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

    private DetailBean detailBean;

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
        mListView = (ListView) view.findViewById(R.id.orderDetailUI_listView);
        View header = LayoutInflater.from(getContext()).inflate(R.layout.order_details_header, null);
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.order_details_footer, null);

        mListView.addHeaderView(header, null, false);
        mListView.addFooterView(footer, null, false);

        mAddressBtn = (ImageButton) header.findViewById(R.id.orderDetailUI_addressBtn);
        mAddressBtn.setOnClickListener(this);
        mName = (TextView) header.findViewById(R.id.orderDetailUI_name);
        mPhone = (TextView) header.findViewById(R.id.orderDetailUI_phone);
        mAddress = (TextView) header.findViewById(R.id.orderDetailUI_address);
        mAmountGoods = (TextView) footer.findViewById(R.id.orderDetailUI_amountGoods);
        mAmountMoney = (TextView) footer.findViewById(R.id.orderDetailUI_amountMoney);
        mPrice = (TextView) view.findViewById(R.id.orderDetailUI_price);
        mOkBtn = (Button) view.findViewById(R.id.orderDetailUI_okBtn);
        mOkBtn.setOnClickListener(this);

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
                if (detailBean == null) {
                    Toast.makeText(getContext(), "请添加收货人信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                CmConstants.setOrderGoodsDatas(mAdapter.getDatas());
                //提交订单
                submitOrder();
                break;
        }
    }

    private void submitOrder() {
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.MARKET);
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "order");
        params.put("price", price2+"");
        String address = "{\"id\":\""+"0"+"\",\"realname\":\""+detailBean.name+"\",\"mobile\":\""+detailBean.phone+"\",\"address\":\""+detailBean.address+"\",\"province\":\""+detailBean.province+"\",\"city\":\""+detailBean.city+"\",\"area\":\""+detailBean.area+"\"}";
        String goods = "{";
        for (int i = 0; i < goodsBean.size(); i++) {
            goods += "\""+i+"\""+":"+"{\"id\":\""+goodsBean.get(i).goodsId+"\",\"total\":\""+goodsBean.get(i).goodsNum+"\"},";
        }
        int lastIndexOf = goods.lastIndexOf(",");
        String substring = goods.substring(0, lastIndexOf);
        substring+="}";
        params.put("address", address);
        params.put("goods", substring);
        params.put("terminalno", "");
        Observable<String> observable = helper.postParams(params);
        observable.subscribe(new Subscriber<Object>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object t) {
                GoodsOrderBean bean = new Gson().fromJson((String) t, GoodsOrderBean.class);
                if (bean.code == 2) {
                    Toast.makeText(getContext(), "订单生成失败,请检查网络", Toast.LENGTH_SHORT).show();
                }else if (bean.code == 1) {
                    Log.d("=======得到的订单号", bean.data);
                    // 要判断信息是否为空
                    //进入订单详情界面
                    startFragment(SubOrderFragment.newInstance(goodsBean, detailBean, bean.data, phoneNum));
                }

            }
        });

    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle result) {
        super.onFragmentResult(requestCode, resultCode, result);
        if (result != null && requestCode == 111 && resultCode == RESULT_OK) {
            Log.e("MyTest", "===========Enter 111=========");
            List<DetailBean> data = (List<DetailBean>) result.getSerializable("AddressList");
            if(result != null){
                for (int i = 0; i < data.size(); i++) {
                    detailBean = new DetailBean();
                    mName.setText(data.get(i).name);
                    mPhone.setText(data.get(i).phone);
                    mAddress.setText(data.get(i).province + data.get(i).city + data.get(i).area + data.get(i).address);
                    detailBean.name = data.get(i).name;
                    detailBean.phone = data.get(i).phone;
                    detailBean.province = data.get(i).province;
                    detailBean.city = data.get(i).city;
                    detailBean.area = data.get(i).area;
                    detailBean.address = data.get(i).address;
                }
            }

        }else if (result != null && requestCode == 222 && resultCode == RESULT_OK) {
            Log.e("MyTest", "===========Enter 222=========");
            List<DetailBean> data = (List<DetailBean>) result.getSerializable("AddressList");
            if(result != null ){
                for (int i = 0; i < data.size(); i++) {
                    detailBean = new DetailBean();
                    mName.setText(data.get(i).name);
                    mPhone.setText(data.get(i).phone);
                    mAddress.setText(data.get(i).province + data.get(i).city + data.get(i).area + data.get(i).address);
                    detailBean.name = data.get(i).name;
                    detailBean.phone = data.get(i).phone;
                    detailBean.province = data.get(i).province;
                    detailBean.city = data.get(i).city;
                    detailBean.area = data.get(i).area;
                    detailBean.address = data.get(i).address;
                }
            }
            phoneNum = result.getString("phoneNum");
            hasVerify = result.getBoolean("hasVerify", false);
        }

    }
}
