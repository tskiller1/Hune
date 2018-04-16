package com.hunegroup.hune.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hunegroup.hune.R;
import com.hunegroup.hune.adapter.ThongBaoAdapter;
import com.hunegroup.hune.dto.Extra;
import com.hunegroup.hune.dto.ThongBaoDTO;
import com.hunegroup.hune.uiv2.ChiTietKhuyenMaiTimViecActivity;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Validate;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 06/07/2017.
 */

public class ThongBaoTimViecActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBack;
    private ImageView imgCheckedAll;
    private LinearLayout lnFooter;
    private RelativeLayout rlTimViec;
    private RelativeLayout rlHethong;
    private ListView listThongBao;
    private TextView txt_SoTimViec, txt_SoHeThong;
    private ProgressBar progressBar;
    private FrameLayout frame_progressbar;

    private List<ThongBaoDTO> thongBaoDTOs;
    private ThongBaoAdapter thongBaoAdapter;

    private getNotificationServer getNotificationServer;
    JSONParser jsonParser = new JSONParser();
    SessionUser sessionUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this, true);
        setContentView(R.layout.activity_thongbao_timviec);
        findViews();

        sessionUser = new SessionUser(getBaseContext());

        setListThongBao("");

        //Lấy số thông báo của Tuyển Dụng & Hệ Thống
        getNotificationServer_Counter getCounter = new getNotificationServer_Counter();
        getCounter.execute(sessionUser.getUserDetails().getToken());


    }

    private void findViews() {
        frame_progressbar = (FrameLayout) findViewById(R.id.frame_progressBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgCheckedAll = (ImageView) findViewById(R.id.imgCheckedAll);
        lnFooter = (LinearLayout) findViewById(R.id.ln_footer);
        rlTimViec = (RelativeLayout) findViewById(R.id.rl_timviec);
        rlHethong = (RelativeLayout) findViewById(R.id.rl_hethong);
        listThongBao = (ListView) findViewById(R.id.listThongBao);
        txt_SoTimViec = (TextView) findViewById(R.id.txt_slTimViec);
        txt_SoHeThong = (TextView) findViewById(R.id.txt_slHeThong);

        imgCheckedAll.setOnClickListener(this);
        rlHethong.setOnClickListener(this);
        rlTimViec.setOnClickListener(this);
        imgCheckedAll.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        listThongBao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ThongBaoDTO item = thongBaoDTOs.get(i);

                if (item.getType() == 4) {
                    Intent intent = new Intent(ThongBaoTimViecActivity.this, ChiTietKhuyenMaiTimViecActivity.class);
                    intent.putExtra("ads_id", item.getExtra().getAds_id());
                    startActivity(intent);
                } else if (item.getPost_id() != 0 && item.getType() != 3) {
                    if (item.getType() == Integer.parseInt(Common.Type.TYPE_ENROLLMENT)) {
                        Intent intent = new Intent(getBaseContext(), ChiTietCongViecTimViecActivity.class);
                        intent.putExtra(Common.JsonKey.KEY_USER_ID, item.getPost_id());
                        intent.putExtra("type", 2);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void setListThongBao(String index) {
        thongBaoDTOs = new ArrayList<>();
        thongBaoAdapter = new ThongBaoAdapter(getBaseContext(), thongBaoDTOs);
        listThongBao.setAdapter(thongBaoAdapter);
        getNotificationServer = new getNotificationServer();
        getNotificationServer.execute(sessionUser.getUserDetails().getToken(), index);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.imgBack: {
                finish();
                break;
            }
            case R.id.imgCheckedAll: {
                final Dialog dialog = new Dialog(ThongBaoTimViecActivity.this);
                dialog.setContentView(R.layout.dialog_thongbao_tuyendung);
                Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getNotificationServer_MarkRead getMarkRead = new getNotificationServer_MarkRead();
                        getMarkRead.execute(sessionUser.getUserDetails().getToken());
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
            }
            case R.id.rl_timviec: {
                thongBaoDTOs.clear();
                thongBaoAdapter.notifyDataSetChanged();
                setListThongBao("1");
                break;
            }
            case R.id.rl_hethong: {
                thongBaoDTOs.clear();
                thongBaoAdapter.notifyDataSetChanged();
                setListThongBao("3");
                break;
            }
        }
    }


    private class getNotificationServer extends AsyncTask<String, Void, String> {

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
                /* params */
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_NOTIFICATION_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_NOTIFICATION_TYPE, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_NOTIFICATION, "GET", params);

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
                    //sessionUser
                    Log.e("notification", result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {

                        JSONArray jsonArray = new JSONArray(jsonResult.getJSONArray(Common.JsonKey.KEY_DATA).toString());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            ThongBaoDTO thongBaoDTO = new ThongBaoDTO();
                            JSONObject jsondata = jsonArray.getJSONObject(i);

                            if (!jsondata.isNull(Common.JsonKey.KEY_NOTIFICATION_ID)) {
                                int id = jsondata.getInt(Common.JsonKey.KEY_NOTIFICATION_ID);
                                thongBaoDTO.setId(id);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_NOTIFICATION_TITLE)) {
                                String title = jsondata.getString(Common.JsonKey.KEY_NOTIFICATION_TITLE).toString().trim();
                                thongBaoDTO.setTitle(title);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_NOTIFICATION_CONTENT)) {
                                String content = jsondata.getString(Common.JsonKey.KEY_NOTIFICATION_CONTENT).toString().trim();
                                thongBaoDTO.setContent(content);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_NOTIFICATION_CHANEL)) {
                                int chanel = jsondata.getInt(Common.JsonKey.KEY_NOTIFICATION_CHANEL);
                                thongBaoDTO.setChanel(chanel);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_NOTIFICATION_TYPE)) {
                                int type = jsondata.getInt(Common.JsonKey.KEY_NOTIFICATION_TYPE);
                                thongBaoDTO.setType(type);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_NOTIFICATION_USER_ID)) {
                                int user_id = jsondata.getInt(Common.JsonKey.KEY_NOTIFICATION_USER_ID);
                                thongBaoDTO.setUser_id(user_id);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_NOTIFICATION_CREATED_AT)) {
                                String created_at = jsondata.getString(Common.JsonKey.KEY_NOTIFICATION_CREATED_AT).toString().trim();

                                Date date = Validate.parseStringToDate(created_at);
                                String time = Validate.FormatDateToString(date);

                                thongBaoDTO.setCreated_at(time);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_NOTIFICATION_POST_ID)) {
                                int post_id = jsondata.getInt(Common.JsonKey.KEY_NOTIFICATION_POST_ID);
                                thongBaoDTO.setPost_id(post_id);
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_NOTIFICATION_OWNER_POST)) {
                                int owner_post = jsondata.getInt(Common.JsonKey.KEY_NOTIFICATION_OWNER_POST);
                                thongBaoDTO.setOwner_post(owner_post);
                            }
                            if (thongBaoDTO.getType() == 4) {
                                Extra extra = new Extra();
                                extra.setAds_id(jsondata.getJSONObject(Common.JsonKey.KEY_NOTIFICATION_EXTRA).getInt("ads_id"));
                                extra.setIcon(jsondata.getJSONObject(Common.JsonKey.KEY_NOTIFICATION_EXTRA).getString("icon"));
                                thongBaoDTO.setExtra(extra);
                            }
                            thongBaoDTOs.add(thongBaoDTO);
                        }
                        thongBaoAdapter.notifyDataSetChanged();
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            frame_progressbar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }

    private class getNotificationServer_Counter extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_NOTIFICATION_TOKEN, urls[0]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_NOTIFICATION_COUNTER, "GET", params);

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
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {

                        JSONObject jsondata = new JSONObject(jsonResult.getJSONObject(Common.JsonKey.KEY_DATA).toString());

                        if (!jsondata.isNull(Common.JsonKey.KEY_NOTIFICATION_SEARCH_JOB) && jsondata.getInt(Common.JsonKey.KEY_NOTIFICATION_SEARCH_JOB) != 0) {
                            String slTimViec = String.valueOf(jsondata.getInt(Common.JsonKey.KEY_NOTIFICATION_SEARCH_JOB));
                            txt_SoTimViec.setText(slTimViec);
                        } else
                            txt_SoTimViec.setText("0");
                        if (!jsondata.isNull(Common.JsonKey.KEY_NOTIFICATION_SYSTEM) && jsondata.getInt(Common.JsonKey.KEY_NOTIFICATION_SYSTEM) != 0) {
                            String slHeThong = String.valueOf(jsondata.getInt(Common.JsonKey.KEY_NOTIFICATION_SYSTEM));
                            txt_SoHeThong.setText(slHeThong);
                        } else
                            txt_SoHeThong.setText("0");

                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }

    }

    private class getNotificationServer_MarkRead extends AsyncTask<String, Void, String> {

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
                /* params */
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_NOTIFICATION_TOKEN, urls[0]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_NOTIFICATION_MARK_READ, "GET", params);

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
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {

                        //Lấy số thông báo của Tìm việc & Hệ Thống
                        Intent intent = new Intent();
                        intent.putExtra("result", "result");
                        setResult(200, intent);
                        txt_SoHeThong.setText("0");
                        txt_SoTimViec.setText("0");

                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.toast_dacoloixayra), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            frame_progressbar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }
}
