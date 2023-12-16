package com.example.myapp.cart;

public class CartItemProduct {
    String id;
    String name;
    String imgD;
    String color;
    String size;
    String idSP;
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getIdSP() {
        return idSP;
    }

    public void setIdSP(String idSP) {
        this.idSP = idSP;
    }

    public CartItemProduct(String id, String name, String imgD, String color, String size, String price, String quantity, String idsp) {
        this.id = id;
        this.name = name;
        this.imgD = imgD;
        this.color = color;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.idSP=idsp;
    }

    String price;
    String quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgD() {
        return imgD;
    }

    public void setImgD(String imgD) {
        this.imgD = imgD;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public CartItemProduct() {
    }
}
