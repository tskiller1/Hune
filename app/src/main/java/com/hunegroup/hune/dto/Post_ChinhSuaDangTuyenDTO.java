package com.hunegroup.hune.dto;

/**
 * Created by USER on 07/08/2017.
 */

public class Post_ChinhSuaDangTuyenDTO {
    private int id;
    private int user_id;
    private int category_parent_id;
    private int category_id;
    private String title;
    private String images;
    private String thumbnail;
    private float rating;
    private int quantity;
    private String address;
    private double latitude;
    private double longitude;
    private String salary;
    private int salary_type;
    private String start_date;
    private String end_date;
    private String description;
    private int type;
    private int sex;
    private int status;
    private String created_at;
    private String updated_at;
    private Category_ChinhSuaDangTuyenDTO category;
    private Parent_Category_ChinhSuaDangTuyenDTO parent_category;
    private User_ChinhSuaDangTuyenDTO user;
    private boolean isLatLog=false;

    public Post_ChinhSuaDangTuyenDTO() {
    }
    public boolean isLatLog()
    {
        String lat=latitude+"";
        String log=longitude+"";
        if(lat.isEmpty()||log.isEmpty())
            return false;
        return true;

    }
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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public Category_ChinhSuaDangTuyenDTO getCategory() {
        return category;
    }

    public void setCategory(Category_ChinhSuaDangTuyenDTO category) {
        this.category = category;
    }

    public Parent_Category_ChinhSuaDangTuyenDTO getParent_category() {
        return parent_category;
    }

    public void setParent_category(Parent_Category_ChinhSuaDangTuyenDTO parent_category) {
        this.parent_category = parent_category;
    }

    public User_ChinhSuaDangTuyenDTO getUser() {
        return user;
    }

    public void setUser(User_ChinhSuaDangTuyenDTO user) {
        this.user = user;
    }

    public Post_ChinhSuaDangTuyenDTO(int id, int user_id, int category_parent_id, int category_id, String title, String images, String thumbnail, float rating, int quantity, String address, double latitude, double longitude, String salary, int salary_type, String start_date, String end_date, String description, int type, int sex, int status, String created_at, String updated_at, Category_ChinhSuaDangTuyenDTO category, Parent_Category_ChinhSuaDangTuyenDTO parent_category, User_ChinhSuaDangTuyenDTO user) {
        this.id = id;
        this.user_id = user_id;
        this.category_parent_id = category_parent_id;
        this.category_id = category_id;
        this.title = title;
        this.images = images;
        this.thumbnail = thumbnail;
        this.rating = rating;
        this.quantity = quantity;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.salary = salary;
        this.salary_type = salary_type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
        this.type = type;
        this.sex = sex;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.category = category;
        this.parent_category = parent_category;
        this.user = user;
    }
}
