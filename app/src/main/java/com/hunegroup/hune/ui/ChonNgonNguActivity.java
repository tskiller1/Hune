package com.hunegroup.hune.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hunegroup.hune.R;
import com.hunegroup.hune.util.SessionUser;

import java.util.Locale;

import static com.hunegroup.hune.util.Utilities.switchLangauge;

/**
 * Created by tskil on 9/14/2017.
 */

public class ChonNgonNguActivity extends AppCompatActivity {
    //public static String KEY_LANGUAGE = "language";

    RadioButton rdbVietnamese;
    RadioButton rdbEnglish;
    ImageView imgBack;
    RadioGroup rdgSelect;
    Button btnHoanTat;

    SessionUser sessionUser;

    int tempLanguage;
    private SharedPreferences sharedPrefs;
    public static final String KEY_LANGUAGE = "key_language";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchLangauge(this,true);
        setContentView(R.layout.activity_chonngonngu);
        sessionUser = new SessionUser(getBaseContext());
        findViews();
        initLanguage();
    }
    private void initLanguage() {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void findViews() {
        btnHoanTat = (Button) findViewById(R.id.btnHoanTat);
        rdbVietnamese = (RadioButton) findViewById(R.id.rdb_vietnamese);
        rdbEnglish = (RadioButton) findViewById(R.id.rdb_english);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        rdgSelect = (RadioGroup) findViewById(R.id.rdg_select);

        Log.e("DEFAULT VALUE", Locale.getDefault().getLanguage());
        if (sessionUser.getLanguage() == -1) {
            if (Locale.getDefault().getLanguage().equals("en")) {
                tempLanguage = 0;
                rdbEnglish.setChecked(true);
            } else if (Locale.getDefault().getLanguage().equals("vi")) {
                rdbVietnamese.setChecked(true);
                tempLanguage = 1;
            }
        } else {
            tempLanguage = sessionUser.getLanguage();
            if (sessionUser.getLanguage() == 1)
                rdbVietnamese.setChecked(true);
            else if (sessionUser.getLanguage() == 0)
                rdbEnglish.setChecked(true);
        }
        rdgSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rdb_vietnamese:
                        sharedPrefs.edit().putInt(KEY_LANGUAGE, 1).commit();
                        tempLanguage = 1;
                        break;
                    case R.id.rdb_english:
                        sharedPrefs.edit().putInt(KEY_LANGUAGE, 0).commit();
                        tempLanguage = 0;
                        break;
                }
                sessionUser.setLanguage(tempLanguage);
            }
        });
        btnHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionUser.setLanguage(tempLanguage);

                switchLangauge(ChonNgonNguActivity.this,false);
                Intent intent = new Intent(ChonNgonNguActivity.this,SplashScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
