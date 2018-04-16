package com.hunegroup.hune.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by apple on 11/22/17.
 */

public class CouponDTO implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("partner_id")
    @Expose
    private int partner_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("to_date")
    @Expose
    private String to_date;
    @SerializedName("from_date")
    @Expose
    private String from_date;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("coupon_count")
    @Expose
    private int coupon_count;
    @SerializedName("coupon_available_count")
    @Expose
    private int coupon_available_count;
    @SerializedName("description_html")
    @Expose
    private String description_html;
    @SerializedName("branch_html")
    @Expose
    private String branch_html;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getCoupon_count() {
        return coupon_count;
    }

    public void setCoupon_count(int coupon_count) {
        this.coupon_count = coupon_count;
    }

    public int getCoupon_available_count() {
        return coupon_available_count;
    }

    public void setCoupon_available_count(int coupon_available_count) {
        this.coupon_available_count = coupon_available_count;
    }

    public String getDescription_html() {
        return description_html;
    }

    public void setDescription_html(String description_html) {
        this.description_html = description_html;
    }

    public String getBranch_html() {
        return branch_html;
    }

    public void setBranch_html(String branch_html) {
        this.branch_html = branch_html;
    }
}
