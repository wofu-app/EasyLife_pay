package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;


import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.LastPassenger;
import com.landicorp.android.wofupay.bean.TrainMeaasgeBean;
import com.landicorp.android.wofupay.bean.TrainPayBean;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.yanzhenjie.fragment.NoFragment;
import java.util.ArrayList;



/**
 * Created by Administrator on 2017/3/28.
 */

public class TrainBillMsgFragment extends NoFragment implements View.OnClickListener {

    private TextView tv_totalPrice;
    private TextView tv_fuwu;
    private TextView tv_amount;
    private View mInflate;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private TrainMeaasgeBean mBean;
    private String mZuoweixibie;
    private TrainPayBean mPaybean;
    private ArrayList<LastPassenger> mList;
    private ListView mListview;
    private TextView mTv_totalPrice;
    private Button mBtn;
    private Button mBtn_cal;

    public TrainBillMsgFragment() {
    }

    public static TrainBillMsgFragment newInstance(TrainMeaasgeBean mbean, String param2, ArrayList<LastPassenger> list, TrainPayBean paybean) {
        TrainBillMsgFragment fragment = new TrainBillMsgFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, mbean);
        args.putString(ARG_PARAM2, param2);
        args.putSerializable(ARG_PARAM3, list);
        args.putParcelable(ARG_PARAM4,paybean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBean = (TrainMeaasgeBean) getArguments().getSerializable(ARG_PARAM1);
            mZuoweixibie = getArguments().getString(ARG_PARAM2);
            mList = (ArrayList<LastPassenger>) getArguments().getSerializable(ARG_PARAM3);
            mPaybean = getArguments().getParcelable(ARG_PARAM4);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_train_msg,null);
        initView();
        initListener();
        mPaybean.seatName = mZuoweixibie;
        mPaybean.arriveStation = mBean.arriveStation;
        mPaybean.departStation = mBean.startStation;
        Myadapter myadapter = new Myadapter();
        mListview.setAdapter(myadapter);
        return mInflate;
    }

    private void initListener() {
        mBtn.setOnClickListener(this);
        mBtn_cal.setOnClickListener(this);
    }

    private void initView() {
        mListview = (ListView) mInflate.findViewById(R.id.billmsg_listview);
        mTv_totalPrice = (TextView) mInflate.findViewById(R.id.bill_totalPrice);
        mBtn = (Button) mInflate.findViewById(R.id.bill_btn);
        mBtn_cal = (Button) mInflate.findViewById(R.id.bill_btn_canel);
        tv_fuwu = (TextView) mInflate.findViewById(R.id.bill_fuwu);
        tv_amount = (TextView) mInflate.findViewById(R.id.bill_totalAmount);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bill_btn:
                //TODO
                //进入付款界面
                Toast.makeText(getContext(),"进入支付界面",Toast.LENGTH_SHORT).show();
                startFragment(PayTypeFragment.newInstance(mPaybean,null,mPaybean.payAmount));
                break;
            case R.id.bill_btn_canel:
                //取消
                finish();
                break;
            default:
                break;
        }
    }
    class Myadapter extends BaseAdapter {
        @Override
        public int getCount() {

            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup perent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_trainbillmsg, null);
                holder.tv_name = (TextView) convertView
                        .findViewById(R.id.bill_name);
                holder.tv_IDcard = (TextView) convertView
                        .findViewById(R.id.bill_Idcrad);
                holder.tv_startTime = (TextView) convertView
                        .findViewById(R.id.bill_startTime);
                holder.tv_start = (TextView) convertView
                        .findViewById(R.id.bill_start);
                holder.tv_end = (TextView) convertView
                        .findViewById(R.id.bill_end);
                holder.tv_trainNo = (TextView) convertView
                        .findViewById(R.id.bill_trainNo);
                holder.tv_zuoweixibie = (TextView) convertView
                        .findViewById(R.id.bill_zuoweixibie);
                holder.tv_price = (TextView) convertView
                        .findViewById(R.id.bill_price);
                holder.tv_fuwu = (TextView) convertView
                        .findViewById(R.id.bill_fuwu);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String name = mList.get(position).name;
            String decodeUnicode = AppUtils.decodeUnicode(name);
            holder.tv_name.setText(decodeUnicode);
            holder.tv_IDcard.setText(mList.get(position).cardNo);
            holder.tv_startTime.setText(mBean.godata);
            holder.tv_start.setText(mBean.startStation + "\r\n" + mBean.departTime);
            holder.tv_end.setText(mBean.arriveStation + "\r\n" + mBean.arriveTime);
            holder.tv_trainNo.setText(mBean.trainNo);
            holder.tv_zuoweixibie.setText(mZuoweixibie);
            holder.tv_price.setText(mList.get(position).ticketPrice + "元");
            double price = mList.get(position).ticketPrice;
            holder.tv_fuwu.setText("10元");
            Double totalPrice = mList.size() * price;
            Double totalFuwu = (double) (10 * mList.size());
            // ToastUtil.showTextToast(TrainBillMsgActivity.this,
            // list.size()+"---------"+price);
            mTv_totalPrice.setText("总票价:" + totalPrice + "元");
            tv_fuwu.setText("服务费:" + totalFuwu + "元");
            tv_amount.setText("总金额:" + (totalFuwu + totalPrice) + "元");
            mPaybean.payAmount = (int) ((totalFuwu + totalPrice) * 100) + "";
            return convertView;
        }

    }

    private class ViewHolder {
        public TextView tv_fuwu;
        public TextView tv_name;
        public TextView tv_IDcard;
        public TextView tv_startTime;
        public TextView tv_start;
        public TextView tv_end;
        public TextView tv_trainNo;
        public TextView tv_zuoweixibie;
        public TextView tv_price;
    }
}
