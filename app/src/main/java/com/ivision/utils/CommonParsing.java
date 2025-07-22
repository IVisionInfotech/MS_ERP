package com.ivision.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.model.CommonModel;
import com.ivision.model.Customer;
import com.ivision.model.Franchise;
import com.ivision.model.Mill;
import com.ivision.model.Order;
import com.ivision.model.OrderDetails;
import com.ivision.model.Product;
import com.ivision.model.Size;
import com.ivision.model.Transport;
import com.ivision.model.TransportVehicle;
import com.ivision.model.User;
import com.ivision.model.Vendor;

import java.util.ArrayList;

public class CommonParsing {

    public static User bindUserData(JsonObject object, User model) {

        if (object.has("userId")) model.setId(object.get("userId").getAsString());
        if (object.has("franchiseId"))
            model.setFranchiseId(object.get("franchiseId").getAsString());
        if (object.has("franchiseName"))
            model.setFranchiseName(object.get("franchiseName").getAsString());
        if (object.has("franchiseContact"))
            model.setFranchiseContact(object.get("franchiseContact").getAsString());
        if (object.has("franchiseCity"))
            model.setFranchiseCity(object.get("franchiseCity").getAsString());
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("contact")) model.setContact(object.get("contact").getAsString());
        if (object.has("password")) model.setPassword(object.get("password").getAsString());
        if (object.has("city")) model.setCity(object.get("city").getAsString());
        if (object.has("dateTimeAdded"))
            model.setDateTimeAdded(object.get("dateTimeAdded").getAsString());

