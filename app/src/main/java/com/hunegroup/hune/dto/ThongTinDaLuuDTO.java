package com.hunegroup.hune.dto;

/**
 * Created by USER on 02/08/2017.
 */

public class ThongTinDaLuuDTO {
    private String id;
    private String user_id;
    private String type;
    private String source_id;
    private String created_at;
    private String updated_at;
    private Post_ThongTinDaLuuDTO post;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
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

    public Post_ThongTinDaLuuDTO getPost() {
        return post;
    }

    public void setPost(Post_ThongTinDaLuuDTO post) {
        this.post = post;
    }

    public ThongTinDaLuuDTO(String id, String user_id, String type, String source_id, String created_at, String updated_at, Post_ThongTinDaLuuDTO post) {
        this.id = id;
        this.user_id = user_id;
        this.type = type;
        this.source_id = source_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.post = post;
    }

    public ThongTinDaLuuDTO() {
    }
}
