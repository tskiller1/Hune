package com.hunegroup.hune.dto;

/**
 * Created by USER on 02/08/2017.
 */

public class Post_ThongTinDaLuuDTO {
    private int id;
    private String title;
    private String description;
    private String category_id;
    private String category_parent_id;
    private String rating = "0";
    private int status;
    private Category_ThongTinDaLuuDTO category;
    private Parent_Category_ThongTinDaLuuDTO parent_category;
    private User_ThongTinDaLuuDTO user;

    public User_ThongTinDaLuuDTO getUser() {
        return user;
    }

    public void setUser(User_ThongTinDaLuuDTO user) {
        this.user = user;
    }

    public Post_ThongTinDaLuuDTO() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_parent_id() {
        return category_parent_id;
    }

    public void setCategory_parent_id(String category_parent_id) {
        this.category_parent_id = category_parent_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Category_ThongTinDaLuuDTO getCategory() {
        return category;
    }

    public void setCategory(Category_ThongTinDaLuuDTO category) {
        this.category = category;
    }

    public Parent_Category_ThongTinDaLuuDTO getParent_category() {
        return parent_category;
    }

    public void setParent_category(Parent_Category_ThongTinDaLuuDTO parent_category) {
        this.parent_category = parent_category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
