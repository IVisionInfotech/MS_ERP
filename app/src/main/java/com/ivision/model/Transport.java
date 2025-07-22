package com.ivision.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Transport implements Serializable {

    String id, userId, name, contact, city, stateId, stateName;
    ArrayList<TransportVehicle> vehicleList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public ArrayList<TransportVehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(ArrayList<TransportVehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }
}
