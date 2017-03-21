package com.landicorp.android.wofupay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.Areabean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */

public class AreaAdaptr extends BaseAdapter {
    private List<Areabean> mDate;
    private Context mContext;


    public AreaAdaptr(Context context,
                      List<Areabean> date) {
        this.mContext = context;
        this.mDate = date;
    }

    @Override
    public int getCount() {
        return mDate.size();
    }

    @Override
    public Object getItem(int position) {
        return mDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Areabean bean= (Areabean) getItem(position);
        if (convertView==null){
            //布局文件的第一个属性会失效
            // convertView = View.inflate(mContext,R.layout.item_province,null);
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.item_province,parent,false);
        }
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        tv_name.setText(bean.name);

        return convertView;
    }
}
