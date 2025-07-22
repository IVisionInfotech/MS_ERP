package com.ivision.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Mill implements Serializable {

    String millId, name, contact, city, basicPrice, franchiseStatus, dateTimeAdded;
    ArrayList<Size> cartList;

    public String getMillId() {
        return millId;
    }

    public void setMillId(String millId) {
        this.millId = millId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(String basicPrice) {
        this.basicPrice = basicPrice;
    }

    public String getFranchiseStatus() {
        return franchiseStatus;
    }

    public void setFranchiseStatus(String franchiseStatus) {
        this.franchiseStatus = franchiseStatus;
    }

    public String getDateTimeAdded() {
        return dateTimeAdded;
    }

    public void setDateTimeAdded(String dateTimeAdded) {
        this.dateTimeAdded = dateTimeAdded;
    }

    public ArrayList<Size> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Size> cartList) {
        this.cartList = cartList;
    }
}
