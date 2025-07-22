package com.ivision.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.ivision.R;
import com.ivision.databinding.ActivityAddCustomerBinding;
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

public class AddCustomerActivity extends BaseActivity {

    private ActivityAddCustomerBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private String name = "", contact = "", city = "", emailId = "", address = "", companyName = "", gstin = "", stateId = "";
    private ArrayList<CommonModel> stateList = new ArrayList<>();
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCustomerBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = AddCustomerActivity.this;

        RealmController.with(context).refresh();
        realm = RealmController.with(this).getRealm();

        setToolbar("Add Customer");

        init();

        getState();
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
        companyName = binding.etCompany.getText().toString();
        gstin = binding.etGSTIN.getText().toString();

        if (name.isEmpty()) {
            binding.etName.setError("Enter name");
        } else if (contact.isEmpty()) {
            binding.etContact.setError("Enter contact no");
        } else if (city.isEmpty()) {
            binding.etCity.setError("Enter city");
        } else {
            addCustomer();
        }
    }

    private void addCustomer() {
        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.addCustomer(Constant.addCustomerUrl, Common.getFranchiseId(), Common.getUserId(), name, contact, city, emailId, address, companyName, gstin, stateId, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Common.hideProgressDialog();
                try {
                    JsonObject jsonObject = response.body();
                    Common.showToast(jsonObject.get("message").getAsString());
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        Constant.customerArray = null;
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

    private void getState() {
        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.stateUrl, Common.getFranchiseId(), Common.getUserId(), Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Common.hideProgressDialog();
                try {
                    JsonObject jsonObject = response.body();
                    Common.showToast(jsonObject.get("message").getAsString());
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        JsonObject result = jsonObject.get("result").getAsJsonObject();
                        JsonArray jsonArray = result.get("listArray").getAsJsonArray();
                        bindStateData(jsonArray);
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

    private void bindStateData(JsonArray jsonArray) {

        stateList.clear();
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            CommonModel model = CommonParsing.bindCommonData(object);
            stateList.add(model);
            list.add(model.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_list_item, list);
        binding.spinner.setAdapter(adapter);

        binding.spinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                stateId = stateList.get(position).getStateId();
            }
        });
    }
}