package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.landicorp.android.wofupay.R;
import com.yanzhenjie.fragment.NoFragment;

/**
 * Created by Administrator on 2017/3/29.
 */

public class TrainMessageFragment extends NoFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public TrainMessageFragment() {
    }

    public static TrainMessageFragment newInstance(String param1, String param2) {
        TrainMessageFragment fragment = new TrainMessageFragment();
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
        View inflate = inflater.inflate(R.layout.fragment_train_message, null);
        return inflate;
    }
}
