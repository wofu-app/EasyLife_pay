package com.landicorp.android.wofupay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.adapter.PassengerAdapter;
import com.landicorp.android.wofupay.bean.LastPassenger;
import com.landicorp.android.wofupay.bean.Passenger;
import com.landicorp.android.wofupay.bean.TrainMeaasgeBean;
import com.landicorp.android.wofupay.bean.TrainPayBean;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.JLog;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.yanzhenjie.fragment.NoFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;

import static android.R.attr.duration;
import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.landicorp.android.wofupay.R.id.gaojiruanwo;
import static com.landicorp.android.wofupay.R.id.ll_erdengzuo;
import static com.landicorp.android.wofupay.R.id.ll_gaojiruanwo;
import static com.landicorp.android.wofupay.R.id.ll_ruanwo;
import static com.landicorp.android.wofupay.R.id.ll_ruanzuo;
import static com.landicorp.android.wofupay.R.id.ll_shangwuzuo;
import static com.landicorp.android.wofupay.R.id.ll_tedengzuo;
import static com.landicorp.android.wofupay.R.id.ll_wuzuo;
import static com.landicorp.android.wofupay.R.id.ll_yidengzuo;
import static com.landicorp.android.wofupay.R.id.ll_yingwo;
import static com.landicorp.android.wofupay.R.id.ll_yingzuo;
import static com.landicorp.android.wofupay.R.id.tv_arriveStation;
import static com.landicorp.android.wofupay.R.id.tv_arriveTime;
import static com.landicorp.android.wofupay.R.id.tv_erdengzuoNum;
import static com.landicorp.android.wofupay.R.id.tv_erdengzuoPrice;
import static com.landicorp.android.wofupay.R.id.tv_gaojiruanwoNum;
import static com.landicorp.android.wofupay.R.id.tv_gaojiruanwoPrice;
import static com.landicorp.android.wofupay.R.id.tv_ruanwoNum;
import static com.landicorp.android.wofupay.R.id.tv_ruanwoPrice;
import static com.landicorp.android.wofupay.R.id.tv_ruanzuoNum;
import static com.landicorp.android.wofupay.R.id.tv_ruanzuoPrice;
import static com.landicorp.android.wofupay.R.id.tv_shangwuzuoNum;
import static com.landicorp.android.wofupay.R.id.tv_startStation;
import static com.landicorp.android.wofupay.R.id.tv_startdata;
import static com.landicorp.android.wofupay.R.id.tv_tedengzuoNum;
import static com.landicorp.android.wofupay.R.id.tv_tedengzuoPrice;
import static com.landicorp.android.wofupay.R.id.tv_trainTime;
import static com.landicorp.android.wofupay.R.id.tv_wuzuoNum;
import static com.landicorp.android.wofupay.R.id.tv_wuzuoPrice;
import static com.landicorp.android.wofupay.R.id.tv_yidengzuoNum;
import static com.landicorp.android.wofupay.R.id.tv_yidengzuoPrice;
import static com.landicorp.android.wofupay.R.id.tv_yingwoNum;
import static com.landicorp.android.wofupay.R.id.tv_yingwoPrice;
import static com.landicorp.android.wofupay.R.id.tv_yingzuoNum;
import static com.landicorp.android.wofupay.R.id.tv_yingzuoPrice;
import static com.landicorp.android.wofupay.R.id.wuzuo;


/**
 * Created by Administrator on 2017/3/24.
 */

