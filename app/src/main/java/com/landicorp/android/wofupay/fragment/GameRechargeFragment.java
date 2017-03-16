package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.landicorp.android.wofupay.R;

/**
 * Created by Administrator on 2017/3/16 0016.
 */
public class GameRechargeFragment extends Fragment implements View.OnClickListener{

    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_recharge, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn = (Button) view.findViewById(R.id.button);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                Snackbar.make(v,"你好，我是Fragment1", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }


}
