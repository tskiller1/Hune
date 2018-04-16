package com.hunegroup.hune.dto;

/**
 * Created by USER on 16/08/2017.
 */

public class User_ThongTinDaLuuDTO {
    private int id;
    private String phone;
    private String full_name;
    private String avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public User_ThongTinDaLuuDTO() {
    }
}
