package com.ivision.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.adapter.CategoryListAdapter;
import com.ivision.databinding.ActivityAddProductsBinding;
import com.ivision.databinding.ToolbarPrimaryBinding;
import com.ivision.dialog.CategoryBottomSheetDialog;
import com.ivision.model.CommonModel;
import com.ivision.retrofit.ApiInterface;
import com.ivision.retrofit.ApiUtils;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.Constant;

import java.util.ArrayList;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends BaseActivity implements CategoryBottomSheetDialog.ItemClickListener {

    private ActivityAddProductsBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;
    private CategoryListAdapter listAdapter;
    private ArrayList<CommonModel> arrayList = new ArrayList<>();
    private String name = "", addStatus = "", categoryName = "", image = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductsBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = AddProductActivity.this;

        listAdapter = new CategoryListAdapter(context, arrayList, new ClickListener() {
            @Override
            public void onItemSelected(int position) {

            }
        });

        setToolbar("Add Product");

        init();
    }

    private void init() {
        binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startImageSelectionForResult(AddProductActivity.this, true, true, true, false, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {
                        if (resultCode == Activity.RESULT_OK) {
                            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                            image = Common.getStringImage(bitmap);
                            binding.ivImage.setImageBitmap(bitmap);
                        }
                    }
                });
            }
        });
        binding.ivAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        binding.cbAddStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    addStatus = "1";
                } else {
                    addStatus = "";
                }
            }
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setAdapter(listAdapter);

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    private void validate() {
        name = binding.etName.getText().toString();

        if (name.isEmpty()) {
            binding.etName.setError("Enter name");
        } else if (image.isEmpty()) {
            Common.showToast("Upload image");
        } else {
            bindCategoryData();
        }
    }

    private void bindCategoryData() {
        categoryName = "";
        if (!arrayList.isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                categoryName = categoryName + arrayList.get(i).getName() + ", ";
            }
            categoryName = Common.removeLastChars(categoryName.trim(), 1);
        }
        addProduct();
    }

    private void addProduct() {
        Common.showProgressDialog(context, getString(R.string.please_wait));

        ApiInterface apiInterface = ApiUtils.getApiCalling();
        apiInterface.addProduct(Constant.addProductUrl, Common.getFranchiseId(), Common.getUserId(), name, addStatus, categoryName, image, Common.getUsername(), Common.getPassword()).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Common.hideProgressDialog();
                try {
                    JsonObject jsonObject = response.body();
                    Common.showToast(jsonObject.get("message").getAsString());
                    if (jsonObject.get("status").getAsInt() == 99) {
                        Common.logout(context, jsonObject.get("message").getAsString());
                    } else if (jsonObject.get("status").getAsInt() == 1) {
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

    private void openDialog() {
        CategoryBottomSheetDialog openBottomSheet = CategoryBottomSheetDialog.newInstance();
        openBottomSheet.show(getSupportFragmentManager(), CategoryBottomSheetDialog.TAG);
    }

    @Override
    public void onItemClick(String name) {
        CommonModel model = new CommonModel();
        model.setName(name);
        arrayList.add(model);
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }
}