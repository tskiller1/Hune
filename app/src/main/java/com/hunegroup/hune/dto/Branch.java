package com.hunegroup.hune.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 12/13/17.
 */

public class Branch {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("ads_id")
    @Expose
    private int ads_id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("extra")
    @Expose
    private String extra;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAds_id() {
        return ads_id;
    }

    public void setAds_id(int ads_id) {
        this.ads_id = ads_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
