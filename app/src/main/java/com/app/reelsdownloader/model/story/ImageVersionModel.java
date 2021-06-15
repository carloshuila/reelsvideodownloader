package com.app.reelsdownloader.model.story;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class ImageVersionModel implements Serializable {
    @SerializedName("candidates")
    private ArrayList<CandidatesModel> candidates;
}
