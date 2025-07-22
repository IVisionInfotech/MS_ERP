package com.ivision.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.adapter.ProductListAdapter;
import com.ivision.databinding.ActivityRecyclerviewListBinding;
import com.ivision.databinding.ToolbarPrimaryBinding;
import com.ivision.model.Product;
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

public class VendorMillProductListActivity extends BaseActivity {

    private ActivityRecyclerviewListBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private ProductListAdapter adapter;
    private ArrayList<Product> list = new ArrayList<>();
    private String vendorId = "", vendorName = "", millId = "", millName = "", basicPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerviewListBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = VendorMillProductListActivity.this;

        if (getIntent() != null) {
            if (getIntent().hasExtra("vendorId")) {
                vendorId = getIntent().getStringExtra("vendorId");
                vendorName = getIntent().getStringExtra("vendorName");
            }
            if (getIntent().hasExtra("millId")) {
                millId = getIntent().getStringExtra("millId");
            }
            if (getIntent().hasExtra("millName")) {
                millName = getIntent().getStringExtra("millName");
            }
            if (getIntent().hasExtra("basicPrice")) {
                basicPrice = getIntent().getStringExtra("basicPrice");
            }
        }

        adapter = new ProductListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                goToActivityForResult(new Intent(context, VendorSizeListActivity.class).putExtra("mill", 1).putExtra("id", list.get(position).getProductId()).putExtra("millId", millId).putExtra("millName", millName).putExtra("basicPrice", basicPrice).putExtra("vendorId", vendorId).putExtra("vendorName", vendorName), new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {
                        if (resultCode == Activity.RESULT_OK) {
                            setResultOfActivity(1, true);
                        }
                    }
                });
            }
        });

        setToolbar("Product List");

        init();

        getList(false);
    }

    private void init() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
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

        if (loadMore) {
            adapter.addLoadingView();
        } else {
            Common.showProgressDialog(context, getString(R.string.please_wait));
            list.clear();
        }

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getVendorProductList(Constant.vendorMillProductListUrl, Common.getFranchiseId(), Common.getUserId(), vendorId, millId, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

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
                        JsonArray jsonArray = result.get("listArray").getAsJsonArray();
                        bindData(jsonArray);
                    } else {
                        if (loadMore) {
                            Common.showToast(jsonObject.get("message").getAsString());
                        } else {
                            list.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
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
            Product model = CommonParsing.bindProductData(object);
            list.add(model);
        }

        adapter.notifyDataSetChanged();
    }
}