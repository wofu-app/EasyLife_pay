package com.landicorp.android.wofupay.fragment;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.adapter.AreaAdaptr;
import com.landicorp.android.wofupay.bean.Areabean;
import com.landicorp.android.wofupay.bean.ElecPayBean;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.JLog;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.yanzhenjie.fragment.NoFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;



/**
 * Created by Administrator on 2017/3/21.
 */

public class ElectricFragment extends NoFragment implements   AdapterView.OnItemSelectedListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Areabean> provincelist = new ArrayList();
    private List<Areabean> citylist = new ArrayList();
    private List<Areabean> districtlist = new ArrayList();
    private int prevousProvincePosition = -1; // 用来记录省份前一个选中的位置
    private int prevousCityPosition = -1; // 用来记录城市前一个选中的位置
    private int prevousDistPosition = -1; // 用来记录地区前一个选中的位置
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private EditText mEdit_account;
    private EditText mEdit_phoneNumber;
    private EditText mEdit_money;
    private Spinner mSpinner_province;
    private Spinner mSpinner_city;
    private Spinner mSpinner_distance;
    private ImageButton mBt_entry;
    private File mFile;
    private static final String TABLE_PROVINCE = "province";
    private static final String TABLE_CITY = "city";
    private static final String TABLE_DISTRICT = "district";
    private AreaAdaptr mAreaAdaptr;
    private AreaAdaptr mCityAdapter;
    private AreaAdaptr mDisAdapter;
    private String provice = "";
    private String city = "";
    private String dis = "";
    private ImageButton mBtn_cancle;
    private String mAccount;
    private String mAmount;
    private String mPhone_number;

    public ElectricFragment() {
    }

    public static ElectricFragment newInstance(String param1, String param2) {
        ElectricFragment fragment = new ElectricFragment();
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
        mInflate = inflater.inflate(R.layout.fragment_electric, null);
        initView();
        initListener();
        try {
            initDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
        readProvinceDate();//得到省份信息
        mAreaAdaptr = new AreaAdaptr(this.getContext(), provincelist);
        mSpinner_province.setAdapter(mAreaAdaptr);
        return mInflate;
    }

    private void readProvinceDate() {
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(mFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = sqLiteDatabase.query(TABLE_PROVINCE, new String[]{"id", "code", "name"}, null, null, null, null, null);
        while (cursor.moveToNext()){
            Areabean areabean = new Areabean();
            areabean.id = cursor.getInt(0);
            areabean.code = cursor.getInt(1);
            byte[] blob = cursor.getBlob(2);
            try {
                String name = new String(blob, "utf-8");
                areabean.name = name;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            provincelist.add(areabean);

        }
        cursor.close();
        sqLiteDatabase.close();
    }

    private void initDB()throws IOException {
        AssetManager assets = getActivity().getAssets();

            InputStream inputStream = assets.open("city2.db");
            mFile = new File(getActivity().getFilesDir(), "city2.db");
        if (mFile.exists()){
            return;
        }else {
            FileOutputStream fos = new FileOutputStream(mFile);
            byte[] bytes = new byte[1024 * 10];
            int len ;
            while ((len = inputStream.read(bytes))!=-1){
                fos.write(bytes,0,len);
            }
            inputStream.close();
            fos.close();
        }

    }

    private void initListener() {
        mSpinner_province.setOnItemSelectedListener(this);
        mSpinner_city.setOnItemSelectedListener(this);
        mSpinner_distance.setOnItemSelectedListener(this);
        mBtn_cancle.setOnClickListener(this);
        mBt_entry.setOnClickListener(this);
    }

    private void initView() {
        mEdit_account = (EditText) mInflate.findViewById(R.id.account);
        mEdit_phoneNumber = (EditText) mInflate.findViewById(R.id.phoneNumber);
        mEdit_money = (EditText) mInflate.findViewById(R.id.account_money);
        mSpinner_province = (Spinner) mInflate.findViewById(R.id.province_spinner);
        mSpinner_city = (Spinner) mInflate.findViewById(R.id.city_spinner);
        mSpinner_distance = (Spinner) mInflate.findViewById(R.id.distance_spinner);
        mBt_entry = (ImageButton) mInflate.findViewById(R.id.bt_entry);
        mBtn_cancle = (ImageButton) mInflate.findViewById(R.id.bt_cancle);
    }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.province_spinner:
                Areabean areabean = (Areabean) parent.getItemAtPosition(position);
                //将前一个选中的位置设置为未选中
                if (prevousProvincePosition>-1){
                    provincelist.get(prevousProvincePosition).isChecked = false;
                }
                prevousProvincePosition = position;
                if (!areabean.isChecked){
                    areabean.isChecked = true;
                    mAreaAdaptr.notifyDataSetChanged();
                }
                //初始化掉城市的选中位置
                updataCityListViewByPcode(areabean);
                prevousCityPosition = -1;
                provice = areabean.name;
                //清空地区的数据
                if (mDisAdapter != null) {

                    districtlist.clear();
                    mDisAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.city_spinner:
                Areabean citybean = (Areabean) parent.getItemAtPosition(position);
                // 将前一个选中的设置为未选中
                if (prevousCityPosition > -1) {
                    citylist.get(prevousCityPosition).isChecked = false;
                }
                prevousCityPosition = position;
                if (!citybean.isChecked) {
                    citybean.isChecked = true;
                    mCityAdapter.notifyDataSetChanged();
                }
                // 根据城市显示相应的地区
                // 初始化掉地区的选中位置
                updataDisByPcode(citybean);
                prevousDistPosition = -1;
                city = citybean.name;
                break;
            case R.id.distance_spinner:
                Areabean disBean = (Areabean) parent.getItemAtPosition(position);
                if (prevousDistPosition > -1) {
                    districtlist.get(prevousDistPosition).isChecked = false;
                }
                prevousDistPosition = position;
                if (!disBean.isChecked) {
                    disBean.isChecked = true;
                    mDisAdapter.notifyDataSetChanged();
                }
                dis = disBean.name;
                dis = districtlist.get(position).name;
                break;
            default:
                break;
        }
    }

    private void updataDisByPcode(Areabean citybean) {
        // 清空集合
        districtlist.clear();
        readDisListByPcode(citybean);
        if (mDisAdapter == null) {

            mDisAdapter = new AreaAdaptr(this.getContext(), districtlist);
            mSpinner_distance.setAdapter(mDisAdapter);

        } else {
            mDisAdapter.notifyDataSetChanged();
        }
         dis = districtlist.get(0).name;
    }

    private void readDisListByPcode(Areabean citybean) {
        readAreaByPcde(citybean, districtlist, TABLE_DISTRICT);
    }

    private void updataCityListViewByPcode(Areabean areabean) {
        //清空集合
        citylist.clear();
        //读取城市数据
        readCityListByPcode(areabean);
        if (mCityAdapter== null){
            mCityAdapter = new AreaAdaptr(getContext(), citylist);
            mSpinner_city.setAdapter(mCityAdapter);
        }else {
            mCityAdapter.notifyDataSetChanged();
        }

    }

    private void readCityListByPcode(Areabean areabean) {
        readAreaByPcde(areabean, citylist, TABLE_CITY);
    }

    private void readAreaByPcde(Areabean areabean, List<Areabean> citylist, String TABLE_CITY) {
        citylist.clear();
        SQLiteDatabase database = SQLiteDatabase.openDatabase(
                mFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        Cursor query = database.query(TABLE_CITY, new String[] { "id", "code",
                        "name" }, "pcode =" + "?", new String[] { areabean.code + "" },
                null, null, null);
        while (query.moveToNext()) {
            Areabean citybean = new Areabean();
            citybean.id = query.getInt(0);
            citybean.code = query.getInt(1);
            byte[] blob = query.getBlob(2);
            try {
                String name = new String(blob, "utf-8");
                citybean.name = name;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            citylist.add(citybean);
        }
        database.close();
        query.close();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_cancle:
                //隐藏当前的fragment
                finish();
                break;

            case R.id.bt_entry:
                //获取billNo
                getBIllNo();
                break;
            default:
                break;
        }
    }

    private void getBIllNo() {
        initBillNo();
        if (TextUtils.isEmpty(mAccount)) {
            mEdit_account.setError("请输入户号!");
            return;
        }
        if (TextUtils.isEmpty(mAmount)) {
            mEdit_money.setError("请输入缴费金额");
            return;
        }
        if (TextUtils.isEmpty(mPhone_number)||!AppUtils.isPhoneNumber(mPhone_number)) {
            mEdit_phoneNumber.setError("请输入正确手机号!");
            return;
        }
        startGetBillNo();
    }

    private void startGetBillNo() {
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.life_recharge);
        HashMap<String, String> params = new HashMap<>();
        params.put("action", "GetBillPayNo");
        params.put("rechargeAccount", mAccount);
        //TODO 终端号
        params.put("terminalcode","04962482");
      //  params.put("terminalcode", DeviceUtils.getDevicePort());
        params.put("mobile", mPhone_number);
        params.put("amount", mAmount + ".0");
        params.put("BN_type", "elec");
        params.put("BN_memo", "电费");
        String p = provice.substring(0,provice.length()-1);
        String c = city.substring(0,city.length()-1);
        params.put("region", p+c);
        String d = dis.substring(0,dis.length()-1);
        params.put("org", d);
        helper.postParams(params).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(getContext(),"获取账单失败",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNext(String s) {
                ElecPayBean bean = new ElecPayBean();
                bean.BillNo = s;
                bean.function = 6;
                bean.payAmount = String.valueOf(Integer.parseInt(mAccount)*100);
                bean.phoneNumber = mPhone_number;
                bean.cardId = mAccount;
                bean.type = "1";//电费
                //TODO 进入支付界面
                startFragment(PayTypeFragment.newInstance(bean,null,bean.payAmount));
            }
        });
//        Observable<String> observable = helper.postParams(params);
//        JLog.d("------------",params.toString());
//        observable.subscribe(new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                Toast.makeText(getContext(),"获取账单失败",Toast.LENGTH_SHORT).show();
//                JLog.d("----------失败的原因",throwable.toString()+"");
//            }
//
//            @Override
//            public void onNext(String s) {
//                ElecPayBean bean = new ElecPayBean();
//                bean.BillNo = s;
//                bean.function = 6;
//                bean.payAmount = String.valueOf(Integer.parseInt(mAccount)*100);
//                bean.phoneNumber = mPhone_number;
//                bean.cardId = mAccount;
//                bean.type = "1";//电费
//                //TODO 进入支付界面
//                startFragment(PayTypeFragment.newInstance(bean,null,bean.payAmount));
//            }
//        });
    }

    private void initBillNo() {
        mAccount = mEdit_account.getText().toString().trim();
        mAmount = mEdit_money.getText().toString().trim();
        mPhone_number = mEdit_phoneNumber.getText().toString().trim();

    }
}
