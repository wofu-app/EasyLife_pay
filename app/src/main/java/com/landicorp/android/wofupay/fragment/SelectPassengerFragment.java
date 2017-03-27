package com.landicorp.android.wofupay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.adapter.SelectPassengerAdapter;
import com.landicorp.android.wofupay.bean.Passenger;
import com.landicorp.android.wofupay.bean.SelectPassenger;
import com.landicorp.android.wofupay.utils.JLog;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/3/24.
 */

public class SelectPassengerFragment extends BaseFragment implements View.OnClickListener {

    private View mInflate;
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView mListview;
    private boolean regrist;
    private int idNum;
    private String pidname;
    private List<Passenger> listpassenger = new ArrayList<Passenger>();
    private Button mBt_back;
    private Button mBt_ok;
    private ImageView mIv_add;
    private SelectPassengerAdapter mAdapter;

    public SelectPassengerFragment() {
    }

    public static SelectPassengerFragment newInstance(String param1, String param2) {
        SelectPassengerFragment fragment = new SelectPassengerFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1); //传过来的电话号码
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_select_passenger, null);
       // Toast.makeText(getContext(),mParam1,Toast.LENGTH_SHORT).show();
        initView();
        initListener();
        return mInflate;
    }

    private void initListener() {
        mBt_back.setOnClickListener(this);
        mBt_ok.setOnClickListener(this);
        mIv_add.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        getPassenger();
        super.onResume();
    }

    private void getPassenger() {
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.base_url + "TrainTickets/TrainTickets.ashx");
        final HashMap<String, String> params = new HashMap<>();
        params.put("action", "GetContactsList");
        params.put("Phone", mParam1);
        helper.postParams(params).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(getContext(),"获取联系人失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String s) {
                SelectPassenger passenger = new Gson().fromJson(s, SelectPassenger.class);

                if (passenger.code.equals("000")){
                    List<SelectPassenger.SelectedPassenger> list = passenger.Data;
                    regrist = false;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).PId == 0){
                            idNum = list.get(i).Id;
                            pidname = list.get(i).UserName;
                        }
                    }
                    //设置adapter
                    mAdapter = new SelectPassengerAdapter(getContext(), list,listpassenger);
                    mListview.setAdapter(mAdapter);
                    JLog.d("--------------","联系人"+list.toString());
                }
            }
        });
    }

    private void initView() {
        mListview = (ListView) mInflate.findViewById(R.id.select_list);
        mBt_back = (Button) mInflate.findViewById(R.id.btn_select_back);
        mBt_ok = (Button) mInflate.findViewById(R.id.btn_select_ok);
        mIv_add = (ImageView) mInflate.findViewById(R.id.iv_select_addpassenger);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_select_back:
                //返回前一个火车票界面
                getFragmentManager().beginTransaction().hide(this).commit();
                break;
            case R.id.btn_select_ok:
                //ok按钮
                List<Passenger> list = mAdapter.getList();
                if (list.size()>5){
                    Toast.makeText(getContext(),"最多添加5个乘车人",Toast.LENGTH_SHORT).show();
                }else {
                    //返回前一个界面 ,并把数据传递过去

                }
                break;
            case R.id.iv_select_addpassenger:
                //进入编辑联系人界面
                switchContent(this,EditPassenegrFragment.newInstance(idNum+"",""));
                break;
            default:
                break;
        }
    }
}
