package com.landicorp.android.wofupay.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.adapter.TrainStationAdapter;
import com.landicorp.android.wofupay.bean.Train_City_Bean;
import com.landicorp.android.wofupay.bean.Train_Station;
import com.landicorp.android.wofupay.bean.TrainsStationsBean;
import com.landicorp.android.wofupay.utils.AppUtils;
import com.landicorp.android.wofupay.utils.DefaultThreadPoll;
import com.landicorp.android.wofupay.utils.JLog;
import com.landicorp.android.wofupay.utils.PayContacts;
import com.landicorp.android.wofupay.volley.HttpParams;
import com.landicorp.android.wofupay.volley.RxVolleyHelper;
import org.litepal.crud.DataSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import static com.landicorp.android.wofupay.R.id.btn_search;
import static com.landicorp.android.wofupay.R.id.end_city1;
import static com.landicorp.android.wofupay.R.id.ll_num;
import static com.landicorp.android.wofupay.R.id.start_city1;
import static com.landicorp.android.wofupay.R.id.start_date1;
import static com.landicorp.android.wofupay.R.id.tv_num;
import static com.landicorp.android.wofupay.R.id.tv_tomorrow;
import static com.landicorp.android.wofupay.R.id.tv_yesterday;
import static com.landicorp.android.wofupay.utils.AppUtils.MD5;
import static com.landicorp.android.wofupay.utils.AppUtils.getStringDate;

/**
 * Created by Administrator on 2017/3/22.
 */

