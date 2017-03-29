package com.landicorp.android.wofupay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.activity.MainActivity;
import com.landicorp.android.wofupay.bean.ShopBean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2017/3/28 0028.
 */
public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private boolean isShowEmpty;
    private OnItemClickListener listener;
    private List<ShopBean> datas;

    public GoodsAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setData(List<ShopBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public List<ShopBean> getData(){
        if(datas == null)
            datas = new ArrayList<>();
        return datas;
    }

    @Override
    public int getItemCount() {
        if(isShowEmpty){
            return 1;
        }else {
            if(datas == null){
                datas = new ArrayList<>();
            }
            return datas.size();
        }
    }

    public void setHeadData(boolean isShowEmpty){
        this.isShowEmpty = isShowEmpty;
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowEmpty)
            return 0;
        else
            return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new GoodsHeaderHolder(mInflater.inflate(R.layout.item_goods_head, parent, false));
        else
            return new GoodsViewHolder(mInflater.inflate(R.layout.item_goods_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof  GoodsHeaderHolder){
        }else if(holder instanceof GoodsViewHolder) {
            final GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
            goodsViewHolder.mParentLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(goodsViewHolder.mParentLy, position);
                }
            });

            Glide.with(mContext).load(datas.get(position).thumb).into(goodsViewHolder.mGoodsImg);
            goodsViewHolder.mGoodsName.setText(datas.get(position).title);
            goodsViewHolder.mGoodsPrice.setText("¥:" + datas.get(position).marketprice);
            goodsViewHolder.mShopCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }

    class GoodsHeaderHolder extends RecyclerView.ViewHolder {
        public GoodsHeaderHolder(View itemView) {
            super(itemView);
        }
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mParentLy;
        ImageView mGoodsImg;
        TextView mGoodsName;
        TextView mGoodsPrice;
        ImageView mShopCar;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            mParentLy = (LinearLayout) itemView.findViewById(R.id.goodsItemUI_parentLy);
            mGoodsImg = (ImageView) itemView.findViewById(R.id.goodsItemUI_goodsImg);

            if(mContext instanceof MainActivity){
                MainActivity activity = (MainActivity) mContext;
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mGoodsImg.getLayoutParams();
                lp.height = (activity.getSreenWidth() - 60) / 3;
                lp.width = (activity.getSreenWidth() - 60) / 3;
                mGoodsImg.setLayoutParams(lp);
            }
            mGoodsName = (TextView) itemView.findViewById(R.id.goodsItem_goodsName);
            mGoodsPrice = (TextView) itemView.findViewById(R.id.goodsItem_goodsPrice);
            mShopCar = (ImageView) itemView.findViewById(R.id.goodsItemUI_shopCar);

        }
    }
}
