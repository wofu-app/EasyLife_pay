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
 *
 * Created by Administrator on 2017/3/30 0030.
 */
public class SubOrderAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater mInflater;
    private List<GoodsBean> datas;

    public SubOrderAdapter(Context context, List<GoodsBean> datas){
        this.context = context;
        this.datas = datas;
        mInflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final GoodsBean goodsItem = datas.get(position);
        ViewHolder holder;
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