public class TrainTwoFragment extends NoFragment implements View.OnClickListener {
    private int num;
    private View mInflate;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TrainMeaasgeBean mBean;
    private String mS;
    private Button mImageButton;
    private TextView mTv_startStation;
    private TextView mTv_startTime;
    private TextView mTv_trainNo;
    private TextView mTv_trainTime;
    private TextView mTv_arriveStation;
    private TextView mTv_arriveTime;
    private LinearLayout mLl_shangwuzuo;
    private TextView mTv_shangwuzuoNum;
    private TextView mTv_shangwuzuoPrice;
    private LinearLayout mLl_tedengzuo;
    private TextView mTv_tedengzuoNum;
    private TextView mTv_tedengzuoPrice;
    private LinearLayout mLl_yidengzuo;
    private TextView mTv_yidengzuoNum;
    private TextView mTv_yidengzuoPrice;
    private LinearLayout mLl_erdengzuo;
    private TextView mTv_erdengzuoNum;
    private TextView mTv_erdengzuoPrice;
    private LinearLayout mLl_gaojiruanwo;
    private TextView mTv_gaojiruanwoNum;
    private TextView mTv_gaojiruanwoPrice;
    private LinearLayout mLl_ruanwo;
    private TextView mTv_ruanwoNum;
    private TextView mTv_ruanwoPrice;
    private LinearLayout mLl_yingwo;
    private TextView mTv_yingwoNum;
    private TextView mTv_yingwoPrice;
    private LinearLayout mLl_ruanzuo;
    private TextView mTv_ruanzuoNum;
    private TextView mTv_ruanzuoPrice;
    private LinearLayout mLl_yingzuo;
    private TextView mTv_yingzuoNum;
    private TextView mTv_yingzuoPrice;
    private LinearLayout mLl_wuzuo;
    private TextView mTv_wuzuoNum;
    private TextView mTv_wuzuoPrice;
    private TextView mTv_startdata;
    private ImageView mIv_add;
    private Button mBtn_ok;
    private ListView mPassenger_listView;
    private double ticketPrice;
    private String zuoweixibie = "";
    private boolean hasVeri;
    private ArrayList<Passenger> list = new ArrayList<Passenger>();
    private String mPidname;
    private String mPhoneNum;
    private PassengerAdapter mAdapter;
    private LastPassenger lastPassenger;
    private ArrayList<LastPassenger> lastPassengers;
    private String stringToUnicode;
    private String substring;
    public TrainTwoFragment() {
    }

