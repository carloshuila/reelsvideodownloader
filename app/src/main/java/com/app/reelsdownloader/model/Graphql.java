package com.app.reelsdownloader.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Graphql implements Serializable {
    @SerializedName("shortcode_media")
    private ShortcodeMedia shortcode_media;

    public ShortcodeMedia getShortcode_media() {
        return this.shortcode_media;
    }
}
