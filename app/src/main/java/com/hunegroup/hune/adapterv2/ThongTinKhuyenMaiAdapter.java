package com.hunegroup.hune.adapterv2;

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
import com.hunegroup.hune.dto.ThongTinKhuyenMai;

import java.util.List;

/**
 * Created by apple on 12/13/17.
 */

public class ThongTinKhuyenMaiAdapter extends ArrayAdapter<ThongTinKhuyenMai> {

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2017-12-13 11:46:17 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout item_v2_thongtinkhuyenmai;
        public final ImageView imvLogo;
        public final TextView tvTitle;
        public final TextView tvDescription;

        private ViewHolder(LinearLayout item_v2_thongtinkhuyenmai, ImageView imvLogo, TextView tvTitle, TextView tvDescription) {
            this.item_v2_thongtinkhuyenmai = item_v2_thongtinkhuyenmai;
            this.imvLogo = imvLogo;
            this.tvTitle = tvTitle;
            this.tvDescription = tvDescription;
        }

        public static ViewHolder create(LinearLayout item_v2_thongtinkhuyenmai) {
            ImageView imvLogo = (ImageView) item_v2_thongtinkhuyenmai.findViewById(R.id.imv_logo);
            TextView tvTitle = (TextView) item_v2_thongtinkhuyenmai.findViewById(R.id.tv_title);
            TextView tvDescription = (TextView) item_v2_thongtinkhuyenmai.findViewById(R.id.tv_description);
            return new ViewHolder(item_v2_thongtinkhuyenmai, imvLogo, tvTitle, tvDescription);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.item_v2_thongtinkhuyenmai, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        ThongTinKhuyenMai item = getItem(position);

        // TODO:Bind your data to the views here
        vh.tvTitle.setText(item.getName());
        vh.tvDescription.setText(item.getDescription());
        Glide.with(getContext()).load(item.getLogo()).into(vh.imvLogo);
        return vh.item_v2_thongtinkhuyenmai;
    }

    private LayoutInflater mInflater;

    // Constructors
    public ThongTinKhuyenMaiAdapter(Context context, List<ThongTinKhuyenMai> objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
    }

    public ThongTinKhuyenMaiAdapter(Context context, ThongTinKhuyenMai[] objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
    }
}
