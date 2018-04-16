package com.hunegroup.hune.uiv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hunegroup.hune.R;
import com.hunegroup.hune.util.DialogUtils;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by apple on 12/12/17.
 */

public class ChonNgayTimViecActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imvBack;
    private ImageView imvComplete;
    private CalendarPickerView calendarView;

    private Dialog dialogQuestion;
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2_chonngay_timviec);
        findViews();
        initData();
    }

    private void findViews() {
        imvBack = (ImageView) findViewById(R.id.imv_back);
        imvComplete = (ImageView) findViewById(R.id.imv_complete);
        calendarView = (CalendarPickerView) findViewById(R.id.calendar_view);

        imvBack.setOnClickListener(this);
        imvComplete.setOnClickListener(this);
    }

    private void initData() {
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        calendarView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.imv_complete:
                dialogQuestion = DialogUtils.dialogQuestionTimViec(this, getString(R.string.select_date_finish), new DialogUtils.DialogCallBack() {
                    @Override
                    public void onYesClickListener() {
                        dialogQuestion.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("days", gson.toJson(calendarView.getSelectedDates()));
                        setResult(200, intent);
                        finish();
                    }

                    @Override
                    public void onCancelClickListener() {
                        dialogQuestion.dismiss();
                    }
                });
                break;
        }
    }
}
