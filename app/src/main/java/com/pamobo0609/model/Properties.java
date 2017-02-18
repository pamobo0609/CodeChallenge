package com.pamobo0609.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pamobo0609 on 2/17/17.
 */

public class Properties {
    @SerializedName("mag")
    @Expose
    private double mag;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("updated")
    @Expose
    private long updated;
    @SerializedName("tz")
    @Expose
    private long tz;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("felt")
    @Expose
    private Object felt;
    @SerializedName("cdi")
    @Expose
    private Object cdi;
    @SerializedName("mmi")
    @Expose
    private Object mmi;
    @SerializedName("alert")
    @Expose
    private Object alert;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tsunami")
    @Expose
    private long tsunami;
    @SerializedName("sig")
    @Expose
    private long sig;
    @SerializedName("net")
    @Expose
    private String net;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("ids")
    @Expose
    private String ids;
    @SerializedName("sources")
    @Expose
    private String sources;
    @SerializedName("types")
    @Expose
    private String types;
    @SerializedName("nst")
    @Expose
    private long nst;
    @SerializedName("dmin")
    @Expose
    private double dmin;
    @SerializedName("rms")
    @Expose
    private double rms;
    @SerializedName("gap")
    @Expose
    private float gap;
    @SerializedName("magType")
    @Expose
    private String magType;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public long getTz() {
        return tz;
    }

    public void setTz(long tz) {
        this.tz = tz;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Object getFelt() {
        return felt;
    }

    public void setFelt(Object felt) {
        this.felt = felt;
    }

    public Object getCdi() {
        return cdi;
    }

    public void setCdi(Object cdi) {
        this.cdi = cdi;
    }

    public Object getMmi() {
        return mmi;
    }

    public void setMmi(Object mmi) {
        this.mmi = mmi;
    }

    public Object getAlert() {
        return alert;
    }

    public void setAlert(Object alert) {
        this.alert = alert;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTsunami() {
        return tsunami;
    }

    public void setTsunami(long tsunami) {
        this.tsunami = tsunami;
    }

    public long getSig() {
        return sig;
    }

    public void setSig(long sig) {
        this.sig = sig;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public long getNst() {
        return nst;
    }

    public void setNst(long nst) {
        this.nst = nst;
    }

    public double getDmin() {
        return dmin;
    }

    public void setDmin(double dmin) {
        this.dmin = dmin;
    }

    public double getRms() {
        return rms;
    }

    public void setRms(double rms) {
        this.rms = rms;
    }

    public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }

    public String getMagType() {
        return magType;
    }

    public void setMagType(String magType) {
        this.magType = magType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
