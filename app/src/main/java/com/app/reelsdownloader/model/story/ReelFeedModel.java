package com.app.reelsdownloader.model.story;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class ReelFeedModel implements Serializable {
    @SerializedName("expiring_atexpiring_at")
    private long expiring_at;
    @SerializedName("id")
    private long id;
    @SerializedName("items")
    private ArrayList<ItemModel> items;
    @SerializedName("latest_reel_media")
    private long latest_reel_media;
    @SerializedName("media_count")
    private int media_count;
    @SerializedName("reel_type")
    private String reel_type;
    @SerializedName("seen")
    private long seen;

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public void setLatest_reel_media(long j) {
        this.latest_reel_media = j;
    }

    public ArrayList<ItemModel> getItems() {
        return this.items;
    }
}
