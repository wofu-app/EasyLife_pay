package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.FailBean;
import com.landicorp.android.wofupay.bean.GasBean;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.utils.StringUtils;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.yanzhenjie.fragment.NoFragment;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * 加油卡充值Fragment
 * Created by Administrator on 2017/3/18.
 */

public class GasRechargeFragment extends NoFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private EditText mCardNoEdit;
    private EditText mAgianNoEdit;
    private EditText mPhoneEdit;

    private RadioGroup mRadioGroup;
    private RadioButton mRadioBtn1;
    private RadioButton mRadioBtn2;
    private RadioButton mRadioBtn3;
    private RadioButton mRadioBtn4;

    private ImageButton mBackBtn;
    private ImageButton mOkBtn;

    private GasBean gasBean = new GasBean();

    public GasRechargeFragment() {
    }

    public static GasRechargeFragment newInstance(String param1, String param2) {
        GasRechargeFragment fragment = new GasRechargeFragment();
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
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_gas_recharge, null);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        mCardNoEdit = (EditText) view.findViewById(R.id.gasRechUI_cardNoEdit);
        mAgianNoEdit = (EditText) view.findViewById(R.id.gasRechUI_agianNoEdit);
        mPhoneEdit = (EditText) view.findViewById(R.id.gasRechUI_phoneEdit);

        mRadioGroup = (RadioGroup) view.findViewById(R.id.gasRechUI_radioGroup);
        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioBtn1 = (RadioButton) view.findViewById(R.id.gasRechUI_radioBtn1);
        mRadioBtn2 = (RadioButton) view.findViewById(R.id.gasRechUI_radioBtn2);
        mRadioBtn3 = (RadioButton) view.findViewById(R.id.gasRechUI_radioBtn3);
        mRadioBtn4 = (RadioButton) view.findViewById(R.id.gasRechUI_radioBtn4);

        mBackBtn = (ImageButton) view.findViewById(R.id.gasRechUI_backBtn);
        mBackBtn.setOnClickListener(this);
        mOkBtn = (ImageButton) view.findViewById(R.id.gasRechUI_okBtn);
        mOkBtn.setOnClickListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int money = 0;
        switch (checkedId) {
            case R.id.gasRechUI_radioBtn1:
                // 全国 中石化加油 固定面值加油卡 直充100元
                money = 100;
                break;
            case R.id.gasRechUI_radioBtn2:
                // 全国 中石化加油 固定面值加油卡 直充200元
                money = 200;
                break;
            case R.id.gasRechUI_radioBtn3:
                // 全国 中石化加油 固定面值加油卡 直充500元
                money = 500;
                break;
            case R.id.gasRechUI_radioBtn4:
                // 全国 中石化加油 固定面值加油卡 直充1000元
                money = 1000;
                break;
        }
        gasBean.payAmount = money*100 + "";

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gasRechUI_backBtn:
                break;
            case R.id.gasRechUI_okBtn:
                String cardNo = mCardNoEdit.getText().toString().trim();
                if (TextUtils.isEmpty(cardNo)) {
//                    ToastMsg("请输入加油卡号");
                    return;
                }

                String cardEntry = mAgianNoEdit.getText().toString().trim();

                if (TextUtils.isEmpty(cardEntry)) {
//                    ToastMsg("请确认加油卡号");
                    return;
                }
                if (!cardEntry.equals(cardNo)) {
//                    ToastMsg("请确认账号是否一致");
                    return;
                }

                int cardType = -1;
                if (cardNo.substring(0, 1)
                        .equals("9")
                        && cardNo.length() == 16) {
                    cardType = 0;
                } else if (cardNo.substring(0, 6)
                        .equals("100011")
                        && cardNo.length() == 19) {
                    cardType = 1;
                } else {
//                    ToastMsg("请输入正确的加油卡号");
                    return;
                }

                String phoneNumber = mPhoneEdit.getText().toString().trim();
                if (!StringUtils.isPhoneNumber(phoneNumber)) {
//                    ToastMsg("请输入正确的手机号码");
                    return;
                }

                if (TextUtils.isEmpty(gasBean.payAmount)) {
//                    ToastMsg("请选择充值金额");
                    return;
                }

                gasBean.cardId = cardNo;
                gasBean.type = cardType;
                gasBean.phoneNumber = phoneNumber;
                gasBean.data = StringUtils.getStringDate("yyyyMMddHHmmss");
                gasBean.function = 2;
                getBillNo();
                break;
        }
    }

    private void getBillNo() {
        Map<String, String> params =new HashMap<String, String>();
        params.put("terminalcode", "");
        params.put("BN_memo", "加油卡充值");
        params.put("rechargeAccount", gasBean.cardId);
        params.put("mobile", gasBean.phoneNumber);
        params.put("amount", Float.parseFloat(gasBean.payAmount)/100+"");
        params.put("BN_type", "gascard");
        params.put("action", "GetHPBillNo");

        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.URL);
        Observable<String> observable = helper.postParams(params);
        //此处要添加提示信息
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                FailBean bean = new FailBean();
                bean.errorCode="404";
                bean.errorMsg="服务器繁忙,请求账单号未完成,请稍后再试";
                bean.function=3;
                jumpToFail(bean);
            }

            @Override
            public void onNext(String s) {
                Log.e("MyTest","======s======"+s);
                gasBean.BillNo = s;
                gasBean.function=3;
                jumpToPay(gasBean,gasBean.payAmount);
            }
        });
    }

    public void jumpToPay(GasBean gasBean, String payAmount) {
        startFragment(PayTypeFragment.newInstance(gasBean, null, payAmount));
    }

    public void jumpToFail(FailBean failBean) {
        startFragment(PayTypeFragment.newInstance(null, failBean, ""));
    }

}
