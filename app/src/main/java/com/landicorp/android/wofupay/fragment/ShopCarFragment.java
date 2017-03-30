package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.adapter.ShopCarAdapter;
import com.landicorp.android.wofupay.adapter.ShopCarListener;
import com.landicorp.android.wofupay.bean.GoodsBean;
import com.landicorp.android.wofupay.utils.CmConstants;
import com.yanzhenjie.fragment.NoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车Fragment
 * Created by Administrator on 2017/3/28 0028.
 */
public class ShopCarFragment extends NoFragment implements View.OnClickListener, ShopCarListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private List<GoodsBean> goodsBean;

    private Button mGoHomeBtn;
    private LinearLayout mNoGoodsLy;
    private Button mGoBuyBtn;
    private View mDivisLine;
    private ListView mListView;
    private LinearLayout mFooterLy;
    private Button mDeleteAllBtn;
    private Button mDeleteBtn;
    private TextView mAmountMoney;
    private Button mCloseAccBtn;

    private Double price;

    private ShopCarAdapter mAdapter;

    public ShopCarFragment() {
    }

    public static ShopCarFragment newInstance(String param1, String param2) {
        ShopCarFragment fragment = new ShopCarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        goodsBean = CmConstants.getGoodsDatas();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_car, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        mGoHomeBtn = (Button) view.findViewById(R.id.shopCarUI_goHomeBtn);
        mGoHomeBtn.setOnClickListener(this);
        mNoGoodsLy = (LinearLayout) view.findViewById(R.id.shopCarUI_noGoodsLy);
        mGoBuyBtn = (Button) view.findViewById(R.id.shopCarUI_goBuyBtn);
        mGoBuyBtn.setOnClickListener(this);
        mDivisLine = view.findViewById(R.id.shopCarUI_divisLine);
        mFooterLy = (LinearLayout) view.findViewById(R.id.shopCarUI_footerLy);
        mDeleteAllBtn = (Button) view.findViewById(R.id.shopCarUI_deleteAllBtn);
        mDeleteAllBtn.setOnClickListener(this);
        mDeleteBtn = (Button) view.findViewById(R.id.shopCarUI_deleteBtn);
        mDeleteBtn.setOnClickListener(this);
        mAmountMoney = (TextView) view.findViewById(R.id.shopCarUI_amountMoney);
        mCloseAccBtn = (Button) view.findViewById(R.id.shopCarUI_closeAccBtn);
        mCloseAccBtn.setOnClickListener(this);
        mListView = (ListView) view.findViewById(R.id.shopCarUI_listView);
        if (goodsBean != null && goodsBean.size() > 0) {
            mAdapter = new ShopCarAdapter(getActivity(), goodsBean, mAmountMoney);
            mAdapter.setmShopCarListener(this);
            mListView.setAdapter(mAdapter);
        }
    }

    public void initData() {
        if(goodsBean.size() == 0) {
            //显示购物车图标,隐藏其他的控件
            emptyCar();
        }else {
            //购物车不为空
            notEmpty();
        }
    }

    private void notEmpty() {
        mGoHomeBtn.setVisibility(View.VISIBLE);
        mDivisLine.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.VISIBLE);
        mFooterLy.setVisibility(View.VISIBLE);
        mNoGoodsLy.setVisibility(View.GONE);
    }

    private void emptyCar() {
        mGoHomeBtn.setVisibility(View.GONE);
        mDivisLine.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        mFooterLy.setVisibility(View.GONE);
        mNoGoodsLy.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shopCarUI_goHomeBtn:
                //回商城首页
                startFragment(MarketFragment.newInstance("", ""));
                finish();
                break;
            case R.id.shopCarUI_goBuyBtn:
                //回商城首页
                startFragment(MarketFragment.newInstance("", ""));
                finish();
                break;
            case R.id.shopCarUI_deleteAllBtn:
                selectall();
                break;
            case R.id.shopCarUI_deleteBtn:
                deletegoods();
                break;
            case R.id.shopCarUI_closeAccBtn:
                List<GoodsBean> isSelectlist = new ArrayList<GoodsBean>();
                isSelectlist.clear();
                for (int i = 0; i < goodsBean.size(); i++) {
                    GoodsBean bean1 = goodsBean.get(i);
                    if (bean1.isSelect == true) {
                        isSelectlist.add(bean1);
                    }

                }
                //计算商品总价
                Double totalPrice = setTotalPrice(isSelectlist);
                if (isSelectlist.size() == 0) {
                    Toast.makeText(getContext(), "请选择商品", Toast.LENGTH_SHORT).show();
                    return;
                }else if (totalPrice.equals(0.0)) {
                    Toast.makeText(getContext(), "商品数据不能少于1件", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    startFragment(OrderDetailsFragment.newInstance(isSelectlist, ""));
                }

                break;
        }
    }

    private void deletegoods() {
        for (int i = goodsBean.size()-1; i>=0; i--) {
            GoodsBean goodsItem = goodsBean.get(i);
            if (goodsItem.isSelect) {
                goodsBean.remove(i);
            }
        }
        CmConstants.setGoodsDatas(goodsBean);
        //计算商品价格
        setTotalPrice(goodsBean);
        //购物车是否为空
        initData();
    }

    private void selectall() {
        for (int i = 0; i < goodsBean.size(); i++) {
            GoodsBean goodsItem = goodsBean.get(i);
            if (!goodsItem.isSelect) {
                goodsItem.isSelect = true;//选中
            }
        }
        //计算商品价格
        setTotalPrice(goodsBean);
    }

    private Double setTotalPrice(List<GoodsBean> datas) {
        Double price2 = 0.00;
        int goodsnum = 0;
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isSelect == true) {
                GoodsBean goodsItem = datas.get(i);
                int pnum = Integer.valueOf(goodsItem.goodsNum);
                price = Double.valueOf(goodsItem.marketprice);
                goodsnum += pnum;
                price2 += pnum * price;
            }

        }
        mAmountMoney.setText(price2 + "元");
        mAdapter.notifyDataSetChanged();
        return price2;
    }

    @Override
    public void onClick(int type, int position, GoodsBean data) {
        switch (type){
            case 0:
                int num = Integer.valueOf(data.goodsNum);
                if (num<=1) {
                    num = 1;
                }
                num--;
                data.goodsNum = num+"";
                mAdapter.notifyDataSetChanged();
                initData();
                break;
            case 1:
                int num1 = Integer.valueOf(data.goodsNum);
                num1++;
                //ToltalPrice += Double.parseDouble(list.get(position).productprice);
                //判断货存数量
                int total =Integer.valueOf(data.total);
                if (num1>=total) {
                    num1 = total;
                }
                data.goodsNum = num1+"";
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}

