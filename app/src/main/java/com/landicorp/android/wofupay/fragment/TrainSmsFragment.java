package com.landicorp.android.wofupay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.JLog;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.volley.HttpParams;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.yanzhenjie.fragment.NoFragment;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/3/24.
 */

public class TrainSmsFragment extends NoFragment implements View.OnClickListener {

    private View mInflate;
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText mEd_phone;
    private EditText mEd_sms;
    private Button mBt_next;
    private Button mBt_submit;
    private String mPhoneNum;
    private TimeCount mTimeCount;
    private int mNumcode;
    private boolean hasVeri;
    public TrainSmsFragment() {
    }

    public static TrainSmsFragment newInstance(String param1, String param2) {
        TrainSmsFragment fragment = new TrainSmsFragment();
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
        mInflate = inflater.inflate(R.layout.fragment_trainsms, null);
        mTimeCount = new TimeCount(120000, 1000);
        initView();
        initListener();
        return mInflate;
    }

    private void initListener() {
        mBt_next.setOnClickListener(this);
        mBt_submit.setOnClickListener(this);
    }

    private void initView() {
        mEd_phone = (EditText) mInflate.findViewById(R.id.et_write_phone);
        mEd_sms = (EditText) mInflate.findViewById(R.id.et_sms_captcha);
        mBt_next = (Button) mInflate.findViewById(R.id.btn_next);
        mBt_submit = (Button) mInflate.findViewById(R.id.btn_submit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                mPhoneNum = mEd_phone.getText().toString().trim();
                if (AppUtils.isPhoneNumber(mPhoneNum)){
                    mBt_next.setClickable(true);
                    mTimeCount.start();
                    mNumcode = (int) ((Math.random() * 9 + 1) * 100000);
                    //TODO 对话框提示
                    //访问短信接口
                    RxVolleyHelper helper = new RxVolleyHelper(PayContacts.base_url + "SystemMange/SysServer.ashx");
                    HttpParams params = HttpParams.getInstance();
                    params.put("action", "SMSServer");
                    //TODO 终端号
                   // params.put("TermNo", DeviceUtils.getDevicePort());
                    params.put("BType", "001");
                    params.put("EndNumber", mNumcode + "");
                    params.put("RecNumber", mPhoneNum);
                    helper.postParams(params.getParams()).subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Toast.makeText(getContext(),"短信发送失败,请稍后重试",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(String s) {
                            JLog.i("--------------验证码请求成功--------------"
                                    + mNumcode);
                        }
                    });
                }else {
                    mEd_phone.setError("请输入正确的手机号码");
                }
                break;
            case R.id.btn_submit:
                String trim = mEd_sms.getText().toString().trim();
                if (TextUtils.isEmpty(trim)){
                    mEd_sms.setError("请输入正确的验证码");
                    return;
                }
                Integer a = Integer.valueOf(trim);
                if (a == mNumcode){
                    //进入选择联系人界面
                    hasVeri = true;
                    startFragmentForResquest(SelectPassengerFragment.newInstance(mPhoneNum,""),1);
                }else {
                    //验证码错误
                    hasVeri = false;
                    mEd_sms.setError("验证码错误,请输入正确的验证码");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode,  Bundle result) {
        super.onFragmentResult(requestCode, resultCode, result);
        if (result == null){
            result = new Bundle();
        }
        result.putBoolean("hasVeri",hasVeri);
        result.putString("mPhoneNum",mPhoneNum);
        setResult(RESULT_OK,result);
        finish();
    }

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {

            mBt_next.setText("重新验证");
            mBt_next.setClickable(true);
        }

        @Override
        public void onTick(long arg0) {

            mBt_next.setClickable(false);
            mBt_next.setText(arg0 /1000+"秒");
        }

    }
}
