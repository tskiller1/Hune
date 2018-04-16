package com.hunegroup.hune.dto;

/**
 * Created by tskil on 8/25/2017.
 */

public class MessageDTO {
    String message;
    String title;

    public MessageDTO(){

    }
    public MessageDTO(String message, String title) {
        this.message = message;
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
