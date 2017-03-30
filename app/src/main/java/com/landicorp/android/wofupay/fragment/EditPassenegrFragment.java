package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.ServiceBean;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.JLog;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.volley.HttpParams;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.yanzhenjie.fragment.NoFragment;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/3/25.
 */

public class EditPassenegrFragment extends NoFragment implements View.OnClickListener {

    private View mInflate;
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private EditText mEd_name;
    private EditText mEd_phone;
    private EditText mEd_idcard;
    private Button mBtn_passengerok;
    private Button mBtn_cancale;
    private String mName;
    private String mPhone;
    private String mIdcard;
    private String mPhonenum;
    private boolean mRegrist;
    private TextView mTitle;

    public EditPassenegrFragment() {
    }

    public static EditPassenegrFragment newInstance(String param1, String param2,String param3,Boolean regrist) {
        EditPassenegrFragment fragment = new EditPassenegrFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putBoolean(ARG_PARAM4,regrist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1); //idNum
            mParam2 = getArguments().getString(ARG_PARAM2); //pidname
            //phonenum
            mPhonenum = getArguments().getString(ARG_PARAM3);
            mRegrist = getArguments().getBoolean(ARG_PARAM4);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_edit_passenger, null);
        initView();
        initListener();
        return mInflate;
    }

    private void initListener() {
        mBtn_cancale.setOnClickListener(this);
        mBtn_passengerok.setOnClickListener(this);
    }

    private void initView() {
        mTitle = (TextView) mInflate.findViewById(R.id.text_title);
        mEd_name = (EditText) mInflate.findViewById(R.id.et_passengerName);
        mEd_phone = (EditText) mInflate.findViewById(R.id.et_passengerPhone);
        mEd_idcard = (EditText) mInflate.findViewById(R.id.et_passengerIdcard);
        mBtn_passengerok = (Button) mInflate.findViewById(R.id.button_ok);
        mBtn_cancale = (Button) mInflate.findViewById(R.id.button_cancel);
        if (mRegrist){
            mEd_phone.setText(mPhone);
            mEd_phone.setKeyListener(null);
            mTitle.setText("注册联系人");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_cancel:
                //返回前一个界面
                finish();
                break;
            case R.id.button_ok:
                //获取数据
                initData();
                break;
            default:
                break;
        }
    }

    private void initData() {
        mName = mEd_name.getText().toString().trim();
        mPhone = mEd_phone.getText().toString().trim();
        mIdcard = mEd_idcard.getText().toString().trim();
        if (TextUtils.isEmpty(mName)){
            mEd_name.setError("请输入乘车人姓名");
            return;
        }
        if (TextUtils.isEmpty(mPhone)||! AppUtils.isPhoneNumber(mPhone)){
            mEd_phone.setError("请输入乘车人电话号码");
            return;
        }
        if (TextUtils.isEmpty(mIdcard)||mEd_idcard.length()!=18){
            mEd_idcard.setError("请输入乘车人身份证号码");
            return;
        }
        //将数据保存到服务器
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.base_url + "TrainTickets/TrainTickets.ashx");
        HttpParams params = HttpParams.getInstance();
        params.put("action", "InsertContacs");
        params.put("phone", mPhone);
        params.put("Idcard", mIdcard);
        params.put("name", mName);
        params.put("PId", mParam1);
        helper.postParams(params.getParams()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(getContext(),"数据提交失败,请重新提交",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String s) {
                //数据发送成功
                JLog.d("--------","数据发送成功");
                ServiceBean bean = new Gson().fromJson(s, ServiceBean.class);
                if (bean.code.equals("002")){
                    Toast.makeText(getContext(),"联系人已存在,不需要重复添加",Toast.LENGTH_SHORT).show();
                }else {
                    finish();

                }
            }
        });
    }
}
