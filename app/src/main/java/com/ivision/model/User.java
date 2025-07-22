package com.ivision.model;

import io.realm.RealmObject;

public class User extends RealmObject {

    private String id, franchiseId, franchiseName, franchiseContact, franchiseCity, name, contact, password, city, dateTimeAdded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(String franchiseId) {
        this.franchiseId = franchiseId;
    }

    public String getFranchiseName() {
        return franchiseName;
    }

    public void setFranchiseName(String franchiseName) {
        this.franchiseName = franchiseName;
    }

    public String getFranchiseContact() {
        return franchiseContact;
    }

    public void setFranchiseContact(String franchiseContact) {
        this.franchiseContact = franchiseContact;
    }

    public String getFranchiseCity() {
        return franchiseCity;
    }

    public void setFranchiseCity(String franchiseCity) {
        this.franchiseCity = franchiseCity;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDateTimeAdded() {
        return dateTimeAdded;
    }

    public void setDateTimeAdded(String dateTimeAdded) {
        this.dateTimeAdded = dateTimeAdded;
    }
}
