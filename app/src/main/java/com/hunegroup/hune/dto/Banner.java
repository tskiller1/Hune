package com.hunegroup.hune.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by apple on 12/19/17.
 */

public class Banner implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("position")
    @Expose
    private int position;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("cover")
    @Expose
    private String cover;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
