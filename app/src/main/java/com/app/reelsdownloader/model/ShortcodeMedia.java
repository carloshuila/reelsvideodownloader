package com.app.reelsdownloader.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class ShortcodeMedia implements Serializable {
    @SerializedName("accessibility_caption")
    private String accessibility_caption;
    @SerializedName("display_resources")
    private List<DisplayResource> display_resources;
    @SerializedName("display_url")
    private String display_url;
    @SerializedName("edge_sidecar_to_children")
    private EdgeSidecarToChildren edge_sidecar_to_children;
    @SerializedName("is_video")
    private boolean is_video;
    @SerializedName("video_url")
    private String video_url;

    public void setDisplay_url(String str) {
        this.display_url = str;
    }

    public List<DisplayResource> getDisplay_resources() {
        return this.display_resources;
    }

    public void setDisplay_resources(List<DisplayResource> list) {
        this.display_resources = list;
    }

    public boolean isIs_video() {
        return this.is_video;
    }

    public String getVideo_url() {
        return this.video_url;
    }

    public EdgeSidecarToChildren getEdge_sidecar_to_children() {
        return this.edge_sidecar_to_children;
    }
}
