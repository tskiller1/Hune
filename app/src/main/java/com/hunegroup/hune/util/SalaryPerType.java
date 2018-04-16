package com.hunegroup.hune.util;

import android.content.Context;

import com.hunegroup.hune.R;

/**
 * Created by USER on 04/08/2017.
 */

public class SalaryPerType {
    Context mContext;

    public SalaryPerType(Context context) {
        this.mContext = context;
        SALARY_TYPE_HOUR = mContext.getString(R.string.hour);
        SALARY_TYPE_DAY = mContext.getString(R.string.day);
        SALARY_TYPE_WEEK = mContext.getString(R.string.tuan);
        SALARY_TYPE_MONTH = mContext.getString(R.string.thang);
        SALARY_TYPE_TIME = mContext.getString(R.string.lan);
        SALARY_TYPE_AGREEMENT = mContext.getString(R.string.thoathuan);
    }

    private String SALARY_TYPE_HOUR;
    private String SALARY_TYPE_DAY;
    private String SALARY_TYPE_WEEK ;
    private String SALARY_TYPE_MONTH;
    private String SALARY_TYPE_TIME;
    private String SALARY_TYPE_AGREEMENT;

    public String getSalaryTypeHour() {
        return SALARY_TYPE_HOUR;
    }

    public String getSalaryTypeDay() {
        return SALARY_TYPE_DAY;
    }

    public String getSalaryTypeWeek() {
        return SALARY_TYPE_WEEK;
    }

    public String getSalaryTypeMonth() {
        return SALARY_TYPE_MONTH;
    }

    public String getSalaryTypeTime() {
        return SALARY_TYPE_TIME;
    }

    public String getSalaryTypeAgreement() {
        return SALARY_TYPE_AGREEMENT;
    }
}
