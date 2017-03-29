package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.adapter.GoodsAdapter;
import com.landicorp.android.wofupay.adapter.OnItemClickListener;
import com.landicorp.android.wofupay.bean.ShopBean;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.utils.SpacesItemDecoration;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.yanzhenjie.fragment.NoFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * 商城首页Fragment
 * Created by Administrator on 2017/3/25 0025.
 */
public class MarketFragment extends NoFragment implements View.OnClickListener, OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RadioGroup shopTitleUI_rGroup;
    private RadioButton shopTitleUI_btn1;
    private RadioButton shopTitleUI_btn2;
    private RadioButton shopTitleUI_btn3;
    private RadioButton shopTitleUI_btn4;
    private ImageButton shopUI_cart;

    private GridLayoutManager mLayoutManager;
    private SwipeRefreshLayout mRefresh;
    private RecyclerView mRecyclerView;
    private GoodsAdapter mAdapter;

    private List<ShopBean> mShopModel = new ArrayList<ShopBean>();

    private boolean isRefresh = true;//是否刷新中

    public MarketFragment() {
    }

    public static MarketFragment newInstance(String param1, String param2) {
        MarketFragment fragment = new MarketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_market, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        httpData();
    }

    private void initView(View view) {
        shopTitleUI_rGroup = (RadioGroup) view.findViewById(R.id.shopTitleUI_rGroup);
        shopTitleUI_btn1 = (RadioButton) view.findViewById(R.id.shopTitleUI_btn1);
        shopTitleUI_btn2 = (RadioButton) view.findViewById(R.id.shopTitleUI_btn2);
        shopTitleUI_btn3 = (RadioButton) view.findViewById(R.id.shopTitleUI_btn3);
        shopTitleUI_btn4 = (RadioButton) view.findViewById(R.id.shopTitleUI_btn4);
        shopUI_cart = (ImageButton) view.findViewById(R.id.shopUI_cart);
        shopUI_cart.setOnClickListener(this);

        mAdapter = new GoodsAdapter(getContext());
        mAdapter.setOnItemClickListener(this);

        mLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(18));
        mRecyclerView.setAdapter(mAdapter);

        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRefresh.setOnRefreshListener(this);
    }

    private void httpData() {

        Log.e("MyTest0328","======Enter002=====");

        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "Goodsall");
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.Market_URL);
        Observable<String> observable = helper.postParams(params);
        //此处要添加提示信息
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                isRefresh = false;
                mRefresh.setRefreshing(false);
            }

            @Override
            public void onNext(String s) {

                Log.e("MyTest","======s====="+s);

                isRefresh = false;
                mRefresh.setRefreshing(false);
                Gson gson = new Gson();
                try {
                    mShopModel = gson.fromJson(s, new TypeToken<List<ShopBean>>() {}.getType());
                    if(mShopModel !=null && mShopModel.size() > 0){
                        mAdapter.setHeadData(false);
                        mAdapter.setData(mShopModel);
                    }else{
                        mAdapter.setHeadData(true);
                        mAdapter.getData().clear();
                        mAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {

                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shopUI_cart:
                startFragment(ShopCarFragment.newInstance("",""));
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        startFragment(GoodsInforFragment.newInstance(mShopModel.get(position).id, ""));
    }

    @Override
    public void onRefresh() {
        if(!isRefresh){
            isRefresh = true;
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            httpData();
        }
    }
}
