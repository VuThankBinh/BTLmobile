package com.example.myapp.Models;

public class UserModel {
    String name;
    String email;
    String password;

    String sdt;

    String img;

    public UserModel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public UserModel(String name, String email, String password, String sdt, String img) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.sdt = sdt;
        this.img = img;
    }
}
