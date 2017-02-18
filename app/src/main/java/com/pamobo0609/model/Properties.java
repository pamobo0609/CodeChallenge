package com.pamobo0609.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pamobo0609 on 2/17/17.
 */

public class Properties implements Serializable {
    @SerializedName("mag")
    @Expose
    private double mag;
    @SerializedName("place")
    @Expose
    private String place;


    public double getMag() {
        return mag;
    }

    public void setMag(double mag) {
        this.mag = mag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

}
