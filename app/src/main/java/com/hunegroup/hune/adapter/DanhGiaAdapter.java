package com.hunegroup.hune.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.DanhGiaDTO;

/**
 * Created by tskil on 8/3/2017.
 */

public class DanhGiaAdapter extends ArrayAdapter<DanhGiaDTO> {

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2017-08-03 11:53:32 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout row_listview_danhgia;
        public final TextView txtTenNguoiDung;
        public final RatingBar rtbDanhGia;
        public final TextView txtNhanXet;

        private ViewHolder(LinearLayout row_listview_danhgia, TextView txtTenNguoiDung, RatingBar rtbDanhGia, TextView txtNhanXet) {
            this.row_listview_danhgia = row_listview_danhgia;
            this.txtTenNguoiDung = txtTenNguoiDung;
            this.rtbDanhGia = rtbDanhGia;
            this.txtNhanXet = txtNhanXet;
        }

        public static ViewHolder create(LinearLayout row_listview_danhgia) {
            TextView txtTenNguoiDung = (TextView)row_listview_danhgia.findViewById( R.id.txtTenNguoiDung );
            RatingBar rtbDanhGia = (RatingBar)row_listview_danhgia.findViewById( R.id.rtbDanhGia );
            TextView txtNhanXet = (TextView)row_listview_danhgia.findViewById( R.id.txtNhanXet );
            return new ViewHolder( row_listview_danhgia, txtTenNguoiDung, rtbDanhGia, txtNhanXet );
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if ( convertView == null ) {
            View view = mInflater.inflate( R.layout.row_listview_danhgia, parent, false );
            vh = ViewHolder.create( (LinearLayout)view );
            view.setTag( vh );
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        DanhGiaDTO item = getItem( position );

        // TODO:Bind your data to the views here
        vh.txtTenNguoiDung.setText(item.getOwner().getFull_name());
        vh.txtNhanXet.setText(item.getComment());
        vh.rtbDanhGia.setRating((float) item.getRate());
        return vh.row_listview_danhgia;
    }

    private LayoutInflater mInflater;

    // Constructors
    public DanhGiaAdapter(Context context, List<DanhGiaDTO> objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from( context );
    }
    public DanhGiaAdapter(Context context, DanhGiaDTO[] objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from( context );
    }
}

