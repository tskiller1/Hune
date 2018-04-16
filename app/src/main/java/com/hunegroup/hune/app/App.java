package com.hunegroup.hune.app;

import android.support.multidex.MultiDexApplication;

import com.hunegroup.hune.R;
import com.hunegroup.hune.dto.PositionBanner;
import com.hunegroup.hune.util.SalaryPerType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tskil on 9/20/2017.
 */

public class App extends MultiDexApplication {
    private static App instance;
    private SalaryPerType salaryInstance;
    private List<PositionBanner> positionBannerList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public synchronized static App getInstance() {
        return instance;
    }

    public SalaryPerType getSalary() {
        if (salaryInstance == null) {
            salaryInstance = new SalaryPerType(this);
        }
        return salaryInstance;
    }

    public List<PositionBanner> getPositionBannerList() {
        if (positionBannerList == null) {
            positionBannerList = new ArrayList<>();
            positionBannerList.add(new PositionBanner(1, getString(R.string.timnguoi_td_dangtimnguoi)));
            positionBannerList.add(new PositionBanner(2, getString(R.string.timviec_dangtimviec)));
            positionBannerList.add(new PositionBanner(3, getString(R.string.profile_td_danhsachungvien)));
            positionBannerList.add(new PositionBanner(4, getString(R.string.profile_tv_danhsachtimnguoi)));
            positionBannerList.add(new PositionBanner(5, getString(R.string.chitietcongviec_tv_title)));
            positionBannerList.add(new PositionBanner(6, getString(R.string.chitietungvien_td_title)));
        }
        return positionBannerList;
    }

    public static SalaryPerType getSalaryPerTypeInstance() {
        return getInstance().getSalary();
    }
}
