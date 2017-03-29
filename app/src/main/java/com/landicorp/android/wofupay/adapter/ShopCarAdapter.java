package com.landicorp.android.wofupay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.bean.GoodsBean;
import com.landicorp.android.wofupay.utils.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车Adapter
 * Created by Administrator on 2017/3/29 0029.
 */
public class ShopCarAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater mInflater;
    private List<GoodsBean> datas;
    private TextView mAmountMoney;

    private ShopCarListener mShopCarListener;

    public void setmShopCarListener(ShopCarListener mShopCarListener) {
        this.mShopCarListener = mShopCarListener;
    }

    public ShopCarAdapter(Context context, List<GoodsBean> datas, TextView mAmountMoney){
        this.context = context;
        this.datas = datas;
        this.mAmountMoney = mAmountMoney;
        mInflater = LayoutInflater.from(context);
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
            convertView = mInflater.inflate(R.layout.item_shop_car, null);
            holder.mCheckbox = (CheckBox) convertView.findViewById(R.id.itemCarUI_checkbox);
            holder.mGoodsImg = (ImageView) convertView.findViewById(R.id.itemCarUI_goodsImg);
            holder.mGoodsName = (TextView) convertView.findViewById(R.id.itemCarUI_goodsName);
            holder.mPrice = (TextView) convertView.findViewById(R.id.itemCarUI_price);
            holder.mDelBtn = (Button) convertView.findViewById(R.id.itemCarUI_delBtn);
            holder.mGoodsNum = (EditText) convertView.findViewById(R.id.itemCarUI_goodsNum);
            holder.mAddBtn = (Button) convertView.findViewById(R.id.itemCarUI_addBtn);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShopCarListener != null) {
                    mShopCarListener.onClick(0, position, goodsItem);
                }
            }
        });

        holder.mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShopCarListener != null) {
                    mShopCarListener.onClick(1, position, goodsItem);
                }
            }
        });

        //设置圆角图片
        Glide.with(context).load(goodsItem.thumb).transform(new GlideRoundTransform(context,15)).into(holder.mGoodsImg);
        holder.mGoodsName.setText(goodsItem.title);
        String price = goodsItem.marketprice;
        String num = goodsItem.goodsNum;

        Double totlePrice = Double.valueOf(price)*Double.valueOf(num);
        holder.mPrice.setText("¥"+totlePrice);
        holder.mGoodsNum.setText(goodsItem.goodsNum);
        changeTotalPrice();//修改总价

        holder.mCheckbox.setChecked(goodsItem.isSelect);
        //给item设计被选中的属性
        holder.mCheckbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                goodsItem.isSelect = !goodsItem.isSelect;
                changeTotalPrice();
            }
        });

        return convertView;
    }

    private void changeTotalPrice() {
        int num = 0;
        Double totalPrice = 0.0;
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isSelect == true) {
                String punm = datas.get(i).goodsNum;
                int singlenum = Integer.valueOf(punm);
                num += singlenum;
                String priceString = datas.get(i).marketprice;
                Double singlePrice = Double.valueOf(priceString);
                totalPrice += singlenum*singlePrice;
            }
            mAmountMoney.setText(totalPrice+"元");
            notifyDataSetChanged();
        }

    }

    static class ViewHolder{
        CheckBox mCheckbox;
        ImageView mGoodsImg;
        TextView mGoodsName;
        TextView mPrice;
        Button mDelBtn;
        EditText mGoodsNum;
        Button mAddBtn;
    }
}
