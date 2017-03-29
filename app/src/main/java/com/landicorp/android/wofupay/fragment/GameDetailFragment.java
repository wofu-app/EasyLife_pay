package com.landicorp.android.wofupay.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.base.BaseApplication;
import com.landicorp.android.wofupay.bean.FailBean;
import com.landicorp.android.wofupay.bean.GameBean;
import com.landicorp.android.wofupay.utils.JLog;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.utils.StringUtils;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.yanzhenjie.fragment.NoFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * 游戏充值详情页
 * Created by Administrator on 2017/3/21 0021.
 */
public class GameDetailFragment extends NoFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int position;
    private String rechargeType;

    private TextView mRechType;
    private EditText mQQEdit;
    private EditText mAgianEdit;
    private EditText mPhoneEdit;
    private TextView mNumberText;
    private FrameLayout mFrameLy;
    private TextView mRechMoney;
    private ImageButton mBackBtn;
    private ImageButton mOkBtn;

    private PopupWindow mPopWindow;

    private GameBean gameBean = new GameBean();
    //spinner数据
    private List<String> spinnerData = new ArrayList<String>();

    public GameDetailFragment() {
    }

    public static GameDetailFragment newInstance(int param1, String param2) {
        GameDetailFragment fragment = new GameDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PARAM1);
            rechargeType = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_detail, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initPaymount();
    }

    private void initView(View view) {

        spinnerData = setSpinerData(position);

        mRechType = (TextView) view.findViewById(R.id.gameDetailUI_rechType);
        mRechType.setText(rechargeType);

        mQQEdit = (EditText) view.findViewById(R.id.gameDetailUI_qqEdit);
        mAgianEdit = (EditText) view.findViewById(R.id.gameDetailUI_agianEdit);
        mPhoneEdit = (EditText) view.findViewById(R.id.gameDetailUI_phoneEdit);
        mNumberText = (TextView) view.findViewById(R.id.gameDetailUI_numberText);
        mNumberText.setText(spinnerData.get(0));
        mNumberText.setOnClickListener(this);

        mFrameLy = (FrameLayout) view.findViewById(R.id.gameDetailUI_frameLy);
        mRechMoney = (TextView) view.findViewById(R.id.gameDetailUI_rechMoney);
        mBackBtn = (ImageButton) view.findViewById(R.id.gameDetailUI_backBtn);
        mBackBtn.setOnClickListener(this);
        mOkBtn = (ImageButton) view.findViewById(R.id.gameDetailUI_okBtn);
        mOkBtn.setOnClickListener(this);

    }

    /**
     * 设置金额
     */
    private void initPaymount() {
        String pay;
        if (TextUtils.isEmpty(rechargeType)) {
            JLog.e("没有数据");
            return;
        }

        if (rechargeType.equals(BaseApplication.getContext().getResources().getString(R.string.game_tx_DNF))) {
            pay = "2000";
            gameBean.isDNF = true;
        } else {
            gameBean.isDNF = false;
            pay = "1000";
        }

        if (rechargeType.equals(BaseApplication.getContext().getResources().getString(R.string.game_tx_qb))) {
            gameBean.isQB = true;
            gameBean.chargeCount = 10;
        } else {
            gameBean.isQB = false;
        }
        gameBean.payAmount = pay;
        mRechMoney.setText(Float.parseFloat(pay) / 100 + "元");
    }

    /**
     * 设置购买数量
     * @param position
     */
    public List<String> setSpinerData(int position) {
        int dataType = 0;
        if (position == 0) {
            dataType = 0;
        } else {
            dataType = 1;
        }
        spinnerData.clear();
        if (dataType == 0) {
            for (int i = 1; i <= 10; i++) {
                spinnerData.add(i * 10 + "个Q币");
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                spinnerData.add(i + "个月");
            }
        }
        return spinnerData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gameDetailUI_numberText:
                showPopupWindow(v);
                break;
            case R.id.gameDetailUI_backBtn:
                finish();
                break;
            case R.id.gameDetailUI_okBtn:
                String QQ = mQQEdit.getText().toString().trim();
                if (TextUtils.isEmpty(QQ)) {
                    Toast.makeText(getContext(),"请输入QQ号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (QQ.length() < 5) {
                    Toast.makeText(getContext(),"最少是5位数号码",Toast.LENGTH_SHORT).show();
                    return;
                }

                String confirmQQ = mAgianEdit.getText().toString().trim();
                if (TextUtils.isEmpty(confirmQQ)) {
                    Toast.makeText(getContext(),"请确认QQ号码",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!QQ.equals(confirmQQ)) {
                    Toast.makeText(getContext(),"QQ号码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }

                String phoneNumber = mPhoneEdit.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(getContext(),"请输入手机号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!StringUtils.isPhoneNumber(phoneNumber)) {
                    Toast.makeText(getContext(),"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                    return;
                }

                gameBean.payData = StringUtils.getStringDate("yyyyMMddHHmmss");
                gameBean.userNumber = QQ;
                gameBean.qQNumber = QQ;
                gameBean.phoneNumber = phoneNumber;
                getBillNo(QQ, phoneNumber);

                break;
        }
    }

    public void getBillNo(String QQ,String phoneNumber) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("terminalcode", "DeviceUtils.getDevicePort()");
        params.put("BN_memo", "游戏充值");
        params.put("rechargeAccount", QQ);
        params.put("mobile", phoneNumber);
        params.put("amount", String.valueOf(Double.parseDouble(gameBean.payAmount) /100));
        params.put("BN_type", "game");
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

                gameBean.BillNo = s;
                gameBean.function = 4;
                jumpToPay(gameBean,gameBean.payAmount);
            }
        });

    }

    public void jumpToPay(GameBean gameBean, String payAmount) {
        startFragment(PayTypeFragment.newInstance(gameBean, null, payAmount));
    }

    public void jumpToFail(FailBean failBean) {
        startFragment(PayTypeFragment.newInstance(null, failBean, ""));
    }

    private void showPopupWindow(View v) {
        if (mPopWindow == null)
            mPopWindow = popSpinner();
        if (mPopWindow != null && !mPopWindow.isShowing()) {
            mPopWindow.showAtLocation(v, Gravity.BOTTOM, 0, 250);
            mFrameLy.setVisibility(View.VISIBLE);
        }
    }

    private PopupWindow popSpinner() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.spinner_game, null);
        PopupWindow window = new PopupWindow(getActivity());
        window.setWidth(600);
        window.setHeight(500);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setContentView(view);// 设置PopupWindow的内容view
        window.setFocusable(true); // 设置PopupWindow可获得焦点
        window.setTouchable(true); // 设置PopupWindow可触摸
        window.setOutsideTouchable(false); // 设置非PopupWindow区域可触摸
        ListView lv = (ListView) view.findViewById(R.id.spinner_listView);
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, spinnerData);

        lv.setAdapter(adapter);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                mFrameLy.setVisibility(View.GONE);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                popSelect(arg2);
            }

        });

        return window;
    }

    private void popSelect(int position) {
        mNumberText.setText(spinnerData.get(position));
        int payAmount = 0;
        int chargeCount = position + 1;
        if (gameBean.isDNF) {
            payAmount = (position + 1) * 2000;
        } else {
            payAmount = (position + 1) * 1000;
        }
        if (gameBean.isQB) {
            chargeCount = chargeCount * 10;
        }
        gameBean.chargeCount = chargeCount;
        gameBean.payAmount = payAmount + "";
        mRechMoney.setText(Float.parseFloat(payAmount + "") / 100 + " 元");
        mPopWindow.dismiss();
    }


}
