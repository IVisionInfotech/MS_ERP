package com.ivision.utils;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonAPI {

    private static String TAG = "CommonParsing";

    public interface OnResponseList {
        void OnResponse(JsonArray jsonArray);
    }

    public interface OnResponseObject {
        void OnResponse(JsonObject jsonObject);
    }

    public static void getCounts(Context context, OnResponseObject positiveResponse) {

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getCounts(Constant.countsUrl, Common.getFranchiseId(), Common.getUserId(), Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        JsonObject result = jsonObject.get("result").getAsJsonObject();
                        positiveResponse.OnResponse(result);
                    } else {
                        Common.showToast(jsonObject.get("message").getAsString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void getCustomerCartCounts(Context context, String customerId, OnResponseObject positiveResponse) {

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getCustomerCounts(Constant.customerCountsUrl, Common.getFranchiseId(), Common.getUserId(), customerId, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        JsonObject result = jsonObject.get("result").getAsJsonObject();
                        positiveResponse.OnResponse(result);
                    } else {
                        Common.showToast(jsonObject.get("message").getAsString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
