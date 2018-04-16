package com.hunegroup.hune.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tskil on 8/19/2017.
 */

public class MetaDataDTO {
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("current_page")
    @Expose
    private int current_page;
    @SerializedName("has_more_page")
    @Expose
    private boolean has_more_page;
    @SerializedName("next_link")
    @Expose
    private String next_link;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public boolean isHas_more_page() {
        return has_more_page;
    }

    public void setHas_more_page(boolean has_more_page) {
        this.has_more_page = has_more_page;
    }

    public String getNext_link() {
        return next_link;
    }

    public void setNext_link(String next_link) {
        this.next_link = next_link;
    }
}
