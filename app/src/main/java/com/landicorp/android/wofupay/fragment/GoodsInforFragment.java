package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.GoodsBean;
import com.landicorp.android.wofupay.loader.GlideImageLoader;
import com.landicorp.android.wofupay.utils.CmConstants;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.yanzhenjie.fragment.NoFragment;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * 商品信息Fragment
 * Created by Administrator on 2017/3/28 0028.
 */
public class GoodsInforFragment extends NoFragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String goodsId;
    private String mParam2;

    private Banner mBanner;

    private TextView mGoodsName;
    private TextView mProName;
    private TextView mGoodsPrice;
    private TextView mSalePrice;
    private TextView mShopAct;

    private TextView mExpress;
    private TextView mStock;
    private TextView mSalesNum;

    private TextView mDelBtn;
    private EditText mEditCount;
    private TextView mAddBtn;

    private Button mBuyBtn;
    private Button mAddCarBtn;
    private WebView mWebView;

    private List<String> imageUrls;

    private int totalNum;

    private GoodsBean goodsItem = new GoodsBean();
    private List<GoodsBean> goodsDatas;
    private List<String> goodsIds;

    public GoodsInforFragment() {
    }

    public static GoodsInforFragment newInstance(String param1, String param2) {
        GoodsInforFragment fragment = new GoodsInforFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        goodsDatas = CmConstants.getGoodsDatas();
        goodsIds = new ArrayList<String>();

        if (getArguments() != null) {
            goodsId = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goods_infor, null);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        httpData();
    }

    private void httpData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "Goodsrow");
        params.put("id", goodsId);
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.Market_URL);
        Observable<String> observable = helper.postParams(params);
        //此处要添加提示信息
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onNext(String s) {
                Log.e("MyTest","======s====="+s);
                Gson gson = new Gson();
                try {
                    goodsItem = gson.fromJson(s, GoodsBean.class);
                    initData(goodsItem);
                } catch (Exception e) {

                }
            }
        });

    }

    private void initData(GoodsBean datas) {
        imageUrls = new ArrayList<String>();
        totalNum = Integer.parseInt(datas.total);
        if(totalNum == 0){
            mEditCount.setText(datas.total);
        }
        mGoodsName.setText(datas.keywords);
        mProName.setText(datas.title);
        mGoodsPrice.setText(datas.productprice + "元");
        mSalePrice.setText(datas.marketprice + "元");
        mShopAct.setText(datas.title);
        if (datas.issendfree == 1) {
            mExpress.setText("快递：包邮");
        }else {
            mExpress.setText("快递:"+datas.dispatchprice+"元");
        }

        mStock.setText("库存：" + datas.total);
        mSalesNum.setText("销量：" + datas.sales + "件");

        WebSettings setting = mWebView.getSettings();
        setting.setUseWideViewPort(true);//关键点
        setting.setJavaScriptEnabled(true);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setLoadWithOverviewMode(true);
        mWebView.loadDataWithBaseURL(null, datas.content, "text/html", null, null);

        if(!"".equals(datas.thumb_url.url_1) && datas.thumb_url.url_1 != null ){
            imageUrls.add(datas.thumb_url.url_1);
        }

        if(!"".equals(datas.thumb_url.url_2) && datas.thumb_url.url_2 != null ){
            imageUrls.add(datas.thumb_url.url_2);
        }

        if(!"".equals(datas.thumb_url.url_3) && datas.thumb_url.url_3 != null ){
            imageUrls.add(datas.thumb_url.url_3);
        }

        if(!"".equals(datas.thumb_url.url_4) && datas.thumb_url.url_4 != null ){
            imageUrls.add(datas.thumb_url.url_4);
        }

        mBanner.setImages(imageUrls)
                .setImageLoader(new GlideImageLoader())
                .start();

    }

    private void initView(View view) {
        mBanner = (Banner) view.findViewById(R.id.goodsInfoUI_banner);

        mGoodsName = (TextView) view.findViewById(R.id.goodsInfoUI_goodsName);
        mProName = (TextView) view.findViewById(R.id.goodsInfoUI_proName);
        mGoodsPrice = (TextView) view.findViewById(R.id.goodsInfoUI_goodsPrice);
        mSalePrice = (TextView) view.findViewById(R.id.goodsInfoUI_salePrice);
        mShopAct = (TextView) view.findViewById(R.id.goodsInfoUI_shopAct);

        mExpress = (TextView) view.findViewById(R.id.goodsInfoUI_express);
        mStock = (TextView) view.findViewById(R.id.goodsInfoUI_stock);
        mSalesNum = (TextView) view.findViewById(R.id.goodsInfoUI_salesNum);

        mDelBtn = (TextView) view.findViewById(R.id.goodsInfoUI_delBtn);
        mEditCount = (EditText) view.findViewById(R.id.goodsInfoUI_editCount);
        mAddBtn = (TextView) view.findViewById(R.id.goodsInfoUI_addBtn);

        mDelBtn.setOnClickListener(new CustomListener(1, mEditCount));
        mAddBtn.setOnClickListener(new CustomListener(2, mEditCount));
        mEditCount.addTextChangedListener(watcher);

        mBuyBtn = (Button) view.findViewById(R.id.goodsInfoUI_buyBtn);
        mBuyBtn.setOnClickListener(this);
        mAddCarBtn = (Button) view.findViewById(R.id.goodsInfoUI_addCarBtn);
        mAddCarBtn.setOnClickListener(this);
        mWebView = (WebView) view.findViewById(R.id.goodsInfoUI_webView);
    }

    TextWatcher watcher = new TextWatcher() {
        private String changeBefor;
        @Override
        public void afterTextChanged(Editable arg0) {
            try{
                if(!changeBefor.equals(mEditCount.getText().toString())) {
                    int num = Integer.parseInt(mEditCount.getText().toString());
                    if (num <= 0) {
                        num = 0;
                    }
                    //判断输入数量是否大于库存
                    if (num > totalNum) {
                        num = totalNum;//库存
                    }
                    mEditCount.setText(String.valueOf(num));
                }
            }catch (Exception e){
                mEditCount.setText("0");
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
            changeBefor = mEditCount.getText().toString();
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

    };

    class CustomListener implements View.OnClickListener {

        private int type;
        private EditText editText;
        public  CustomListener(int type, EditText editText){
            this.type = type;
            this.editText = editText;
        }

        @Override
        public void onClick(View v) {
            if(type == 1){
                int num = Integer.parseInt(editText.getText().toString());
                if (num <= 0)
                    return;
                num = num - 1;
                editText.setText((num < 0 ? 0 : num) + "");
            }else if(type == 2){
                int num = Integer.parseInt(editText.getText().toString());
                //判断是否大于库存
                if (num >= totalNum)
                    return;
                num = num + 1;
                editText.setText((num < 0 ? 0 : num) + "");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goodsInfoUI_buyBtn:
                //去订单详情
                if(Integer.parseInt(mEditCount.getText().toString()) > 0){
                    goodsItem.goodsId = goodsId;
                    goodsItem.goodsNum = mEditCount.getText().toString();

                    List<GoodsBean> goodsBean = new ArrayList<GoodsBean>();
                    goodsBean.add(goodsItem);

                    startFragment(OrderDetailsFragment.newInstance(goodsBean, ""));
                }
                break;
            case R.id.goodsInfoUI_addCarBtn:
                //去购物车
                if(Integer.parseInt(mEditCount.getText().toString()) > 0){
                    goodsItem.goodsId = goodsId;
                    goodsItem.goodsNum = mEditCount.getText().toString();

                    if(goodsDatas.size()>0){
                        for(int i = 0;i < goodsDatas.size();i++){
                            goodsIds.add(goodsDatas.get(i).goodsId);
                        }
                        if(!goodsIds.contains(goodsItem.goodsId)){
                            goodsDatas.add(goodsItem);
                        }else{
                            for(int j = 0;j < goodsDatas.size();j++){
                                if(goodsItem.goodsId.equals(goodsDatas.get(j).goodsId)){
                                    goodsDatas.get(j).goodsNum = String.valueOf(
                                            Integer.parseInt(goodsDatas.get(j).goodsNum)
                                                    + Integer.parseInt(goodsItem.goodsNum));
                                }
                            }
                        }
                    }else{
                        goodsDatas.add(goodsItem);
                    }
                    CmConstants.setGoodsDatas(goodsDatas);
                    startFragment(ShopCarFragment.newInstance("", ""));
                }
                break;
        }

    }
}
