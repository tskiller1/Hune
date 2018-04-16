package com.hunegroup.hune.dto;

/**
 * Created by USER on 03/08/2017.
 */

public class ThongTinTimViecDTO {
    private int id;
    private String title;
    private String decription;
    private float rating = 0;
    private int user_id;
    private int category_id;
    private int category_parent_id;
    private String distance;
    private User_ThongTinTimViecDTO user;
    private Category_ThongTinTimViecDTO category;
    private Parent_Category_ThongTinTimViecDTO parent_category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public User_ThongTinTimViecDTO getUser() {
        return user;
    }

    public void setUser(User_ThongTinTimViecDTO user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getCategory_parent_id() {
        return category_parent_id;
    }

    public void setCategory_parent_id(int category_parent_id) {
        this.category_parent_id = category_parent_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Category_ThongTinTimViecDTO getCategory() {
        return category;
    }

    public void setCategory(Category_ThongTinTimViecDTO category) {
        this.category = category;
    }

    public Parent_Category_ThongTinTimViecDTO getParent_category() {
        return parent_category;
    }

    public void setParent_category(Parent_Category_ThongTinTimViecDTO parent_category) {
        this.parent_category = parent_category;
    }


    public ThongTinTimViecDTO() {
    }
}
