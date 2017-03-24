package com.landicorp.android.wofupay.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.model.FileBean;
import com.landicorp.android.wofupay.model.FunctionBean;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.widget.CustomLinerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */

public class MainFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private List<Integer> functions;
    private CustomLinerLayout mCusLy;
    private int[] ids = { R.mipmap.buscard, R.mipmap.yecx, R.mipmap.gay,
            R.mipmap.phone, R.mipmap.caipiao, R.mipmap.game,
            R.mipmap.zhunong, R.mipmap.water,R.mipmap.train_name,
            R.mipmap.wofu_bank, R.mipmap.gongyi,R.mipmap.market,};

    public MainFragment() {
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, null);
        mCusLy = (CustomLinerLayout) view.findViewById(R.id.mainFragUI_cusLy);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showView(getDatas());
    }

    public List<Integer> getDatas() {
        FunctionBean readFromFile = AppUtils.readFromFile(FileBean.FUNCTION,
                FileBean.FUNCTIONNANE);
        AppUtils.upFucntion(readFromFile);

        functions = new ArrayList<Integer>();
        functions.clear();

        for (Integer id : ids) {
            if (Build.VERSION.SDK_INT < 15) {
                if (id == R.mipmap.yecx || id == R.mipmap.zhunong) {
                    continue;
                }
            }

            int function = getFunction(id);
            int functionState = AppUtils.getFunctionState(function);
            if (functionState == 2) {
                continue;
            }

            functions.add(id);
        }

        if (PayContacts.isDebug) {
            functions.add(R.mipmap.violation);
        }

        return functions;
    }
    /**
     * 根据id获取按钮状态
     *
     * @param buttonID
     *            id 功能id 1:公交卡充值 2.cpu卡充值 3.pboc卡充值 4.余额查询 5.加油卡充值 6.手机充值
     *            7.话费充值 8.流量充值 9.游戏充值 10.彩票 11福彩 12.体彩 13.助农取款 14.公益 15.水电煤
     */
    public int getFunction(int buttonID) {
        int id = -1;
        switch (buttonID) {
            // 公交卡充值
            case R.mipmap.buscard:
                id = 1;
                break;
            case R.mipmap.yecx:
                id = 4;
                break;
            // 加油卡充值
            case R.mipmap.gay:
                id = 5;
                break;
            // 手机充值
            case R.mipmap.phone:
                id = 6;
                break;
            // 游戏充值
            case R.mipmap.game:
                id = 9;
                break;
            // 彩票
            case R.mipmap.caipiao:
                id = 10;
                break;

            case R.mipmap.zhunong:// 助农
                id = 13;
                break;

            // 公益
            case R.mipmap.gongyi:
                id = 14;
                break;
            // 水电煤
            case R.mipmap.water:
                id = 15;
                break;

            default:
                id = 100;
                break;
        }
        return id;
    }

    public void showView(List<Integer> ids) {
        mCusLy.removeAllViews();
        for (int i = 0; i < ids.size(); i++) {
            ImageButton imageButton = null;
            Integer id = ids.get(i);
            if (id == R.mipmap.buscard) {
                imageButton = (ImageButton) getActivity().getLayoutInflater().inflate(
                        R.layout.main_big_item, mCusLy, false);
            } else {
                imageButton = (ImageButton) getActivity().getLayoutInflater().inflate(
                        R.layout.main_small_item, mCusLy, false);
            }
            imageButton.setImageResource(id);
            imageButton.setTag(id);
            imageButton.setOnClickListener(this);
            mCusLy.addChild(imageButton);
        }
    }

    @Override
    public void onClick(View v) {
        int tag = (Integer) v.getTag();
        switch (tag) {
            // 公交卡充值
            case R.mipmap.buscard:
                switchContent(this,GameRechargeFragment.newInstance("",""));
                break;
            case R.mipmap.phone:
                switchContent(this,PhoneRechargeFragment.newInstance("",""));
                break;
            case R.mipmap.game:
                switchContent(this,GameRechargeFragment.newInstance("",""));
                break;
            case R.mipmap.gay://油卡充值
                switchContent(this,GasRechargeFragment.newInstance("",""));
                break;
            case R.mipmap.water://水电煤选择界面
                switchContent(this,ChooseButtonFragment.newInstance("",""));
                break;
            case R.mipmap.train_name://火车票界面
                switchContent(this,TrainOneFragment.newInstance("",""));
                break;
            default:
                break;

        }
    }

}
