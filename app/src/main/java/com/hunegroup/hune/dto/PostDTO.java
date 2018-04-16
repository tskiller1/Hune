package com.hunegroup.hune.dto;

/**
 * Created by tskil on 7/28/2017.
 */

public class PostDTO {
    String token;
    int category_parent_id;
    int category_id;
    String title;
    float star;
    int quantity;
    String address;
    float latitude;
    float longtitude;
    double salary;
    int salary_type;
    String start_date;
    String end_date;
    String description;
    int type;
    public boolean isLatLog()
    {
        String lat=latitude+"";
        String log=longtitude+"";
        if(lat.isEmpty()||log.isEmpty())
            return false;
        return true;

    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCategory_parent_id() {
        return category_parent_id;
    }

    public void setCategory_parent_id(int category_parent_id) {
        this.category_parent_id = category_parent_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PostDTO() {
    }

    public PostDTO(String token, int category_parent_id, int category_id, String title, float star, int quantity, String address, float latitude, float longtitude, double salary, int salary_type, String start_date, String end_date, String description, int type) {
        this.token = token;
        this.category_parent_id = category_parent_id;
        this.category_id = category_id;
        this.title = title;
        this.star = star;
        this.quantity = quantity;
        this.address = address;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.salary = salary;
        this.salary_type = salary_type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
        this.type = type;
    }
}
