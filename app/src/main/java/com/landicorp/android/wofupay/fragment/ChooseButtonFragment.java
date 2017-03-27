package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.landicorp.android.wofupay.R;
import com.yanzhenjie.fragment.NoFragment;


/**
 * Created by Administrator on 2017/3/20.
 */

public class ChooseButtonFragment extends NoFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private RadioGroup mRg;
    private RadioButton mBtn_jump_electric;
    private RadioButton mBtn_jump_water;
    private RadioButton mBtn_jump_gas;
    private int id;
    private ImageButton mCancle;
    private ImageButton mBt_entry;

    public ChooseButtonFragment() {
    }

    public static ChooseButtonFragment newInstance(String param1, String param2) {
        ChooseButtonFragment fragment = new ChooseButtonFragment();
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
        mInflate = inflater.inflate(R.layout.fragment_choose_button, null);
        initView();
        initListener();
        return mInflate;
    }

    private void initListener() {
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                id = checkedId;
            }
        });
        mBtn_jump_electric.setChecked(true);
        mCancle.setOnClickListener(this);
        mBt_entry.setOnClickListener(this);
    }

    private void initView() {
        mRg = (RadioGroup) mInflate.findViewById(R.id.rg);
        mBtn_jump_electric = (RadioButton) mInflate.findViewById(R.id.btn_jump_electric);
        mBtn_jump_water = (RadioButton) mInflate.findViewById(R.id.btn_jump_water);
        mBtn_jump_gas = (RadioButton) mInflate.findViewById(R.id.btn_jump_gas);
        mCancle = (ImageButton) mInflate.findViewById(R.id.bt_cancle);
        mBt_entry = (ImageButton) mInflate.findViewById(R.id.bt_entry);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_cancle:
                //返回主fragment
                finish();
                break;
            case R.id.bt_entry:
                switch (id){
                    case R.id.btn_jump_electric:
                        //进入电费充值界面
                        startFragment(ElectricFragment.newInstance("",""));
                        break;
                    case R.id.btn_jump_water:
                        //进入水费充值界面
                        startFragment(WaterFragment.newInstance("",""));
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
