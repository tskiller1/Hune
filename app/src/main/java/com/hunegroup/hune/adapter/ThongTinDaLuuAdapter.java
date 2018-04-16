package com.hunegroup.hune.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.ThongTinDaLuuDTO;
import com.hunegroup.hune.ui.ThongTinDaLuuTimViecActivity;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 02/08/2017.
 */

public class ThongTinDaLuuAdapter extends ArrayAdapter<ThongTinDaLuuDTO> {

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2017-08-02 16:55:11 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout row_listview_thongtindaluu_tuyendung;
        public final RelativeLayout lnNghenghiep;
        public final ImageView imgNgheNghiep;
        public final TextView txtNgheNghiep;
        public final TextView txtTenUngVien;
        public final ImageView imgXoa;
        public final TextView txtMoTa;
        public final RatingBar rtbDanhGia;

        private ViewHolder(LinearLayout row_listview_thongtindaluu_tuyendung, RelativeLayout lnNghenghiep, ImageView imgNgheNghiep, TextView txtNgheNghiep, TextView txtTenUngVien, ImageView imgXoa, TextView txtMoTa, RatingBar rtbDanhGia) {
            this.row_listview_thongtindaluu_tuyendung = row_listview_thongtindaluu_tuyendung;
            this.lnNghenghiep = lnNghenghiep;
            this.imgNgheNghiep = imgNgheNghiep;
            this.txtNgheNghiep = txtNgheNghiep;
            this.txtTenUngVien = txtTenUngVien;
            this.imgXoa = imgXoa;
            this.txtMoTa = txtMoTa;
            this.rtbDanhGia = rtbDanhGia;
        }

        public static ViewHolder create(LinearLayout row_listview_thongtindaluu_tuyendung) {
            RelativeLayout lnNghenghiep = (RelativeLayout) row_listview_thongtindaluu_tuyendung.findViewById(R.id.ln_nghenghiep);
            ImageView imgNgheNghiep = (ImageView) row_listview_thongtindaluu_tuyendung.findViewById(R.id.imgNgheNghiep);
            TextView txtNgheNghiep = (TextView) row_listview_thongtindaluu_tuyendung.findViewById(R.id.txtNgheNghiep);
            TextView txtTenUngVien = (TextView) row_listview_thongtindaluu_tuyendung.findViewById(R.id.txtTenUngVien);
            ImageView imgXoa = (ImageView) row_listview_thongtindaluu_tuyendung.findViewById(R.id.imgXoa);
            TextView txtMoTa = (TextView) row_listview_thongtindaluu_tuyendung.findViewById(R.id.txtMoTa);
            RatingBar rtbDanhGia = (RatingBar) row_listview_thongtindaluu_tuyendung.findViewById(R.id.rtbDanhGia);
            return new ViewHolder(row_listview_thongtindaluu_tuyendung, lnNghenghiep, imgNgheNghiep, txtNgheNghiep, txtTenUngVien, imgXoa, txtMoTa, rtbDanhGia);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.row_listview_thongtindaluu_tuyendung, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final ThongTinDaLuuDTO item = getItem(position);

        // TODO:Bind your data to the views here

        vh.txtTenUngVien.setText(Html.fromHtml(item.getPost().getTitle()));
        vh.txtMoTa.setText(item.getPost().getDescription());
        vh.rtbDanhGia.setRating(Float.parseFloat(item.getPost().getRating()));
        vh.txtNgheNghiep.setText(item.getPost().getCategory().getName());
        Picasso.with(context).load(item.getPost().getCategory().getUrlImage()).into(vh.imgNgheNghiep);

        vh.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFavouriteServer deleteFavouriteServer = new deleteFavouriteServer();
                deleteFavouriteServer.execute(position + "", sessionUser.getUserDetails().getToken(), item.getSource_id(), item.getType());

            }
        });
/*        vh.row_listview_thongtindaluu_tuyendung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getPost().getStatus() == Common.Type.TYPE_STATUS_INT_ON) {
                    Intent intent = new Intent(getContext(), ChiTietCongViecTimViecActivity.class);
                    intent.putExtra(Common.JsonKey.KEY_USER_ID, item.getPost().getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            }
        });*/

        return vh.row_listview_thongtindaluu_tuyendung;
    }

    private LayoutInflater mInflater;
    private SessionUser sessionUser;

    // Constructors
    public ThongTinDaLuuAdapter(Context context, List<ThongTinDaLuuDTO> objects) {
        super(context, 0, objects);
        this.context = context;
        sessionUser = new SessionUser(context);
        this.mInflater = LayoutInflater.from(context);
    }

    public ThongTinDaLuuAdapter(Context context, ThongTinDaLuuDTO[] objects) {
        super(context, 0, objects);
        this.context = context;
        sessionUser = new SessionUser(context);
        this.mInflater = LayoutInflater.from(context);
    }


    private Context context;
    JSONParser jsonParser = new JSONParser();

    private class deleteFavouriteServer extends AsyncTask<String, Void, String> {

        private int poi;

        protected void onPreExecute() {

        }

        // Call after onPreExecute method
        protected String doInBackground(String... urls) {
            String result = null;
            try {
                if (android.os.Build.VERSION.SDK_INT < 11) {
                    Thread.sleep(1000);
                }
                poi = Integer.parseInt(urls[0]);
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                /* params */
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TOKEN, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_SOURCE_ID, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TYPE, urls[3]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_FAVOURITE, "DELETE", params);

            } catch (ParseException e) {
                e.printStackTrace();
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }

        protected void onPostExecute(String result) {

            if (context == null)
                return;
            if (result != null) {
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        ThongTinDaLuuTimViecActivity.thongTinDaLuuDTOs.remove(poi);
                        notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, context.getString(R.string.toast_dacoloixayra), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }

    }


}

