package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.BasicBean;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.PayContacts;

/**
 * 选择充值方式Fragment
 * Created by Administrator on 2017/3/22 0022.
 */
public class PayTypeFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private BasicBean basicBean;
    private String payAmount;
    //支付方式
    private int payType;// 0支付宝 1微信 2银联

    private TextView mTitle;

    private RadioGroup mRadioGroup;
    private RadioButton mAlipay;
    private RadioButton mWxpay;
    private RadioButton mYinpay;

    private ImageButton mBackBtn;
    private ImageButton mOkBtn;

    public PayTypeFragment() {
    }

    public static PayTypeFragment newInstance(BasicBean param1, String param2) {
        PayTypeFragment fragment = new PayTypeFragment();
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
            payAmount = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay_type, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        showView(basicBean.function);
    }

    private void initView(View view) {
        mTitle = (TextView) view.findViewById(R.id.payTypeUI_title);

        mRadioGroup = (RadioGroup) view.findViewById(R.id.payTypeUI_rg);
        mRadioGroup.setOnCheckedChangeListener(this);

        mAlipay = (RadioButton) view.findViewById(R.id.payTypeUI_alipay);
        mWxpay = (RadioButton) view.findViewById(R.id.payTypeUI_wxpay);
        mYinpay = (RadioButton) view.findViewById(R.id.payTypeUI_yinpay);

        mBackBtn = (ImageButton) view.findViewById(R.id.payTypeUI_backBtn);
        mBackBtn.setOnClickListener(this);
        mOkBtn = (ImageButton) view.findViewById(R.id.payTypeUI_okBtn);
        mOkBtn.setOnClickListener(this);
    }


    private void showView(int function) {
        mTitle.setText(AppUtils.getFunctionByID(basicBean.function));

        if ( android.os.Build.VERSION.SDK_INT>15) {
            //公交卡充值方式只能用银联
            if (function == 1 || function == 0) {
                mAlipay.setVisibility(View.GONE);
                mWxpay.setVisibility(View.GONE);
                mYinpay.setChecked(true);
            } else {
                mAlipay.setVisibility(View.VISIBLE);
                mWxpay.setVisibility(View.VISIBLE);
                mWxpay.setChecked(true);
            }
        }else {
            mAlipay.setVisibility(View.VISIBLE);
            mWxpay.setVisibility(View.VISIBLE);
            mYinpay.setVisibility(View.GONE);
            mWxpay.setChecked(true);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.payTypeUI_backBtn:

                break;
            case R.id.payTypeUI_okBtn:
                onEntryClick();
                break;
        }
    }

    private void onEntryClick() {
        switch (payType) {
            //支付宝支付
            case 0:
                if(basicBean == null){
                    throw new RuntimeException("尚未输入支付内容");
                }
                switchContent(this, PayDetailsFragment.newInstance(basicBean, PayContacts.ali_service));
                break;
            //微信支付
            case 1:
                switchContent(this, PayDetailsFragment.newInstance(basicBean, PayContacts.weixin_service));
                break;
            //银联支付
            case 2:

                break;

        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == group.getChildAt(0).getId()) {
            payType = 0;
        } else if (checkedId == group.getChildAt(1).getId()) {
            payType = 1;
        } else if (checkedId == group.getChildAt(2).getId()) {
            payType = 2;
        }
    }
}
