package com.hunegroup.hune.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.ThongTinTimViecDTO;
import com.hunegroup.hune.ui.ThongTinTimViecTuyenDungActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by USER on 03/08/2017.
 */

public class ThongTinTimViecAdapter_TuyenDung extends ArrayAdapter<ThongTinTimViecDTO> {

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2017-08-04 09:55:41 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout row_listview_thongtintimviec_tuyendung;
        public final RelativeLayout lnNghenghiep;
        public final ImageView imgNgheNghiep;
        public final TextView txtNgheNghiep;
        public final TextView txtTenUngVien;
        public final ImageView imgXoa;
        public final TextView txtMoTa;
        public final RatingBar rtbDanhGia;

        private ViewHolder(LinearLayout row_listview_thongtintimviec_tuyendung, RelativeLayout lnNghenghiep, ImageView imgNgheNghiep, TextView txtNgheNghiep, TextView txtTenUngVien, ImageView imgXoa, TextView txtMoTa, RatingBar rtbDanhGia) {
            this.row_listview_thongtintimviec_tuyendung = row_listview_thongtintimviec_tuyendung;
            this.lnNghenghiep = lnNghenghiep;
            this.imgNgheNghiep = imgNgheNghiep;
            this.txtNgheNghiep = txtNgheNghiep;
            this.txtTenUngVien = txtTenUngVien;
            this.imgXoa = imgXoa;
            this.txtMoTa = txtMoTa;
            this.rtbDanhGia = rtbDanhGia;
        }

        public static ViewHolder create(LinearLayout row_listview_thongtintimviec_tuyendung) {
            RelativeLayout lnNghenghiep = (RelativeLayout)row_listview_thongtintimviec_tuyendung.findViewById( R.id.ln_nghenghiep );
            ImageView imgNgheNghiep = (ImageView)row_listview_thongtintimviec_tuyendung.findViewById( R.id.imgNgheNghiep );
            TextView txtNgheNghiep = (TextView)row_listview_thongtintimviec_tuyendung.findViewById( R.id.txtNgheNghiep );
            TextView txtTenUngVien = (TextView)row_listview_thongtintimviec_tuyendung.findViewById( R.id.txtTenUngVien );
            ImageView imgXoa = (ImageView)row_listview_thongtintimviec_tuyendung.findViewById( R.id.imgXoa );
            TextView txtMoTa = (TextView)row_listview_thongtintimviec_tuyendung.findViewById( R.id.txtMoTa );
            RatingBar rtbDanhGia = (RatingBar)row_listview_thongtintimviec_tuyendung.findViewById( R.id.rtbDanhGia );
            return new ViewHolder( row_listview_thongtintimviec_tuyendung, lnNghenghiep, imgNgheNghiep, txtNgheNghiep, txtTenUngVien, imgXoa, txtMoTa, rtbDanhGia );
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if ( convertView == null ) {
            View view = mInflater.inflate( R.layout.row_listview_thongtintimviec_tuyendung, parent, false );
            vh = ViewHolder.create( (LinearLayout)view );
            view.setTag( vh );
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        final ThongTinTimViecDTO item = getItem( position );

        // TODOBind your data to the views here
        vh.txtTenUngVien.setText(item.getTitle());
        vh.txtMoTa.setText(item.getDecription());
        vh.rtbDanhGia.setRating(item.getRating());
        vh.txtNgheNghiep.setText(item.getCategory().getName());
        Picasso.with(context).load(item.getCategory().getUrlImage()).into(vh.imgNgheNghiep);

        vh.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThongTinTimViecTuyenDungActivity.thongTinTimViecDTOs.remove(position);
                notifyDataSetChanged();
            }
        });
        /*vh.row_listview_thongtintimviec_tuyendung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ChiTietUngVienTuyenDungActivity.class);
                intent.putExtra("id_post",item.getId());
                intent.putExtra(Common.JsonKey.KEY_USER_ID,item.getUser_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });*/

        return vh.row_listview_thongtintimviec_tuyendung;
    }

    private LayoutInflater mInflater;

    private Context context;
    // Constructors
    public ThongTinTimViecAdapter_TuyenDung(Context context, List<ThongTinTimViecDTO> objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from( context );
        this.context=context;
    }
    public ThongTinTimViecAdapter_TuyenDung(Context context, ThongTinTimViecDTO[] objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from( context );
        this.context=context;
    }
}

