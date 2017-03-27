package com.landicorp.android.wofupay.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.Passenger;


import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/27.
 */

public class PassengerAdapter extends BaseAdapter {
    private Context context;
    private  ArrayList<Passenger> list;
    private  String zuoweixibie;

    public PassengerAdapter(Context context, ArrayList<Passenger> list, String zuoweixibie) {
        this.context = context;
        this.list = list;
        this.zuoweixibie = zuoweixibie;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return getItem(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_passenger, null);
            holder.item_tv_name = (TextView) convertView.findViewById(R.id.passenger_name);
            holder.item_tv_Idcard = (TextView) convertView.findViewById(R.id.passenger_idcard);
            holder.item_tv_zuoweixibie = (TextView) convertView.findViewById(R.id.passenger_zuoweixibie);
            holder.item_btn = (Button) convertView.findViewById(R.id.passenger_btn);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).name.length()==2) {
            holder.item_tv_name.setText(list.get(position).name+"    ");
        }else {
            holder.item_tv_name.setText(list.get(position).name);
        }
        String hintnum = hintnum(list.get(position).idcard);
        holder.item_tv_Idcard.setText(hintnum);
        if(position%2==0){
            convertView.setBackgroundColor(Color.parseColor("#EEF1F8"));
        }else{
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        //holder.item_tv_name.setText(list.get(position).name);
        //holder.item_tv_Idcard.setText(list.get(position).idcard);
        holder.item_tv_zuoweixibie.setText(zuoweixibie);
//        holder.item_btn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle(context.getString(R.string.Title));
//                builder.setMessage("确定删除该乘客吗?");
//                builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        list.remove(position);
//                        notifyDataSetChanged();
//                    }
//                });
//                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        notifyDataSetChanged();
//                    }
//                });
//                builder.create().show();
//
//
//            }
//        });
        return convertView;
    }
    private class ViewHolder{
        public TextView item_tv_name;
        public TextView item_tv_Idcard;
        public TextView item_tv_zuoweixibie;
        public TextView item_tv_passengerType;
        public Button item_btn;
    }
    private String hintnum(String idCard) {
        return idCard.substring(0,6)+"*******"+idCard.substring(14, idCard.length());
    }
    public void setZuoWei(String text) {
        this.zuoweixibie = text;
        notifyDataSetChanged();
    }
}
