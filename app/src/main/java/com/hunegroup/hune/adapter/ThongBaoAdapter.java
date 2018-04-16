package com.hunegroup.hune.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.ThongBaoDTO;

import java.util.List;

public class ThongBaoAdapter extends ArrayAdapter<ThongBaoDTO> {

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2017-12-14 11:16:22 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout ln_item_thongbao;
        public final ImageView imvLogo;
        public final TextView txtTenThongBao;
        public final TextView txtNoiDung;
        public final TextView txtThoiGian;

        private ViewHolder(LinearLayout ln_item_thongbao, ImageView imvLogo, TextView txtTenThongBao, TextView txtNoiDung, TextView txtThoiGian) {
            this.ln_item_thongbao = ln_item_thongbao;
            this.imvLogo = imvLogo;
            this.txtTenThongBao = txtTenThongBao;
            this.txtNoiDung = txtNoiDung;
            this.txtThoiGian = txtThoiGian;
        }

        public static ViewHolder create(LinearLayout ln_item_thongbao) {
            ImageView imvLogo = (ImageView) ln_item_thongbao.findViewById(R.id.imv_logo);
            TextView txtTenThongBao = (TextView) ln_item_thongbao.findViewById(R.id.txtTenThongBao);
            TextView txtNoiDung = (TextView) ln_item_thongbao.findViewById(R.id.txtNoiDung);
            TextView txtThoiGian = (TextView) ln_item_thongbao.findViewById(R.id.txtThoiGian);
            return new ViewHolder(ln_item_thongbao, imvLogo, txtTenThongBao, txtNoiDung, txtThoiGian);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.row_listview_thongbao_tuyendung, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        ThongBaoDTO item = getItem(position);

        // TODO:Bind your data to the views here
        vh.txtNoiDung.setText(item.getContent());
        vh.txtTenThongBao.setText(item.getTitle());
        vh.txtThoiGian.setText(item.getCreated_at());
        if (item.getType() == 4) {
            Glide.with(getContext()).load(item.getExtra().getIcon()).into(vh.imvLogo);
        } else {
            vh.imvLogo.setImageResource(R.drawable.logo);
        }
        return vh.ln_item_thongbao;
    }

    private LayoutInflater mInflater;

    // Constructors
    public ThongBaoAdapter(Context context, List<ThongBaoDTO> objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
    }

    public ThongBaoAdapter(Context context, ThongBaoDTO[] objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
    }
}
