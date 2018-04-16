package com.hunegroup.hune.uiv2;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.TaskDTO;
import com.hunegroup.hune.dto.UserDTO;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionType;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Utilities;
import com.hunegroup.hune.util.Validate;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by apple on 11/17/17.
 */

public class ThongTinViecLamTimViecActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ImageView imvBack;
    private ImageView imvDelete;
    private CircleImageView imvAvatar;
    private TextView tvUserName;
    private ImageView imvStatus;
    private TextView tvStatus;
    private ImageView imvHeart;
    private TextView tvFollowCount;
    private ImageButton btnContact;
    private TextView tvTitlePost;
    private TextView tvNameLoaihinh;
    private TextView tvAddress;
    private TextView tvViewMap;
    private TextView tvSalary;
    private TextView tvSalaryUnit;
    private TextView tvStatusPayment;
    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvDescription;
    private Button btnAction;
    private SwipeRefreshLayout srlRefresh;

    private Dialog dialogProgress;
    private Dialog dialogQuestion;

    //TODO: Declaring
    Gson gson = new Gson();
    TaskDTO taskDTO;
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();
    UserDTO userDTO;
    String phone_post;


    int type_action;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            Map<String, Integer> perms = new HashMap<String, Integer>();
            perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
            perms.put(android.Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_post));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                    startActivity(intent);
                } else {
                    return;
                }
            }
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_thongtinvieclam_timviec);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvDelete = (ImageView) findViewById(R.id.imv_delete);
        imvAvatar = (CircleImageView) findViewById(R.id.imv_avatar);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        imvStatus = (ImageView) findViewById(R.id.imv_status);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        imvHeart = (ImageView) findViewById(R.id.imv_heart);
        tvFollowCount = (TextView) findViewById(R.id.tv_follow_count);
        btnContact = (ImageButton) findViewById(R.id.btn_contact);
        tvTitlePost = (TextView) findViewById(R.id.tv_title_post);
        tvNameLoaihinh = (TextView) findViewById(R.id.tv_name_loaihinh);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvViewMap = (TextView) findViewById(R.id.tv_view_map);
        tvSalary = (TextView) findViewById(R.id.tv_salary);
        tvSalaryUnit = (TextView) findViewById(R.id.tv_salary_unit);
        tvStatusPayment = (TextView) findViewById(R.id.tv_status_payment);
        tvStart = (TextView) findViewById(R.id.tv_start);
        tvEnd = (TextView) findViewById(R.id.tv_end);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        btnAction = (Button) findViewById(R.id.btn_action);
        srlRefresh = findViewById(R.id.srl_refresh);

        srlRefresh.setOnRefreshListener(this);

        btnContact.setOnClickListener(this);
        btnAction.setOnClickListener(this);
        imvBack.setOnClickListener(this);
        imvDelete.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        srlRefresh.setColorSchemeColors(getResources().getColor(R.color.color_main_timviec));
        if (getIntent().getExtras() != null) {
            taskDTO = gson.fromJson(getIntent().getStringExtra(Common.IntentKey.KEY_TASK), TaskDTO.class);
            if (taskDTO != null && taskDTO.getId() != 0) {
                updateView();
                if (taskDTO.getOwner_id() != sessionUser.getUserDetails().getId()) {
                    ProfileServer profileServer = new ProfileServer();
                    profileServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(taskDTO.getOwner_id()));
                } else {
                    ProfileServer profileServer = new ProfileServer();
                    profileServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(taskDTO.getUser_id()));
                }
            }
        }
    }

    private void updateView() {

        tvTitlePost.setText(taskDTO.getName());
        tvNameLoaihinh.setText(taskDTO.getPost().getCategory().getName());
        tvAddress.setText(taskDTO.getPost().getAddress());
        tvSalary.setText(Utilities.NubmerFormatText(String.format("%.0f", taskDTO.getAmount())));
        String start = taskDTO.getStart_hour() + " - " + Validate.StringDateFormatToSetText(taskDTO.getStart_date());
        tvStart.setText(start);
        String end = taskDTO.getEnd_hour() + " - " + Validate.StringDateFormatToSetText(taskDTO.getEnd_date());
        tvEnd.setText(end);
        tvDescription.setText(taskDTO.getDescription());
        if (taskDTO.getStatus_payment() == 1) {
            tvStatusPayment.setVisibility(View.VISIBLE);
        } else {
            tvStatusPayment.setVisibility(View.GONE);
        }
        if (taskDTO.getStatus() == 1) {
            imvStatus.setImageResource(R.mipmap.ic_waitting);
            tvStatus.setText(R.string.waiting);
            tvStatus.setTextColor(getResources().getColor(R.color.colorText));
            if (taskDTO.getOwner_id() != sessionUser.getUserDetails().getId()) {
                type_action = 1;
                btnAction.setText(getString(R.string.confirm));
            } else {
                btnAction.setVisibility(View.GONE);
            }
        } else if (taskDTO.getStatus() == 2) {
            imvStatus.setImageResource(R.mipmap.ic_working);
            tvStatus.setText(R.string.working);
            tvStatus.setTextColor(getResources().getColor(R.color.color_main));
            btnAction.setVisibility(View.GONE);
        } else {
            imvStatus.setImageResource(R.mipmap.ic_finished);
            tvStatus.setText(R.string.finished);
            tvStatus.setTextColor(getResources().getColor(R.color.green));
            if (taskDTO.getOwner_id() == sessionUser.getUserDetails().getId()) {
                type_action = 2;
                btnAction.setText(getString(R.string.rating));
            } else {
                btnAction.setVisibility(View.GONE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.btn_contact:
                if (ActivityCompat.checkSelfPermission(ThongTinViecLamTimViecActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.CALL_PHONE);
                    permissions.add(Manifest.permission.READ_PHONE_STATE);
                    requestPermissions(permissions.toArray(new String[permissions.size()]), 100);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_post));
                    SessionType.getInstance().setType(3);
                    startActivity(intent);
                }
                break;
            case R.id.imv_delete:
                dialogQuestion = DialogUtils.dialogQuestionTimViec(this, getString(R.string.dialog_delete_text), new DialogUtils.DialogCallBack() {
                    @Override
                    public void onYesClickListener() {
                        if (taskDTO.getStatus() == 1) {
                            DeleteTaskServer deleteTaskServer = new DeleteTaskServer();
                            deleteTaskServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(taskDTO.getId()));
                            dialogQuestion.dismiss();
                        } else {
                            dialogQuestion.dismiss();
                            Toast.makeText(getBaseContext(), R.string.rules_of_delete_task, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelClickListener() {
                        dialogQuestion.dismiss();
                    }
                });
                break;
            case R.id.btn_action:
                if (type_action == 1) {
                    PutTaskServer putTaskServer = new PutTaskServer();
                    putTaskServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(taskDTO.getId()), String.valueOf(taskDTO.getStatus() + 1));
                } else if (type_action == 2) {

                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        GetTaskServer getTaskServer = new GetTaskServer();
        getTaskServer.execute(sessionUser.getUserDetails().getToken(), String.valueOf(taskDTO.getId()));
    }

    private class ProfileServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            dialogProgress.show();
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
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_ID)) {
                                userDTO.setId(jsondata.getInt(Common.JsonKey.KEY_USER_ID));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_FACEBOOK_ID)) {
                                userDTO.setFacebook_id(jsondata.getInt(Common.JsonKey.KEY_USER_FACEBOOK_ID));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_AVATAR)) {
                                userDTO.setAvatar(jsondata.getString(Common.JsonKey.KEY_USER_AVATAR));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_PHONE)) {
                                userDTO.setPhone(jsondata.getString(Common.JsonKey.KEY_USER_PHONE));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_SEX)) {
                                if (jsondata.getInt(Common.JsonKey.KEY_USER_SEX) == 1)
                                    userDTO.setSex(getString(R.string.nu));
                                else userDTO.setSex(getString(R.string.nam));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_FULL_NAME)) {
                                userDTO.setFull_name(jsondata.getString(Common.JsonKey.KEY_USER_FULL_NAME));
                            }
                            if (!jsondata.isNull(Common.JsonKey.KEY_USER_FAVOURITE_COUNT)) {
                                userDTO.setFavourite_count(jsondata.getInt(Common.JsonKey.KEY_USER_FAVOURITE_COUNT));
                            }
                        }
                        phone_post = userDTO.getPhone();
                        tvUserName.setText(userDTO.getFull_name());
                        tvFollowCount.setText(String.valueOf(userDTO.getFavourite_count()));
                        Picasso.with(ThongTinViecLamTimViecActivity.this).load(userDTO.getAvatar()).into(imvAvatar);
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }

    }

    private class PutTaskServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            dialogProgress.show();
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_ID, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_STATUS, urls[2]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_TASK + "/" + urls[1], "PUT", params);

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
                Log.e("PUT TASK", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Toast.makeText(ThongTinViecLamTimViecActivity.this, R.string.confirm_information_success, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ThongTinViecLamTimViecActivity.this, DanhSachViecLamTimViecActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }

    }

    private class DeleteTaskServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_ID, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_TASK + "/" + urls[1], "DELETE", params);

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
                    Log.e("Delete Task", result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Intent intent = new Intent();
                        intent.putExtra("result", "result");
                        setResult(200, intent);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), R.string.rules_of_delete_task, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            super.onPostExecute(result);
        }

    }

    private class GetTaskServer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            dialogProgress.show();
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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_TOKEN, urls[0]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_ID, urls[1]));
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_TASK + "/" + urls[1], "GET", params);

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
                Log.e("PUT TASK", result.toString());
                try {
                    //sessionUser
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        if (!jsonResult.isNull(Common.JsonKey.KEY_DATA)) {
                            taskDTO = new Gson().fromJson(jsonResult.getJSONObject(Common.JsonKey.KEY_DATA).toString(), TaskDTO.class);
                            updateView();
                        }
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            srlRefresh.setRefreshing(false);
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }

    }

}
