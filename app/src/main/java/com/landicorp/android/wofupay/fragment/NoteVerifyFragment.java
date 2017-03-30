package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.utils.JLog;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.utils.StringUtils;
import com.landicorp.android.wofupay.volley.HttpParams;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.yanzhenjie.fragment.NoFragment;

import rx.Subscriber;

/**
 * 短信验证Fragment
 * Created by Administrator on 2017/3/29 0029.
 */
public class NoteVerifyFragment extends NoFragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText mPhoneEdit;
    private EditText mCodeEdit;
    private Button mCodeBtn;
    private Button mOkBtn;

    private String phoneNum;
    private int numcode;
    private boolean hasVerify;
    private TimeCount time;

    public NoteVerifyFragment() {
    }

    public static NoteVerifyFragment newInstance(String param1, String param2) {
        NoteVerifyFragment fragment = new NoteVerifyFragment();
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
        return inflater.inflate(R.layout.fragment_note_verify, null);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        time = new TimeCount(120000, 1000);
        initView(view);
    }

    private void initView(View view) {
        mPhoneEdit = (EditText) view.findViewById(R.id.noteVerifyUI_phoneEdit);
        mCodeEdit = (EditText) view.findViewById(R.id.noteVerifyUI_codeEdit);
        mCodeBtn = (Button) view.findViewById(R.id.noteVerifyUI_codeBtn);
        mCodeBtn.setOnClickListener(this);
        mOkBtn = (Button) view.findViewById(R.id.noteVerifyUI_okBtn);
        mOkBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.noteVerifyUI_codeBtn:
                phoneNum = mPhoneEdit.getText().toString().trim();
                if(TextUtils.isEmpty(phoneNum)){
                    Toast.makeText(getContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!StringUtils.isPhoneNumber(phoneNum)){
                    Toast.makeText(getContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }

                time.start();
                numcode = (int) ((Math.random() * 9 + 1) * 100000);
                // 进行短信验证

                RxVolleyHelper helper = new RxVolleyHelper(PayContacts.base_url + "SystemMange/SysServer.ashx");
                HttpParams params = HttpParams.getInstance();
                params.put("action", "SMSServer");
                params.put("TermNo", "");
                params.put("BType", "002");
                params.put("EndNumber", numcode + "");
                params.put("RecNumber", phoneNum);
                helper.postParams(params.getParams()).subscribe(
                        new Subscriber<String>() {

                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                hasVerify = false;
                                Toast.makeText(getContext(),"短信发送失败,请稍后重试",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(String t) {
                                JLog.i("----验证码请求成功----" + numcode);
                            }
                        });

                break;
            case R.id.noteVerifyUI_okBtn:
                String codeNum = mCodeEdit.getText().toString().trim();
                if(TextUtils.isEmpty(codeNum)){
                    Toast.makeText(getContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Integer.valueOf(codeNum) == numcode) {
                    // 验证通过 进入选择地址界面
                    hasVerify = true;
                    startFragmentForResquest(SelectAddressFragment.newInstance(phoneNum, ""), 222);
                } else {
                    hasVerify = false;
                    Toast.makeText(getContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {
            mCodeBtn.setText("重新验证");
            mCodeBtn.setClickable(true);
        }

        @Override
        public void onTick(long arg0) {
            mCodeBtn.setClickable(false);
            mCodeBtn.setText(arg0 /1000+"秒");
        }

    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, @Nullable Bundle result) {
        super.onFragmentResult(requestCode, resultCode, result);
        if (result == null) {
            result = new Bundle();
        }
        result.putBoolean("hasVerify", hasVerify);
        result.putString("phoneNum", phoneNum);
        setResult(RESULT_OK, result);
        finish();
    }
}
