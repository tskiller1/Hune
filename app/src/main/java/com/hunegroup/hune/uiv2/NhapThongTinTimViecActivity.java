package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment;
import com.hunegroup.hune.R;
import com.hunegroup.hune.ui.ChiTietCongViecTimViecActivity;
import com.hunegroup.hune.ui.ThongBaoTimViecActivity;
import com.hunegroup.hune.util.Common;
import com.hunegroup.hune.util.DialogUtils;
import com.hunegroup.hune.util.JSONParser;
import com.hunegroup.hune.util.SessionType;
import com.hunegroup.hune.util.SessionUser;
import com.hunegroup.hune.util.Validate;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by apple on 11/15/17.
 */

public class NhapThongTinTimViecActivity extends AppCompatActivity implements View.OnClickListener {
    Dialog dialogQuestion;
    private ImageView imvBack;
    private ImageView imvNotification;
    private TextView tvPhone;
    private TextView tvNameLoaihinh;
    private EditText edSalary;
    private TextView tvTimeStart;
    private RelativeLayout rlDateStart;
    private TextView tvDateStart;
    private ImageView imvDateStart;
    private TextView tvTimeEnd;
    private RelativeLayout rlDateEnd;
    private TextView tvDateEnd;
    private ImageView imvDateEnd;
    private EditText edDescription;
    private Button btnConfirm;
    private TextView tvCancel;

    private Dialog dialogProgress;

    //TODO: Declaring
    SessionUser sessionUser;
    JSONParser jsonParser = new JSONParser();

