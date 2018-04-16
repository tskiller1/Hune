package com.hunegroup.hune.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apple on 12/6/17.
 */

public class PostAdsNotification implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("branch")
    @Expose
    private List<String> branch;
    @SerializedName("start_hour")
    @Expose
    private String start_hour;
    @SerializedName("end_hour")
    @Expose
    private String end_hour;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("end_date")
    @Expose
    private String end_date;
    @SerializedName("total_coupon")
    @Expose
    private int total_coupon;
    @SerializedName("discount")
    @Expose
    private int discount;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("long")
    @Expose
    private double log;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("gender")
    @Expose
    private List<Integer> gender;
    @SerializedName("location")
    @Expose
    private List<Integer> location;

    @SerializedName("dates")
    @Expose
    private List<String> dates;

    @SerializedName("ads_branch")
    @Expose
    private List<Branch> ads_branch;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getBranch() {
        return branch;
    }

    public String getStart_hour() {
        return start_hour;
    }

    public String getEnd_hour() {
        return end_hour;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public int getTotal_coupon() {
        return total_coupon;
    }

    public int getDiscount() {
        return discount;
    }

    public double getPrice() {
        return price;
    }

    public String getLogo() {
        return logo;
    }

    public String getCover() {
        return cover;
    }

    public double getLat() {
        return lat;
    }

    public double getLog() {
        return log;
    }

    public List<Integer> getGender() {
        return gender;
    }

    public List<Integer> getLocation() {
        return location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBranch(List<String> branch) {
        this.branch = branch;
    }

    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }

    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setTotal_coupon(int total_coupon) {
        this.total_coupon = total_coupon;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public void setGender(List<Integer> gender) {
        this.gender = gender;
    }

    public void setLocation(List<Integer> location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<Branch> getAds_branch() {
        return ads_branch;
    }

    public void setAds_branch(List<Branch> ads_branch) {
        this.ads_branch = ads_branch;
    }
}
