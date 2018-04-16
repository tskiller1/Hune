package com.hunegroup.hune.dto;

import com.google.android.gms.maps.model.Marker;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tskil on 7/31/2017.
 */

public class PostResultDTO implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("star")
    @Expose
    private double star;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("latitude")
    @Expose
    private float latitude;
    @SerializedName("longitude")
    @Expose
    private float longitude;

    @SerializedName("distance")
    @Expose
    private double distance;
    @SerializedName("user_id")
    @Expose
    private int user_id;
    @SerializedName("category_id")
    @Expose
    private int category_id;
    @SerializedName("category_parent_id")
    @Expose
    private int category_parent_id;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("images")
    @Expose
    private List<String> images;
    @SerializedName("user")
    @Expose
    private UserDTO user;
    @SerializedName("category")
    @Expose
    private CategoryDTO category;
    @SerializedName("parent_category")
    @Expose
    private CategoryDTO category_parent;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("salary")
    @Expose
    private double salary;
    @SerializedName("salary_type")
    @Expose
    private int salary_type;
    @SerializedName("date_start")
    @Expose
    private String date_start;
    @SerializedName("date_end")
    @Expose
    private String date_end;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    /*@SerializedName("parent_category")
    @Expose
    private int parent_category;*/

    private transient Marker marker;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getCategory_parent_id() {
        return category_parent_id;
    }

    public void setCategory_parent_id(int category_parent_id) {
        this.category_parent_id = category_parent_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }
/*
    public int getParent_category() {
        return parent_category;
    }

    public void setParent_category(int parent_category) {
        this.parent_category = parent_category;
    }*/

    public CategoryDTO getCategory_parent() {
        return category_parent;
    }

    public void setCategory_parent(CategoryDTO category_parent) {
        this.category_parent = category_parent;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getSalary_type() {
        return salary_type;
    }

    public void setSalary_type(int salary_type) {
        this.salary_type = salary_type;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
