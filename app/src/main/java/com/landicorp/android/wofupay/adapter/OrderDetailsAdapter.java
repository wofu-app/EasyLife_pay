package com.landicorp.android.wofupay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.GoodsBean;
import com.landicorp.android.wofupay.utils.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29 0029.
 */
public class OrderDetailsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<GoodsBean> datas;

    public OrderDetailsAdapter(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<GoodsBean> datas){
        for(int i=0;i<datas.size();i++){
            if("0".equals(datas.get(i).goodsNum)){
                datas.remove(i);
            }
        }
        this.datas = datas;
        notifyDataSetChanged();
    }

    public List<GoodsBean> getDatas(){
        if(datas == null){
            datas = new ArrayList<GoodsBean>();
        }
        return datas;
    }

    @Override
    public int getCount() {
        if(datas == null){
            datas = new ArrayList<GoodsBean>();
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
        final GoodsBean goodsItem = datas.get(position);
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_order_details, null);
            holder.mGoodsImg = (ImageView) convertView.findViewById(R.id.orderItemsUI_goodsImg);
            holder.mGoodsName = (TextView) convertView.findViewById(R.id.orderItemsUI_goodsName);
            holder.mGoodsPrice = (TextView) convertView.findViewById(R.id.orderItemsUI_goodsPrice);
            holder.mGoodsNum = (TextView) convertView.findViewById(R.id.orderItemsUI_goodsNum);
            holder.mPostage = (TextView) convertView.findViewById(R.id.orderItemsUI_postage);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //设置圆角图片
        Glide.with(context).load(goodsItem.thumb).transform(new GlideRoundTransform(context, 15)).into(holder.mGoodsImg);
        holder.mGoodsName.setText(goodsItem.title);
        String price = goodsItem.marketprice;
        String num = goodsItem.goodsNum;

        Double totlePrice = Double.valueOf(price)*Double.valueOf(num);
        holder.mGoodsPrice.setText("¥"+totlePrice);
        holder.mGoodsNum.setText("x"+goodsItem.goodsNum);

        if (goodsItem.issendfree == 1) {
            holder.mPostage.setText("包邮");
        }else {
            holder.mPostage.setText("邮费:" + goodsItem.dispatchprice+"元");
        }

        return convertView;
    }

    static class ViewHolder{
        ImageView mGoodsImg;
        TextView mGoodsName;
        TextView mGoodsPrice;
        TextView mGoodsNum;
        TextView mPostage;
    }
}