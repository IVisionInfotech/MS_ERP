package com.ivision.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.adapter.OrderListAdapter;
import com.ivision.databinding.ActivityCustomerDetailsBinding;
import com.ivision.databinding.ToolbarPrimaryBinding;
import com.ivision.model.Customer;
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

public class CustomerDetailsActivity extends BaseActivity {

    private ActivityCustomerDetailsBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private OrderListAdapter adapter;
    private ArrayList<Order> list = new ArrayList<>();
    private int offset = 0, limit = 10;
    private String customerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerDetailsBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = CustomerDetailsActivity.this;

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
                Common.confirmationDialog(CustomerDetailsActivity.this, message, "No", "Yes", new Runnable() {
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

        setToolbar("Customer Details");

        init();

        if (getIntent() != null) {
            if (getIntent().hasExtra("details")) {
                Customer model = (Customer) getIntent().getSerializableExtra("details");
                customerId = model.getCustomerId();
                bindData(model);
                getList(false);
            }
        }
    }

    private void init() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setAdapter(adapter);

        if (binding.nestedScrollView != null) {
            binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (v.getChildAt(v.getChildCount() - 1) != null) {
                        if (scrollY > oldScrollY) {
                            if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {
                                getList(true);
                            }
                        }
                    }
                }
            });
        }
    }

    private void bindData(Customer model) {
        String title = model.getName();
        Common.bindShortName(context, binding.includedLayout.tvShort, 0, title);
        binding.tvName.setText(title);
        binding.tvContact.setText(model.getContact());
        binding.tvCity.setText(model.getCity());
        binding.tvEmail.setText(model.getEmailId());
        binding.tvAddress.setText(model.getAddress());
        binding.tvCompName.setText(model.getCompanyName());
        binding.tvGSTIN.setText(model.getGstin());
        binding.tvStateName.setText(model.getStateName());

        binding.ivContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.dialNumber(context, model.getContact());
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
        apiInterface.getList(Constant.orderListUrl, Common.getFranchiseId(), Common.getUserId(), "-1", customerId, String.valueOf(limit), String.valueOf(offset), Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

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
//                        scrollListener.setLoaded();
                    } else {
                        if (loadMore) {
                            Common.showToast(jsonObject.get("message").getAsString());
                        } else {
                            list.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
//                            binding.swipeRefreshLayout.setVisibility(View.GONE);
//                            binding.ivNotFound.setVisibility(View.VISIBLE);
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

//        binding.swipeRefreshLayout.setVisibility(View.VISIBLE);
//        binding.ivNotFound.setVisibility(View.GONE);
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

        String shareMessage = "\nLet me recommend you this quotation\n\n";
        shareMessage = shareMessage + link + "\n\n";

        Intent sendIntent = new Intent();

        try {
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            sendIntent.putExtra("jid", contact + "@s.whatsapp.net");
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
        } catch (Exception e) {
            e.printStackTrace();

            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            sendIntent.putExtra("jid", contact + "@s.whatsapp.net");
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp.w4b");
        }

        goToActivityForResult(sendIntent, new OnActivityResultLauncher() {
            @Override
            public void onActivityResultData(Intent data, int resultCode) {
            }
        });
    }
}