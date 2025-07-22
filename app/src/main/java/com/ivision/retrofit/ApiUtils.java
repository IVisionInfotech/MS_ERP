package com.ivision.retrofit;

import com.ivision.utils.Constant;

public class ApiUtils {

    public static ApiInterface getApiCalling() {
        return RetrofitBuilder.getClient(Constant.baseURL).create(ApiInterface.class);
    }
}