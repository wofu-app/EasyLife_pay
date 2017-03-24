package com.landicorp.android.wofupay.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.landicorp.android.eptapi.utils.Log;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.base.BaseDialog;
import com.landicorp.android.wofupay.bean.Train_City_Bean;
import com.landicorp.android.wofupay.utils.AppUtils;

import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.landicorp.android.wofupay.R.id.btn_search;
import static com.landicorp.android.wofupay.R.id.city_one;
import static com.landicorp.android.wofupay.R.id.city_two;
import static com.landicorp.android.wofupay.R.id.mudidi;
import static com.landicorp.android.wofupay.R.id.tv_pro;

/**
 * Created by Administrator on 2017/3/22.
 */

public class TrainCityDialog extends BaseDialog implements View.OnClickListener {

    private MainAdapter mAdapter;
    private final OnItemSelected onItemSelected;
    private EditText mMudidi;
    private TextView mCity_one;
    private TextView mCity_two;
    private Button mBtn_search;
    private TextView mTv_pro;
    private LinearLayout mLl;
    private GridView mGridview;
    private List<Train_City_Bean> hotlist = new ArrayList<Train_City_Bean>();
    private List<Train_City_Bean> list = new ArrayList<Train_City_Bean>();// 传入的车站的集合
    private List<Train_City_Bean> list2 = new ArrayList<Train_City_Bean>();
    public TrainCityDialog(Context context, OnItemSelected onItemSelected) {
        super(context);
        this.onItemSelected = onItemSelected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_city_dialog);
        initView();
        initListener();
    }

    private void initListener() {
        mBtn_search.setOnClickListener(this);
        mCity_one.setOnClickListener(this);
        mCity_two.setOnClickListener(this);
        mGridview = (GridView) findViewById(R.id.grid_view);
        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrainCityDialog.this.dismiss();
                if (onItemSelected!=null){
                    onItemSelected.onSelectedItem(hotlist.get(position));
                }
            }
        });
        //设置数据
        setData();

    }

    private void setData() {
        hotlist.clear();
        ArrayList<String> listhot = new ArrayList<>();
        listhot.add("北京");
        listhot.add("上海");
        listhot.add("天津");
        listhot.add("西安");
        listhot.add("深圳");
        listhot.add("重庆");
        listhot.add("武汉");
        listhot.add("广州");
        listhot.add("苏州");
        listhot.add("成都");
        listhot.add("杭州");
        listhot.add("济南");
        listhot.add("南京");
        listhot.add("郑州");
        listhot.add("长春");
        listhot.add("哈尔滨");
        listhot.add("东莞");
        listhot.add("长沙");
        listhot.add("宁波");
        listhot.add("兰州");
        listhot.add("佛山");
        listhot.add("大连");
        listhot.add("无锡");
        listhot.add("沈阳");
        listhot.add("温州");
        listhot.add("青岛");
        listhot.add("保定");
        listhot.add("石家庄");
        listhot.add("南昌");
        listhot.add("合肥");
        listhot.add("福州");
        listhot.add("太原");
        listhot.add("徐州");
        listhot.add("南宁");
        listhot.add("乌鲁木齐");
        Observable.from(listhot)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<String, List<Train_City_Bean>> () {
                    @Override
                    public List<Train_City_Bean> call(String s) {
                        return DataSupport.where("name = ?",s).find(Train_City_Bean.class);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Train_City_Bean>>() {



                    @Override
                    public void onStart() {
                        list.clear();
                        mLl.setVisibility(View.VISIBLE);
                        mTv_pro.setText("热门城市搜索中...");
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        hotlist.clear();
                        hotlist.addAll(list);
                        //Toast.makeText(getContext(),hotlist.toString()+"",Toast.LENGTH_SHORT).show();
                        mAdapter = new MainAdapter();
                        mGridview.setAdapter(mAdapter);
                        mLl.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mLl.setVisibility(View.VISIBLE);
                        mTv_pro.setText("数据加载失败,请返回重试");
                    }

                    @Override
                    public void onNext(List<Train_City_Bean> s) {

                        list.addAll(s);
                        Toast.makeText(getContext(),list.toString()+"list的集合",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initView() {
        mMudidi = (EditText) findViewById(mudidi);
        mCity_one = (TextView) findViewById(city_one);
        mCity_two = (TextView) findViewById(city_two);
        mBtn_search = (Button) findViewById(btn_search);
        mTv_pro = (TextView) findViewById(tv_pro);
        mLl = (LinearLayout) findViewById(R.id.ll_pr);
        mTv_pro.setText("热门城市加载中");
        mLl.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.city_one:
                hotlist.clear();
                mCity_one.setBackgroundColor(Color.parseColor("#2d9fe8"));
                mCity_two.setBackgroundColor(Color.parseColor("#949495"));
                if (hotlist.size()==0){
                    setData();
                }else {
                    hotlist.addAll(list);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.city_two:
                mCity_one.setBackgroundColor(Color.parseColor("#949495"));
                mCity_two.setBackgroundColor(Color.parseColor("#2d9fe8"));
                hotlist.clear();
                hotlist.addAll(list2);
                mAdapter.notifyDataSetChanged();
            case R.id.btn_search:
                if (mMudidi.getText().toString().trim().equals("")){
                    AppUtils.showMessage(this.getContext(),"请输入查询条件");
                }else {
                    InputMethodManager imm = (InputMethodManager) getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mBtn_search.getWindowToken(), 0); // 强制隐藏键盘

                    mCity_one.setBackgroundColor(Color.parseColor("#949495"));
                    mCity_two.setBackgroundColor(Color.parseColor("#2d9fe8"));
                    list2.clear();
                    searchData(mMudidi.getText()
                            .toString().trim());
                }
            default:
                break;
        }
    }
    private Subscriber<List<Train_City_Bean>> search;
    private void searchData(String msg) {
        search=new Subscriber<List<Train_City_Bean>>() {
            @Override
            public void onStart() {
                list2.clear();
                mLl.setVisibility(View.VISIBLE);
                mTv_pro.setText("城市搜索中..");
            };

            @Override
            public void onNext(List<Train_City_Bean> t) {
                list2.addAll(t);
                mTv_pro.setText("城市搜索中..("+list2.size()+")");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mLl.setVisibility(View.VISIBLE);
                mTv_pro.setText("数据加载失败,请返回重试!");
            }

            @Override
            public void onCompleted() {
                hotlist.clear();
                if (list2.size()==0) {
                    mLl.setVisibility(View.VISIBLE);
                    mTv_pro.setText("没有找到对应的城市,核对好首字母后重新查询");
                }else {
                    hotlist.addAll(list2);
                    mAdapter = new MainAdapter();
                    mGridview.setAdapter(mAdapter);
                    mLl.setVisibility(View.GONE);
                }
            }
        };
        Observable.just(msg).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io())
                .map(new Func1<String, List<Train_City_Bean>>() {

                    @Override
                    public List<Train_City_Bean> call(String t) {
                        // 中文内容也匹配
                        List<Train_City_Bean> find;
                        String pstr = "[\u4e00-\u9fa5]";
                        if (Pattern.compile(pstr)
                                .matcher(t).find()) {
                            find = DataSupport.where("name like ?",
                                    "%" + t + "%").find(Train_City_Bean.class);
                        } else {
                            find = DataSupport.where("shortSpell like ?",
                                    "%" + t + "%").find(Train_City_Bean.class);
                        }


                        return find;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(search);



    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            resertTimer();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }
    };

    public interface OnItemSelected{
        public void onSelectedItem(Train_City_Bean train_City_Bean);
    }
    class MainAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return hotlist.size();
        }

        @Override
        public Object getItem(int position) {
            return hotlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(getContext()).inflate(
                    R.layout.y_tickets_city_selectcity_gridview_item, null);
            TextView textView = (TextView) view
                    .findViewById(R.id.y_tickets_gridview_text);
            textView.setText(hotlist.get(position).name);
            return view;

        }
    }
}
