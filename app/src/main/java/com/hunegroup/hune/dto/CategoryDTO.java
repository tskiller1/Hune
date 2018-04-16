package com.hunegroup.hune.dto;

import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tskil on 7/28/2017.
 */

public class CategoryDTO implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parent_id")
    @Expose
    private int parent_id;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("child")
    @Expose
    private List<CategoryDTO> child;

    private transient CheckBox checkBox;
    private transient TextView txtParent;
    private transient List<TextView> listTxtChild;

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

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<CategoryDTO> getChild() {
        return child;
    }

    public void setChild(List<CategoryDTO> child) {
        this.child = child;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public TextView getTxtParent() {
        return txtParent;
    }

    public void setTxtParent(TextView txtParent) {
        this.txtParent = txtParent;
    }

    public List<TextView> getListTxtChild() {
        return listTxtChild;
    }

    public void setListTxtChild(List<TextView> listTxtChild) {
        this.listTxtChild = listTxtChild;
    }
}
