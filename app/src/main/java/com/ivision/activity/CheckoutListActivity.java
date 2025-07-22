package com.ivision.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.adapter.CheckoutListAdapter;
import com.ivision.databinding.ActivitySizeListBinding;
import com.ivision.databinding.ToolbarPrimaryBinding;
import com.ivision.model.Size;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.Common;
import com.ivision.utils.CommonParsing;
import com.ivision.utils.Constant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutListActivity extends BaseActivity {

    private ActivitySizeListBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private CheckoutListAdapter adapter;
    private ArrayList<Size> list = new ArrayList<>();
    private String customerId = "", customerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySizeListBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = CheckoutListActivity.this;

        if (getIntent() != null) {
            if (getIntent().hasExtra("customerId")) {
                customerId = getIntent().getStringExtra("customerId");
                customerName = getIntent().getStringExtra("customerName");
            }
        }

        setToolbar("Checkout");

        init();

        getSizeList(false);
    }

    private void init() {
        binding.llMill.setVisibility(View.GONE);

        binding.btnSubmit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.isEmpty()) {
                    Common.showToast("Enter size in cart");
                } else {
                    Common.confirmationDialog(CheckoutListActivity.this, getString(R.string.confirmation_place_order), "No", "Yes", new Runnable() {
                        @Override
                        public void run() {
                            placeOrder();
                        }
                    });
                }
            }
        });

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
                getSizeList(false);
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
                        String gst = result.get("gst").getAsString();
                        bindData(jsonArray, gst);
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

    private void bindData(JsonArray jsonArray, String gst) {

        list.clear();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            Size model = CommonParsing.bindSizeData(object);
            list.add(model);
        }

        adapter = new CheckoutListAdapter(context, list);

        binding.recyclerView.setAdapter(adapter);
        bindCartData(gst);
    }

    private void bindCartData(String gstPer) {
        binding.llCheckout.setVisibility(View.VISIBLE);
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
        float gst = totalPrice * Float.parseFloat(gstPer) / 100;
        binding.tvTotalPrice.setText(getString(R.string.rupee) + " " + totalPrice);
        binding.tvGST.setText(getString(R.string.rupee) + " " + gst);
        binding.tvGrandTotal.setText(getString(R.string.rupee) + " " + (gst + totalPrice));
    }

    private void placeOrder() {
        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.getCartList(Constant.placeOrderUrl, Common.getFranchiseId(), Common.getUserId(), customerId, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Common.hideProgressDialog();
                try {
                    JsonObject jsonObject = response.body();
                    Common.showToast(jsonObject.get("message").getAsString());
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
                        String link = jsonObject.get("link").getAsString();
                        String contact = jsonObject.get("contact").getAsString();
                        quotationDialog(link, contact);
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

    private void quotationDialog(String link, String contact) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);

        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                ivClose.setVisibility(View.VISIBLE);
            }
        }, 3000);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultOfActivity(1, true);
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePDF(link, contact, dialog);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultOfActivity(1, true);
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void sharePDF(String link, String contact, Dialog dialog) {

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
                setResultOfActivity(1, true);
                dialog.dismiss();
            }
        });
    }
}