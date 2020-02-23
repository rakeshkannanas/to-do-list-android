package com.kannan.todohometask.Model;

import org.json.JSONObject;


//Data Encapsulation
public class DataModel {
    private String date;
    private String id;
    private String data;
    private String time;


    public DataModel(JSONObject jsonObject){
        if(jsonObject == null) return;
        this.date = jsonObject.optString("Date");
        this.id = jsonObject.optString("ID");//
        this.time = jsonObject.optString("Time");
        this.data = jsonObject.optString("Data");
    }

    public String getdate() {
        return date;
    }
    public String getdata() {
        return data;
    }
    public String gettime() {
        return time;
    }
    public String getid() {
        return id;
    }
}

