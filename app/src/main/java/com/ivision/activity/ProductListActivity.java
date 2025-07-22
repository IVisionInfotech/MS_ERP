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

public class ProductListActivity extends BaseActivity {

    private ActivityRecyclerviewListBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private ProductListAdapter adapter;
    private ArrayList<Product> list = new ArrayList<>();
    private String millId = "", millName = "", basicPrice = "";
    private int mill = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerviewListBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = ProductListActivity.this;

        if (getIntent() != null) {
            if (getIntent().hasExtra("id")) {
                millId = getIntent().getStringExtra("id");
            }
            if (getIntent().hasExtra("name")) {
                millName = getIntent().getStringExtra("name");
            }
            if (getIntent().hasExtra("basicPrice")) {
                basicPrice = getIntent().getStringExtra("basicPrice");
            }
            if (getIntent().hasExtra("mill")) {
                mill = getIntent().getIntExtra("mill", 0);
            }
        }

        adapter = new ProductListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                if (mill == 0) {
                    goToActivityForResult(new Intent(context, SizeListActivity.class).putExtra("mill", 1).putExtra("id", list.get(position).getProductId()).putExtra("millId", millId).putExtra("millName", millName).putExtra("basicPrice", basicPrice), new BaseActivity.OnActivityResultLauncher() {
                        @Override
                        public void onActivityResultData(Intent data, int resultCode) {
                            if (resultCode == Activity.RESULT_OK) {
                                setResultOfActivity(1, true);
                            }
                        }
                    });
                }
                /*if (list.get(position).getAddStatus().equals("1")) {
                    //
                } else {

                }*/
            }
        });

        setToolbar("Product List");

        init();

        if (mill == 0 || millId.isEmpty()) {
            getList(false);
        } else {
            getMillList(false);
        }
    }

    private void init() {
        if (Common.getFranchiseId().isEmpty() || Common.getFranchiseId().equals("0") || mill != 0 || !millId.isEmpty())
            toolbarBinding.ivAdd.setVisibility(View.VISIBLE);
        toolbarBinding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mill == 0 || millId.isEmpty()) {
                    goToActivityForResult(new Intent(context, AddProductActivity.class), new OnActivityResultLauncher() {
                        @Override
                        public void onActivityResultData(Intent data, int resultCode) {
                            if (resultCode == Activity.RESULT_OK) {
                                getList(false);
                            }
                        }
                    });
                } else {
                    goToActivityForResult(new Intent(context, AddMillProductActivity.class).putExtra("millId", millId), new OnActivityResultLauncher() {
                        @Override
                        public void onActivityResultData(Intent data, int resultCode) {
                            if (resultCode == Activity.RESULT_OK) {
                                getMillList(false);
                            }
                        }
                    });
                }
            }
        });

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
                if (mill == 0 || millId.isEmpty()) {
                    getList(false);
                } else {
                    getMillList(false);
                }
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
        apiInterface.getList(Constant.productListUrl, Common.getFranchiseId(), Common.getUserId(), Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

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

    private void getMillList(boolean loadMore) {

        if (loadMore) {
            adapter.addLoadingView();
        } else {
            Common.showProgressDialog(context, getString(R.string.please_wait));
            list.clear();
        }

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.millProductListUrl, Common.getFranchiseId(), Common.getUserId(), millId, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

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
            Product model = CommonParsing.bindProductData(object);
            list.add(model);
        }

        adapter.notifyDataSetChanged();
    }
}