        return model;
    }

    public static CommonModel bindCommonData(JsonObject object) {

        CommonModel model = new CommonModel();
        if (object.has("stateId")) model.setStateId(object.get("stateId").getAsString());
        if (object.has("catId")) model.setCatId(object.get("catId").getAsString());
        if (object.has("productId")) model.setProductId(object.get("productId").getAsString());
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("image")) model.setImage(object.get("image").getAsString());
        if (object.has("dateTimeAdded"))
            model.setDateTimeAdded(object.get("dateTimeAdded").getAsString());

        return model;
    }

    public static Product bindProductData(JsonObject object) {

        Product model = new Product();
        if (object.has("productId")) model.setProductId(object.get("productId").getAsString());
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("description"))
            model.setDescription(object.get("description").getAsString());
        if (object.has("image")) model.setImage(object.get("image").getAsString());
        if (object.has("basicPrice")) model.setBasicPrice(object.get("basicPrice").getAsString());
        if (object.has("addStatus")) model.setAddStatus(object.get("addStatus").getAsString());
        if (object.has("dateTimeAdded"))
            model.setDateTimeAdded(object.get("dateTimeAdded").getAsString());

        if (object.has("detailsArray")) {
            JsonArray jsonArray = object.get("detailsArray").getAsJsonArray();
            ArrayList<CommonModel> arrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                CommonModel product = bindCommonData(jsonObject);
                arrayList.add(product);
            }
            model.setCategoryList(arrayList);
        }

        return model;
    }

    public static Mill bindMillData(JsonObject object) {

        Mill model = new Mill();
        if (object.has("millId")) model.setMillId(object.get("millId").getAsString());
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("contact")) model.setContact(object.get("contact").getAsString());
        if (object.has("city")) model.setCity(object.get("city").getAsString());
        if (object.has("basicPrice")) model.setBasicPrice(object.get("basicPrice").getAsString());
        if (object.has("franchiseStatus"))
            model.setFranchiseStatus(object.get("franchiseStatus").getAsString());
        if (object.has("dateTimeAdded"))
            model.setDateTimeAdded(object.get("dateTimeAdded").getAsString());

        if (object.has("detailsArray")) {
            JsonArray jsonArray = object.get("detailsArray").getAsJsonArray();
            ArrayList<Size> arrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                Size size = bindSizeData(jsonObject);
                arrayList.add(size);
            }
            model.setCartList(arrayList);
        }

        return model;
    }

    public static Size bindSizeData(JsonObject object) {

        Size model = new Size();
        if (object.has("sizeId")) model.setSizeId(object.get("sizeId").getAsString());
        if (object.has("millId")) model.setMillId(object.get("millId").getAsString());
        if (object.has("catId")) model.setCatId(object.get("catId").getAsString());
        if (object.has("catName")) model.setCatName(object.get("catName").getAsString());
        if (object.has("productId")) model.setProductId(object.get("productId").getAsString());
        if (object.has("productName"))
            model.setProductName(object.get("productName").getAsString());
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("mType")) model.setmType(object.get("mType").getAsString());
        if (object.has("nosKg")) model.setNosKg(object.get("nosKg").getAsString());
        if (object.has("diffPrice")) model.setDiffPrice(object.get("diffPrice").getAsString());
        if (object.has("basicPrice")) model.setBasicPrice(object.get("basicPrice").getAsString());
        if (object.has("cartId")) model.setCartId(object.get("cartId").getAsString());
        if (object.has("cartMillBasicPrice"))
            model.setCartMillBasicPrice(object.get("cartMillBasicPrice").getAsString());
        if (object.has("cartBasicPrice"))
            model.setCartBasicPrice(object.get("cartBasicPrice").getAsString());
        if (object.has("basicPrice")) model.setBasicPrice(object.get("basicPrice").getAsString());
        if (object.has("cartDiffPrice"))
            model.setCartDiffPrice(object.get("cartDiffPrice").getAsString());
        if (object.has("systemPrice"))
            model.setSystemPrice(object.get("systemPrice").getAsString());
        if (object.has("oneKgPrice")) model.setOneKgPrice(object.get("oneKgPrice").getAsString());
        if (object.has("quantity")) model.setQuantity(object.get("quantity").getAsString());
        if (object.has("piece")) model.setPiece(object.get("piece").getAsString());
        if (object.has("weight")) model.setWeight(object.get("weight").getAsString());
        if (object.has("cartMType")) model.setCartMType(object.get("cartMType").getAsString());
        if (object.has("productType"))
            model.setProductType(object.get("productType").getAsString());
        if (object.has("dateTimeAdded"))
            model.setDateTimeAdded(object.get("dateTimeAdded").getAsString());

        return model;
    }

    public static Customer bindCustomerData(JsonObject object) {

        Customer model = new Customer();
        if (object.has("customerId")) model.setCustomerId(object.get("customerId").getAsString());
        if (object.has("userId")) model.setUserId(object.get("userId").getAsString());
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("contact")) model.setContact(object.get("contact").getAsString());
        if (object.has("city")) model.setCity(object.get("city").getAsString());
        if (object.has("emailId")) model.setEmailId(object.get("emailId").getAsString());
        if (object.has("address")) model.setAddress(object.get("address").getAsString());
        if (object.has("companyName"))
            model.setCompanyName(object.get("companyName").getAsString());
        if (object.has("gstin")) model.setGstin(object.get("gstin").getAsString());
        if (object.has("stateId")) model.setStateId(object.get("stateId").getAsString());
        if (object.has("stateName")) model.setStateName(object.get("stateName").getAsString());
        if (object.has("dateTimeAdded"))
            model.setDateTimeAdded(object.get("dateTimeAdded").getAsString());

        return model;
    }

    public static Vendor bindVendorData(JsonObject object) {

        Vendor model = new Vendor();
        if (object.has("vendorId")) model.setVendorId(object.get("vendorId").getAsString());
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("contact")) model.setContact(object.get("contact").getAsString());
        if (object.has("emailId")) model.setEmailId(object.get("emailId").getAsString());
        if (object.has("address")) model.setAddress(object.get("address").getAsString());
        if (object.has("city")) model.setCity(object.get("city").getAsString());

        return model;
    }

    public static TransportVehicle bindTransportVehicleData(JsonObject object) {

        TransportVehicle model = new TransportVehicle();
        if (object.has("id")) model.setId(object.get("id").getAsString());
        if (object.has("transportId"))
            model.setTransportId(object.get("transportId").getAsString());
        if (object.has("vehicleName"))
            model.setVehicleName(object.get("vehicleName").getAsString());
        if (object.has("fromCity")) model.setFromCity(object.get("fromCity").getAsString());
        if (object.has("toCity")) model.setToCity(object.get("toCity").getAsString());

        return model;
    }

    public static Transport bindTransportData(JsonObject object) {

        Transport model = new Transport();
        if (object.has("id")) model.setId(object.get("id").getAsString());
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("contact")) model.setContact(object.get("contact").getAsString());
        if (object.has("city")) model.setCity(object.get("city").getAsString());
        if (object.has("stateId")) model.setStateId(object.get("stateId").getAsString());
        if (object.has("stateName")) model.setStateName(object.get("stateName").getAsString());

        if (object.has("detailsArray")) {
            JsonArray jsonArray = object.get("detailsArray").getAsJsonArray();
            ArrayList<TransportVehicle> arrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                TransportVehicle product = bindTransportVehicleData(jsonObject);
                arrayList.add(product);
            }
            model.setVehicleList(arrayList);
        }

        return model;
    }

    public static Franchise bindFranchiseData(JsonObject object) {

        Franchise model = new Franchise();
        if (object.has("franchiseId"))
            model.setFranchiseId(object.get("franchiseId").getAsString());
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("contact")) model.setContact(object.get("contact").getAsString());
        if (object.has("password")) model.setPassword(object.get("password").getAsString());
        if (object.has("city")) model.setCity(object.get("city").getAsString());
        if (object.has("image")) model.setImage(object.get("image").getAsString());
        if (object.has("emailId")) model.setEmailId(object.get("emailId").getAsString());
        if (object.has("address")) model.setAddress(object.get("address").getAsString());
        if (object.has("gstin")) model.setGstin(object.get("gstin").getAsString());
        if (object.has("stateId")) model.setStateId(object.get("stateId").getAsString());
        if (object.has("stateName")) model.setStateName(object.get("stateName").getAsString());
        if (object.has("dateTimeAdded"))
            model.setDateTimeAdded(object.get("dateTimeAdded").getAsString());

        return model;
    }

    public static OrderDetails bindOrderDetailsData(JsonObject object) {

        OrderDetails model = new OrderDetails();
        if (object.has("id")) model.setId(object.get("id").getAsString());
        if (object.has("orderId")) model.setOrderId(object.get("orderId").getAsString());
        if (object.has("vendorId")) model.setVendorId(object.get("vendorId").getAsString());
        if (object.has("millId")) model.setMillId(object.get("millId").getAsString());
        if (object.has("productId")) model.setProductId(object.get("productId").getAsString());
        if (object.has("catId")) model.setCatId(object.get("catId").getAsString());
        if (object.has("sizeId")) model.setSizeId(object.get("sizeId").getAsString());
        if (object.has("millBasicPrice"))
            model.setMillBasicPrice(object.get("millBasicPrice").getAsString());
        if (object.has("basicPrice")) model.setBasicPrice(object.get("basicPrice").getAsString());
        if (object.has("diffPrice")) model.setDiffPrice(object.get("diffPrice").getAsString());
        if (object.has("systemPrice"))
            model.setSystemPrice(object.get("systemPrice").getAsString());
        if (object.has("oneKgPrice")) model.setOneKgPrice(object.get("oneKgPrice").getAsString());
        if (object.has("quantity")) model.setQuantity(object.get("quantity").getAsString());
        if (object.has("piece")) model.setPiece(object.get("piece").getAsString());
        if (object.has("mType")) model.setmType(object.get("mType").getAsString());
        if (object.has("status")) model.setStatus(object.get("status").getAsString());
        if (object.has("millName")) model.setMillName(object.get("millName").getAsString());
        if (object.has("millContact"))
            model.setMillContact(object.get("millContact").getAsString());
        if (object.has("millCity")) model.setMillCity(object.get("millCity").getAsString());
        if (object.has("productName"))
            model.setProductName(object.get("productName").getAsString());
        if (object.has("catName")) model.setCatName(object.get("catName").getAsString());
        if (object.has("sizeName")) model.setSizeName(object.get("sizeName").getAsString());
        if (object.has("productType"))
            model.setProductType(object.get("productType").getAsString());

        return model;
    }

    public static Order bindOrderData(JsonObject object) {

        Order model = new Order();
        if (object.has("id")) model.setId(object.get("id").getAsString());
        if (object.has("orderPrefix"))
            model.setOrderPrefix(object.get("orderPrefix").getAsString());
        if (object.has("orderNo")) model.setOrderNo(object.get("orderNo").getAsString());
        if (object.has("userId")) model.setUserId(object.get("userId").getAsString());
        if (object.has("customerId")) model.setCustomerId(object.get("customerId").getAsString());
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("contact")) model.setContact(object.get("contact").getAsString());
        if (object.has("city")) model.setCity(object.get("city").getAsString());
        if (object.has("companyName"))
            model.setCompanyName(object.get("companyName").getAsString());
        if (object.has("gst")) model.setGst(object.get("gst").getAsString());
        if (object.has("link")) model.setLink(object.get("link").getAsString());
        if (object.has("status")) model.setStatus(object.get("status").getAsString());
        if (object.has("dateTimeAdded"))
            model.setDateTimeAdded(object.get("dateTimeAdded").getAsString());

        if (object.has("detailsArray")) {
            JsonArray jsonArray = object.get("detailsArray").getAsJsonArray();
            ArrayList<OrderDetails> arrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                OrderDetails details = bindOrderDetailsData(jsonObject);
                arrayList.add(details);
            }
            model.setDetailsList(arrayList);
        }

        return model;
    }

}
