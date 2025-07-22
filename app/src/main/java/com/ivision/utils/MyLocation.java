package com.ivision.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocation {

    public static String getPlaceLatitude(String input) {
        String message = "";
        int open = input.indexOf("(");
        int close = input.indexOf(")");
        if (open != -1 && close != -1) {
            message = input.substring(open + 1, close);
        }
        String[] placeLat = message.split(",");
        return placeLat[0];
    }

    public static String getPlaceLongitude(String input) {
        String message = "";
        int open = input.indexOf("(");
        int close = input.indexOf(")");
        if (open != -1 && close != -1) {
            message = input.substring(open + 1, close);
        }
        String[] placeLong = message.split(",");
        return placeLong[1];
    }

    public static String getPincode(Context context, String gps_latitude, String gps_longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String pinCode = "";
        try {
            double latitude = Double.parseDouble(gps_latitude);
            double longitude = Double.parseDouble(gps_longitude);
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                pinCode = address.getPostalCode();
            }
        } catch (IOException e) {
            Log.e("Error", "Unable connect to Geocoder", e);
        }
        return pinCode;
    }
}