    Calendar myCalendar;
    CalendarDatePickerDialogFragment calendar;
    RadialTimePickerDialogFragment timePicker;
    int type;
    Date today = new Date();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_nhapthongtin_timviec);
        findViews();
        createDialog();
        initData();
    }

    private void findViews() {
        if (SessionType.getInstance().getSessionType() == 1) {
            dialogQuestion = DialogUtils.dialogQuestionTimViec(this, getString(R.string.dialog_create_task_text), new DialogUtils.DialogCallBack() {
                @Override
                public void onYesClickListener() {
                    dialogQuestion.dismiss();
                }

                @Override
                public void onCancelClickListener() {
                    dismissScreen();
                    dialogQuestion.dismiss();
                }
            });
        }
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvNotification = (ImageView) findViewById(R.id.imv_notification);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvNameLoaihinh = (TextView) findViewById(R.id.tv_name_loaihinh);
        edSalary = (EditText) findViewById(R.id.ed_salary);
        tvTimeStart = (TextView) findViewById(R.id.tv_time_start);
        rlDateStart = (RelativeLayout) findViewById(R.id.rl_date_start);
        tvDateStart = (TextView) findViewById(R.id.tv_date_start);
        imvDateStart = (ImageView) findViewById(R.id.imv_date_start);
        tvTimeEnd = (TextView) findViewById(R.id.tv_time_end);
        rlDateEnd = (RelativeLayout) findViewById(R.id.rl_date_end);
        tvDateEnd = (TextView) findViewById(R.id.tv_date_end);
        imvDateEnd = (ImageView) findViewById(R.id.imv_date_end);
        edDescription = (EditText) findViewById(R.id.ed_description);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);

        imvBack.setOnClickListener(this);
        imvNotification.setOnClickListener(this);
        tvTimeStart.setOnClickListener(this);
        tvTimeEnd.setOnClickListener(this);
        rlDateStart.setOnClickListener(this);
        rlDateEnd.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    private void initData() {
        dialogProgress = DialogUtils.dialogProgress(this);
        sessionUser = new SessionUser(this);
        tvPhone.setText(SessionType.getInstance().getPhone());
        tvNameLoaihinh.setText(SessionType.getInstance().getCategory().getName());
    }

    private void dismissScreen() {
        Intent intent = new Intent(NhapThongTinTimViecActivity.this, ChiTietCongViecTimViecActivity.class);
        intent.putExtra(Common.JsonKey.KEY_USER_ID, SessionType.getInstance().getPost_id());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void createDialog() {
        myCalendar = Calendar.getInstance();
        calendar = new CalendarDatePickerDialogFragment()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDoneText(getString(R.string.ok))
                .setCancelText(getString(R.string.cancel));
        calendar.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if(!myCalendar.getTime().before(today)) {
                    if (type == 1) {
                        tvDateStart.setText(Validate.DateToStringSetText(myCalendar.getTime()));
                    } else if (type == 2) {
                        tvDateEnd.setText(Validate.DateToStringSetText(myCalendar.getTime()));
                    }
                }else {
                    Toast.makeText(NhapThongTinTimViecActivity.this, R.string.please_select_after_date, Toast.LENGTH_SHORT).show();
                }
            }
        });

        timePicker = new RadialTimePickerDialogFragment()
                .setStartTime(12, 0)
                .setDoneText(getString(R.string.ok))
                .setCancelText(getString(R.string.cancel));
        timePicker.setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;
                if (minute == 0) {
                    time += "0";
                }
                if (type == 1) {
                    tvTimeStart.setText(time);
                    tvTimeStart.setCompoundDrawables(null, null, null, null);
                } else if (type == 2) {
                    tvTimeEnd.setText(time);
                    tvTimeEnd.setCompoundDrawables(null, null, null, null);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                dismissScreen();
                break;
            case R.id.imv_notification:
                startActivity(new Intent(this, ThongBaoTimViecActivity.class));
            case R.id.tv_time_start:
                timePicker.show(getSupportFragmentManager(), String.valueOf(RecurrencePickerDialogFragment.STYLE_NORMAL));
                type = 1;
                break;
            case R.id.tv_time_end:
                timePicker.show(getSupportFragmentManager(), String.valueOf(RecurrencePickerDialogFragment.STYLE_NORMAL));
                type = 2;
                break;
            case R.id.rl_date_start:
                calendar.show(getSupportFragmentManager(), String.valueOf(RecurrencePickerDialogFragment.STYLE_NORMAL));
                type = 1;
                break;
            case R.id.rl_date_end:
                calendar.show(getSupportFragmentManager(), String.valueOf(RecurrencePickerDialogFragment.STYLE_NORMAL));
                type = 2;
                break;
            case R.id.btn_confirm:
                if (!TextUtils.isEmpty(edSalary.getText())
                        && !TextUtils.isEmpty(tvTimeStart.getText())
                        && !TextUtils.isEmpty(tvDateStart.getText())
                        && !TextUtils.isEmpty(tvTimeEnd.getText())
                        && !TextUtils.isEmpty(tvDateEnd.getText())) {
                    if (!checkDate(Validate.parseStringToDate2(Validate.StringDateFormatToUploadData(tvDateStart.getText().toString())), Validate.parseStringToDate2(Validate.StringDateFormatToUploadData(tvDateEnd.getText().toString())))) {
                        Toast.makeText(getBaseContext(), getString(R.string.toast_ngaybatdaukhongduoclonhonnhayketthuc), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PostTaskServer postTaskServer = new PostTaskServer();
                    postTaskServer.execute(
                            sessionUser.getUserDetails().getToken(),
                            SessionType.getInstance().getPost_id() + "",
                            edSalary.getText().toString(),
                            Validate.StringDateFormatToUploadData(tvDateStart.getText().toString()),
                            Validate.StringDateFormatToUploadData(tvDateEnd.getText().toString()),
                            tvTimeStart.getText().toString().trim() + ":00",
                            tvTimeEnd.getText().toString().trim() + ":00",
                            edDescription.getText().toString()
                    );
                } else {
                    Toast.makeText(this, getString(R.string.toast_vuilongnhapthongtin), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private boolean checkDate(Date startDate, Date endDate) {
        if (startDate.compareTo(endDate) == 1) {
            return false;
        }
        return true;
    }

    private class PostTaskServer extends AsyncTask<String, Void, String> {

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
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_POST_ID, urls[1]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_AMOUNT, urls[2]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_START_DATE, urls[3]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_END_DATE, urls[4]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_START_HOUR, urls[5]));
                params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_END_HOUR, urls[6]));
                if (urls[7] != null) {
                    params.add(new BasicNameValuePair(Common.JsonKey.KEY_TASK_DESCRIPTION, urls[7]));
                }
                result = jsonParser.makeHttpRequest(Common.RequestURL.API_TASK, "POST", params);

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
                    Log.e("Create Task", result);
                    JSONObject jsonResult = new JSONObject(result);
                    String code = jsonResult.getString(Common.JsonKey.KEY_CODE).toString().trim();

                    if (code.contains(Common.JsonKey.KEY_SUCCESSFULLY)) {
                        Intent intent = new Intent(NhapThongTinTimViecActivity.this, DanhSachViecLamTimViecActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            dialogProgress.dismiss();
            super.onPostExecute(result);
        }

    }

}
