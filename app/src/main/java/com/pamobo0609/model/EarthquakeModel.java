package com.pamobo0609.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pamobo0609 on 2/17/17.
 */

public class EarthquakeModel implements Serializable {

    public EarthquakeModel(List<Feature> features) {
        this.features = features;
    }

    @SerializedName("features")
    @Expose
    private List<Feature> features = null;

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
