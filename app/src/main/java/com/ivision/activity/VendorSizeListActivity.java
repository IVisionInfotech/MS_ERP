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
import com.ivision.adapter.CategoryListAdapter;
import com.ivision.adapter.ProductHorizontalListAdapter;
import com.ivision.adapter.SizeListAdapter;
import com.ivision.databinding.ActivitySizeListBinding;
import com.ivision.databinding.ToolbarPrimaryBinding;
import com.ivision.loadmore.OnLoadMoreListener;
import com.ivision.loadmore.RecyclerViewLoadMoreScroll;
import com.ivision.model.CommonModel;
import com.ivision.model.Mill;
import com.ivision.model.Product;
import com.ivision.model.Size;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.CommonAPI;
import com.ivision.utils.CommonParsing;
import com.ivision.utils.Constant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorSizeListActivity extends BaseActivity {

    private ActivitySizeListBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private CategoryListAdapter categoryListAdapter;
    private ProductHorizontalListAdapter productAdapter;
    private ArrayList<Product> productList = new ArrayList<>();
    private ArrayList<CommonModel> categoryList = new ArrayList<>();
    private SizeListAdapter adapter;
    private ArrayList<Size> list = new ArrayList<>();
    private int offset = 0, limit = 10;
    private RecyclerViewLoadMoreScroll scrollListener;
    private String customerId = "", customerName = "", vendorId = "", vendorName = "", millId = "", millName = "", productId = "",
            categoryId = "", millBasicPrice = "", basicPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySizeListBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = VendorSizeListActivity.this;

        setToolbar("Size List");

        init();

        if (getIntent() != null) {
            if (getIntent().hasExtra("vendorId")) {
                vendorId = getIntent().getStringExtra("vendorId");
                vendorName = getIntent().getStringExtra("vendorName");
                bindVendorName();
            }
            if (getIntent().hasExtra("millId")) {
                millId = getIntent().getStringExtra("millId");
                millName = getIntent().getStringExtra("millName");
                millBasicPrice = getIntent().getStringExtra("basicPrice");
                basicPrice = getIntent().getStringExtra("basicPrice");
                bindMillName();
            }
            if (getIntent().hasExtra("customerId")) {
                customerId = getIntent().getStringExtra("customerId");
                customerName = getIntent().getStringExtra("customerName");
                bindCustomerName();
            }
            if (getIntent().hasExtra("id")) {
                productId = getIntent().getStringExtra("id");
            }
        }

        if (millId.isEmpty()) binding.llMill.performClick();
    }

    private void init() {
        toolbarBinding.llCustomer.setVisibility(View.VISIBLE);
        toolbarBinding.llCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(new Intent(context, SelectCustomerListActivity.class).putExtra("customerId", customerId).putExtra("customerName", customerName), new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {
                        if (resultCode == Activity.RESULT_OK) {
                            if (data != null) {
                                if (data.hasExtra("id")) {
                                    customerId = data.getStringExtra("id");
                                    customerName = data.getStringExtra("name");
                                    bindCustomerName();
                                    if (!productList.isEmpty()) {
                                        if (!productId.isEmpty()) {
                                            for (int i = 0; i < productList.size(); i++) {
                                                if (productList.get(i).getProductId().equals(productId)) {
                                                    if (productList.get(i).getCategoryList() != null) {
                                                        if (!productList.get(i).getCategoryList().isEmpty()) {
                                                            bindCategoryData(productList.get(i).getCategoryList());
                                                            if (!categoryId.isEmpty())
                                                                getSizeList(false);
                                                        } else {
                                                            clearCategoryData();
                                                            getSizeList(false);
                                                        }
                                                    } else {
                                                        clearCategoryData();
                                                        getSizeList(false);
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });

        binding.llVendor.setVisibility(View.VISIBLE);
        binding.llVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(new Intent(context, SelectVendorListActivity.class).putExtra("vendorId", vendorId).putExtra("vendorName", vendorName), new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {
                        if (resultCode == Activity.RESULT_OK) {
                            if (data != null) {
                                if (data.hasExtra("id")) {
                                    vendorId = data.getStringExtra("id");
                                    vendorName = data.getStringExtra("name");
                                    bindVendorName();
                                    millId = "";
                                    millName = "";
                                    basicPrice = "";
                                    millBasicPrice = "";
                                    bindMillName();
                                }
                            }
                        }
                    }
                });
            }
        });
        binding.llMill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(new Intent(context, VendorMillListActivity.class).putExtra("select", 1).putExtra("millId", millId).putExtra("millName", millName).putExtra("vendorId", vendorId).putExtra("vendorName", vendorName), new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {
                        if (resultCode == Activity.RESULT_OK) {
                            if (data != null) {
                                if (data.hasExtra("details")) {
                                    Mill details = (Mill) data.getSerializableExtra("details");
                                    millId = details.getMillId();
                                    millName = details.getName();
                                    millBasicPrice = details.getBasicPrice();
                                    basicPrice = details.getBasicPrice();
                                    bindMillName();
                                }
                            }
                        }
                    }
                });
            }
        });
        binding.llMillCart.setVisibility(View.VISIBLE);
        binding.llChangeMill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llMill.performClick();
            }
        });

        StaggeredGridLayoutManager layoutManager3 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        binding.recyclerView3.setLayoutManager(layoutManager3);
        binding.recyclerView3.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView3.setDrawingCacheEnabled(true);
        binding.recyclerView3.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView3.setItemViewCacheSize(20);
        binding.recyclerView3.setHasFixedSize(false);

        StaggeredGridLayoutManager layoutManager2 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        binding.recyclerView2.setLayoutManager(layoutManager2);
        binding.recyclerView2.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView2.setDrawingCacheEnabled(true);
        binding.recyclerView2.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView2.setItemViewCacheSize(20);
        binding.recyclerView2.setHasFixedSize(false);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setHasFixedSize(false);

        scrollListener = new RecyclerViewLoadMoreScroll(layoutManager);
        scrollListener.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getSizeList(true);
            }
        });
        binding.recyclerView.addOnScrollListener(scrollListener);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
                getSizeList(false);
            }
        });
    }

    private void bindCustomerName() {
        if (!customerName.isEmpty()) toolbarBinding.tvCustomer.setText(customerName);
        getCounts();
    }

    private void bindMillName() {
        if (!millName.isEmpty()) {
            binding.tvMillName.setText(millName);
            getProductList();
            binding.swipeRefreshLayout.setEnabled(true);
        } else {
            binding.tvMillName.setText("N/A");
            productId = "";
            categoryId = "";
            clearProductData();
            clearCategoryData();
            clearSizeData();
            binding.swipeRefreshLayout.setEnabled(false);
        }
    }

    private void bindVendorName() {
        if (!vendorName.isEmpty()) binding.tvVendorName.setText(vendorName);
    }

    private void getProductList() {

        clearCategoryData();
        clearSizeData();

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getVendorProductList(Constant.vendorMillProductListUrl, Common.getFranchiseId(), Common.getUserId(), vendorId, millId, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        JsonObject result = jsonObject.get("result").getAsJsonObject();
                        JsonArray jsonArray = result.get("listArray").getAsJsonArray();
                        bindProductData(jsonArray);
                    } else {
                        productList.clear();
                        if (productAdapter != null) {
                            productAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getSizeList(boolean loadMore) {

        if (loadMore) {
            adapter.addLoadingView();
        } else {
            Common.showProgressDialog(context, getString(R.string.please_wait));
            offset = 0;
            limit = 10;
            list.clear();
        }

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getList(Constant.vendorSizeListUrl, Common.getFranchiseId(), Common.getUserId(), customerId, vendorId, millId, productId, categoryId, String.valueOf(limit), String.valueOf(offset), Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

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

    private void bindProductData(JsonArray jsonArray) {

        productList.clear();

        if (jsonArray != null) {
            if (!jsonArray.isJsonNull()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject object = jsonArray.get(i).getAsJsonObject();
                    Product model = CommonParsing.bindProductData(object);
                    productList.add(model);
                    if (productId.equals(model.getProductId())) {
                        clearSizeData();
                        getSizeList(false);
                    }
                }
            }
        }

        productAdapter = new ProductHorizontalListAdapter(context, productList, productId, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                categoryId = "";
                productId = productList.get(position).getProductId();
                productAdapter.notifyDataSetChanged();
                clearSizeData();
                if (productList.get(position).getCategoryList() != null) {
                    if (!productList.get(position).getCategoryList().isEmpty()) {
                        bindCategoryData(productList.get(position).getCategoryList());
                    } else {
                        clearCategoryData();
                        getSizeList(false);
                    }
                } else {
                    clearCategoryData();
                    getSizeList(false);
                }
            }
        });

        binding.recyclerView2.setAdapter(productAdapter);
        binding.recyclerView2.setVisibility(View.VISIBLE);
    }

    private void bindCategoryData(ArrayList<CommonModel> list) {

        categoryList.clear();

        if (list != null) {
            if (!list.isEmpty()) {
                categoryList.addAll(list);
            }
        }

        categoryListAdapter = new CategoryListAdapter(context, categoryList, categoryId, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                categoryId = categoryList.get(position).getCatId();
                getSizeList(false);
            }
        });

        binding.recyclerView3.setAdapter(categoryListAdapter);

        clearSizeData();
    }

    private void bindData(JsonArray jsonArray) {

        list.clear();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            Size model = CommonParsing.bindSizeData(object);
            list.add(model);
        }

        adapter = new SizeListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position, Size model) {
                Common.confirmationDialog(VendorSizeListActivity.this, context.getString(R.string.confirmation_remove), "No", "Yes", new Runnable() {
                    @Override
                    public void run() {
                        removeCart(position);
                    }
                });
            }
        }, new SizeListAdapter.CartClickListener() {
            @Override
            public void onItemSelected(int position, Size model, String quantity, String piece, String type, int status) {
                if (customerId.isEmpty()) {
                    Common.showToast("Select customer first");
                } else {
                    if (status == 1) {
                        addCart(position, quantity, piece, type);
                    } else if (status == 2) {
                        updateCart(position, quantity, piece, type);
                    }
                }
            }
        });

        binding.recyclerView.setAdapter(adapter);
    }

    private void clearProductData() {
        if (productAdapter != null) {
            productList.clear();
            productAdapter.notifyDataSetChanged();
        }
    }

    private void clearCategoryData() {
        if (categoryListAdapter != null) {
            categoryList.clear();
            categoryListAdapter.notifyDataSetChanged();
        }
    }

    private void clearSizeData() {
        if (categoryId.isEmpty()) {
            if (adapter != null) {
                list.clear();
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void addCart(int position, String quantity, String piece, String type) {

        hideSoftKeyboard();
        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.addCart(Constant.addCartUrl, Common.getFranchiseId(), Common.getUserId(), customerId, vendorId, millId, productId, categoryId, list.get(position).getSizeId(), millBasicPrice, basicPrice, list.get(position).getDiffPrice(), quantity, piece, type, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

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
                        list.get(position).setCartId(result.get("cartId").getAsString());
                        list.get(position).setCartMillBasicPrice(millBasicPrice);
                        list.get(position).setCartBasicPrice(basicPrice);
                        list.get(position).setCartDiffPrice(list.get(position).getDiffPrice());
                        list.get(position).setQuantity(quantity);
                        list.get(position).setPiece(piece);
                        list.get(position).setCartMType(type);
                        if (adapter != null) {
                            adapter.notifyItemChanged(position);
                        }
                        getCounts();
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

    private void updateCart(int position, String quantity, String piece, String type) {

        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.updateCart(Constant.updateCartUrl, Common.getFranchiseId(), Common.getUserId(), list.get(position).getCartId(), quantity, piece, type, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Common.hideProgressDialog();
                try {
                    JsonObject jsonObject = response.body();
                    Common.showToast(jsonObject.get("message").getAsString());
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        list.get(position).setQuantity(quantity);
                        list.get(position).setPiece(piece);
                        list.get(position).setCartMType(type);
                        if (adapter != null) {
                            adapter.notifyItemChanged(position);
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

    private void removeCart(int position) {

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
                        list.get(position).setCartId("");
                        list.get(position).setCartMillBasicPrice("");
                        list.get(position).setCartBasicPrice("");
                        list.get(position).setCartDiffPrice("");
                        list.get(position).setQuantity("");
                        list.get(position).setWeight("");
                        list.get(position).setCartMType("");
                        if (adapter != null) {
                            adapter.notifyItemChanged(position);
                        }
                        getCounts();
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

    private void getCounts() {
        CommonAPI.getCustomerCartCounts(context, customerId, new CommonAPI.OnResponseObject() {
            @Override
            public void OnResponse(JsonObject jsonObject) {
                binding.tvCartCount.setText(String.format("%02d", jsonObject.get("cartCount").getAsInt()));
                binding.llGoToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (jsonObject.get("cartCount").getAsInt() > 0) {
                            goToActivityForResult(new Intent(context, CartListActivity.class).putExtra("customerId", customerId), new OnActivityResultLauncher() {
                                @Override
                                public void onActivityResultData(Intent data, int resultCode) {
                                    if (resultCode == Activity.RESULT_OK) {
                                        setResultOfActivity(1, true);
                                    }
                                }
                            });
                        } else {
                            Common.showToast("Add size into cart first");
                        }
                    }
                });
            }
        });
    }
}