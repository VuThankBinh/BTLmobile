package com.example.myapp.Models;

import java.util.ArrayList;
import com.example.myapp.Models.InfoItem_colorModel;

public class InFoItemModel {
    String name;
    String giaC;
    String giaM;
    String rating;
    String loai;
    String imgD;
    ArrayList<InfoItem_colorModel> models;

    String count_color;

    public InFoItemModel() {
    }

    public String getImgD() {
        return imgD;
    }

    public void setImgD(String imgD) {
        this.imgD = imgD;
    }

    public InFoItemModel(String name, String giaC, String giaM, String rating, String loai, String imgD, ArrayList<InfoItem_colorModel> models,String count_color) {
        this.name = name;
        this.giaC = giaC;
        this.giaM = giaM;
        this.rating = rating;
        this.loai = loai;
        this.models = models;
        this.imgD=imgD;
        this.count_color=count_color;
    }

    public String getCount_color() {
        return count_color;
    }

    public void setCount_color(String count_color) {
        this.count_color = count_color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGiaC() {
        return giaC;
    }

    public void setGiaC(String giaC) {
        this.giaC = giaC;
    }

    public String getGiaM() {
        return giaM;
    }

    public void setGiaM(String giaM) {
        this.giaM = giaM;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public ArrayList<InfoItem_colorModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<InfoItem_colorModel> models) {
        this.models = models;
    }
}
