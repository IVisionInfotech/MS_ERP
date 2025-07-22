package com.ivision.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.adapter.SelectVendorListAdapter;
import com.ivision.databinding.ActivityRecyclerviewListBinding;
import com.ivision.databinding.ToolbarPrimaryBinding;
import com.ivision.model.Vendor;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.CommonParsing;
import com.ivision.utils.Constant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectVendorListActivity extends BaseActivity {

    private ActivityRecyclerviewListBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private SelectVendorListAdapter adapter;
    private ArrayList<Vendor> list = new ArrayList<>();
    private String vendorId = "", vendorName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerviewListBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = SelectVendorListActivity.this;

        if (getIntent() != null) {
            if (getIntent().hasExtra("vendorId")) {
                vendorId = getIntent().getStringExtra("vendorId");
                vendorName = getIntent().getStringExtra("vendorName");
            }
        }

        adapter = new SelectVendorListAdapter(context, list, vendorId, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                Intent intent = new Intent();
                intent.putExtra("id", list.get(position).getVendorId());
                intent.putExtra("name", list.get(position).getName());
                setResultOfActivity(intent, 1, true);
            }
        });

        setToolbar("Select Vendor");

        init();

        getList(false);
    }

    private void init() {
        toolbarBinding.ivAdd.setVisibility(View.VISIBLE);
        toolbarBinding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, AddVendorActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {
                        if (resultCode == Activity.RESULT_OK) {
                            getList(false);
                        }
                    }
                });
            }
        });

        binding.llSearch.setVisibility(View.VISIBLE);
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    binding.ivClose.setVisibility(View.VISIBLE);
                } else {
                    binding.ivClose.setVisibility(View.GONE);
                }
                if (adapter != null) adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etSearch.getText().clear();
            }
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setAdapter(adapter);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
                getList(false);
            }
        });
    }

    private void getList(boolean loadMore) {

        if (Constant.vendorArray != null) {
            if (!Constant.vendorArray.isEmpty()) {
                bindData(Constant.vendorArray);
                return;
            }
        }

        if (loadMore) {
            adapter.addLoadingView();
        } else {
            Common.showProgressDialog(context, getString(R.string.please_wait));
            list.clear();
        }

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getCustomerList(Constant.vendorListUrl, Common.getFranchiseId(), Common.getUserId(), "half", Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (loadMore) {
                    adapter.removeLoadingView();
                } else {
                    Common.hideProgressDialog();
                }
                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        JsonObject result = jsonObject.get("result").getAsJsonObject();
                        Constant.vendorArray = result.get("listArray").getAsJsonArray();
                        bindData(Constant.vendorArray);
                        binding.swipeRefreshLayout.setVisibility(View.VISIBLE);
                        binding.ivNotFound.setVisibility(View.GONE);
                    } else {
                        if (loadMore) {
                            Common.showToast(jsonObject.get("message").getAsString());
                        } else {
                            list.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            binding.swipeRefreshLayout.setVisibility(View.GONE);
                            binding.ivNotFound.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                if (loadMore) {
                    adapter.removeLoadingView();
                } else {
                    Common.hideProgressDialog();
                }
            }
        });
    }

    private void bindData(JsonArray jsonArray) {

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            Vendor model = CommonParsing.bindVendorData(object);
            list.add(model);
        }

        adapter.notifyDataSetChanged();
    }
}