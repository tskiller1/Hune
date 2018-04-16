package com.hunegroup.hune.dto;

/**
 * Created by USER on 04/08/2017.
 */

public class Parent_Category_ThongTinTimViecDTO {
    private int id;
    private String name;

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

    public Parent_Category_ThongTinTimViecDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Parent_Category_ThongTinTimViecDTO() {
    }
}
