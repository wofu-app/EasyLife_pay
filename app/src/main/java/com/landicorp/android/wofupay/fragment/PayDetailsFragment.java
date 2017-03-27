package com.landicorp.android.wofupay.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.base.BaseApplication;
import com.landicorp.android.wofupay.bean.BasicBean;
import com.landicorp.android.wofupay.bean.FailBean;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.JLog;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.utils.StringUtils;
import com.landicorp.android.wofupay.utils.XmlUtils;
import com.landicorp.android.wofupay.volley.CallBackListener;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.landicorp.android.wofupay.volley.VolleyHelper;
import com.yanzhenjie.fragment.NoFragment;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 支付详情Fragment
 * Created by Administrator on 2017/3/23 0023.
 */
public class PayDetailsFragment extends NoFragment implements View.OnClickListener, CallBackListener<String> {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private BasicBean basicBean;
    private String payway;

    private String dateTime;

    private ImageView mPayTitle;
    private ImageView mPayImage;
    private TextView mGoodsName;
    private TextView mDealMoney;
    private TextView mShopName;
    private TextView mOrderNum;

    private Button mCancelBtn;
    private Button mCompleteBtn;

    public PayDetailsFragment() {
    }

    public static PayDetailsFragment newInstance(BasicBean param1, String param2) {
        PayDetailsFragment fragment = new PayDetailsFragment();
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
            payway = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay_details, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        creatCode();
    }

    private void initView(View view) {
        mPayTitle = (ImageView) view.findViewById(R.id.payDetailsUI_payTitle);
        mPayImage = (ImageView) view.findViewById(R.id.payDetailsUI_payTitle);
        mGoodsName = (TextView) view.findViewById(R.id.payDetailsUI_goodsName);
        mDealMoney = (TextView) view.findViewById(R.id.payDetailsUI_dealMoney);
        mShopName = (TextView) view.findViewById(R.id.payDetailsUI_shopName);
        mOrderNum = (TextView) view.findViewById(R.id.payDetailsUI_orderNum);

        mCancelBtn = (Button) view.findViewById(R.id.payDetailsUI_cancelBtn);
        mCancelBtn.setOnClickListener(this);
        mCompleteBtn = (Button) view.findViewById(R.id.payDetailsUI_completeBtn);
        mCompleteBtn.setOnClickListener(this);

    }

    private void initData() {
        mDealMoney.setText(Float.parseFloat(basicBean.payAmount) / 100 + "");
        mOrderNum.setText(basicBean.BillNo);
        mGoodsName.setText(AppUtils.getFunctionByID(basicBean.function));
        if (TextUtils.equals(payway, PayContacts.ali_service)) {
            mPayTitle.setBackgroundResource(R.mipmap.ali_logo);
        }else {
            mPayTitle.setBackgroundResource(R.mipmap.weixin_logo);
        }
    }

    /**
     * 获取二维码
     */
    private void creatCode() {
        VolleyHelper helper = new VolleyHelper(PayContacts.weixin_url, this);
        helper.postBody(getPayString());
    }

    private String getPayString() {
        dateTime = StringUtils.formDateTime("yyyyMMDDHHmmss");

        String amount = basicBean.payAmount;
        String pay = amount;
        if (PayContacts.isDebug) {
            pay = "1";
        }

        String mdt = "body=" + AppUtils.getFunctionByID(basicBean.function) + "&"
                + "mch_create_ip=" + PayContacts.weixin_mch_create_ip + "&"
                + "mch_id=" + PayContacts.weixin_mch_id + "&" + "nonce_str="
                + dateTime + "&" + "notify_url="
                + PayContacts.weixin_notify_url + "&" + "out_trade_no="
                + basicBean.BillNo + "&" + "service=" + payway
                + "&" + "total_fee=" + pay + "&" + "key="
                + PayContacts.weixin_key;

        String s = StringUtils.MD5(mdt);
        String payString = "<xml>\n" + "<body><![CDATA["
                + AppUtils.getFunctionByID(basicBean.function) + "]]></body>\n"
                + "<mch_create_ip>" + PayContacts.weixin_mch_create_ip
                + "</mch_create_ip>\n" + "<mch_id>" + PayContacts.weixin_mch_id
                + "</mch_id>\n" + "<nonce_str>" + dateTime + "</nonce_str>\n"
                + "<notify_url >" + PayContacts.weixin_notify_url
                + "</notify_url >\n" + "<out_trade_no>" + basicBean.BillNo
                + "</out_trade_no>\n" + "<service>"
                + payway + "</service>\n" + "<total_fee>"
                + pay + "</total_fee>\n" + "<sign><![CDATA[" + s
                + "]]></sign>\n" + "</xml>";
        return payString;
    }

