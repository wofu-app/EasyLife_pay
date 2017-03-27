package com.landicorp.android.wofupay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.Passenger;
import com.landicorp.android.wofupay.bean.SelectPassenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



/**
 * Created by Administrator on 2017/3/25.
 */

public class SelectPassengerAdapter extends BaseAdapter {
    private final Context context;
    private List<SelectPassenger.SelectedPassenger> list = new ArrayList<>();
    private List<Passenger> listpassenger = new ArrayList<Passenger>();
    private HashMap<Integer, Passenger> map = new HashMap<Integer, Passenger>();
    private HashMap<Integer, Boolean> map1 = new HashMap<Integer, Boolean>();
    private Passenger mPassenger;

    public SelectPassengerAdapter(Context context, List<SelectPassenger.SelectedPassenger> list, List<Passenger> listpassenger) {
        this.context = context;
        this.list = list;
        this.listpassenger = listpassenger;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_selectpassenger, null);
            holder = new ViewHolder();
            holder.item_checkbox = (CheckBox) convertView.findViewById(R.id.item_checkbox);
            holder.item_tv_name = (TextView) convertView.findViewById(R.id.item_tv_name);


            holder.item_tv_Idcard = (TextView) convertView.findViewById(R.id.item_tv_IDcard);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CheckBox ck_select = holder.item_checkbox;
        ck_select.setChecked(false);
        listpassenger.clear();
        ck_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < list.size(); i++) {
                    if (isChecked){
                        Passenger passenger = new Passenger();
                        passenger.name = list.get(i).UserName;
                        passenger.idcard = list.get(i).IdCard;
                        passenger.phoneNum = list.get(i).Phone;
                        map.put(position,passenger);
                        map1.put(position,true);
                        ck_select.setChecked(true);
                    }else {
                        map.remove(position);
                        map1.put(position,false);
                        ck_select.setChecked(false);
                    }
                }
                listpassenger.clear();
                Iterator<Integer> iterator = map.keySet().iterator();
                while (iterator.hasNext()){
                    Integer next = iterator.next();
                    mPassenger = map.get(next);
                    listpassenger.add(mPassenger);
                }
            }
        });
        if (list.get(position).UserName.length() ==2) {
            holder.item_tv_name.setText(list.get(position).UserName+"\t\t");
        }else {
            holder.item_tv_name.setText(list.get(position).UserName);
        }
        String hintnum = hintnum(list.get(position).IdCard);
        holder.item_tv_Idcard.setText(hintnum);
        if(position%2==0){
            convertView.setBackgroundColor(Color.parseColor("#EEF1F8"));
        }else{
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        return convertView;
    }
    private class ViewHolder{
        public CheckBox item_checkbox;
        public TextView item_tv_name;
        public TextView item_tv_Idcard;
    }
    private String hintnum(String idCard) {
//		return idCard.substring(0,6)+"*******"+idCard.substring(14, idCard.length());
        return idCard;
    }
    public  List<Passenger> getList(){
        return listpassenger;
    }
}
