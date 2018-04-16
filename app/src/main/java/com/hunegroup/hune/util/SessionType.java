package com.hunegroup.hune.util;

import com.hunegroup.hune.dto.CategoryDTO;

/**
 * Created by tskil on 8/8/2017.
 */

public class SessionType {
    private static SessionType ourInstance;
    private int type; // 1: Inrollment, 2: Search Job
    private int id;
    private int post_id;
    private String phone;
    private CategoryDTO category;
    private int sessionType;

    public static SessionType getInstance() {
        if (ourInstance == null)
            ourInstance = new SessionType();
        return ourInstance;
    }

    private SessionType() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public int getSessionType() {
        return sessionType;
    }

    public void setSessionType(int sessionType) {
        this.sessionType = sessionType;
    }
}
