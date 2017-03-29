package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.landicorp.android.wofupay.R;
import com.yanzhenjie.fragment.NoFragment;

/**
 * 选择和添加地址Fragment
 * Created by Administrator on 2017/3/29 0029.
 */
public class SelectAddressFragment extends NoFragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String phoneNum;
    private String mParam2;

    public SelectAddressFragment() {
    }

    public static SelectAddressFragment newInstance(String param1, String param2) {
        SelectAddressFragment fragment = new SelectAddressFragment();
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
            phoneNum = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goods_infor, null);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {


    }

}
