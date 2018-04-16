package com.hunegroup.hune.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.FavouriteDTO;
import com.hunegroup.hune.ui.DanhSachYeuThichTimViec;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionType;
import com.hunegroup.hune.util.SessionUser;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by USER on 03/08/2017.
 */

public class DanhSachYeuThichAdapter_TimViec extends ArrayAdapter<FavouriteDTO> {
    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2017-08-01 16:20:10 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout row_listview_danhsachyeuthich_tuyendung;
        public final CircleImageView imgAvatar;
        public final TextView txtTenNguoiDung;
        public final TextView txtSoLuotTheoDoi;
        public final RatingBar rtbTong;
        public final ImageView imgTheoDoi;
        public final ImageView imgChiaSe;
        public final ImageButton btnLienLac;

        private ViewHolder(LinearLayout row_listview_danhsachyeuthich_tuyendung, CircleImageView imgAvatar, TextView txtTenNguoiDung, TextView txtSoLuotTheoDoi, RatingBar rtbTong, ImageView imgTheoDoi, ImageView imgChiaSe, ImageButton btnLienLac) {
            this.row_listview_danhsachyeuthich_tuyendung = row_listview_danhsachyeuthich_tuyendung;
            this.imgAvatar = imgAvatar;
            this.txtTenNguoiDung = txtTenNguoiDung;
            this.txtSoLuotTheoDoi = txtSoLuotTheoDoi;
            this.rtbTong = rtbTong;
            this.imgTheoDoi = imgTheoDoi;
            this.imgChiaSe = imgChiaSe;
            this.btnLienLac = btnLienLac;
        }

        public static ViewHolder create(LinearLayout row_listview_danhsachyeuthich_tuyendung) {
            CircleImageView imgAvatar = (CircleImageView)row_listview_danhsachyeuthich_tuyendung.findViewById( R.id.imgAvatar );
            TextView txtTenNguoiDung = (TextView)row_listview_danhsachyeuthich_tuyendung.findViewById( R.id.txtTenNguoiDung );
            TextView txtSoLuotTheoDoi = (TextView)row_listview_danhsachyeuthich_tuyendung.findViewById( R.id.txtSoLuotTheoDoi );
            RatingBar rtbTong = (RatingBar)row_listview_danhsachyeuthich_tuyendung.findViewById( R.id.rtbTong );
            ImageView imgTheoDoi = (ImageView)row_listview_danhsachyeuthich_tuyendung.findViewById( R.id.imgTheoDoi );
            ImageView imgChiaSe = (ImageView)row_listview_danhsachyeuthich_tuyendung.findViewById( R.id.imgChiaSe );
            ImageButton btnLienLac = (ImageButton) row_listview_danhsachyeuthich_tuyendung.findViewById(R.id.btnLienLac);
            return new ViewHolder( row_listview_danhsachyeuthich_tuyendung, imgAvatar, txtTenNguoiDung, txtSoLuotTheoDoi, rtbTong, imgTheoDoi, imgChiaSe, btnLienLac);
        }
    }

    JSONParser jsonParser = new JSONParser();
    SessionUser sessionUser;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DanhSachYeuThichAdapter_TimViec.ViewHolder vh;
        if ( convertView == null ) {
            View view = mInflater.inflate( R.layout.row_listview_danhsachyeuthich_tuyendung, parent, false );
            vh = DanhSachYeuThichAdapter_TimViec.ViewHolder.create( (LinearLayout)view );
            view.setTag( vh );
        } else {
            vh = (DanhSachYeuThichAdapter_TimViec.ViewHolder)convertView.getTag();
        }

        final FavouriteDTO item = getItem( position );

        // TODO:Bind your data to the views here
//        vh.imgAvatar.setImageResource();
        vh.txtTenNguoiDung.setText(item.getUserDTO().getFull_name());
        vh.txtSoLuotTheoDoi.setText(item.getUserDTO().getFavourite_count()+"");
        vh.rtbTong.setRating((float) item.getUserDTO().getRating());
        vh.imgTheoDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DanhSachYeuThichAdapter_TimViec.DeleteServer deleteServer = new DanhSachYeuThichAdapter_TimViec.DeleteServer();
                deleteServer.execute(sessionUser.getUserDetails().getToken(),item.getSource_id()+"",item.getType()+"",position+"");
            }
        });
        vh.btnLienLac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.getUserDTO().getPhone()));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                SessionType.getInstance().setType(3);
                getContext().startActivity(intent);
            }
        });
        /*vh.row_listview_danhsachyeuthich_tuyendung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),ChiTietCongViecTimViecActivity.class);
                intent.putExtra(Common.JsonKey.KEY_USER_ID,item.getUserDTO().getId());
                getContext().startActivity(intent);
            }
        });*/
        Picasso.with(getContext()).load(item.getUserDTO().getAvatar()).error(R.mipmap.ic_no_image).placeholder(R.mipmap.ic_no_image).into(vh.imgAvatar);
        return vh.row_listview_danhsachyeuthich_tuyendung;
    }

    private LayoutInflater mInflater;

    // Constructors
    public DanhSachYeuThichAdapter_TimViec(Context context, List<FavouriteDTO> objects) {
        super(context, 0, objects);
        sessionUser = new SessionUser(context);
        this.mInflater = LayoutInflater.from( context );
    }
    public DanhSachYeuThichAdapter_TimViec(Context context, FavouriteDTO[] objects) {
        super(context, 0, objects);
        sessionUser = new SessionUser(context);
        this.mInflater = LayoutInflater.from( context );
    }

    private class DeleteServer extends AsyncTask<String, Void, String> {

        private int poi;
        protected void onPreExecute() {
            // prsbLoadingDataList.setVisibility(View.VISIBLE);
        }

        // Call after onPreExecute method
        protected String doInBackground(String... urls) {
            String result = null;
            poi=Integer.parseInt(urls[3]);
            try {
                if (android.os.Build.VERSION.SDK_INT < 11) {
                    Thread.sleep(1000);
                }
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                /* params */
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_SOURCE_ID,urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TYPE,urls[2]));
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

            if (getContext() == null)
                return;
            if (result != null) {

                //prsbLoadingDataList.setVisibility(View.GONE);


                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();


                    if(code.contains(Common.JsonKey.KEY_SUCCESSFULLY)){
                        DanhSachYeuThichTimViec.favouriteDTOList.remove(poi);
                        notifyDataSetChanged();
                    }else{
                        Toast.makeText(getContext(),getContext().getString(R.string.toast_dacoloixayra), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }
    }
}
