package com.example.myapplication3;

import java.io.Serializable;

/**
 * @创建时间 2020/10/16 11:18
 */
public class Bean_one implements Serializable {
    private Integer id;
    private String disc;
    private String title;
    private String image;

    public Bean_one(Integer id, String disc, String title, String image) {
        this.id = id;
        this.disc = disc;
        this.title = title;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
