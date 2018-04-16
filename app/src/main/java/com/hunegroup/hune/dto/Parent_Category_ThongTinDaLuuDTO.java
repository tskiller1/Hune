package com.hunegroup.hune.dto;

/**
 * Created by USER on 02/08/2017.
 */

public class Parent_Category_ThongTinDaLuuDTO {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parent_Category_ThongTinDaLuuDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Parent_Category_ThongTinDaLuuDTO() {
    }
}
