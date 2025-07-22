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
import com.ivision.adapter.SelectMillListAdapter;
import com.ivision.databinding.ActivityRecyclerviewListBinding;
import com.ivision.databinding.ToolbarPrimaryBinding;
import com.ivision.model.Mill;
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

public class SelectMillListActivity extends BaseActivity {

    private ActivityRecyclerviewListBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private SelectMillListAdapter adapter;
    private ArrayList<Mill> list = new ArrayList<>();
    private String millId = "", millName = "", customerId = "", customerName = "", vendorId = "", vendorName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerviewListBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = SelectMillListActivity.this;

        if (getIntent() != null) {
            if (getIntent().hasExtra("millId")) {
                millId = getIntent().getStringExtra("millId");
                millName = getIntent().getStringExtra("millName");
            }
            if (getIntent().hasExtra("customerId")) {
                customerId = getIntent().getStringExtra("customerId");
                customerName = getIntent().getStringExtra("customerName");
            }
            if (getIntent().hasExtra("vendorId")) {
                vendorId = getIntent().getStringExtra("vendorId");
                vendorName = getIntent().getStringExtra("vendorName");
            }
        }

        adapter = new SelectMillListAdapter(context, list, millId, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                millId = list.get(position).getMillId();
                millName = list.get(position).getName();
                if (!customerId.isEmpty()) {
                    goToActivityForResult(new Intent(context, SizeListActivity.class).putExtra("customerId", customerId).putExtra("customerName", customerName).putExtra("millId", list.get(position).getMillId()).putExtra("millName", list.get(position).getName()).putExtra("basicPrice", list.get(position).getBasicPrice()), new OnActivityResultLauncher() {
                        @Override
                        public void onActivityResultData(Intent data, int resultCode) {
                            if (resultCode == Activity.RESULT_OK) {
                                setResultOfActivity(1, true);
                            }
                        }
                    });
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("details", list.get(position));
                    setResultOfActivity(intent, 1, true);
                }
            }
        });

        setToolbar("Select Mill");

        init();

        if (vendorId.isEmpty()) {
            getList(false);
        } else {
            getVendorMillList(false);
        }
    }

    private void init() {
        if (vendorId.isEmpty()) toolbarBinding.ivAdd.setVisibility(View.VISIBLE);
        toolbarBinding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, AddMillActivity.class, new OnActivityResultLauncher() {
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
                if (vendorId.isEmpty()) {
                    getList(false);
                } else {
                    getVendorMillList(false);
                }
            }
        });
    }

    private void getList(boolean loadMore) {

        if (Constant.millArray != null) {
            if (!Constant.millArray.isEmpty()) {
                bindData(Constant.millArray);
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
        apiInterface.getCustomerList(Constant.millListUrl, Common.getFranchiseId(), Common.getUserId(), "half", Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

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
                        Constant.millArray = result.get("listArray").getAsJsonArray();
                        bindData(Constant.millArray);
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

    private void getVendorMillList(boolean loadMore) {

        if (Constant.millArray != null) {
            if (!Constant.millArray.isEmpty()) {
                bindData(Constant.millArray);
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
        apiInterface.getVendorMillList(Constant.vendorMillListUrl, Common.getFranchiseId(), Common.getUserId(), vendorId, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

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
                        Constant.millArray = result.get("listArray").getAsJsonArray();
                        bindData(Constant.millArray);
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
            Mill model = CommonParsing.bindMillData(object);
            if ((Common.getFranchiseId().isEmpty() || Common.getFranchiseId().equals("0")) || model.getFranchiseStatus().equals("1")) {
                list.add(model);
            }
        }

        adapter.notifyDataSetChanged();
    }
}