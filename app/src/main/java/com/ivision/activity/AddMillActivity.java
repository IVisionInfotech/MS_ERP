package com.ivision.activity;

import android.os.Bundle;
import android.view.View;

import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.databinding.ActivityAddMillBinding;
import com.ivision.databinding.ToolbarPrimaryBinding;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.Common;
import com.ivision.utils.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMillActivity extends BaseActivity {

    private ActivityAddMillBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private String name = "", contact = "", price = "", city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMillBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = AddMillActivity.this;

        setToolbar("Add Mill");

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
        price = binding.etPrice.getText().toString();

        if (name.isEmpty()) {
            binding.etName.setError("Enter name");
        } else if (contact.isEmpty()) {
            binding.etContact.setError("Enter contact");
        } else if (city.isEmpty()) {
            binding.etCity.setError("Enter city");
        } else if (price.isEmpty()) {
            binding.etPrice.setError("Enter price");
        } else {
            addMill();
        }
    }

    private void addMill() {
        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.addMill(Constant.addMillUrl, Common.getFranchiseId(), Common.getUserId(), name, contact, city, price, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Common.hideProgressDialog();
                try {
                    JsonObject jsonObject = response.body();
                    Common.showToast(jsonObject.get("message").getAsString());
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        Constant.millArray = null;
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