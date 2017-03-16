package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.landicorp.android.wofupay.R;

/**
 * Created by Administrator on 2017/3/16.
 */

public class PhoneRechargeFragment extends Fragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_phone_rechargement, null);
        initView();
        Toast.makeText(getContext(),"---------",Toast.LENGTH_SHORT).show();
        return mView;
    }

    private void initView() {

    }

}
