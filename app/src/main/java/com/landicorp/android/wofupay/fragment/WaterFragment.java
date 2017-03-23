package com.landicorp.android.wofupay.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.ElecPayBean;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;

import java.util.HashMap;

import rx.Subscriber;

import static com.landicorp.android.wofupay.R.string.phone_number;


/**
 * Created by Administrator on 2017/3/21.
 */

public class WaterFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String[] provinces = {"安徽省"};
    private String[] citys = {"合肥","芜湖","淮北","安庆","六安","黄山","马鞍山"};
    private View mInflate;
    private String mParam1;
    private String mParam2;
    private EditText mEdit_account;
    private EditText mEdit_phoneNumber;
    private EditText mEdit_money;
    private Spinner mSpinner_province;
    private Spinner mSpinner_city;
    private ImageButton mBt_entry;
    private ImageButton mBtn_cancle;
    private String address,org,province;
    private String city = "";
    private String mAccount;
    private String mMoney;
    private String mPhoneNumber;

    public WaterFragment() {
    }

    public static WaterFragment newInstance(String param1, String param2) {
        WaterFragment fragment = new WaterFragment();
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
        mInflate = inflater.inflate(R.layout.fragment_water, null);
        initView();
        initData();
        initListener();
        return mInflate;
    }

    private void initData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, provinces);
        mSpinner_province.setAdapter(adapter);
        mSpinner_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    province = provinces[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    province = "";
            }
        });
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, citys);
        mSpinner_city.setAdapter(cityAdapter);
        mSpinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = citys[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                org = "";
            }
        });
    }

    private void initListener() {
        mBtn_cancle.setOnClickListener(this);
        mBt_entry.setOnClickListener(this);
    }

    private void initView() {
        mEdit_account = (EditText) mInflate.findViewById(R.id.account);
        mEdit_phoneNumber = (EditText) mInflate.findViewById(R.id.phoneNumber);
        mEdit_money = (EditText) mInflate.findViewById(R.id.account_money);
        mSpinner_province = (Spinner) mInflate.findViewById(R.id.province_spinner);
        mSpinner_city = (Spinner) mInflate.findViewById(R.id.city_spinner);

        mBt_entry = (ImageButton) mInflate.findViewById(R.id.bt_entry);
        mBtn_cancle = (ImageButton) mInflate.findViewById(R.id.bt_cancle);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_cancle:
                //返回主fragment
                switchContent(this,MainFragment.newInstance("",""));
                break;
            case R.id.bt_entry:
                getBillNo();
                break;
            default:
                break;
        }
    }

    private void getBillNo() {
        initBillNo();
        if (TextUtils.isEmpty(mAccount)){
            mEdit_account.setError("请输入户号");
            return;
        }
        if (TextUtils.isEmpty(mMoney)){
            mEdit_money.setError("请输入缴费金额");
            return;
        }
        if (TextUtils.isEmpty(mPhoneNumber)|| !AppUtils.isPhoneNumber(mPhoneNumber)){
            mEdit_phoneNumber.setError("请输入正确的手机号码!");
            return;
        }
        startGetBillNo();
    }

    private void startGetBillNo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("action", "GetBillPayNo");
        params.put("rechargeAccount", mAccount);
       // params.put("terminalcode", DeviceUtils.getDevicePort());
        params.put("mobile", mPhoneNumber);
        params.put("amount", mAccount + ".0");
        params.put("BN_type", "water");
        params.put("BN_memo", "水费");
        params.put("region", address+"");
        params.put("org", org+"");
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.life_recharge);

        helper.postParams(params).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(String s) {
                //得到billNO;
                ElecPayBean bean = new ElecPayBean();
                bean.BillNo = s;
                bean.function = 6;
                bean.payAmount = String.valueOf(Integer.parseInt(mAccount)*100);
                bean.phoneNumber = mPhoneNumber;
                bean.cardId = mAccount;
                bean.type = "2"; //水费
                //进入支付界面
            }
        });
    }

    private void initBillNo() {
        mAccount = mEdit_account.getText().toString().trim();
        mMoney = mEdit_money.getText().toString().trim();
        mPhoneNumber = mEdit_phoneNumber.getText().toString().trim();
        address=province+city;
    }
}
