package com.example.myapp.Models;

public class PopulartModel {
    String name;
    String description;

    String rating;

    String type;
    String img;
    String count_color;
    String Id;
    public PopulartModel() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public PopulartModel(String id, String name, String description, String rating, String type, String img, String count_color) {
        this.Id=id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.type = type;
        this.img = img;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
