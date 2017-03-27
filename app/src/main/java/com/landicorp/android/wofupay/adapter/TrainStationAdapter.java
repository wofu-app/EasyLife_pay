package com.landicorp.android.wofupay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.TrainMeaasgeBean;
import com.landicorp.android.wofupay.bean.Train_Station;
import com.landicorp.android.wofupay.fragment.TrainOneFragment;
import com.landicorp.android.wofupay.fragment.TrainTwoFragment;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.duration;

/**
 * Created by Administrator on 2017/3/24.
 */

public class TrainStationAdapter extends BaseAdapter {
    private final TrainOneFragment context;
    private final ArrayList<Train_Station.StationBean.TrainStationBean> arrayList;
    List<Integer> views = new ArrayList<Integer>();
    private Train_Station station;
    public String startStation = "";
    public String departTime = "";
    public String trainNo = "";
    public String duration = "";
    public String arriveStation = "";
    public String arriveTime = "";

    private String godata = "";
    private String departStationCode;
    private String arriveStationCode;
    private String queryKey;

    public TrainStationAdapter(TrainOneFragment context, ArrayList<Train_Station.StationBean.TrainStationBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        views.add(1);
        views.add(2);
        views.add(3);
        views.add(4);
        views.add(5);
        views.add(8);
        views.add(9);
        views.add(11);
        views.add(14);
        views.add(15);
    }
    public void setData(String godata) {
        this.godata = godata;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context.getContext()).inflate(
                    R.layout.item_train_station, null);
            holder = new ViewHolder();
            holder.item_type = (TextView) convertView
                    .findViewById(R.id.train_trainNo);
            holder.item_startStation = (TextView) convertView
                    .findViewById(R.id.train_startStation);
            holder.item_endStation = (TextView) convertView
                    .findViewById(R.id.train_endStation);
            holder.item_time = (TextView) convertView
                    .findViewById(R.id.train_time);
            holder.item_time1 = (TextView) convertView
                    .findViewById(R.id.train_time1);
            holder.item_shangwuzuo = (TextView) convertView
                    .findViewById(R.id.train_shangwuzuo);
            holder.item_tedengzuo = (TextView) convertView
                    .findViewById(R.id.train_tedengseat);
            holder.item_yidengzuo = (TextView) convertView
                    .findViewById(R.id.train_yidengseat);
            holder.item_erdengzuo = (TextView) convertView
                    .findViewById(R.id.train_erdengseat);
            holder.item_gaojiruanwo = (TextView) convertView
                    .findViewById(R.id.gaojiruanwo);
            holder.item_ruanwo = (TextView) convertView
                    .findViewById(R.id.train_ruanwo);
            holder.item_yingwo = (TextView) convertView
                    .findViewById(R.id.train_yingwo);
            holder.item_ruanzuo = (TextView) convertView
                    .findViewById(R.id.train_ruanzuo);
            holder.item_yingzuo = (TextView) convertView
                    .findViewById(R.id.train_yingzuo);
            holder.item_wuzuo = (TextView) convertView.findViewById(R.id.wuzuo);
            holder.item_else = (TextView) convertView
                    .findViewById(R.id.train_else);
            holder.item_yuding = (Button) convertView
                    .findViewById(R.id.train_yuding);
            holder.item_startPicture = (ImageView) convertView
                    .findViewById(R.id.train_startPicture);
            holder.item_endPicture = (ImageView) convertView
                    .findViewById(R.id.train_endPicture);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_type.setText(arrayList.get(position).trainNo);

        holder.item_startStation.setText(arrayList.get(position).departStation);

        if (arrayList.get(position).isStart == 1) {
            holder.item_startPicture
                    .setBackgroundResource(R.mipmap.train_shi);
        } else {
            holder.item_startPicture
                    .setBackgroundResource(R.mipmap.train_guo);
        }
        holder.item_endStation.setText(arrayList.get(position).arriveStation);
        if (arrayList.get(position).isEnd == 1) {
            holder.item_endPicture
                    .setBackgroundResource(R.mipmap.train_zhong);
        } else {
            holder.item_endPicture.setBackgroundResource(R.mipmap.train_guo);
        }
        holder.item_time.setText(Html.fromHtml("<b>"
                + arrayList.get(position).departTime + "</b><br>"
                + arrayList.get(position).arriveTime));
        // 把分钟转换成小时
        String timeString = changtohour(arrayList.get(position).duration);

        holder.item_time1.setText(timeString);
        // 得到座位的信息

