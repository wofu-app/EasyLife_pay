package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.JLog;
import com.landicorp.android.wofupay.volley.VolleyHelper;
import com.landicorp.android.wofupay.widget.MyRadioGroup;

import static android.R.attr.data;
import static com.landicorp.android.wofupay.R.id.bt_entry;

/**
 * Created by Administrator on 2017/3/16.
 */

public class PhoneRechargeFragment extends Fragment {

    private View mView;
    private TextView mTv_phone_entry;
    private RadioGroup rGroup;
    private EditText mEt_phone;
    private MyRadioGroup myRadioGroup;
    private TextView tv_pay;
    private TextView tv_load;
    private ImageButton mBt_entry;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_phone_rechargement, null);
        initView();
    //    initListener();
        return mView;
    }

//    private void initListener() {
//        mEt_phone.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                String formatPhone = AppUtils.formatPhone((s.toString()));
//                mTv_phone_entry.setText(formatPhone);
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                myRadioGroup.removeAll();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if (rGroup.getCheckedRadioButtonId() > 0) {
//                    rGroup.clearCheck();
//                }
//
//            }
//        });
//
//        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                myRadioGroup.removeAll();
//                showPrice("0.0元");
//                String trim = mEt_phone.getText().toString().trim();
//                VolleyHelper.cancle("flowQuery");
//                if (pCom.checkMNO(trim)) {
//                    JLog.i(pCom.checkMNO(trim) + "  " + trim);
//                    switch (group.getCheckedRadioButtonId()) {
//                        // 话费充值
//                        case R.id.phone_rb_fees:
//                            showFees();
//                            break;
//                        // 流量充值
//                        case R.id.phone_rb_flow:
//                            pCom.showFloaw();
//                            break;
//                        default:
//                            break;
//                    }
//                }
//
//            }
//
//        });
//
//        myRadioGroup.setOnCheckerListener(new MyRadioGroup.OnCheckerListener() {
//
//            @Override
//            public void onItemChecked(MyRadioGroup group, int position,
//                                      boolean checked) {
//
//                String trim = mEt_phone.getText().toString().trim();
//
//                if (AppUtils.isPhoneNumber(trim)
//                        && pCom.getData().phoneNumber.equals(trim)) {
//                    mBt_entry.setEnabled(true);
//                    pCom.checkAmount(position);
//                } else {
//                    mBt_entry.setEnabled(false);
//                    showError("手机号码变动,请重新确认输入手机号!");
//                    myRadioGroup.removeAll();
//                    tv_load.setText("");
//                    tv_load.requestFocus();
//                    return;
//                }
//            }
//        });
//    }
//
//    private void showFees() {
//        data.getData().type = "2";
//        if (AppUtils.getFunctionState(7)==0) {
//            showFees(data.getFeesData());
//        }else {
//            showMNO(AppUtils.getMsg(7));
//        }
//    }
//
//    private void showPrice(String s) {
//        tv_pay.setText(s);
//    }

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

}
