package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.landicorp.android.wofupay.R;
import com.yanzhenjie.fragment.NoFragment;


/**
 * Created by Administrator on 2017/3/28.
 */

public class JumpFragment extends NoFragment implements View.OnClickListener {
    private Button card,daikuan,licai ;
    private View mInflate;
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public JumpFragment() {
    }

    public static JumpFragment newInstance(String param1, String param2) {
        JumpFragment fragment = new JumpFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_jump, null);
        initView();
        initListener();
        return mInflate;
    }

    private void initListener() {
        card.setOnClickListener(this);
        daikuan.setOnClickListener(this);
        licai.setOnClickListener(this);
    }

    private void initView() {
        card = (Button) mInflate.findViewById(R.id.card);
        daikuan = (Button) mInflate.findViewById(R.id.daikuan);
        licai = (Button) mInflate.findViewById(R.id.licai);
    }

    @Override
    public void onClick(View v) {
        String url="";
        switch (v.getId()){
            case R.id.card:
                url = "https://ccshop.cib.com.cn:8010/application/cardapp/Fast/TwoBar/viewNew?id=872ca0f231334bef992b86748868bccb";
                break;
            case R.id.daikuan:
                url = "https://www.ysfas.com//appweb/finance.do";
                break;
            case R.id.licai:
                url = "http://wfb.ahwofu.com/app/index.php?i=8&c=home&t=7";
                break;
            default:
                break;
        }
        startFragment(FinanceFragment.newInstance(url,""));
    }
}
