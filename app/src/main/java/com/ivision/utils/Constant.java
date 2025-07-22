package com.ivision.utils;

import com.google.gson.JsonArray;
import com.ivision.model.Product;

import java.util.ArrayList;

public class Constant {

    public static String baseURL = "https://madhavsteel.in/MSERP/API/FV2/";
    public static String detailsBaseURL = "www.gocamle.com/item-details.php?";
    public static String credentials = "admin:123";

    public static String loginBaseUrl = "api-login.php/";
    public static String loginUrl = loginBaseUrl + "login";
    public static String registerUrl = loginBaseUrl + "register";
    public static String resendOTPUrl = loginBaseUrl + "resendOTP";
    public static String verifyOTPUrl = loginBaseUrl + "verifyOTP";
    public static String forgetPasswordUrl = loginBaseUrl + "lostPassword";
    public static String generatePasswordUrl = loginBaseUrl + "generatePassword";

    public static String homeBaseUrl = "api-home.php/";
    public static String transportListUrl = homeBaseUrl + "getAllTransportList";
    public static String franchiseListUrl = homeBaseUrl + "getAllFranchiseList";
    public static String homeListUrl = homeBaseUrl + "getHomeScreenList";
    public static String searchListUrl = homeBaseUrl + "getSearchList";
    public static String bloodDonorUrl = homeBaseUrl + "uploadBloodDonorDetails";
    public static String registerUserUrl = homeBaseUrl + "registerUser";
    public static String userChallengeListUrl = homeBaseUrl + "getUserChallengeList";
    public static String stateUrl = homeBaseUrl + "getState";
    public static String countsUrl = homeBaseUrl + "getCounts";
    public static String customerCountsUrl = homeBaseUrl + "getCustomerCounts";

    public static String productBaseUrl = "api-product.php/";
    public static String productListUrl = productBaseUrl + "getAllProductList";
    public static String addProductUrl = productBaseUrl + "addProduct";
    public static String productDetailsUrl = productBaseUrl + "getMedicineDetails";

    public static String millBaseUrl = "api-mill.php/";
    public static String millListUrl = millBaseUrl + "getAllMillList";
    public static String addMillUrl = millBaseUrl + "addMill";
    public static String editMillPriceUrl = millBaseUrl + "editMillPrice";
    public static String millProductListUrl = millBaseUrl + "getMillProductList";
    public static String remainingProductListUrl = millBaseUrl + "getRemainingProductList";
    public static String addMillProductUrl = millBaseUrl + "addMillProduct";

    public static String vendorBaseUrl = "api-vendor.php/";
    public static String vendorListUrl = vendorBaseUrl + "getAllVendorList";
    public static String addVendorUrl = vendorBaseUrl + "addVendor";
    public static String vendorMillListUrl = vendorBaseUrl + "getVendorMillList";
    public static String vendorMillProductListUrl = vendorBaseUrl + "getVendorMillProductList";
    public static String vendorProductListUrl = vendorBaseUrl + "getVendorProductList";
    public static String vendorAddProductUrl = vendorBaseUrl + "vendorAddProduct";

    public static String customerBaseUrl = "api-customer.php/";
    public static String customerListUrl = customerBaseUrl + "getAllCustomerList";
    public static String searchCustomerListUrl = customerBaseUrl + "searchCustomerList";
    public static String addCustomerUrl = customerBaseUrl + "addCustomer";

    public static String orderBaseUrl = "api-order.php/";
    public static String sizeListUrl = orderBaseUrl + "getProductSizeList";
    public static String vendorSizeListUrl = orderBaseUrl + "getVendorSizeList";
    public static String vendorProductSizeListUrl = orderBaseUrl + "getVendorProductSizeList";
    public static String addCartUrl = orderBaseUrl + "addToCart";
    public static String updateCartUrl = orderBaseUrl + "editCart";
    public static String updateCartPriceUrl = orderBaseUrl + "editCartMillPrice";
    public static String updateOneKgPriceUrl = orderBaseUrl + "editOneKgPrice";
    public static String removeCartUrl = orderBaseUrl + "removeFromCart";
    public static String cartCustomerListUrl = orderBaseUrl + "getCartCustomerList";
    public static String cartListUrl = orderBaseUrl + "getCartList";
    public static String placeOrderUrl = orderBaseUrl + "placeOrder";
    public static String orderListUrl = orderBaseUrl + "getAllOrderList";
    public static String updateStatusUrl = orderBaseUrl + "editOrderStatus";

    public static JsonArray customerArray = null;
    public static JsonArray millArray = null;
    public static JsonArray vendorArray = null;
    public static JsonArray categoryArray = null;
    public static JsonArray listArray1 = null;
    public static JsonArray listArray2 = null;
    public static JsonArray listArray3 = null;
    public static JsonArray listArray4 = null;

    public static boolean refreshStatus = false;
    public static boolean _hasLoadedOnce = false;

    public static int space = -13;
    public static int plyType = 1;
    public static int boxType = 2;
    public static int printType = 3;

    public static String pages = "";
    public static String refreshCount = "refreshCount";
    public static String customerId = "";
    public static String selectedItem = "";
    public static String selectedQuantity = "";
    public static String selectedSubItem = "";

    public static ArrayList<String> reasonList = new ArrayList<>();
//    public static ArrayList<Category> selectedSubItemList = new ArrayList<>();

    public static ArrayList<Product> selectedItemList = new ArrayList<>();

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";
}