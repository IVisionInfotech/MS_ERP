package com.ivision.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {

    @FormUrlEncoded
    @POST
    Call<JsonObject> login(
            @Url String url,
            @Field("username") String username,
            @Field("password") String password,
            @Field("playerId") String playerId
    );

    @FormUrlEncoded
    @POST
    Call<JsonObject> getCounts(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST
    Call<JsonObject> getCustomerCounts(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("customerId") String customerId,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getVendorProductList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("vendorId") String vendorId,
            @Field("millId") String millId,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getVendorProductList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("vendorId") String vendorId,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getCustomerList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("listType") String listType,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getVendorMillList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("vendorId") String vendorId,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("millId") String millId,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("type") String type,
            @Field("limit") String limit,
            @Field("offset") String offset,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("type") String type,
            @Field("customerId") String customerId,
            @Field("limit") String limit,
            @Field("offset") String offset,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getCartList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("customerId") String customerId,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("millId") String millId,
            @Field("productId") String productId,
            @Field("categoryId") String categoryId,
            @Field("limit") String limit,
            @Field("offset") String offset,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("limit") String limit,
            @Field("offset") String offset,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> searchList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("keyword") String keyword,
            @Field("limit") String limit,
            @Field("offset") String offset,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("customerId") String customerId,
            @Field("vendorId") String vendorId,
            @Field("millId") String millId,
            @Field("productId") String productId,
            @Field("categoryId") String categoryId,
            @Field("limit") String limit,
            @Field("offset") String offset,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getList(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("customerId") String customerId,
            @Field("vendorId") String vendorId,
            @Field("productId") String productId,
            @Field("categoryId") String categoryId,
            @Field("limit") String limit,
            @Field("offset") String offset,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> addProduct(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("name") String name,
            @Field("addStatus") String addStatus,
            @Field("categoryName") String categoryName,
            @Field("image") String image,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> addProduct(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("vendorId") String vendorId,
            @Field("name") String name,
            @Field("categoryName") String categoryName,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> addMill(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("name") String name,
            @Field("contact") String contact,
            @Field("city") String city,
            @Field("basicPrice") String basicPrice,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> editMillPrice(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("millId") String millId,
            @Field("basicPrice") String basicPrice,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> editCartPrice(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("cartId") String cartId,
            @Field("basicPrice") String basicPrice,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> addMillProductUrl(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("millId") String millId,
            @Field("productId") String productId,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> addCustomer(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("name") String name,
            @Field("contact") String contact,
            @Field("city") String city,
            @Field("emailId") String emailId,
            @Field("address") String address,
            @Field("companyName") String companyName,
            @Field("gstin") String gstin,
            @Field("stateId") String stateId,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> addVendor(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("name") String name,
            @Field("contact") String contact,
            @Field("city") String city,
            @Field("emailId") String emailId,
            @Field("address") String address,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> addCart(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("customerId") String customerId,
            @Field("vendorId") String vendorId,
            @Field("millId") String millId,
            @Field("productId") String productId,
            @Field("catId") String catId,
            @Field("sizeId") String sizeId,
            @Field("millBasicPrice") String millBasicPrice,
            @Field("basicPrice") String basicPrice,
            @Field("diffPrice") String diffPrice,
            @Field("quantity") String quantity,
            @Field("piece") String piece,
            @Field("type") String type,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> addCart(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("customerId") String customerId,
            @Field("vendorId") String vendorId,
            @Field("productId") String productId,
            @Field("catId") String catId,
            @Field("sizeId") String sizeId,
            @Field("quantity") String quantity,
            @Field("piece") String piece,
            @Field("type") String type,
            @Field("price") String price,
            @Field("productType") String productType,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> updateCart(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("cartId") String cartId,
            @Field("quantity") String quantity,
            @Field("piece") String piece,
            @Field("type") String type,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> updateCart(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("cartId") String cartId,
            @Field("quantity") String quantity,
            @Field("piece") String piece,
            @Field("type") String type,
            @Field("price") String price,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> removeCart(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("userId") String userId,
            @Field("cartId") String cartId,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> updateStatus(
            @Url String url,
            @Field("franchiseId") String franchiseId,
            @Field("orderId") String orderId,
            @Field("status") String status,
            @Field("username") String username,
            @Field("userPassword") String userPassword
    );

}