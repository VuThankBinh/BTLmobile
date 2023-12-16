package com.example.myapp.Models;

public class Sizee {
    String sizeDes;
    String quantity;

    public Sizee() {
    }
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public Sizee(String sizeDes, String quantity) {
        this.sizeDes = sizeDes;
        this.quantity = quantity;
    }

    public String getSizeDes() {
        return sizeDes;
    }

    public void setSizeDes(String sizeDes) {
        this.sizeDes = sizeDes;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
