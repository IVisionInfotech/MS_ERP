package com.ivision.model;

import java.io.Serializable;

public class OrderDetails implements Serializable {

    String id, orderId, vendorId, millId, productId, catId, sizeId, millBasicPrice, basicPrice, diffPrice, systemPrice, oneKgPrice, quantity,
            piece, mType, status, millName, millContact, millCity, productName, catName, sizeName, productType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getMillId() {
        return millId;
    }

    public void setMillId(String millId) {
        this.millId = millId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getMillBasicPrice() {
        return millBasicPrice;
    }

    public void setMillBasicPrice(String millBasicPrice) {
        this.millBasicPrice = millBasicPrice;
    }

    public String getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(String basicPrice) {
        this.basicPrice = basicPrice;
    }

    public String getDiffPrice() {
        return diffPrice;
    }

    public void setDiffPrice(String diffPrice) {
        this.diffPrice = diffPrice;
    }

    public String getSystemPrice() {
        return systemPrice;
    }

    public void setSystemPrice(String systemPrice) {
        this.systemPrice = systemPrice;
    }

    public String getOneKgPrice() {
        return oneKgPrice;
    }

    public void setOneKgPrice(String oneKgPrice) {
        this.oneKgPrice = oneKgPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMillName() {
        return millName;
    }

    public void setMillName(String millName) {
        this.millName = millName;
    }

    public String getMillContact() {
        return millContact;
    }

    public void setMillContact(String millContact) {
        this.millContact = millContact;
    }

    public String getMillCity() {
        return millCity;
    }

    public void setMillCity(String millCity) {
        this.millCity = millCity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
