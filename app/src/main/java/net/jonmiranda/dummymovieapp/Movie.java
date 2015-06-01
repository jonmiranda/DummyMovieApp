package net.jonmiranda.dummymovieapp;

import io.realm.RealmObject;

public class Movie extends RealmObject {

    private String title;

    private int imgId;

    private int color;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
