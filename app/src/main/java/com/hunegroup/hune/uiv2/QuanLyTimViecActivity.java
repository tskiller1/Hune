package com.hunegroup.hune.uiv2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.ThongTinTimViecDTO;
import com.hunegroup.hune.ui.QuanLyTinDangTimViecActivity;
import com.hunegroup.hune.ui.ThongTinTuyenDungTimViecActivity;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionLocation;
import com.hunegroup.hune.util.SessionUser;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 12/14/17.
 */

public class QuanLyTimViecActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int CODE = 100;

    private ImageView imgBack;
    private LinearLayout lnDanhsachtimnguoi;
    private TextView txtSoLuongUngVien;
    private LinearLayout lnDanhsachviec;
    private LinearLayout lnQuanlydangviec;

    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    public List<ThongTinTimViecDTO> thongTinTimViecDTOs;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE) {
            getPostRelateServer getPostRelateServer = new getPostRelateServer();
            if (SessionLocation.getInstance().isLatLng())
                getPostRelateServer.execute(sessionUser.getUserDetails().getToken(), "1", SessionLocation.getInstance().getLocation().latitude + "", SessionLocation.getInstance().getLocation().longitude + "", SessionLocation.getInstance().getRadius() + "");
            else
                Toast.makeText(getBaseContext(), "Chưa có thông tin về vị trí của bạn", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_quanlytimviec_timviec);
        findViews();
        setListThongTin();
    }

    private void findViews() {
        imgBack = (ImageView) findViewById(R.id.imgBack);
        lnDanhsachtimnguoi = (LinearLayout) findViewById(R.id.ln_danhsachtimnguoi);
        txtSoLuongUngVien = (TextView) findViewById(R.id.txtSoLuongUngVien);
        lnDanhsachviec = (LinearLayout) findViewById(R.id.ln_danhsachviec);
        lnQuanlydangviec = (LinearLayout) findViewById(R.id.ln_quanlydangviec);

        imgBack.setOnClickListener(this);
        lnDanhsachtimnguoi.setOnClickListener(this);
        lnDanhsachviec.setOnClickListener(this);
        lnQuanlydangviec.setOnClickListener(this);
    }

    private void setListThongTin() {
        thongTinTimViecDTOs = new ArrayList<>();
        sessionUser = new SessionUser(getBaseContext());
        getPostRelateServer getPostRelateServer = new getPostRelateServer();

        if (SessionLocation.getInstance().isLatLng())
            getPostRelateServer.execute(sessionUser.getUserDetails().getToken(), "1", SessionLocation.getInstance().getLocation().latitude + "", SessionLocation.getInstance().getLocation().longitude + "", SessionLocation.getInstance().getRadius() + "");
        else
            Toast.makeText(getBaseContext(), "Chưa có thông tin về vị trí của bạn", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.ln_danhsachtimnguoi:
                startActivity(new Intent(this, ThongTinTuyenDungTimViecActivity.class));
                break;
            case R.id.ln_quanlydangviec:
                startActivityForResult(new Intent(this, QuanLyTinDangTimViecActivity.class), CODE);
                break;
            case R.id.ln_danhsachviec:
                startActivity(new Intent(this, DanhSachViecLamTimViecActivity.class));
                break;
        }
    }

    private class getPostRelateServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_TYPE, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LATITUDE, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_LONGITUDE, urls[3]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_POST_RADIUS, urls[4]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_POST_RELATE, "GET", params);

            } catch (ParseException e) {
                e.printStackTrace();
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return result;
        }

        protected void onPostExecute(String result) {

            if (getBaseContext() == null)
                return;
            if (result != null) {
                try {
                    Log.e("result", result);
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                            if (!jsonResult.isNull(Common.JsonKey.KEY_META_DATA)) {
                                JSONObject jsonMetaData = jsonResult.getJSONObject(Common.JsonKey.KEY_META_DATA);
                                txtSoLuongUngVien.setText(jsonMetaData.getInt(Common.JsonKey.KEY_META_DATA_TOTAL) + "");
                            }
                        } else {

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }

    }

}
