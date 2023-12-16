package com.example.myapp.Models;

import java.util.ArrayList;

public class InfoItem_colorModel {
    String colorDes;
    String img;
    ArrayList<Sizee> size;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public String getColorDes() {
        return colorDes;
    }

    public void setColorDes(String colorDes) {
        this.colorDes = colorDes;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ArrayList<Sizee> getSize() {
        return size;
    }

    public void setSize(ArrayList<Sizee> size) {
        this.size = size;
    }

    public InfoItem_colorModel(String colorDes, String img, ArrayList<Sizee> size) {
        this.colorDes = colorDes;
        this.img = img;
        this.size = size;
    }

    public InfoItem_colorModel() {
    }
}
