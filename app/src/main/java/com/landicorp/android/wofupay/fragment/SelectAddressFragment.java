package com.landicorp.android.wofupay.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.adapter.SelectAddAdapter;
import com.landicorp.android.wofupay.bean.DetailBean;
import com.landicorp.android.wofupay.bean.SearchAddressBean;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.yanzhenjie.fragment.NoFragment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * 选择和添加地址Fragment
 * Created by Administrator on 2017/3/29 0029.
 */
public class SelectAddressFragment extends NoFragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String phoneNum;
    private String mParam2;

    private Button mOkBtn;
    private ImageButton mAddBtn;

    private ListView mListView;
    private SelectAddAdapter mAdapter;
    private List<DetailBean> detailBean;

    private List<SearchAddressBean.AddressInfo> addInfoData;

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
        return inflater.inflate(R.layout.fragment_select_address, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        httpData();
    }

    private void initView(View view) {
        mOkBtn = (Button) view.findViewById(R.id.selectUI_okBtn);
        mOkBtn.setOnClickListener(this);
        mAddBtn = (ImageButton) view.findViewById(R.id.selectUI_addBtn);
        mAddBtn.setOnClickListener(this);
        mListView = (ListView) view.findViewById(R.id.selectUI_listView);

    }

    @Override
    public void onResume() {
        super.onResume();
        httpData();
    }

    private void httpData() {
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.SEARCH_address);
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "GetShopAddress");
        params.put("Phone", phoneNum);
        Observable<String> observable = helper.postParams(params );

        observable.subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "获取地址列表失败,是否重试?", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Object t) {
                Log.e("MyTest0330","======联系人列表======="+t.toString());

                SearchAddressBean bean = new Gson().fromJson(t.toString(), SearchAddressBean.class);

                if (bean.code.equals("000")) {
                    addInfoData = bean.Data;
                    Log.d("获取到的联系人列表", bean.toString());
                    mAdapter = new SelectAddAdapter(getActivity(), addInfoData);
                    mListView.setAdapter(mAdapter);
                }else if (bean.code.equals("001")) {
                    Toast.makeText(getContext(), "该号码第一次使用本系统,请添加收货地址信息", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "获取地址列表失败,是否重试?", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectUI_okBtn:
                detailBean = mAdapter.getDetailDatas();
                if (detailBean.size()>1) {
                    Toast.makeText(getContext(), "只能选择一个收货地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (detailBean.size() == 0) {
                    Toast.makeText(getContext(), "请添加或选择地址", Toast.LENGTH_SHORT).show();
                    return;
                }

                //返回前一个界面 ,并把数据传递过去
                Bundle bundle = new Bundle();
                bundle.putSerializable("AddressList", (Serializable) detailBean);
                bundle.putString("phoneNum", phoneNum);
                setResult(RESULT_OK,bundle);
                finish();
                break;
            case R.id.selectUI_addBtn:
                //进入编辑地址界面
                startFragment(AddAddressFragment.newInstance(phoneNum, ""));
                break;
        }
    }
}
