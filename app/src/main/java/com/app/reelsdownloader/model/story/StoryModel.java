package com.app.reelsdownloader.model.story;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class StoryModel implements Serializable {
    @SerializedName("status")
    private String status;
    @SerializedName("tray")
    private ArrayList<TrayModel> tray;
}
