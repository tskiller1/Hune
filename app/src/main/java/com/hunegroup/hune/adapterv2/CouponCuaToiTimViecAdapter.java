package com.hunegroup.hune.adapterv2;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.MyCoupon;

import java.util.List;

/**
 * Created by apple on 11/22/17.
 */

public class CouponCuaToiTimViecAdapter extends RecyclerView.Adapter<CouponCuaToiTimViecAdapter.CouponHolder> {
    private Context mContext;
    private List<MyCoupon> mList;

    int selectItem = -1;

    public CouponCuaToiTimViecAdapter(Context mContext, List<MyCoupon> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public CouponHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CouponHolder(LayoutInflater.from(mContext).inflate(R.layout.item_v2_couponcuatoi_tuyendung, parent, false));
    }

    @Override
    public void onBindViewHolder(final CouponHolder holder, int position) {
        MyCoupon item = mList.get(position);
        if (selectItem == position) {
            holder.lnlCopy.setBackgroundResource(R.drawable.custom_button_danhgia_timviec);
            holder.tvCopy.setTextColor(mContext.getResources().getColor(R.color.colorTextWhite));
            holder.imvCopy.setImageResource(R.mipmap.ic_copy);
        } else {
            holder.lnlCopy.setBackgroundResource(R.drawable.custom_edittext_danhgia_timviec);
            holder.tvCopy.setTextColor(mContext.getResources().getColor(R.color.color_main_timviec));
            holder.imvCopy.setImageResource(R.mipmap.ic_copy_red);
        }
        Glide.with(mContext).load(item.getCoupon_DTO_group().getImage()).into(holder.imvCoupon);
        holder.tvCode.setText(item.getCode());
        holder.lnlCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem = holder.getAdapterPosition();
                notifyDataSetChanged();
                ClipboardManager clipMan = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                clipMan.setPrimaryClip(ClipData.newPlainText("code", holder.tvCode.getText().toString().toUpperCase()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CouponHolder extends RecyclerView.ViewHolder {
        private ImageView imvCoupon;
        private TextView tvCode;
        private LinearLayout lnlCopy;
        private ImageView imvCopy;
        private TextView tvCopy;

        public CouponHolder(View itemView) {
            super(itemView);
            imvCoupon = (ImageView) itemView.findViewById(R.id.imv_coupon);
            tvCode = (TextView) itemView.findViewById(R.id.tv_code);
            lnlCopy = (LinearLayout) itemView.findViewById(R.id.lnl_copy);
            imvCopy = (ImageView) itemView.findViewById(R.id.imv_copy);
            tvCopy = (TextView) itemView.findViewById(R.id.tv_copy);
        }
    }
}
