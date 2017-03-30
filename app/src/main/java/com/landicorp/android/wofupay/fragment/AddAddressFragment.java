package com.landicorp.android.wofupay.fragment;

import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.CityInfoModel;
import com.landicorp.android.wofupay.bean.DistrictInfoModel;
import com.landicorp.android.wofupay.bean.GoodsOrderBean;
import com.landicorp.android.wofupay.bean.ProvinceInfoModel;
import com.landicorp.android.wofupay.utils.AddrXmlParser;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.utils.StringUtils;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import com.landicorp.android.wofupay.widget.WheelView;
import com.yanzhenjie.fragment.NoFragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import rx.Observable;
import rx.Subscriber;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 添加收货人信息Fragment
 * Created by Administrator on 2017/3/30 0030.
 */
public class AddAddressFragment extends NoFragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String phoneNum;
    private String mParam2;

    private LinearLayout rootview;
    private EditText address_edit_name;// 收货人姓名
    private EditText address_edit_phone;// 电话
    private TextView address_edit_province;
    private EditText address_deit_addressdetail;// 详细地址
    private Button address_edit_postcode;
    private String name;

    private View contentView;
    private PopupWindow addrPopWindow;
    private WheelView mProvincePicker;
    private WheelView mCityPicker;
    private WheelView mCountyPicker;
    private LinearLayout boxBtnCancel;
    private LinearLayout boxBtnOk;
    protected boolean isDataLoaded = false;
    private boolean isAddrChoosed = false;

    protected ArrayList<String> mProvinceDatas;
    protected Map<String, ArrayList<String>> mCitisDatasMap = new HashMap<String, ArrayList<String>>();
    protected Map<String, ArrayList<String>> mDistrictDatasMap = new HashMap<String, ArrayList<String>>();
    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName;

    private String phone;
    private String prvoince;
    private String detailaddress;

    public AddAddressFragment(){

    }

    public static AddAddressFragment newInstance(String param1, String param2){
        AddAddressFragment fragment = new AddAddressFragment();
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
        return inflater.inflate(R.layout.fragment_add_address, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initProviceSelectView();
    }

    private void initView(View view) {
        rootview=(LinearLayout) view.findViewById(R.id.root_view);
        address_edit_name = (EditText) view.findViewById(R.id.address_edit_name);
        address_edit_phone = (EditText) view.findViewById(R.id.address_edit_phone);
        address_edit_province = (TextView) view.findViewById(R.id.address_edit_province);
        address_deit_addressdetail = (EditText) view.findViewById(R.id.address_edit_addressdetail);
        address_edit_postcode = (Button) view.findViewById(R.id.address_edit_postcode);

        address_edit_province.setOnClickListener(this);
        address_edit_postcode.setOnClickListener(this);
    }

    private void initProviceSelectView() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.addr_picker, null);
        mProvincePicker = (WheelView) contentView.findViewById(R.id.addr_province);
        mCityPicker = (WheelView) contentView.findViewById(R.id.addr_city);
        mCountyPicker = (WheelView) contentView.findViewById(R.id.addr_county);
        boxBtnCancel = (LinearLayout) contentView.findViewById(R.id.box_btn_cancel);
        boxBtnOk = (LinearLayout) contentView.findViewById(R.id.box_btn_ok);

        addrPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // addrPopWindow.setBackgroundDrawable(new ColorDrawable(0xffffff));
        addrPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String provinceText = mProvinceDatas.get(id);
                if (!mCurrentProviceName.equals(provinceText)) {
                    mCurrentProviceName = provinceText;
                    ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                    mCityPicker.resetData(mCityData);
                    mCityPicker.setDefault(0);
                    mCurrentCityName = mCityData.get(0);

                    ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                    mCountyPicker.resetData(mDistrictData);
                    mCountyPicker.setDefault(0);
                    mCurrentDistrictName = mDistrictData.get(0);
                }
            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        mCityPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                String cityText = mCityData.get(id);
                if (!mCurrentCityName.equals(cityText)) {
                    mCurrentCityName = cityText;
                    ArrayList<String> mCountyData = mDistrictDatasMap.get(mCurrentCityName);
                    mCountyPicker.resetData(mCountyData);
                    mCountyPicker.setDefault(0);
                    mCurrentDistrictName = mCountyData.get(0);
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mCountyPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                String districtText = mDistrictData.get(id);
                if (!mCurrentDistrictName.equals(districtText)) {
                    mCurrentDistrictName = districtText;
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        boxBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrPopWindow.dismiss();
            }
        });

        boxBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAddrChoosed = true;
                String addr = mCurrentProviceName + mCurrentCityName;
                if (!mCurrentDistrictName.equals("其他")) {
                    addr += mCurrentDistrictName;
                }else{
                    mCurrentDistrictName = "";
                }
                address_edit_province.setText(addr);
                addrPopWindow.dismiss();
            }
        });

        // 启动线程读取数据
        new Thread() {
            @Override
            public void run() {
                // 读取数据
                isDataLoaded = readAddrDatas();
                // 通知界面线程
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mProvincePicker.setData(mProvinceDatas);
                        mProvincePicker.setDefault(0);
                        mCurrentProviceName = mProvinceDatas.get(0);

                        ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                        mCityPicker.setData(mCityData);
                        mCityPicker.setDefault(0);
                        mCurrentCityName = mCityData.get(0);

                        ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                        mCountyPicker.setData(mDistrictData);
                        mCountyPicker.setDefault(0);
                        mCurrentDistrictName = mDistrictData.get(0);
                    }
                });
            }
        }.start();
    }

    /**
     * 读取地址数据，请使用线程进行调用
     *
     * @return
     */
    protected boolean readAddrDatas() {
        List<ProvinceInfoModel> provinceList = null;
        AssetManager asset = getActivity().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            AddrXmlParser handler = new AddrXmlParser();
            parser.parse(input, handler);
            input.close();
            provinceList = handler.getDataList();
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityInfoModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictInfoModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                }
            }
            mProvinceDatas = new ArrayList<String>();
            for (int i = 0; i < provinceList.size(); i++) {
                mProvinceDatas.add(provinceList.get(i).getName());
                List<CityInfoModel> cityList = provinceList.get(i).getCityList();
                ArrayList<String> cityNames = new ArrayList<String>();
                for (int j = 0; j < cityList.size(); j++) {
                    cityNames.add(cityList.get(j).getName());
                    List<DistrictInfoModel> districtList = cityList.get(j).getDistrictList();
                    ArrayList<String> distrinctNameArray = new ArrayList<String>();
                    DistrictInfoModel[] distrinctArray = new DistrictInfoModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        DistrictInfoModel districtModel = new DistrictInfoModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray.add(districtModel.getName());
                    }
                    mDistrictDatasMap.put(cityNames.get(j), distrinctNameArray);
                }
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.address_edit_province:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                if (isDataLoaded)
                    addrPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
                else
                    //toast("加载数据失败，请稍候再试！");
                    break;

                break;
            case R.id.address_edit_postcode:
                name = address_edit_name.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    address_edit_name.setError("请输入收货人姓名");
                    return;
                }
                phone = address_edit_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    address_edit_phone.setError("请输入收货人手机号");
                    return;
                }
                if(!StringUtils.isPhoneNumber(phone)){
                    address_edit_phone.setError("手机号码不合法");
                    return;
                }

                prvoince = address_edit_province.getText().toString().trim();
                if (TextUtils.isEmpty(prvoince)) {
                    address_edit_province.setError("请选择收货地址");
                    return;
                }

                detailaddress = address_deit_addressdetail.getText().toString().trim();
                if (TextUtils.isEmpty(detailaddress)) {
                    address_deit_addressdetail.setError("请输入详细地址");
                    return;
                }
                //同时把填写的信息发送给服务器
                sendAddressMessage();
                break;
        }
    }

    private void sendAddressMessage() {
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.SEARCH_address);
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "InsertShopAddress");
        params.put("Phone", phoneNum);
        params.put("LinkName", name);
        params.put("LinkPhone", phone);
        params.put("Province", mCurrentProviceName);
        params.put("City", mCurrentCityName);
        params.put("County", mCurrentDistrictName);
        params.put("Address", detailaddress);
        Observable<String> observable = helper.postParams(params );
        observable.subscribe(new Subscriber<Object>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "地址信息提交失败,是否重试?", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Object t) {
                GoodsOrderBean bean = new Gson().fromJson((String) t, GoodsOrderBean.class);
                if (bean.code == 0000) {
                    Toast.makeText(getContext(), "地址添加成功", Toast.LENGTH_SHORT).show();
                }else if (bean.code == 0001) {
                    Toast.makeText(getContext(), "地址添加失败", Toast.LENGTH_SHORT).show();
                }else if (bean.code == 0002) {
                    Toast.makeText(getContext(), "地址已经存在,不要重复添加", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "地址添加失败", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }
}
