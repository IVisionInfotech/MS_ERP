package com.ivision.activity;

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
import com.ivision.adapter.OrderListAdapter;
import com.ivision.databinding.ActivityRecyclerviewListBinding;
import com.ivision.databinding.ToolbarPrimaryBinding;
import com.ivision.loadmore.OnLoadMoreListener;
import com.ivision.loadmore.RecyclerViewLoadMoreScroll;
import com.ivision.model.Order;
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

public class OrderListActivity extends BaseActivity {

    private ActivityRecyclerviewListBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private OrderListAdapter adapter;
    private ArrayList<Order> list = new ArrayList<>();
    private int offset = 0, limit = 10;
    private RecyclerViewLoadMoreScroll scrollListener;
    private String type = "0"; // 0 pending, 1 confirm, 2 dispatch, 3 deliver, 4 reject

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerviewListBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = OrderListActivity.this;

        adapter = new OrderListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                /*goToActivityForResult(new Intent(context, ProductDetailsActivity.class).putExtra("id", list.get(position).getProductId()), new BaseActivity.OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });*/
            }
        }, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                String message = "", type = "";
                if (list.get(position).getStatus().equals("0")) {
                    type = "1";
                    message = getString(R.string.confirmation_confirm_order);
                } else if (list.get(position).getStatus().equals("1")) {
                    type = "2";
                    message = getString(R.string.confirmation_dispatch_order);
                } else if (list.get(position).getStatus().equals("2")) {
                    type = "3";
                    message = getString(R.string.confirmation_deliver_order);
                }
                String finalType = type;
                Common.confirmationDialog(OrderListActivity.this, message, "No", "Yes", new Runnable() {
                    @Override
                    public void run() {
                        updateStatus(position, finalType);
                    }
                });
            }
        }, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                sharePDF(list.get(position).getLink(), list.get(position).getContact());
            }
        });

        if (getIntent() != null) {
            if (getIntent().hasExtra("type")) {
                type = getIntent().getStringExtra("type");
                if (type.equals("0")) {
                    setToolbar("Pending Order");
                } else if (type.equals("1")) {
                    setToolbar("Confirmed Order");
                } else if (type.equals("2")) {
                    setToolbar("Dispatched Order");
                } else if (type.equals("3")) {
                    setToolbar("Delivered Order");
                }
            }
        }

        init();

        getList(false);
    }

    private void init() {
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

        scrollListener = new RecyclerViewLoadMoreScroll(layoutManager);
        scrollListener.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getList(true);
            }
        });
        binding.recyclerView.addOnScrollListener(scrollListener);

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
            offset = 0;
            limit = 10;
            list.clear();
        }

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.orderListUrl, Common.getFranchiseId(), Common.getUserId(), type, String.valueOf(limit), String.valueOf(offset), Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

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
                        offset = offset + jsonArray.size();
                        bindData(jsonArray);
                        scrollListener.setLoaded();
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
            Order model = CommonParsing.bindOrderData(object);
            list.add(model);
        }

        adapter.notifyDataSetChanged();

        binding.swipeRefreshLayout.setVisibility(View.VISIBLE);
        binding.ivNotFound.setVisibility(View.GONE);
    }

    private void updateStatus(int position, String type) {
        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.updateStatus(Constant.updateStatusUrl, Common.getFranchiseId(), list.get(position).getId(), type, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Common.hideProgressDialog();
                try {
                    JsonObject jsonObject = response.body();
                    Common.showToast(jsonObject.get("message").getAsString());
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        list.remove(position);
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
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

    private void sharePDF(String link, String contact) {

        String shareMessage = getString(R.string.shareText) + "\n\n" + link;

        Intent sendIntent = new Intent();

        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        sendIntent.setType("text/plain");

        if (Common.isPackageExisted(context, "com.whatsapp")) {
            sendIntent.setPackage("com.whatsapp");
        } else if (Common.isPackageExisted(context, "com.whatsapp.w4b")) {
            sendIntent.setPackage("com.whatsapp.w4b");
        }

        goToActivityForResult(sendIntent, new OnActivityResultLauncher() {
            @Override
            public void onActivityResultData(Intent data, int resultCode) {
            }
        });
    }
}