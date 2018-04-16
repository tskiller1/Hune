package com.hunegroup.hune.adapterv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hunegroup.hune.R;
import com.hunegroup.hune.app.App;
import com.hunegroup.hune.dto.Ads;
import com.hunegroup.hune.util.Utilities;

import java.util.List;

/**
 * Created by apple on 12/21/17.
 */
public class QuanLyHuneAdsAdapter extends ArrayAdapter<Ads> {

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2017-12-21 10:00:22 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout item_v2_quanlyhuneads;
        public final RelativeLayout lnlStatus;
        public final ImageView imvStatus;
        public final TextView tvStatus;
        public final TextView tvTitle;
        public final TextView tvDescription;
        public final TextView tvLabelPayment;
        public final TextView tvPrice;
        public final TextView tvUnit;

        private ViewHolder(LinearLayout item_v2_quanlyhuneads, RelativeLayout lnlStatus, ImageView imvStatus, TextView tvStatus, TextView tvTitle, TextView tvDescription, TextView tvLabelPayment, TextView tvPrice, TextView tvUnit) {
            this.item_v2_quanlyhuneads = item_v2_quanlyhuneads;
            this.lnlStatus = lnlStatus;
            this.imvStatus = imvStatus;
            this.tvStatus = tvStatus;
            this.tvTitle = tvTitle;
            this.tvDescription = tvDescription;
            this.tvLabelPayment = tvLabelPayment;
            this.tvPrice = tvPrice;
            this.tvUnit = tvUnit;
        }

        public static ViewHolder create(LinearLayout item_v2_quanlyhuneads) {
            RelativeLayout lnlStatus = (RelativeLayout) item_v2_quanlyhuneads.findViewById(R.id.lnl_status);
            ImageView imvStatus = (ImageView) item_v2_quanlyhuneads.findViewById(R.id.imv_status);
            TextView tvStatus = (TextView) item_v2_quanlyhuneads.findViewById(R.id.tv_status);
            TextView tvTitle = (TextView) item_v2_quanlyhuneads.findViewById(R.id.tv_title);
            TextView tvDescription = (TextView) item_v2_quanlyhuneads.findViewById(R.id.tv_description);
            TextView tvLabelPayment = (TextView) item_v2_quanlyhuneads.findViewById(R.id.tv_label_payment);
            TextView tvPrice = (TextView) item_v2_quanlyhuneads.findViewById(R.id.tv_price);
            TextView tvUnit = (TextView) item_v2_quanlyhuneads.findViewById(R.id.tv_unit);
            return new ViewHolder(item_v2_quanlyhuneads, lnlStatus, imvStatus, tvStatus, tvTitle, tvDescription, tvLabelPayment, tvPrice, tvUnit);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.item_v2_quanlyhuneads, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Ads item = getItem(position);

        // TODO:Bind your data to the views here
        if (item.getType() == 1) {
            vh.tvTitle.setText(getContext().getString(R.string.notification_ads));
            vh.tvDescription.setText(item.getName());
        } else if (item.getType() == 2) {
            vh.tvTitle.setText(getContext().getString(R.string.location_ads));
            vh.tvDescription.setText(item.getName());
        } else if (item.getType() == 3) {
            vh.tvTitle.setText(getContext().getString(R.string.banner_ads));
            String description = getContext().getString(R.string.location) + ": " + App.getInstance().getPositionBannerList().get(item.getPosition() - 1);
            vh.tvDescription.setText(description);
        }
        if (item.getStatus() == 1) {
            vh.imvStatus.setImageResource(R.mipmap.ic_waitting);
            vh.tvStatus.setTextColor(getContext().getResources().getColor(R.color.colorText));
            vh.tvStatus.setText(R.string.not_payment);
        } else if (item.getStatus() == 2) {
            vh.imvStatus.setImageResource(R.mipmap.ic_finished);
            vh.tvStatus.setTextColor(getContext().getResources().getColor(R.color.green));
            vh.tvStatus.setText(R.string.paid);
        } else if (item.getStatus() == 3) {
            vh.imvStatus.setImageResource(R.mipmap.ic_remove);
            vh.tvStatus.setTextColor(getContext().getResources().getColor(R.color.colorRed));
            vh.tvStatus.setText(R.string.reject);
        }
        vh.tvPrice.setText(Utilities.NubmerFormatText(String.format("%.0f", item.getAmount())));
        return vh.item_v2_quanlyhuneads;
    }

    private LayoutInflater mInflater;

    // Constructors
    public QuanLyHuneAdsAdapter(Context context, List<Ads> objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
    }

    public QuanLyHuneAdsAdapter(Context context, Ads[] objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
    }
}

