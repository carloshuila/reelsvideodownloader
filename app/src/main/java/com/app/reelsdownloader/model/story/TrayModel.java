package com.app.reelsdownloader.model.story;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class TrayModel implements Serializable {
    @SerializedName("id")
    private String id;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }
}
