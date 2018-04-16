package com.hunegroup.hune.adapterv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.TransactionDTO;
import com.hunegroup.hune.util.Utilities;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by apple on 11/20/17.
 */

public class LichSuGiaoDichCoinTuyenDungAdapter extends ArrayAdapter<TransactionDTO> {

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2017-11-20 12:48:54 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout item_v2_lichsugiaodichcash;
        public final CircleImageView imvStatus;
        public final LinearLayout lnlContent;
        public final TextView tvName;
        public final TextView tvTime;
        public final TextView tvValue;
        public final LinearLayout lnlInformation;
        public final TextView tvCode;
        public final TextView tvDescription;

        private ViewHolder(LinearLayout item_v2_lichsugiaodichcash, CircleImageView imvStatus, LinearLayout lnlContent, TextView tvName, TextView tvTime, TextView tvValue, LinearLayout lnlInformation, TextView tvCode, TextView tvDescription) {
            this.item_v2_lichsugiaodichcash = item_v2_lichsugiaodichcash;
            this.imvStatus = imvStatus;
            this.lnlContent = lnlContent;
            this.tvName = tvName;
            this.tvTime = tvTime;
            this.tvValue = tvValue;
            this.lnlInformation = lnlInformation;
            this.tvCode = tvCode;
            this.tvDescription = tvDescription;
        }

        public static ViewHolder create(LinearLayout item_v2_lichsugiaodichcash) {
            CircleImageView imvStatus = (CircleImageView) item_v2_lichsugiaodichcash.findViewById(R.id.imv_status);
            LinearLayout lnlContent = (LinearLayout) item_v2_lichsugiaodichcash.findViewById(R.id.lnl_content);
            TextView tvName = (TextView) item_v2_lichsugiaodichcash.findViewById(R.id.tv_name);
            TextView tvTime = (TextView) item_v2_lichsugiaodichcash.findViewById(R.id.tv_time);
            TextView tvValue = (TextView) item_v2_lichsugiaodichcash.findViewById(R.id.tv_value);
            LinearLayout lnlInformation = (LinearLayout) item_v2_lichsugiaodichcash.findViewById(R.id.lnl_information);
            TextView tvCode = (TextView) item_v2_lichsugiaodichcash.findViewById(R.id.tv_code);
            TextView tvDescription = (TextView) item_v2_lichsugiaodichcash.findViewById(R.id.tv_description);
            return new ViewHolder(item_v2_lichsugiaodichcash, imvStatus, lnlContent, tvName, tvTime, tvValue, lnlInformation, tvCode, tvDescription);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.item_v2_lichsugiaodichcoin, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        TransactionDTO item = getItem(position);

        // TODO:Bind your data to the views here
        if (item.getDefine_transaction() == 3) {
            vh.imvStatus.setImageResource(R.mipmap.ic_increase);
        } else if (item.getDefine_transaction() == 4) {
            vh.imvStatus.setImageResource(R.mipmap.ic_decrease);
        } else {
            vh.imvStatus.setImageResource(R.mipmap.ic_transaction);
        }
        vh.tvName.setText(item.getName());
        String value = "";
        if (item.getType() == 1) {
            vh.tvValue.setTextColor(getContext().getResources().getColor(R.color.colorText));
            value = "+" + Utilities.NubmerFormatText(String.format("%.0f", item.getAmount()));
        } else {
            vh.tvValue.setTextColor(getContext().getResources().getColor(R.color.colorRed));
            value = "-" + Utilities.NubmerFormatText(String.format("%.0f", item.getAmount()));
        }
        vh.tvValue.setText(value);
        vh.tvTime.setText(Utilities.dateStringFormatToString(item.getCreated_at(), "yyyy-MM-dd HH:mm:ss", "HH:mm - dd/MM/yyyy"));
        vh.tvCode.setText(item.getTxid());
        return vh.item_v2_lichsugiaodichcash;
    }

    private LayoutInflater mInflater;

    // Constructors
    public LichSuGiaoDichCoinTuyenDungAdapter(Context context, List<TransactionDTO> objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
    }

    public LichSuGiaoDichCoinTuyenDungAdapter(Context context, TransactionDTO[] objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
    }
}

