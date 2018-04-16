package com.hunegroup.hune.dto;

/**
 * Created by apple on 11/22/17.
 */

public class MyCoupon {
    private int id;
    private String code;
    private int group_id;
    private int user_id;
    private CouponDTO coupon_DTO_group;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public CouponDTO getCoupon_DTO_group() {
        return coupon_DTO_group;
    }

    public void setCoupon_DTO_group(CouponDTO coupon_DTO_group) {
        this.coupon_DTO_group = coupon_DTO_group;
    }
}
