package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;

import com.landicorp.android.wofupay.R;
import com.yanzhenjie.fragment.NoFragment;

/**
 * 游戏充值Fragment
 * Created by Administrator on 2017/3/16 0016.
 */
public class GameRechargeFragment extends NoFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String[] itemsStrings;
    private GridView mGridView;
    private ImageButton mBackBtn;

    public GameRechargeFragment() {
    }

    public static GameRechargeFragment newInstance(String param1, String param2) {
        GameRechargeFragment fragment = new GameRechargeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_recharge, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        itemsStrings = getResources().getStringArray(R.array.tx_games);
        mGridView = (GridView) view.findViewById(R.id.gameUI_gridView);
        mBackBtn = (ImageButton) view.findViewById(R.id.gameUI_backBtn);
        mBackBtn.setOnClickListener(this);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.item_gridview,
                R.id.textView,
                itemsStrings);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gameUI_backBtn:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startFragment(GameDetailFragment.newInstance(position,itemsStrings[position]));
    }

}
