package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.ikidou.fragmentBackHandler.BackHandledFragment;
import com.landicorp.android.wofupay.R;

/**
 * Created by Administrator on 2017/3/16 0016.
 */
public class GameRechargeFragment extends BackHandledFragment implements View.OnClickListener {

    private Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_recharge, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

}
