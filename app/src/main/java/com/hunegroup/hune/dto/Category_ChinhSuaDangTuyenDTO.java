package com.hunegroup.hune.dto;

/**
 * Created by USER on 07/08/2017.
 */

public class Category_ChinhSuaDangTuyenDTO {
    private int id;
    private String name;
    private String urlImage;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Category_ChinhSuaDangTuyenDTO(int id, String name, String urlImage) {
        this.id = id;
        this.name = name;
        this.urlImage = urlImage;
    }

    public Category_ChinhSuaDangTuyenDTO() {
    }
}