        List<Train_Station.StationBean.TrainStationBean.SeatsBean> seats = arrayList.get(position).seats;
        List<Integer> list = new ArrayList<Integer>();
        list.clear();
        for (int i = 0; i < seats.size(); i++) {
            TextView seatType = null;
            int seatNum = seats.get(i).seatNum;
            seatType = getViewByNum(holder, seats.get(i).seatType);
            setSeat(seatType, seatNum);
            list.add(seats.get(i).seatType);
        }
        for (Integer view : views) {

            if (list.contains(view)) {

            } else {

                setSeat(getViewByNum(holder, view), 0);
            }
        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#EEF1F8"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if (arrayList.get(position).isCanBook == 0) {
            holder.item_yuding.setEnabled(false);

        } else if (arrayList.get(position).isCanBook == 1) {
            holder.item_yuding.setEnabled(true);
            holder.item_yuding.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // 拿到要传递的值
                    getData(position);
                    // 进入车次信息界面


                }
            });

        }

        return convertView;
    }

    private class ViewHolder {
        TextView item_type;
        TextView item_startStation;
        TextView item_endStation;
        TextView item_time;
        TextView item_time1;
        TextView item_shangwuzuo;
        TextView item_tedengzuo;
        TextView item_yidengzuo;
        TextView item_erdengzuo;
        TextView item_gaojiruanwo;
        TextView item_ruanwo;
        TextView item_yingwo;
        TextView item_ruanzuo;
        TextView item_yingzuo;
        TextView item_wuzuo;
        TextView item_else;
        Button item_yuding;
        ImageView item_startPicture;
        ImageView item_endPicture;
    }

    // 把分钟转换成小时
    private String changtohour(String duration) {
        int s = Integer.valueOf(duration);
        int h = s / 60;

        int m = s % 60;
        String a = String.valueOf(m);
        String b = "";
        if (a.length() == 2) {
            b = a;
        } else if (a.length() == 1) {
            b = "0" + a;
        }
        String c = String.valueOf(h);
        return c + ":" + b;

    }

    private TextView getViewByNum(ViewHolder holder,
                                  int type) {
        TextView seatType = null;
        switch (type) {
            case 15:// 高级软卧
                seatType = holder.item_gaojiruanwo;
                break;
            case 14:// 软卧上铺
                seatType = holder.item_ruanwo;
                break;
            // case 10://硬卧上铺
            case 11:// 硬卧中铺
                // case 12://硬卧下铺
                seatType = holder.item_yingwo;
                break;
            case 9:// 商务座
                seatType = holder.item_shangwuzuo;
                break;
            case 8:// 特等座
                seatType = holder.item_tedengzuo;
                break;
            case 5:// 一等座
                seatType = holder.item_yidengzuo;
                break;
            case 4:// 二等座
                seatType = holder.item_erdengzuo;
                break;
            case 3:// 软座
                seatType = holder.item_ruanzuo;
                break;
            case 2:// 硬座
                seatType = holder.item_yingzuo;
                break;
            case 1:// 无座
                seatType = holder.item_wuzuo;
                break;
            default:
                break;
        }
        return seatType;
    }

    public void setStation(Train_Station station) {
        this.station = station;
    }

    private void setSeat(TextView seatType, int seatNum) {
        if (seatType == null) {
            return;
        }
        if (seatNum >= 40) {
            seatType.setText(Html.fromHtml("<font  color=\"#26A306\">" + "有"));
        } else if (seatNum <= 0) {
            seatType.setText(Html.fromHtml("<font  color=\"#999999\">" + "无"));
        } else {
            seatType.setText(seatNum + "");
        }
    }

    protected void getData(int position) {
        startStation = arrayList.get(position).departStation;
        departTime = arrayList.get(position).departTime;
        trainNo = arrayList.get(position).trainNo;
        arriveStation = arrayList.get(position).arriveStation;
        arriveTime = arrayList.get(position).arriveTime;
        duration = arrayList.get(position).duration;
        queryKey = station.data.queryKey;
        departStationCode = arrayList.get(position).departStationCode;
        arriveStationCode = arrayList.get(position).arriveStationCode;
        List<Train_Station.StationBean.TrainStationBean.SeatsBean> seat = arrayList.get(position).seats;
        String shangwuzuo = "";
        double shangwuzuoPrice = 0;
        String tedengzuo = "";
        double tedengzuoPrice = 0;
        String yidengzuo = "";
        double yidengzuoPrice = 0;
        String erdengzuo = "";
        double erdengzuoPrice = 0;
        String gaojiruanwo = "";
        double gaojiruanwoPrice = 0;
        String ruanwoshang = "";
        double ruanwoshangPrice = 0;
        String ruanwoxia = "";
        double ruanwoxiaPrice = 0;
        double yingwoshangPrice = 0;
        String yingwoshang = null;
        String yingwozhong = "";
        double yingwozhongPrice = 0;
        String yingwoxia = "";
        double yingwoxiaPrice = 0;
        String ruanzuo = "";
        double ruanzuoPrice = 0;
        String yingzuo = "";
        double yingzuoPrice = 0;
        String wuzuo = "";
        double wuzuoPrice = 0;
        for (int i = 0; i < seat.size(); i++) {
            switch (seat.get(i).seatType) {
                case 9:// 商务座
                    shangwuzuo = seat.get(i).seatNum + "";
                    shangwuzuoPrice = seat.get(i).seatPrice;
                    break;
                case 8:// 特等座
                    tedengzuo = seat.get(i).seatNum + "";
                    tedengzuoPrice = seat.get(i).seatPrice;
                    break;
                case 5:// 一等座
                    yidengzuo = seat.get(i).seatNum + "";
                    yidengzuoPrice = seat.get(i).seatPrice;
                    break;
                case 4:// 二等座
                    erdengzuo = seat.get(i).seatNum + "";
                    erdengzuoPrice = seat.get(i).seatPrice;
                    break;
                case 15:// 高级软卧
                    gaojiruanwo = seat.get(i).seatNum + "";
                    gaojiruanwoPrice = seat.get(i).seatPrice;
                    break;
                case 14:// 软卧上铺
                    ruanwoshang = seat.get(i).seatNum + "";
                    ruanwoshangPrice = seat.get(i).seatPrice;
                    break;
                case 13:// 软卧下铺
                    ruanwoxia = seat.get(i).seatNum + "";
                    ruanwoxiaPrice = seat.get(i).seatPrice;
                    break;
                case 10:// 硬卧上铺
                    yingwoshang = seat.get(i).seatNum + "";
                    yingwoshangPrice = seat.get(i).seatPrice;
                    break;
                case 11:// 硬卧中铺
                    yingwozhong = seat.get(i).seatNum + "";
                    yingwozhongPrice = seat.get(i).seatPrice;
                    break;
                case 12:// 硬卧下铺
                    yingwoxia = seat.get(i).seatNum + "";
                    yingwoxiaPrice = seat.get(i).seatPrice;
                    break;
                case 3: // 软座
                    ruanzuo = seat.get(i).seatNum + "";
                    ruanzuoPrice = seat.get(i).seatPrice;
                    break;
                case 2: // 硬座
                    yingzuo = seat.get(i).seatNum + "";
                    yingzuoPrice = seat.get(i).seatPrice;
                    break;
                case 1:// 无座
                    wuzuo = seat.get(i).seatNum + "";
                    wuzuoPrice = seat.get(i).seatPrice;
                    break;
                default:
                    break;
            }
        }
        TrainMeaasgeBean trainMeaasgeBean = new TrainMeaasgeBean();
        trainMeaasgeBean.startStation = startStation;
        trainMeaasgeBean.departTime = departTime;
        trainMeaasgeBean.trainNo = trainNo;
        trainMeaasgeBean.duration = duration;
        trainMeaasgeBean.arriveStation = arriveStation;
        trainMeaasgeBean.arriveTime = arriveTime;
        trainMeaasgeBean.shangwuzuo = shangwuzuo;
        trainMeaasgeBean.shangwuzuoPrice = shangwuzuoPrice;
        trainMeaasgeBean.tedengzuo = tedengzuo;
        trainMeaasgeBean.tedengzuoPrice = tedengzuoPrice;
        trainMeaasgeBean.yidengzuo = yidengzuo;
        trainMeaasgeBean.yidengzuoPrice = yidengzuoPrice;
        trainMeaasgeBean.erdengzuo = erdengzuo;
        trainMeaasgeBean.erdengzuoPrice = erdengzuoPrice;
        trainMeaasgeBean.gaojiruanwo = gaojiruanwo;
        trainMeaasgeBean.gaojiruanwoPrice = gaojiruanwoPrice;
        trainMeaasgeBean.ruanwoshang = ruanwoshang;
        trainMeaasgeBean.ruanwoshangPrice = ruanwoshangPrice;
        trainMeaasgeBean.ruanwoxia = ruanwoxia;
        trainMeaasgeBean.ruanwoxiaPrice = ruanwoxiaPrice;
        trainMeaasgeBean.yingwoshang = yingwoshang;
        trainMeaasgeBean.yingwoshangPrice = yingwoshangPrice;
        trainMeaasgeBean.yingwozhong = yingwozhong;
        trainMeaasgeBean.yingwozhongPrice = yingwozhongPrice;
        trainMeaasgeBean.yingwoxia = yingwoxia;
        trainMeaasgeBean.yingwoxiaPrice = yingwoxiaPrice;
        trainMeaasgeBean.ruanzuo = ruanzuo;
        trainMeaasgeBean.ruanzuoPrice = ruanzuoPrice;
        trainMeaasgeBean.yingzuo = yingzuo;
        trainMeaasgeBean.yingzuoPrice = yingzuoPrice;
        trainMeaasgeBean.wuzuo = wuzuo;
        trainMeaasgeBean.wuzuoPrice = wuzuoPrice;
        trainMeaasgeBean.godata = godata;
        trainMeaasgeBean.queryKey = queryKey;
        trainMeaasgeBean.departStationCode = departStationCode;
        trainMeaasgeBean.arriveStationCode = arriveStationCode;
        TrainOneFragment fragment = context;
        fragment.switchContent(context,new TrainTwoFragment().newInstance(trainMeaasgeBean,""));
    }
}