    public static TrainTwoFragment newInstance(TrainMeaasgeBean param1, String param2) {
        TrainTwoFragment fragment = new TrainTwoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBean = (TrainMeaasgeBean) getArguments().getSerializable(ARG_PARAM1);
            mS = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_train_two, null);
        JLog.d("--------------","传递过来的bean"+mBean.toString());
        initView();
        initData();
        initListener();
        initPassengerListViewData();
        return mInflate;
    }

    private void initPassengerListViewData() {
        mAdapter = new PassengerAdapter(this.getContext(), list, zuoweixibie);
        mPassenger_listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        mLl_shangwuzuo.setOnClickListener(this);
        mIv_add.setOnClickListener(this);
        mLl_tedengzuo.setOnClickListener(this);
        mLl_yidengzuo.setOnClickListener(this);
        mLl_erdengzuo.setOnClickListener(this);
        mLl_gaojiruanwo.setOnClickListener(this);
        mLl_ruanwo.setOnClickListener(this);
        mLl_yingwo.setOnClickListener(this);
        mLl_ruanzuo.setOnClickListener(this);
        mLl_yingzuo.setOnClickListener(this);
        mLl_wuzuo.setOnClickListener(this);
        mBtn_ok.setOnClickListener(this);
        mImageButton.setOnClickListener(this);
    }

    // 把分钟转换成小时
    private String changtohour(String duration) {
        Integer s = Integer.valueOf(duration);
        Integer h = s / 60;
        Integer m = s % 60;
        return h + "时" + m + "分";

    }
    private void initData() {
        mTv_startStation.setText(mBean.startStation);
        // ToastUtil.showToast(Train_Two_Activity.this, station1);
        mTv_startTime.setText(mBean.departTime + "出发");
        mTv_trainNo.setText(mBean.trainNo);
        String time = changtohour(mBean.duration);
        mTv_trainTime.setText(time);
        mTv_arriveStation.setText(mBean.arriveStation);
        mTv_arriveTime.setText(mBean.arriveTime + "到达");
        // 商务座
        if (mBean.shangwuzuo.equals("") || mBean.shangwuzuo.equals("0")) {
            mLl_shangwuzuo.setVisibility(View.GONE);

        } else {
            mTv_shangwuzuoNum.setText(Integer.valueOf(mBean.shangwuzuo) + "张");
            mTv_shangwuzuoPrice.setText("¥" + mBean.shangwuzuoPrice + "元");
        }
        // 特等座
        if (mBean.tedengzuo.equals("") || mBean.tedengzuo.equals("0")) {
            mLl_tedengzuo.setVisibility(View.GONE);
        } else {
            mTv_tedengzuoNum.setText(Integer.valueOf(mBean.tedengzuo) + "张");
            mTv_tedengzuoPrice.setText("¥" + mBean.tedengzuoPrice + "元");
        }
        // 一等座
        if (mBean.yidengzuo.equals("") || mBean.yidengzuo.equals("0")) {
            mLl_yidengzuo.setVisibility(View.GONE);
        } else {
            mTv_yidengzuoNum.setText(Integer.valueOf(mBean.yidengzuo) + "张");
            mTv_yidengzuoPrice.setText("¥" + mBean.yidengzuoPrice + "元");
        }
        // 二等座
        if (mBean.erdengzuo.equals("") ||mBean.erdengzuo.equals("0")) {
            mLl_erdengzuo.setVisibility(View.GONE);
        } else {
            mTv_erdengzuoNum.setText(Integer.valueOf(mBean.erdengzuo) + "张");
            mTv_erdengzuoPrice.setText("¥" + mBean.erdengzuoPrice + "元");
        }
        // 高级软卧
        if (mBean.gaojiruanwo.equals("") || mBean.gaojiruanwo.equals("0")) {
            mLl_gaojiruanwo.setVisibility(View.GONE);
        } else {
            mTv_gaojiruanwoNum.setText(Integer.valueOf(mBean.gaojiruanwo) + "张");
            mTv_gaojiruanwoPrice.setText("¥" + mBean.gaojiruanwoPrice + "元");
        }
        // 软卧
        if (mBean.ruanwoshang.equals("") ||mBean.ruanwoshang.equals("0")) {
            mLl_ruanwo.setVisibility(View.GONE);
        } else {
            mTv_ruanwoNum.setText(Integer.valueOf(mBean.ruanwoshang) + "张");
            mTv_ruanwoPrice.setText("¥" + mBean.ruanwoshangPrice + "元");
        }
        // 硬卧
        if (mBean.yingwozhong.equals("") ||mBean. yingwozhong.equals("0")) {
            mLl_yingwo.setVisibility(View.GONE);
        } else {
            mTv_yingwoNum.setText(Integer.valueOf(mBean.yingwozhong) + "张");
            mTv_yingwoPrice.setText("¥" + mBean.yingwozhongPrice + "元");
        }
        // 软座
        if (mBean.ruanzuo.equals("") || mBean.ruanzuo.equals("0")) {
            mLl_ruanzuo.setVisibility(View.GONE);
        } else {
            mTv_ruanzuoNum.setText(Integer.valueOf(mBean.ruanzuo) + "张");
            mTv_ruanzuoPrice.setText("¥" + mBean.ruanzuoPrice + "元");
        }
        // 硬座
        if (mBean.yingzuo.equals("") || mBean.yingzuo.equals("0")) {
            mLl_yingzuo.setVisibility(View.GONE);
        } else {
            mTv_yingzuoNum.setText(Integer.valueOf(mBean.yingzuo) + "张");
            mTv_yingzuoPrice.setText("¥" + mBean.yingzuoPrice + "元");
        }
        // 无座
        if (mBean.wuzuo.equals("0") || mBean.wuzuo.equals("")) {
            mLl_wuzuo.setVisibility(View.GONE);
        } else {
            mTv_wuzuoNum.setText(Integer.valueOf(mBean.wuzuo) + "张");
            mTv_wuzuoPrice.setText("¥" + mBean.wuzuoPrice + "元");
        }
        // 出发时间
        mTv_startdata.setText("出发日期: " + mBean.godata);
    }

    private void initView() {
        mImageButton = (Button) mInflate.findViewById(R.id.btn_message);
        mTv_startStation = (TextView) mInflate.findViewById(tv_startStation);
        mTv_startTime = (TextView) mInflate.findViewById(R.id.tv_start_Time);
        mTv_trainNo = (TextView) mInflate.findViewById(R.id.tv_TrainNo);
        mTv_trainTime = (TextView) mInflate.findViewById(tv_trainTime);
        mTv_arriveStation = (TextView) mInflate.findViewById(tv_arriveStation);
        mTv_arriveTime = (TextView) mInflate.findViewById(tv_arriveTime);
        //商务座信息
        mLl_shangwuzuo = (LinearLayout) mInflate.findViewById(ll_shangwuzuo);
        mTv_shangwuzuoNum = (TextView) mInflate.findViewById(tv_shangwuzuoNum);
        mTv_shangwuzuoPrice = (TextView) mInflate.findViewById(R.id.tv_shangwuPrice);
        //特等座信息
        mLl_tedengzuo = (LinearLayout) mInflate.findViewById(ll_tedengzuo);
        mTv_tedengzuoNum = (TextView) mInflate.findViewById(tv_tedengzuoNum);
        mTv_tedengzuoPrice = (TextView) mInflate.findViewById(tv_tedengzuoPrice);
        //一等座信息
        mLl_yidengzuo = (LinearLayout) mInflate.findViewById(ll_yidengzuo);
        mTv_yidengzuoNum = (TextView) mInflate.findViewById(tv_yidengzuoNum);
        mTv_yidengzuoPrice = (TextView) mInflate.findViewById(tv_yidengzuoPrice);
        //二等座
        mLl_erdengzuo = (LinearLayout) mInflate.findViewById(ll_erdengzuo);
        mTv_erdengzuoNum = (TextView) mInflate.findViewById(tv_erdengzuoNum);
        mTv_erdengzuoPrice = (TextView) mInflate.findViewById(tv_erdengzuoPrice);
        //高级软卧
        mLl_gaojiruanwo = (LinearLayout) mInflate.findViewById(ll_gaojiruanwo);
        mTv_gaojiruanwoNum = (TextView) mInflate.findViewById(tv_gaojiruanwoNum);
        mTv_gaojiruanwoPrice = (TextView) mInflate.findViewById(tv_gaojiruanwoPrice);
        //软卧
        mLl_ruanwo = (LinearLayout) mInflate.findViewById(ll_ruanwo);
        mTv_ruanwoNum = (TextView) mInflate.findViewById(tv_ruanwoNum);
        mTv_ruanwoPrice = (TextView) mInflate.findViewById(tv_ruanwoPrice);
        //硬卧
        mLl_yingwo = (LinearLayout) mInflate.findViewById(ll_yingwo);
        mTv_yingwoNum = (TextView) mInflate.findViewById(tv_yingwoNum);
        mTv_yingwoPrice = (TextView) mInflate.findViewById(tv_yingwoPrice);
        //软座
        mLl_ruanzuo = (LinearLayout)mInflate. findViewById(ll_ruanzuo);
        mTv_ruanzuoNum = (TextView) mInflate.findViewById(tv_ruanzuoNum);
        mTv_ruanzuoPrice = (TextView) mInflate.findViewById(tv_ruanzuoPrice);
        //硬座
        mLl_yingzuo = (LinearLayout) mInflate.findViewById(ll_yingzuo);
        mTv_yingzuoNum = (TextView)mInflate. findViewById(tv_yingzuoNum);
        mTv_yingzuoPrice = (TextView)mInflate. findViewById(tv_yingzuoPrice);
        //无座
        mLl_wuzuo = (LinearLayout)mInflate. findViewById(ll_wuzuo);
        mTv_wuzuoNum = (TextView) mInflate.findViewById(tv_wuzuoNum);
        mTv_wuzuoPrice = (TextView)mInflate. findViewById(tv_wuzuoPrice);
        //出发时间
        mTv_startdata = (TextView)mInflate. findViewById(tv_startdata);
        mIv_add = (ImageView)mInflate. findViewById(R.id.iv_addpassenger);
        mBtn_ok = (Button) mInflate.findViewById(R.id.select_btn);
        mPassenger_listView = (ListView) mInflate.findViewById(R.id.passenger_listview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_shangwuzuo:// 商务座
                num = Integer.valueOf(mBean.shangwuzuo);
                mLl_shangwuzuo.setBackgroundResource(R.mipmap.train_lan);
                // 修改背景色
                mLl_tedengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yidengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_erdengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_gaojiruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_wuzuo.setBackgroundResource(R.mipmap.train_bai);
                ticketPrice = mBean.shangwuzuoPrice;
                zuoweixibie = "商务座";
                if (mAdapter != null) {
                    mAdapter.setZuoWei(zuoweixibie);
                }

                break;
            case R.id.ll_tedengzuo:// 特等座
                num = Integer.valueOf(mBean.tedengzuo);
                mLl_tedengzuo.setBackgroundResource(R.mipmap.train_lan);
                mLl_shangwuzuo.setBackgroundResource(R.mipmap.train_bai);// 修改背景色

                mLl_yidengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_erdengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_gaojiruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_wuzuo.setBackgroundResource(R.mipmap.train_bai);
                ticketPrice = mBean.tedengzuoPrice;
                zuoweixibie = "特等座";
                if (mAdapter != null) {
                    mAdapter.setZuoWei(zuoweixibie);
                }
                break;
            case R.id.ll_yidengzuo:// 一等座
                num = Integer.valueOf(mBean.yidengzuo);
                mLl_tedengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_shangwuzuo.setBackgroundResource(R.mipmap.train_bai);// 修改背景色
                mLl_yidengzuo.setBackgroundResource(R.mipmap.train_lan);
                mLl_erdengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_gaojiruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_wuzuo.setBackgroundResource(R.mipmap.train_bai);
                ticketPrice = mBean.yidengzuoPrice;
                zuoweixibie = "一等座";
                if (mAdapter != null) {
                    mAdapter.setZuoWei(zuoweixibie);
                }
                break;
            case R.id.ll_erdengzuo:// 二等座
                num = Integer.valueOf(mBean.erdengzuo);
                mLl_tedengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_shangwuzuo.setBackgroundResource(R.mipmap.train_bai);// 修改背景色
                mLl_yidengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_erdengzuo.setBackgroundResource(R.mipmap.train_lan);
                mLl_gaojiruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_wuzuo.setBackgroundResource(R.mipmap.train_bai);
                ticketPrice = mBean.erdengzuoPrice;
                zuoweixibie = "二等座";
                if (mAdapter != null) {
                    mAdapter.setZuoWei(zuoweixibie);
                }
                break;
            case R.id.ll_gaojiruanwo:// 高级软卧
                num = Integer.valueOf(mBean.gaojiruanwo);
                mLl_tedengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_shangwuzuo.setBackgroundResource(R.mipmap.train_bai);// 修改背景色
                mLl_yidengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_erdengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_gaojiruanwo.setBackgroundResource(R.mipmap.train_lan);
                mLl_ruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_wuzuo.setBackgroundResource(R.mipmap.train_bai);
                ticketPrice = mBean.gaojiruanwoPrice;
                zuoweixibie = "高级软卧";
                if (mAdapter != null) {
                    mAdapter.setZuoWei(zuoweixibie);
                }
                break;
            case R.id.ll_ruanwo:// 软卧
                num = Integer.valueOf(mBean.ruanwoshang);
                mLl_tedengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_shangwuzuo.setBackgroundResource(R.mipmap.train_bai);// 修改背景色
                mLl_yidengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_erdengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_gaojiruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanwo.setBackgroundResource(R.mipmap.train_lan);
                mLl_yingwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_wuzuo.setBackgroundResource(R.mipmap.train_bai);
                ticketPrice = mBean.ruanwoshangPrice;
                zuoweixibie = "软卧";
                if (mAdapter != null) {
                    mAdapter.setZuoWei(zuoweixibie);
                }
                break;
            case R.id.ll_yingwo:// 硬卧
                num = Integer.valueOf(mBean.yingwozhong);
                mLl_tedengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_shangwuzuo.setBackgroundResource(R.mipmap.train_bai);// 修改背景色
                mLl_yidengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_erdengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_gaojiruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingwo.setBackgroundResource(R.mipmap.train_lan);
                mLl_ruanzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_wuzuo.setBackgroundResource(R.mipmap.train_bai);
                ticketPrice = mBean.yingwozhongPrice;
                zuoweixibie = "硬卧";
                if (mAdapter != null) {
                    mAdapter.setZuoWei(zuoweixibie);
                }
                break;
            case R.id.ll_ruanzuo:// 软座
                num = Integer.valueOf(mBean.ruanzuo);
                mLl_tedengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_shangwuzuo.setBackgroundResource(R.mipmap.train_bai);// 修改背景色

                mLl_yidengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_erdengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_gaojiruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanzuo.setBackgroundResource(R.mipmap.train_lan);
                mLl_yingzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_wuzuo.setBackgroundResource(R.mipmap.train_bai);
                ticketPrice = mBean.ruanzuoPrice;
                zuoweixibie = "软座";
                if (mAdapter != null) {
                    mAdapter.setZuoWei(zuoweixibie);
                }
                break;
            case R.id.ll_yingzuo:// 硬座
                num = Integer.valueOf(mBean.yingzuo);
                mLl_tedengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_shangwuzuo.setBackgroundResource(R.mipmap.train_bai);// 修改背景色

                mLl_yidengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_erdengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_gaojiruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingzuo.setBackgroundResource(R.mipmap.train_lan);
                mLl_wuzuo.setBackgroundResource(R.mipmap.train_bai);
                ticketPrice = mBean.yingzuoPrice;
                zuoweixibie = "硬座";
                if (mAdapter != null) {
                    mAdapter.setZuoWei(zuoweixibie);
                }
                break;
            case R.id.ll_wuzuo:// 无座
                num = Integer.valueOf(mBean.wuzuo);
                mLl_tedengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_shangwuzuo.setBackgroundResource(R.mipmap.train_bai);// 修改背景色
                mLl_yidengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_erdengzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_gaojiruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingwo.setBackgroundResource(R.mipmap.train_bai);
                mLl_ruanzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_yingzuo.setBackgroundResource(R.mipmap.train_bai);
                mLl_wuzuo.setBackgroundResource(R.mipmap.train_lan);
                ticketPrice = mBean.wuzuoPrice;
                zuoweixibie = "无座";
                if (mAdapter != null) {
                    mAdapter.setZuoWei(zuoweixibie);
                }
                break;
            case R.id.iv_addpassenger://点击进入添加联系人界面
                //
                if (hasVeri){
                    //进入选择联系人界面
                    startFragmentForResquest(SelectPassengerFragment.newInstance(mPhoneNum,""),1);
                }else {
                    startFragmentForResquest(TrainSmsFragment.newInstance("",""),1); //短信验证界面
                }
                break;
            case R.id.select_btn: //提交订单
                if (list.size()>0&&zuoweixibie!=null){
                    initMsg(); //提交订单的信息

                if (lastPassengers.size() > 0
                        && num >= lastPassengers.size()) {
                    getBillNo();

                } else if (num >= lastPassengers.size()) {
                    Toast.makeText(this.getContext(),"余票少于乘客人次,请选择其他座位席别",Toast.LENGTH_SHORT).show();

                } else if (lastPassengers.size() > 5) {
                    Toast.makeText(this.getContext(),"最多只能选择5位乘客,请修改乘客数量",Toast.LENGTH_SHORT).show();

                } else if (num==0) {
                    Toast.makeText(this.getContext(),"请选择席别",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this.getContext(),"请添加乘客",Toast.LENGTH_SHORT).show();

                }

        } else if (list.size() > 0 && zuoweixibie == null) {
                    Toast.makeText(this.getContext(),"请选择座位席别",Toast.LENGTH_SHORT).show();

        } else if (list.size() == 0 && zuoweixibie != null) {
                    Toast.makeText(this.getContext(),"请添加联系人",Toast.LENGTH_SHORT).show();

        } else {
                    Toast.makeText(this.getContext(),"请添加联系人并且选择座位席别",Toast.LENGTH_SHORT).show();
        }

                break;
            default:
                break;
        }
    }

    private void getBillNo() {
        //TODO 提示框
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.URL);
        HashMap<String, String> params = new HashMap<>();
        Double amount = (lastPassenger.ticketPrice+10)
                * lastPassengers.size();
        params.put("action", "GetHPBillNo");
        params.put("rechargeAccount", "");
        //TODO 终端号
       // params.put("terminalcode", DeviceUtils.getDevicePort());
        params.put("mobile", mPhoneNum);
        params.put("amount", amount + "");
        params.put("BN_type", "train");
        params.put("BN_memo", "火车票");
        helper.postParams(params).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(getContext(),"订单生成失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String s) {
                if (s!=null){
                    //进入账单信息界面
                    TrainPayBean bean = new TrainPayBean();
                    bean.reqDateTime = AppUtils
                            .getStringDate("yyyyMMddHHmmss");
                    bean.queryKey = mBean.queryKey;
                    bean.trainNo = mBean.trainNo;
                    bean.departStationCode = mBean.departStationCode;
                    bean.arriveStationCode = mBean.arriveStationCode;
                    bean.departDate = mBean.godata;
                    bean.departTime = mBean.departTime;
                    bean.contactName = stringToUnicode;
                    bean.contactMobile =mPhoneNum;
                    bean.passengers = substring;
                    bean.function = 7;
                    bean.BillNo = s;
                    bean.phoneNumber = mPhoneNum;
                    startFragment(TrainBillMsgFragment.newInstance(mBean,zuoweixibie,lastPassengers,bean));
                }else {
                    Toast.makeText(getContext(),"订单生成失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initMsg() {

        lastPassengers = new ArrayList<LastPassenger>();
        if (list != null && list.size() > 0) {

            for (int i = 0; i < list.size(); i++) {
                lastPassenger = new LastPassenger();
                stringToUnicode = AppUtils.stringToUnicode(mPidname);
                lastPassenger.name = list.get(i).name;
                lastPassenger.cardNo = list.get(i).idcard;
                lastPassenger.phonenum=list.get(i).phoneNum;
                if (zuoweixibie.equals("商务座")) {
                    lastPassenger.seatType = 9;
                } else if (zuoweixibie.equals("一等座")) {
                    lastPassenger.seatType = 5;
                } else if (zuoweixibie.equals("特等座")) {
                    lastPassenger.seatType = 8;
                } else if (zuoweixibie.equals("二等座")) {
                    lastPassenger.seatType = 4;
                } else if (zuoweixibie.equals("高级软座")) {
                    lastPassenger.seatType = 15;
                } else if (zuoweixibie.equals("软卧")) {
                    lastPassenger.seatType = 14;
                } else if (zuoweixibie.equals("硬卧")) {
                    lastPassenger.seatType = 11;
                } else if (zuoweixibie.equals("软座")) {
                    lastPassenger.seatType = 3;
                } else if (zuoweixibie.equals("硬座")) {
                    lastPassenger.seatType = 2;
                } else if (zuoweixibie.equals("无座")) {
                    lastPassenger.seatType = 1;
                }
                lastPassenger.ticketPrice = ticketPrice;
                lastPassenger.type = 1;// 固定为成人
                lastPassenger.cardType = 1;// 固定为身份证
                String birthString = getbirth(lastPassenger.cardNo);
                lastPassenger.birth = birthString;

                lastPassengers.add(lastPassenger);
            }



            if (lastPassengers != null) {
                String string = "[";
                for (LastPassenger list : lastPassengers) {
                    string += "{\"type\":1,\"name\":\"" + list.name
                            + "\",\"cardType\":1,\"cardNo\":\"" + list.cardNo
                            + "\",\"birth\":\"" + list.birth
                            + "\",\"seatType\":" + list.seatType
                            + ",\"ticketPrice\":" + list.ticketPrice + "},";
                }
                int lastIndexOf = string.lastIndexOf(",");
                substring = string.substring(0, lastIndexOf);
                substring += "]";
                Gson gson = new Gson();
                substring= gson.toJson(lastPassengers);
            }

        } else {
            AppUtils.showMessage(this.getContext(),"请添加联系人");
        }

    }

    private String getbirth(String cradNo) {
        String year = cradNo.substring(6, 10);
        String month = cradNo.substring(10, 12);
        String day = cradNo.substring(12, 14);
        return year + "-" + month + "-" + day;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode,  Bundle result) {
        super.onFragmentResult(requestCode, resultCode, result);
        if (result !=null&&resultCode!=RESULT_CANCELED){
            mPidname = result.getString("pidname");
            mPhoneNum = result.getString("mPhoneNum");
        }
        if (resultCode==RESULT_OK||resultCode==RESULT_CANCELED){
            list.clear();
            List<Passenger> result1 = (List<Passenger>) result.getSerializable("passenger");
            JLog.d("--------",result1.toString());
            hasVeri = result.getBoolean("hasVeri");
            if (result1!=null){
                list.addAll(result1);
            }
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
