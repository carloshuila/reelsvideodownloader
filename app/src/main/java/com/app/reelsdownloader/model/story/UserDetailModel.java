package com.app.reelsdownloader.model.story;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserDetailModel implements Serializable {
    @SerializedName("user")
    private User user;
}