    @Override
    public void onSuccess(String response) {
        JLog.i("返回数据成功了"+response);
//        pComl.dismiss();
        Observable.just(response).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String response) {
                if (TextUtils.isEmpty(response)) {
                    doFail(404 + "", "服务器繁忙,未返回支付数据,请返回重试", basicBean.function);
                }
                return !TextUtils.isEmpty(response);
            }
        }).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String result) {
                String status = XmlUtils.getSpecialValue(result, "status");
                if (TextUtils.isEmpty(status) || !TextUtils.equals("0", status)) {
                    doFail(status + "", "服务器繁忙,未返回支付数据,请返回重试", basicBean.function);
                    return false;
                } else {
                    return true;
                }
            }
        }).map(new Func1<String, String>() {

            @Override
            public String call(String result) {
                return  XmlUtils.getSpecialValue(result, "code_img_url");
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String imgUrl) {
                JLog.i(imgUrl);
                showCode(imgUrl);
            }
        });
    }

    @Override
    public void onFail(VolleyError error) {
//        dismiss();
        doFail(404+"", "服务器繁忙,未返回支付数据,请返回重试", basicBean.function);
        error.printStackTrace();
    }

    @Override
    public void onRequestStart() {
//        shwoProg("二维码数据加载中...");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.payDetailsUI_cancelBtn:

                break;
            case R.id.payDetailsUI_completeBtn:
                queryResult();
                break;
        }

    }

    private void queryResult() {
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.weixin_url);
        Observable<String> observable = helper.postBody(getQueryBody());
        //此处要添加提示信息
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(String s) {

                Log.e("MyTest","======s======"+s);
            }
        });

    }

    public String getQueryBody() {
        String mdt = "mch_id=" + PayContacts.weixin_mch_id + "&" + "nonce_str="
                + dateTime + "&" + "out_trade_no=" + basicBean.BillNo
                + "&" + "service=" + PayContacts.weixin_query_seriver_url + "&"
                + "key=" + PayContacts.weixin_key;
        String s = StringUtils.MD5(mdt);
        String queryString = "<xml>\n" + "<mch_id>" + PayContacts.weixin_mch_id
                + "</mch_id>\n" + "<nonce_str>" + dateTime + "</nonce_str>\n"
                + "<out_trade_no>" + basicBean.BillNo
                + "</out_trade_no>\n" + "<service>"
                + PayContacts.weixin_query_seriver_url + "</service>\n"
                + "<sign><![CDATA[" + s + "]]></sign>\n" + "</xml>";
        return queryString;
    }

    public void showCode(String imgUrl) {
        //构建ImageRequest 实例
        ImageRequest request = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                //给imageView设置图片
                mPayImage.setImageBitmap(response);
            }
        }, mPayImage.getWidth(), mPayImage.getHeight(), Bitmap.Config.RGB_565, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                doFail("405", "加载二维码失败请返回重试", basicBean.function);
            }
        });
        BaseApplication.queue.add(request);

    }

    /**
     * 跳向失败页面
     * @param code
     * @param msg
     * @param function
     */
    public void doFail(String code, String msg, int function) {
//        dismissPro();
        FailBean faliBena = new FailBean();
        faliBena.errorCode = code;
        faliBena.errorMsg = msg;
        switch ( function) {
            case 0:
                break;
            case 1:
                break;
            case 3:
                break;
            case 2:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            default:
                break;
        }
    }

    /**
     * 支付成功时候调用
     * @param function
     */
    public void doSuccess(int function) {
        switch (function) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            default:
                break;
        }

    }


}
