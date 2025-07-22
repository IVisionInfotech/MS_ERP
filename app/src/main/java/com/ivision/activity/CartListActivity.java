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
import com.ivision.adapter.CartListAdapter;
import com.ivision.adapter.CategoryListAdapter;
import com.ivision.databinding.ActivitySizeListBinding;
import com.ivision.databinding.ToolbarPrimaryBinding;
import com.ivision.dialog.PriceBottomSheetDialog;
import com.ivision.model.Customer;
import com.ivision.model.Size;
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

public class CartListActivity extends BaseActivity implements PriceBottomSheetDialog.ItemClickListener {

    private ActivitySizeListBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private CategoryListAdapter customerListAdapter;
    private ArrayList<Customer> customerList = new ArrayList<>();
    private CartListAdapter adapter;
    private ArrayList<Size> list = new ArrayList<>();
    private String customerId = "", customerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySizeListBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = CartListActivity.this;

        if (getIntent() != null) {
            if (getIntent().hasExtra("customerId")) {
                customerId = getIntent().getStringExtra("customerId");
                customerName = getIntent().getStringExtra("customerName");
            }
        }

        getCartCustomerList(true);

        setToolbar("Cart List");

        init();
    }

    private void init() {
        binding.llMill.setVisibility(View.GONE);

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.isEmpty()) {
                    Common.showToast("Enter size in cart");
                } else {
                    goToActivityForResult(new Intent(context, CheckoutListActivity.class).putExtra("customerId", customerId), new OnActivityResultLauncher() {
                        @Override
                        public void onActivityResultData(Intent data, int resultCode) {
                            if (resultCode == Activity.RESULT_OK) {
                                setResultOfActivity(1, true);
                            }
                        }
                    });
                }
            }
        });

        StaggeredGridLayoutManager layoutManager3 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        binding.recyclerView3.setLayoutManager(layoutManager3);
        binding.recyclerView3.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView3.setDrawingCacheEnabled(true);
        binding.recyclerView3.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView3.setItemViewCacheSize(20);
        binding.recyclerView3.setHasFixedSize(false);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setHasFixedSize(false);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
                getCartCustomerList(false);
            }
        });
    }

    private void getSizeList(boolean loadMore) {

        if (loadMore) {
            adapter.addLoadingView();
        } else {
            Common.showProgressDialog(context, getString(R.string.please_wait));
            list.clear();
        }

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getCartList(Constant.cartListUrl, Common.getFranchiseId(), Common.getUserId(), customerId, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

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

        list.clear();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            Size model = CommonParsing.bindSizeData(object);
            list.add(model);
        }

        adapter = new CartListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position, String price) {
                Common.confirmationDialog(CartListActivity.this, context.getString(R.string.confirmation_price), "No", "Yes", new Runnable() {
                    @Override
                    public void run() {
                        editPrice(position, price);
                    }
                });
            }
        }, new ClickListener() {
            @Override
            public void onItemSelected(int position, String price) {
                Common.confirmationDialog(CartListActivity.this, context.getString(R.string.confirmation_price), "No", "Yes", new Runnable() {
                    @Override
                    public void run() {
                        editOneKgPrice(position, price);
                    }
                });
            }
        }, new ClickListener() {
            @Override
            public void onItemSelected(int position, Size model) {
                Common.confirmationDialog(CartListActivity.this, context.getString(R.string.confirmation_remove), "No", "Yes", new Runnable() {
                    @Override
                    public void run() {
                        removeCart(position, model);
                    }
                });
            }
        });

        binding.recyclerView.setAdapter(adapter);
        bindCartData();
    }

    private void removeCart(int position, Size model) {

        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.removeCart(Constant.removeCartUrl, Common.getFranchiseId(), Common.getUserId(), list.get(position).getCartId(), Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

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
                        bindCartData();
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

    private void getCartCustomerList(boolean progress) {

        if (progress) Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.cartCustomerListUrl, Common.getFranchiseId(), Common.getUserId(), Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (progress) Common.hideProgressDialog();
                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        JsonObject result = jsonObject.get("result").getAsJsonObject();
                        JsonArray jsonArray = result.get("listArray").getAsJsonArray();
                        bindCustomerData(jsonArray);
                    } else {
                        customerList.clear();
                        if (customerListAdapter != null) {
                            customerListAdapter.notifyDataSetChanged();
                        }
                        binding.swipeRefreshLayout.setVisibility(View.GONE);
                        binding.ivNotFound.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                if (progress) Common.hideProgressDialog();
            }
        });
    }

    private void bindCustomerData(JsonArray jsonArray) {
        customerList.clear();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            Customer model = CommonParsing.bindCustomerData(object);
            customerList.add(model);
        }

        if (customerList.size() == 1) {
            customerId = customerList.get(0).getCustomerId();
            customerName = customerList.get(0).getName();
        }

        customerListAdapter = new CategoryListAdapter(context, customerList, customerId, 2, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                customerId = customerList.get(position).getCustomerId();
                customerName = customerList.get(position).getName();
                getSizeList(false);
            }
        });
        binding.recyclerView3.setAdapter(customerListAdapter);

        binding.swipeRefreshLayout.setVisibility(View.VISIBLE);
        binding.ivNotFound.setVisibility(View.GONE);

        if (!customerId.isEmpty()) {
            getSizeList(false);
        }
    }

    private void editPrice(int position, String price) {
        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.editCartPrice(Constant.updateCartPriceUrl, Common.getFranchiseId(), Common.getUserId(), list.get(position).getCartId(), price, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Common.hideProgressDialog();
                try {
                    JsonObject jsonObject = response.body();
                    Common.showToast(jsonObject.get("message").getAsString());
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        list.get(position).setSystemPrice(price);
                        adapter.notifyItemChanged(position);
                        bindCartData();
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

    private void editOneKgPrice(int position, String price) {
        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.editCartPrice(Constant.updateOneKgPriceUrl, Common.getFranchiseId(), Common.getUserId(), list.get(position).getCartId(), price, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Common.hideProgressDialog();
                try {
                    JsonObject jsonObject = response.body();
                    Common.showToast(jsonObject.get("message").getAsString());
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        list.get(position).setOneKgPrice(price);
                        adapter.notifyItemChanged(position);
                        bindCartData();
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

    private void bindCartData() {
        if (list.isEmpty()) {
            getCartCustomerList(true);
            binding.llCart.setVisibility(View.GONE);
        } else {
            binding.llCart.setVisibility(View.VISIBLE);
            binding.tvItems.setText(list.size() + " Items");
            float totalPrice = 0;
            for (int i = 0; i < list.size(); i++) {
                Size model = list.get(i);
                float oneKgPrice = 0;

                if (!model.getOneKgPrice().isEmpty() && Float.parseFloat(model.getOneKgPrice()) > 0) {
                    oneKgPrice = Float.parseFloat(model.getOneKgPrice());
                } else if (!model.getSystemPrice().isEmpty() && Float.parseFloat(model.getSystemPrice()) > 0) {
                    oneKgPrice = Float.parseFloat(model.getSystemPrice()) / Float.parseFloat(model.getQuantity());
                } else {
                    oneKgPrice = (Float.parseFloat(model.getDiffPrice()) + Float.parseFloat(model.getBasicPrice())) / 1000;
                }
                totalPrice = totalPrice + (oneKgPrice * Float.parseFloat(model.getQuantity()));
            }
            binding.tvTotal.setText(getString(R.string.rupee) + " " + totalPrice);
        }
        binding.swipeRefreshLayout.setVisibility(View.VISIBLE);
        binding.ivNotFound.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(int position, String model) {
        editPrice(position, model);
    }
}