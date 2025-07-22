package com.ivision.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.ivision.R;
import com.ivision.databinding.ActivityAddCustomerBinding;
import com.ivision.databinding.ActivityAddVendorBinding;
import com.ivision.databinding.ToolbarPrimaryBinding;
import com.ivision.model.CommonModel;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.Common;
import com.ivision.utils.CommonParsing;
import com.ivision.utils.Constant;
import com.ivision.utils.RealmController;

import java.util.ArrayList;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVendorActivity extends BaseActivity {

    private ActivityAddVendorBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private String name = "", contact = "", city = "", emailId = "", address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddVendorBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = AddVendorActivity.this;

        setToolbar("Add Vendor");

        init();
    }

    private void init() {
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    private void validate() {
        name = binding.etName.getText().toString();
        contact = binding.etContact.getText().toString();
        city = binding.etCity.getText().toString();
        emailId = binding.etEmail.getText().toString();
        address = binding.etAddress.getText().toString();

        if (name.isEmpty()) {
            binding.etName.setError("Enter name");
        } else if (contact.isEmpty()) {
            binding.etContact.setError("Enter contact no");
        } else if (city.isEmpty()) {
            binding.etCity.setError("Enter city");
        } else {
            addVendor();
        }
    }

    private void addVendor() {
        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.addVendor(Constant.addVendorUrl, Common.getFranchiseId(), Common.getUserId(), name, contact, city, emailId, address, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Common.hideProgressDialog();
                try {
                    JsonObject jsonObject = response.body();
                    Common.showToast(jsonObject.get("message").getAsString());
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        Constant.vendorArray = null;
                        setResultOfActivity(1, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Common.hideProgressDialog();
            }
        });
    }
}