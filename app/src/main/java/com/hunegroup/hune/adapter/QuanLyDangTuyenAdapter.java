package com.hunegroup.hune.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
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
import com.hunegroup.hune.dto.PostResultDTO;
import com.hunegroup.hune.ui.QuanLyDangTuyenTuyenDungActivity;
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
 * Created by tskil on 05/07/2017.
 */

public class QuanLyDangTuyenAdapter extends ArrayAdapter<PostResultDTO> {

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2017-08-02 17:33:32 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout row_listview_quanlydangtuyen_tuyendung;
        public final RelativeLayout lnNghenghiep;
        public final ImageView imgNgheNghiep;
        public final TextView txtNgheNghiep;
        public final TextView txtTinTuyenDung;
        public final ImageView imgXoa;
        public final TextView txtMoTa;
        public final RatingBar rtbTieuChuan;

        private ViewHolder(LinearLayout row_listview_quanlydangtuyen_tuyendung, RelativeLayout lnNghenghiep, ImageView imgNgheNghiep, TextView txtNgheNghiep, TextView txtTinTuyenDung, ImageView imgXoa, TextView txtMoTa, RatingBar rtbTieuChuan) {
            this.row_listview_quanlydangtuyen_tuyendung = row_listview_quanlydangtuyen_tuyendung;
            this.lnNghenghiep = lnNghenghiep;
            this.imgNgheNghiep = imgNgheNghiep;
            this.txtNgheNghiep = txtNgheNghiep;
            this.txtTinTuyenDung = txtTinTuyenDung;
            this.imgXoa = imgXoa;
            this.txtMoTa = txtMoTa;
            this.rtbTieuChuan = rtbTieuChuan;
        }

        public static ViewHolder create(LinearLayout row_listview_quanlydangtuyen_tuyendung) {
            RelativeLayout lnNghenghiep = (RelativeLayout)row_listview_quanlydangtuyen_tuyendung.findViewById( R.id.ln_nghenghiep );
            ImageView imgNgheNghiep = (ImageView)row_listview_quanlydangtuyen_tuyendung.findViewById( R.id.imgNgheNghiep );
            TextView txtNgheNghiep = (TextView)row_listview_quanlydangtuyen_tuyendung.findViewById( R.id.txtNgheNghiep );
            TextView txtTinTuyenDung = (TextView)row_listview_quanlydangtuyen_tuyendung.findViewById( R.id.txtTinTuyenDung );
            ImageView imgXoa = (ImageView)row_listview_quanlydangtuyen_tuyendung.findViewById( R.id.imgXoa );
            TextView txtMoTa = (TextView)row_listview_quanlydangtuyen_tuyendung.findViewById( R.id.txtMoTa );
            RatingBar rtbTieuChuan = (RatingBar)row_listview_quanlydangtuyen_tuyendung.findViewById( R.id.rtbTieuChuan );
            return new ViewHolder( row_listview_quanlydangtuyen_tuyendung, lnNghenghiep, imgNgheNghiep, txtNgheNghiep, txtTinTuyenDung, imgXoa, txtMoTa, rtbTieuChuan );
        }
    }

    JSONParser jsonParser = new JSONParser();
    SessionUser sessionUser;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if ( convertView == null ) {
            View view = mInflater.inflate( R.layout.row_listview_quanlydangtuyen_tuyendung, parent, false );
            vh = ViewHolder.create( (LinearLayout)view );
            view.setTag( vh );
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        final PostResultDTO item = getItem( position );

        // TODO:Bind your data to the views here
        vh.txtTinTuyenDung.setText(item.getTitle());
        vh.txtMoTa.setText(item.getDescription());
        vh.txtNgheNghiep.setText(item.getCategory().getName());
        vh.rtbTieuChuan.setRating((float) item.getStar());
        Picasso.with(context).load(item.getCategory().getIcon()).into(vh.imgNgheNghiep);
//        vh.imgNgheNghiep.setImageResource();
        vh.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteServer deleteServer = new DeleteServer();
                deleteServer.execute(sessionUser.getUserDetails().getToken(),item.getId()+"");
            }
        });
        return vh.row_listview_quanlydangtuyen_tuyendung;
    }

    private LayoutInflater mInflater;

    // Constructors
    public QuanLyDangTuyenAdapter(Context context, List<PostResultDTO> objects) {
        super(context, 0, objects);
        sessionUser = new SessionUser(context);
        this.mInflater = LayoutInflater.from( context );
        this.context=context;
    }
    public QuanLyDangTuyenAdapter(Context context, PostResultDTO[] objects) {
        super(context, 0, objects);
        sessionUser = new SessionUser(context);
        this.mInflater = LayoutInflater.from( context );
        this.context=context;
    }
    private Context context;
    private class DeleteServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            // prsbLoadingDataList.setVisibility(View.VISIBLE);
        }

        // Call after onPreExecute method
        protected String doInBackground(String... urls) {
            String result = null;
            try {
                if (android.os.Build.VERSION.SDK_INT < 11) {
                    Thread.sleep(1000);
                }
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                /* params */
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_ID,urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST_ID_POST +urls[1], "DELETE", params);

            } catch (ParseException e) {
                e.printStackTrace();
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }

        protected void onPostExecute(String result) {

            if (getContext() == null)
                return;
            if (result != null) {

                //prsbLoadingDataList.setVisibility(View.GONE);

                Log.e("caca", "register:"+result);

                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    Log.e("caca", "code:"+code);

                    if(code.contains(Common.JsonKey.KEY_SUCCESSFULLY)){
                        if(getContext() instanceof Activity){
                            ((Activity)getContext()).finish(); }
                        Intent intent = new Intent(getContext(), QuanLyDangTuyenTuyenDungActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    }else{
//                        Toast.makeText(getContext(),getContext().getString(R.string.toast_suathongtin_thatbai), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }
    }

}