public class TrainOneFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private List<Train_City_Bean> list = new ArrayList<Train_City_Bean>();// 车站的集合
    private List<Train_Station.StationBean.TrainStationBean> trainList;
    private ArrayList<Train_Station.StationBean.TrainStationBean> arrayList = new ArrayList<Train_Station.StationBean.TrainStationBean>();
    private Train_Station station;
    private View mInflate;
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView mStart_date1;
    private TextView mStart_city1;
    private TextView mEnd_city1;
    private Button mBtn_search;
    private ListView mTrain_listview;
    private RadioGroup mRg;
    private TextView mTv_num;
    private LinearLayout mLl_num;
    private TextView mTv_yesterday;
    private TextView mTv_today;
    private TextView mTv_tomorrow;
    private ProgressBar mBar;
    private LinearLayout mLl_pro;
    private TextView mTv_pro;
    private Train_City_Bean startBean, endBean;
    private String startDate = "";// 出发日期
    String checkStr = "";
    private TrainStationAdapter mAdapter2;

    public TrainOneFragment() {
    }

    public static TrainOneFragment newInstance(String param1, String param2) {
        TrainOneFragment fragment = new TrainOneFragment();
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
        mInflate = inflater.inflate(R.layout.fragment_train_one, null);
        initView();
        initListener();
        initData();
        //访问网络获取车站信息
        getCityBySql();
        return mInflate;
    }

    private void getCityBySql() {
        DefaultThreadPoll.getThreadPool().addTask(new Runnable() {
            @Override
            public void run() {
                final boolean exist = DataSupport.isExist(Train_City_Bean.class, "name = ?", "北京");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!exist){
                            //不存在就访问网络获取信息
                            getCityData();
                            Toast.makeText(getContext(),"访问网络获取车站信息",Toast.LENGTH_SHORT).show();
                        }else {
                            //TODO 隐藏提示框
                            Toast.makeText(getContext(),"车站信息已经存在",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void getCityData() {
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.URL_YIN);
        HttpParams params = HttpParams.getInstance();
        String data = getStringDate("yyyyMMddHHmmss");

        params.put("urltype", PayContacts.YIN);
        params.put("serveruri", PayContacts.YIN_TRAIN_STATION);
        params.put("terminalID", PayContacts.YIN_TERMINALID);
        params.put("factoryID", PayContacts.YIN_FACTORYID);
        params.put("reqDateTime", data);
        params.put("termTransID", data);
        String sign =
                MD5("terminalID=" + PayContacts.YIN_TERMINALID + "&factoryID="
                        + PayContacts.YIN_FACTORYID + "&reqDateTime=" + data
                        + "&termTransID=" + data + "&key="
                        + PayContacts.YIN_KEY);
        params.put("sign", sign);
        Observable<String> observable = helper.postParams(params.getParams());
        observable.observeOn(Schedulers.io())
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        TrainsStationsBean bean = new Gson().fromJson(s, TrainsStationsBean.class);

                        if (bean.status.equals("0000")){
                            return  true;
                        }else {
                            Toast.makeText(getContext(),"获取车站信息失败，请重新进入",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                }).map(new Func1<String, List<DataSupport>>() {
            @Override
            public List<DataSupport> call(String s) {
                TrainsStationsBean bean = new Gson().fromJson(s, TrainsStationsBean.class);
                ArrayList<DataSupport> list = new ArrayList<>();
                list.addAll(bean.data.list);

                return list;
            }
        }).flatMap(new Func1<List<DataSupport>, Observable<? extends DataSupport>>() {
            @Override
            public Observable<? extends DataSupport> call(List<DataSupport> dataSupports) {
                return Observable.from(dataSupports);
            }
        }).subscribe(new Subscriber<DataSupport>() {
            @Override
            public void onStart() {
                DataSupport.deleteAll(Train_City_Bean.class);
                super.onStart();
            }

            @Override
            public void onCompleted() {
                //TODO 对话框隐藏操作
                if (list.size()==0){
                    Toast.makeText(getContext(),"车站信息获取失败,请重新进入",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //TODO 对话框隐藏操作
                Toast.makeText(getContext(),"车站信息获取失败,请重新进入",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNext(DataSupport dataSupport) {
                if (dataSupport.save()&&dataSupport instanceof Train_City_Bean){
                    list.add((Train_City_Bean) dataSupport);
                    JLog.d("----------=========",list.toString());
                }

            }
        });

    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        mStart_date1.setText(formatter.format(calendar.getTime()));
        startDate = formatter.format(calendar.getTime());
        mAdapter2 = new TrainStationAdapter(this, arrayList);
        mAdapter2.setData(startDate);
        mTrain_listview.setAdapter(mAdapter2);
    }

    private void initListener() {
        mStart_date1.setOnClickListener(this);
        mStart_city1.setOnClickListener(this);
        mEnd_city1.setOnClickListener(this);
        mBtn_search.setOnClickListener(this);

        mTv_yesterday.setOnClickListener(this);
        mTv_today.setOnClickListener(this);
        mTv_tomorrow.setOnClickListener(this);
        mRg.setOnCheckedChangeListener(this);
    }

    private void initView() {
        mStart_date1 = (TextView) mInflate.findViewById(start_date1);
        mStart_city1 = (TextView) mInflate.findViewById(start_city1);
        mEnd_city1 = (TextView) mInflate.findViewById(end_city1);
        mBtn_search = (Button) mInflate.findViewById(btn_search);
        mTrain_listview = (ListView) mInflate.findViewById(R.id.train_listview);
        mRg = (RadioGroup) mInflate.findViewById(R.id.check_group);
        mTv_num = (TextView) mInflate.findViewById(tv_num);
        mLl_num = (LinearLayout) mInflate.findViewById(ll_num);
        mTv_yesterday = (TextView) mInflate.findViewById(tv_yesterday);
        mTv_today = (TextView) mInflate.findViewById(R.id.tv_toady);
        mTv_tomorrow = (TextView) mInflate.findViewById(tv_tomorrow);
        mBar = (ProgressBar) mInflate.findViewById(R.id.progressBar1);
        mLl_pro = (LinearLayout) mInflate.findViewById(R.id.ll_pro);
        mTv_pro = (TextView) mInflate.findViewById(R.id.tv_pro);
        hidePro();
    }

    private void hidePro() {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLl_pro.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_date1:
                //出发时间
                createTime();
                break;
            case R.id.start_city1:
                //出发城市
                TrainCityDialog trainCityDialog = new TrainCityDialog(this.getContext(), new TrainCityDialog.OnItemSelected() {
                    @Override
                    public void onSelectedItem(Train_City_Bean train_City_Bean) {
                        String trim = mEnd_city1.getText().toString()
                                .trim();
                        if (!TextUtils.isEmpty(trim)&&TextUtils.equals(trim,train_City_Bean.name)){
                            AppUtils.showMessage(getContext(),
                                    "出发城市与到达城市一样，请正确选择城市！");
                        }else {
                            mStart_city1.setText(train_City_Bean.name);
                            mStart_city1.setError(null);
                            if (train_City_Bean != null) {
                                startBean = train_City_Bean;
                            }
                        }
                    }
                });
                trainCityDialog.show();
                trainCityDialog.getWindow().setGravity(Gravity.BOTTOM);

                break;
            case R.id.end_city1:
                //到达城市
                TrainCityDialog dialog = new TrainCityDialog(this.getContext(), new TrainCityDialog.OnItemSelected() {
                    @Override
                    public void onSelectedItem(Train_City_Bean train_City_Bean) {
                        String trim = mStart_city1.getText().toString()
                                .trim();
                        if (!TextUtils.isEmpty(trim)&&TextUtils.equals(trim,train_City_Bean.name)){
                            AppUtils.showMessage(getContext(),
                                    "出发城市与到达城市一样，请正确选择城市！");
                        }else {
                            mEnd_city1.setText(train_City_Bean.name);
                            mEnd_city1.setError(null);
                            if (train_City_Bean != null) {
                                endBean = train_City_Bean;
                            }
                        }
                    }
                });
                dialog.show();
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                break;
            case R.id.btn_search:
                //查询车站信息
                // 访问网络查询车次信息
                if (startBean == null) {
                    mStart_city1.setError("请选择始发站");
                    return;
                }
                if (endBean == null) {
                    mStart_city1.setError("请选择到达站");
                    return;
                }

                if (startBean != null && endBean != null) {
                    startDate = mStart_date1.getText().toString().trim();
                    serachTrainList(startDate);
                }
                break;
            case R.id.tv_yesterday://拿到前一天的时间
                break;
            case R.id.tv_toady: //拿到当天的时间
                break;
            case R.id.tv_tomorrow: //拿到后一天的时间
                break;
            default:
                break;
        }
    }

    private void serachTrainList(String godata) {
        RxVolleyHelper helper = new RxVolleyHelper(PayContacts.URL_YIN);
        HttpParams params = HttpParams.getInstance();
        String data = AppUtils.getStringDate("yyyyMMddHHmmss");
        String sign = AppUtils.MD5("terminalID=" + PayContacts.YIN_TERMINALID
                + "&factoryID=" + PayContacts.YIN_FACTORYID + "&reqDateTime="
                + data + "&departStationCode=" + startBean.code
                + "&arriveStationCode=" + endBean.code + "&departDate="
                + godata + "&key=" + PayContacts.YIN_KEY);

        params.put("serveruri", PayContacts.YIN_TRAIN_CITY);
        params.put("terminalID", PayContacts.YIN_TERMINALID);
        params.put("factoryID", PayContacts.YIN_FACTORYID);
        params.put("reqDateTime", data);
        params.put("departStationCode", startBean.code);
        params.put("arriveStationCode", endBean.code);
        params.put("departDate", godata);
        params.put("urltype", PayContacts.YIN);
        params.put("sign", sign);
        JLog.d("===========",params.getParams().toString()+"");
        helper.postParams(params.getParams()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(getContext(),"查询车次信息失败请重试",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String s) {
                station = new Gson().fromJson(s,
                        Train_Station.class);
                JLog.d("---------------","station的集合"+station.toString());
                if (TextUtils.equals("0000", station.status)) {
                    trainList = station.data.trainList;
                    arrayList.addAll(trainList);

                    mLl_num.setVisibility(View.VISIBLE);
                    mTv_num.setText(startDate + "\t"
                            + startBean.name + "-->" + endBean.name
                            + "共有" + arrayList.size() + "条记录");
                    fill_date(startDate, station);
            }
        }
        });
    }

    private void fill_date(final String startDate, final Train_Station station) {
        Observable.from(trainList).subscribeOn(Schedulers.io())
                .filter(new Func1<Train_Station.StationBean.TrainStationBean, Boolean>() {
                    @Override
                    public Boolean call(Train_Station.StationBean.TrainStationBean trainStationBean) {
                        if (TextUtils.isEmpty(checkStr)){
                            return  true;
                        }
                        return checkStr.contains(trainStationBean.trainNo.substring(0,1));
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Train_Station.StationBean.TrainStationBean>() {
            @Override
            public void onStart() {
                arrayList.clear();
                showPro("正在筛选",false);
                super.onStart();
            }

            @Override
            public void onCompleted() {
                //设置数据
                mTv_num.setText(startDate+"\t"+startBean.name+"-->"+endBean.name+"共有"+arrayList.size()+"条记录");
                mAdapter2.setData(startDate);
                mAdapter2.setStation(station);
                mAdapter2.notifyDataSetChanged();
                hidePro();
            }

            @Override
            public void onError(Throwable throwable) {
                showPro("筛选失败,请重试",true);
            }

            @Override
            public void onNext(Train_Station.StationBean.TrainStationBean trainStationBean) {
                arrayList.add(trainStationBean);
            }
        });
    }

    private void showPro(final String msg, final boolean b) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLl_pro.setVisibility(View.VISIBLE);
                if (b){
                    mBar.setVisibility(View.GONE);
                }else {
                    mBar.setVisibility(View.VISIBLE);
                }
                mTv_pro.setText(msg);
            }
        });
    }

    private void createTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String day = dayOfMonth+"";
                String month = (monthOfYear+1)+"";
                if (day.length()<2){
                    day = "0"+day;
                }
                if (month.length()<2){
                    month = "0"+month;
                }
                mStart_date1.setText(year + "-" + month + "-"+day);
                startDate = mStart_date1.getText().toString().trim();
            }
        },year,month,day).show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String check = null;
        switch (checkedId) {
            case R.id.check_G:
                check = "G";
                break;
            case R.id.check_D:

                check = "D";

                break;
            case R.id.check_Z:

                check = "Z";

                break;
            case R.id.check_T:

                check = "T";

                break;
            case R.id.check_K:

                check = "K";

                break;

            case R.id.check_else:
            case R.id.check_all:
                check = "";
                break;
            default:
                break;
        }
        if (check != null && !TextUtils.equals(check, checkStr)) {
            checkStr = check;
            fill_date(startDate, station);
        }

    }

}
