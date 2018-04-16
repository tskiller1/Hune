package com.hunegroup.hune.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tskil on 7/27/2017.
 */

public class UserDTO implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("facebook_id")
    @Expose
    private int facebook_id;
    @SerializedName("full_name")
    @Expose
    private String full_name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("fcm_token")
    @Expose
    private String fcm_token;
    @SerializedName("favourite_count")
    @Expose
    private int favourite_count;
    @SerializedName("rating")
    @Expose
    private double rating;
    @SerializedName("isFavourite")
    @Expose
    private boolean isFavourite;
    @SerializedName("status")
    @Expose
    private int status = 1;

    @SerializedName("total_cash")
    @Expose
    private double total_cash;
    @SerializedName("balance_cash")
    @Expose
    private double balance_cash;
    @SerializedName("total_coin")
    @Expose
    private double total_coin;
    @SerializedName("balance_coin")
    @Expose
    private double balance_coin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(int facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public int getFavourite_count() {
        return favourite_count;
    }

    public void setFavourite_count(int favourite_count) {
        this.favourite_count = favourite_count;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotal_cash() {
        return total_cash;
    }

    public void setTotal_cash(double total_cash) {
        this.total_cash = total_cash;
    }

    public double getBalance_cash() {
        return balance_cash;
    }

    public void setBalance_cash(double balance_cash) {
        this.balance_cash = balance_cash;
    }

    public double getTotal_coin() {
        return total_coin;
    }

    public void setTotal_coin(double total_coin) {
        this.total_coin = total_coin;
    }

    public double getBalance_coin() {
        return balance_coin;
    }

    public void setBalance_coin(double balance_coin) {
        this.balance_coin = balance_coin;
    }
}
