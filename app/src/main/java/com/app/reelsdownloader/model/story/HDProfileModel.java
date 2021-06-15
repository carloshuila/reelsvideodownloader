package com.app.reelsdownloader.model.story;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class HDProfileModel implements Serializable {
    @SerializedName("height")
    private int height;
    @SerializedName("url")
    private String url;
    @SerializedName("width")
    private int width;

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int i) {
        this.height = i;
    }
}
