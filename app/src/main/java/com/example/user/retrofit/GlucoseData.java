package com.example.user.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 08/08/2017.
 */

public class GlucoseData {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("glucose")
    @Expose
    private Integer glucose;
    public String getName(){
        return  name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Integer getGlucose(){
        return glucose;
    }
    public void setGlucose(Integer glucose){
        this.glucose = glucose;
    }
}
