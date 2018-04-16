package com.hunegroup.hune.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionType;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Validate;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 03/07/2017.
 */

public class DanhGiaTuyenDungActivity extends AppCompatActivity {

    /*todo: 3/7/17 */
    private ImageView imgBack;
    private ImageView imgNotify;
    private CircleImageView imgAvatar;
    private TextView txtTenNguoiDung;
    private TextView txtSoLuotTheoDoi;
    private RatingBar rtbTong;
    private ImageView imgTheoDoi;
    private ImageView imgChiaSe;
    private RatingBar rtbDanhGia;
    private EditText edtNhanXet;
    private Button btnHoanTat;
    private TextView txtReport;
    private FrameLayout frame_progressbar;
    String id_user;
    String id_post;
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    UserDTO userDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this, true);
        setContentView(R.layout.activity_danhgia_tuyendung);
        findViews();
        sessionUser = new SessionUser(getBaseContext());
        ProfileServer profileServer = new ProfileServer();
        id_user = SessionType.getInstance().getId() + "";
        id_post = SessionType.getInstance().getPost_id() + "";
        profileServer.execute(sessionUser.getUserDetails().getToken(), id_user);

    }

    private void findViews() {
        frame_progressbar = (FrameLayout) findViewById(R.id.frame_progressBar);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgNotify = (ImageView) findViewById(R.id.imgNotify);
        imgAvatar = (CircleImageView) findViewById(R.id.imgAvatar);
        txtTenNguoiDung = (TextView) findViewById(R.id.txtTenNguoiDung);
        txtSoLuotTheoDoi = (TextView) findViewById(R.id.txtSoLuotTheoDoi);
        rtbTong = (RatingBar) findViewById(R.id.rtbTong);
        imgTheoDoi = (ImageView) findViewById(R.id.imgTheoDoi);
        imgChiaSe = (ImageView) findViewById(R.id.imgChiaSe);
        rtbDanhGia = (RatingBar) findViewById(R.id.rtbDanhGia);
        edtNhanXet = (EditText) findViewById(R.id.edtNhanXet);
        btnHoanTat = (Button) findViewById(R.id.btnHoanTat);
        txtReport = (TextView) findViewById(R.id.txtReport);

        btnHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = edtNhanXet.getText().toString();
                if (Validate.isEmpty(comment)) {
                    Toast.makeText(DanhGiaTuyenDungActivity.this, getString(R.string.toast_vuilongnhapnhanxet), Toast.LENGTH_SHORT).show();
                } else {
                    if (rtbDanhGia.getRating() != 0) {
                        VotePostServer votePostServer = new VotePostServer();
                        String token = sessionUser.getUserDetails().getToken();
                        String source_id_post = id_post;
                        String rate = rtbDanhGia.getRating() + "";
                        String type_post = Common.Type.TYPE_POST;
                        Log.e("VOTE", token + " " + source_id_post + " " + rate + " " + comment + " " + type_post);
                        votePostServer.execute(token, source_id_post, rate, comment, type_post);
                    } else {
                        Toast.makeText(DanhGiaTuyenDungActivity.this, getString(R.string.toast_vuilongdanhgiacaohon), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionType.getInstance().getType() != 4) {
                    Intent intent = new Intent(DanhGiaTuyenDungActivity.this, ChiTietUngVienTuyenDungActivity.class);
                    intent.putExtra("id_post", SessionType.getInstance().getPost_id());
                    intent.putExtra(Common.JsonKey.KEY_USER_ID, SessionType.getInstance().getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
            }
        });

        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DanhGiaTuyenDungActivity.this, ThongBaoTuyenDungActivity.class));

            }
        });

        imgTheoDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouriteServer deleteFavouriteServer = new FavouriteServer();
                if (userDTO.isFavourite()) {
                    deleteFavouriteServer.execute(sessionUser.getUserDetails().getToken(), userDTO.getId() + "", "1", "DELETE");
                } else {
                    deleteFavouriteServer.execute(sessionUser.getUserDetails().getToken(), userDTO.getId() + "", "1", "POST");
                }
            }
        });
    }
    /*private class VoteServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            frame_progressbar.setVisibility(View.VISIBLE);
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
                *//* params *//*
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_SOURCE_ID, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_RATE, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_COMMENT, urls[3]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_TYPE, urls[4]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_VOTE, "POST", params);

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
                Log.e("vote",result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Toast.makeText(DanhGiaTuyenDungActivity.this, getString(R.string.camonbadadanhgia), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(DanhGiaTuyenDungActivity.this,getString(R.string.bandadanhgiachonguoinayroi), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            frame_progressbar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }*/

    private class ProfileServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            frame_progressbar.setVisibility(View.GONE);
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_USER_USER_ID, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_USER_PROFILE, "GET", params);

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
                Log.e("RESULT", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        userDTO = new UserDTO();
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            JSONObject jsondata = new JSONObject(jsonResult.getString(Common.JsonKey.KEY_DATA).toString());
                            userDTO.setId(jsondata.getInt(Common.JsonKey.KEY_USER_ID));

                            userDTO.setPhone(jsondata.getString(Common.JsonKey.KEY_USER_PHONE));
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_SEX)) {
                                if (jsondata.getInt(Common.JsonKey.KEY_USER_SEX) == 1)
                                    userDTO.setSex(getString(R.string.nu));
                                else userDTO.setSex(getString(R.string.nam));
                            }
                            userDTO.setFull_name(jsondata.getString(Common.JsonKey.KEY_USER_FULL_NAME));
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_BIRTHDAY)) {
                                userDTO.setBirthday(Validate.StringDateFormatToSetText(jsondata.getString(Common.JsonKey.KEY_USER_BIRTHDAY)));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_AVATAR))
                                userDTO.setAvatar(jsondata.getString(Common.JsonKey.KEY_USER_AVATAR));
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_DESCRIPTION))
                                userDTO.setBirthday(Validate.StringDateFormatToSetText(jsondata.getString(Common.JsonKey.KEY_USER_DESCRIPTION)));
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_ADDRESS))
                                userDTO.setAddress(jsondata.getString(Common.JsonKey.KEY_USER_ADDRESS));
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_RATING))
                                userDTO.setRating(jsondata.getDouble(Common.JsonKey.KEY_USER_RATING));
                            else userDTO.setRating(0);
                            userDTO.setFavourite_count(jsondata.getInt(Common.JsonKey.KEY_USER_FAVOURITE_COUNT));
                            userDTO.setFavourite(jsondata.getBoolean(Common.JsonKey.KEY_USER_IS_FAVOURITE));
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_AVATAR)) {
                                userDTO.setAvatar(jsondata.getString(Common.JsonKey.KEY_USER_AVATAR));
                            }
                        }
                        Picasso.with(getBaseContext()).load(userDTO.getAvatar()).error(R.mipmap.ic_no_image).placeholder(R.mipmap.ic_no_image).into(imgAvatar);
                        txtTenNguoiDung.setText(userDTO.getFull_name());
                        txtSoLuotTheoDoi.setText(userDTO.getFavourite_count() + "");
                        rtbTong.setRating((float) userDTO.getRating());
                        Picasso.with(getBaseContext()).load(userDTO.getAvatar()).into(imgAvatar);
                        if (userDTO.isFavourite())
                            imgTheoDoi.setImageResource(R.mipmap.ic_heart_fill);
                        else imgTheoDoi.setImageResource(R.mipmap.ic_heart_red);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            frame_progressbar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }

    private class FavouriteServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TOKEN, urls[0]));
                if (urls[3] == "DELETE") {
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_SOURCE_ID, urls[1]));
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TYPE, urls[2]));
                } else {
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_SOURCE_ID, urls[1]));
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_FAVOURITE_TYPE, urls[2]));
                }
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_FAVOURITE, urls[3], params);

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
                Log.e("RESULT FAVOURITE", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();
                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Intent intent = new Intent(DanhGiaTuyenDungActivity.this, DanhGiaTuyenDungActivity.class);
                        intent.putExtra("id", Integer.parseInt(id_user));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }

    }

    private class VotePostServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_SOURCE_ID, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_RATE, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_COMMENT, urls[3]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_VOTE_TYPE, urls[4]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_VOTE, "POST", params);

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
                Log.e("vote", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Toast.makeText(DanhGiaTuyenDungActivity.this, getString(R.string.camonbadadanhgia), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DanhGiaTuyenDungActivity.this, TimNguoiTuyenDungActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DanhGiaTuyenDungActivity.this, getString(R.string.bandadanhgiachonguoinayroi), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            super.onPostExecute(result);
        }

    }

}
