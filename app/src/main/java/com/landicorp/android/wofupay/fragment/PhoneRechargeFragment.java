package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.PhoneBean;
import com.landicorp.android.wofupay.bean.Yin_FlowQueryBean;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.JLog;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.landicorp.android.wofupay.volley.VolleyHelper;
import com.landicorp.android.wofupay.widget.MyRadioGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

import static com.landicorp.android.wofupay.R.id.bt_entry;
import static com.landicorp.android.wofupay.utils.AppUtils.isPhoneNumber;

/**
 * Created by Administrator on 2017/3/16.
 */

public class PhoneRechargeFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View mView;
    private TextView mTv_phone_entry;
    private RadioGroup rGroup;
    private EditText mEt_phone;
    private MyRadioGroup myRadioGroup;
    private TextView tv_pay;
    private TextView tv_load;
    private ImageButton mBt_entry;
    String[] fees = { "30", "50", "100", "300", "500" };

    private PhoneBean bean;
    private Yin_FlowQueryBean flowBean;
    private Yin_FlowQueryBean.RecordsBean mRecordsBean;

    public PhoneRechargeFragment() {
    }

    public static PhoneRechargeFragment newInstance(String param1, String param2) {
        PhoneRechargeFragment fragment = new PhoneRechargeFragment();
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
        mView = inflater.inflate(R.layout.fragment_phone_rechargement, null);
        bean = new PhoneBean();
        flowBean = new Yin_FlowQueryBean();
        mRecordsBean = new Yin_FlowQueryBean.RecordsBean();

        initView();
        initListener();
        return mView;
    }

    private void initListener() {
        mBt_entry.setOnClickListener(this);
        mEt_phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String formatPhone = AppUtils.formatPhone((s.toString()));
                mTv_phone_entry.setText(formatPhone);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                myRadioGroup.removeAll();
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (rGroup.getCheckedRadioButtonId() > 0) {
                    rGroup.clearCheck();
                }

            }
        });

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                myRadioGroup.removeAll();
                showPrice("0.0元");
                String trim = mEt_phone.getText().toString().trim();
                VolleyHelper.cancle("flowQuery");
                if (checkMNO(trim)) {
                    JLog.i(checkMNO(trim) + "  " + trim);
                    switch (group.getCheckedRadioButtonId()) {
                        // 话费充值
                        case R.id.phone_rb_fees:
                            showFees1();
                            break;
                        // 流量充值
                        case R.id.phone_rb_flow:
                            showFloaw();
                            break;
                        default:
                            break;
                    }
                }

            }

        });

        myRadioGroup.setOnCheckerListener(new MyRadioGroup.OnCheckerListener() {

            @Override
            public void onItemChecked(MyRadioGroup group, int position,
                                      boolean checked) {

                String trim = mEt_phone.getText().toString().trim();

                if (isPhoneNumber(trim)
                        && getData().phoneNumber.equals(trim)) {
                    mBt_entry.setEnabled(true);
                    checkAmount(position);
                } else {
                    mBt_entry.setEnabled(false);
                    showError("手机号码变动,请重新确认输入手机号!");
                    myRadioGroup.removeAll();
                    tv_load.setText("");
                    tv_load.requestFocus();
                    return;
                }
            }
        });
    }
    public PhoneBean getData() {
        return bean;
    }
    public void checkAmount(int position) {
        selectCombo(position);
    }
    public void selectCombo(int position) {
        // 话费充值
        if (TextUtils.equals(bean.type, "2")) {
            // 支付金额
            bean.payAmount = (int)Float.parseFloat(fees[position])*100+"";

//		流量
        } else if (TextUtils.equals("1", bean.type)) {
            Yin_FlowQueryBean.RecordsBean recordsBean =flowBean.getRecords().get(position);
            bean.payAmount = (int)Float.parseFloat(recordsBean.getFaceValue())*100+"";
            bean.templetId = recordsBean.getTempletId();

        }
        showPrice(Float.parseFloat(bean.payAmount)/100+"元");


    }

        public void showFloaw() {
            getData().type = "1";
            if (AppUtils.getFunctionState(8)==0) {
                if (!TextUtils.equals("V", getData().serFlag)) {
                   queryFlow();
                } else {
                    showMNO("流量充值暂不支持虚拟号段!");
                }
            }else {
                showMNO(AppUtils.getMsg(8));
            }

        }

    public void queryFlow() {
        String reqDateTime = AppUtils.getStringDate("yyyyMMddHHmmss");
        String termTransID_flow = "flow_query" + reqDateTime;

        String sing_flow = sign_flow(reqDateTime, termTransID_flow, bean.phoneNumber);
        JLog.i(sing_flow);
        Map<String, String> params = new HashMap<String, String>();
        params.put("serveruri", "flowRateQuery.cgi");
        params.put("terminalID", PayContacts.YIN_TERMINALID);
        params.put("factoryID", PayContacts.YIN_FACTORYID);
        params.put("reqDateTime", reqDateTime);
        params.put("termTransID", termTransID_flow);
        params.put("userNumber", bean.phoneNumber);
        params.put("sign", sing_flow);
        params.put("urltype", PayContacts.YIN);
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.URL_YIN);
        Observable<String> observable = helper.postParams(params);
        //此处要添加提示信息
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                showMNO("加载失败,请重试!");
            }

            @Override
            public void onNext(String s) {
                //得到要展示的信息
                Gson gson = new Gson();
                try {
                    flowBean = gson.fromJson(s,
                            Yin_FlowQueryBean.class);
                    if (TextUtils.equals("0000", flowBean.getBase().getStatus())) {
                        showMNO("请选择套餐");
                        showFlow(flowBean.getRecords());
                    } else {
                        showMNO(flowBean.getBase().getMsg());
                    }
                } catch (Exception e) {
                    showMNO("加载失败,请重试!");
                }
            }
        });
    }
    public void showFlow(List<Yin_FlowQueryBean.RecordsBean> records) {
        if (records != null && records.size() > 0) {
            for (int i = 0; i < records.size(); i++) {
                myRadioGroup.append(records.get(i).getFlowNum());
            }
        }

    }
    /**
     * 单笔流量充值查询获取签名
     */
    public String sign_flow(String reqDateTime, String termTransID_flow,
                            String phone) {
        return AppUtils.MD5("terminalID=" + PayContacts.YIN_TERMINALID
                + "&factoryID=" + PayContacts.YIN_FACTORYID + "&reqDateTime="
                + reqDateTime + "&termTransID=" + termTransID_flow
                + "&userNumber=" + phone + "&key=" + PayContacts.YIN_KEY);
    }
    private void showFees1() {
        getData().type = "2";
        if (AppUtils.getFunctionState(7)==0) {
            showFees(getFeesData());
        }else {
            showMNO(AppUtils.getMsg(7));
        }
    }
    public void showFees(String[] data) {
        for (String string : data) {
            myRadioGroup.append((String) TextUtils.concat(string, "元"));
        }

    }
    public String[] getFeesData(){
        return fees;
    }
    private void showPrice(String s) {
        tv_pay.setText(s);
    }

    private void initView() {
        mTv_phone_entry = (TextView) mView.findViewById(R.id.phone_tv_entry);
        rGroup = (RadioGroup) mView.findViewById(R.id.phone_rg);
        mEt_phone = (EditText) mView.findViewById(R.id.phone_et);
        myRadioGroup = (MyRadioGroup)mView. findViewById(R.id.phone_mrg);
        tv_pay = (TextView) mView.findViewById(R.id.phone_tv_pay);
        mBt_entry = (ImageButton) mView.findViewById(bt_entry);
        tv_load = (TextView) mView.findViewById(R.id.phone_tv_load);
        mBt_entry.setEnabled(false);
    }
    public boolean checkMNO(String phone) {
        String oper = "";
        String serFlag = null;
        int operator = 0;
        if (AppUtils.isPhoneNumber(phone)) {

            int phoneNumberType = AppUtils.PhoneNumberType(phone);

            switch (phoneNumberType) {
                // 移动
                case 0:
                    serFlag = "G";
                    operator = 0;
                    oper = "中国移动";
                    break;
                // 联通
                case 1:
                    serFlag = "G";
                    operator = 1;
                    oper = "中国联通";
                    break;
                // 电信
                case 2:
                    serFlag = "G";
                    operator = 2;
                    oper = "中国电信";
                    break;
                case 3:
                    serFlag = "V";
                    operator = 3;
                    oper = "虚拟号段";
                    break;
                // 其它
                case 4:
                default:
                    serFlag = null;
                    oper = "暂不支持该类型的手机号段...";
                    showMNO(oper);
                    return false;

            }
            showMNO(oper);

            getData().serFlag = serFlag;
            getData().phoneNumber = phone;
            getData().operator = operator;

        } else {
            showError("请输入正确号码!");
        }

        return !TextUtils.isEmpty(serFlag);

    }
    public void showMNO(String oper) {
        tv_load.setText(oper);

    }
    public void showError(String errMsg) {
        mEt_phone.setError(Html.fromHtml("<font color='red'>" + errMsg
                + "</font>"));
        showPrice("0.0元");
        mEt_phone.requestFocus();
        return;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_entry:
                getBillNo();
                break;
            default:
                break;
        }
    }

    private void getBillNo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "GetHPBillNo");
        params.put("rechargeAccount", bean.phoneNumber);
        // params.put("terminalcode", DeviceUtils.getDevicePort());
        params.put("mobile", bean.phoneNumber);
        params.put("amount", Float.parseFloat(bean.payAmount)/100+"");
        if (TextUtils.equals("2", bean.type)) {
            params.put("BN_type", "phone");
            params.put("BN_memo", "话费充值");

        } else if (TextUtils.equals("1", bean.type)) {
            params.put("BN_type", "flow");
            params.put("BN_memo", "流量充值");
        }
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.URL);
        Observable<String> observable = helper.postParams(params);
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                showMNO("加载失败,请重试!");
            }

            @Override
            public void onNext(String s) {
                PhoneBean bean =getData();
                bean.BillNo = s;
                bean.function = 3;
                bean.payData = AppUtils.getStringDate("yyyyMMddHHmmss");
                JLog.e(bean.toString());
                Toast.makeText(getContext(),bean.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }


}
