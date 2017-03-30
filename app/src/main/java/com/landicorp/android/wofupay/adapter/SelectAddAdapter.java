package com.landicorp.android.wofupay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.DetailBean;
import com.landicorp.android.wofupay.bean.SearchAddressBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 选择和添加收货人Adapter
 * Created by Administrator on 2017/3/30 0030.
 */
public class SelectAddAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater mInflater;
    private List<SearchAddressBean.AddressInfo> datas;
    private List<DetailBean> detailBeans = new ArrayList<DetailBean>();
    private Map<Integer, DetailBean> map = new HashMap<Integer, DetailBean>();
    private Map<Integer, Boolean> map1 = new HashMap<Integer, Boolean>();
    public SelectAddAdapter(Context context, List<SearchAddressBean.AddressInfo> datas) {
        this.context = context;
        this.datas = datas;
        mInflater = LayoutInflater.from(context);
    }

    public  List<DetailBean> getDetailDatas(){
        return detailBeans;
    }

    @Override
    public int getCount() {
        if(datas == null){
            datas = new ArrayList<>();
        }
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SearchAddressBean.AddressInfo addInfodata = datas.get(position);
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_select_address, null);
            holder.item_ck = (CheckBox) convertView.findViewById(R.id.item_checkbox);
            holder.item_name = (TextView) convertView.findViewById(R.id.item_tv_name);
            holder.item_phone = (TextView) convertView.findViewById(R.id.item_tv_phone);
            holder.item_province = (TextView) convertView.findViewById(R.id.item_tv_province);
            holder.item_city = (TextView) convertView.findViewById(R.id.item_tv_city);
            holder.item_area = (TextView) convertView.findViewById(R.id.item_tv_area);
            holder.item_address = (TextView) convertView.findViewById(R.id.item_tv_address);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final CheckBox ck_select = holder.item_ck;
        ck_select.setChecked(false);
        detailBeans.clear();
        ck_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                for (int i = 0; i < datas.size(); i++) {
                    if (isChecked) {
                        DetailBean dteailBean = new DetailBean();
                        dteailBean.name = addInfodata.LinkName;
                        dteailBean.phone = addInfodata.LinkPhone;
                        dteailBean.province = addInfodata.Province;
                        dteailBean.city = addInfodata.City;
                        dteailBean.area = addInfodata.County;
                        dteailBean.address = addInfodata.Address;
                        map.put(position, dteailBean);
                        map1.put(position, true);
                        ck_select.setChecked(true);
                    }else {
                        map.remove(position);
                        map1.put(position, false);
                        ck_select.setChecked(false);
                    }
                }
                detailBeans.clear();
                Iterator<Integer> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    Integer integer = (Integer) iterator.next();
                    DetailBean bean = map.get(integer);
                    detailBeans.add(bean);
                }
            }
        });
        holder.item_name.setText(addInfodata.LinkName);
        holder.item_phone.setText(addInfodata.LinkPhone);
        holder.item_province.setText(addInfodata.Province);
        holder.item_city.setText(addInfodata.City);
        holder.item_area.setText(addInfodata.County);
        holder.item_address.setText(addInfodata.Address);
        return convertView;
    }

    static class ViewHolder{
        CheckBox item_ck;
        TextView item_name;
        TextView item_phone;
        TextView item_province;
        TextView item_city;
        TextView item_area;
        TextView item_address;
    }

}
