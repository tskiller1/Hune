package com.hunegroup.hune.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by apple on 12/14/17.
 */

public class Promotion implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private int user_id;
    @SerializedName("ads_id")
    @Expose
    private int ads_id;
    @SerializedName("branch_id")
    @Expose
    private int branch_id;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("used_at")
    @Expose
    private String used_at;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("ads")
    @Expose
    private PostAdsNotification ads;
    @SerializedName("branch")
    @Expose
    private Branch branch;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAds_id() {
        return ads_id;
    }

    public void setAds_id(int ads_id) {
        this.ads_id = ads_id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsed_at() {
        return used_at;
    }

    public void setUsed_at(String used_at) {
        this.used_at = used_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public PostAdsNotification getAds() {
        return ads;
    }

    public void setAds(PostAdsNotification ads) {
        this.ads = ads;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}
