package com.ivision.activity;

import android.os.Bundle;

import com.ivision.R;
import com.ivision.databinding.ActivityOrderDetailsBinding;
import com.ivision.databinding.ToolbarPrimaryBinding;
import com.ivision.model.Product;

import java.util.ArrayList;

public class OrderDetailsActivity extends BaseActivity {

    private ActivityOrderDetailsBinding binding;
    private ToolbarPrimaryBinding toolbarBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = OrderDetailsActivity.this;

        setToolbar("Order Details");

        init();

        bindListData();
    }

    private void init() {
        binding.tvTotal.setText(getString(R.string.rupee) + " 99999.00");
    }

    private void bindListData() {

        ArrayList<Product> list = new ArrayList<>();

        /*list.add(new Product("Zedex Syrup"));
        list.add(new Product("Bro-Zedex Syrup"));
        list.add(new Product("Eliptin 20 Tab"));
        list.add(new Product("Glimistar PM 2 Tab"));
        list.add(new Product("Cofsils Lozenges Orange"));
        list.add(new Product("Huggies Dry Pants M5"));
        list.add(new Product("Zedex SF Syrup"));
        list.add(new Product("Huggies Dry Pants s5"));*/

        /*OrderDetailsListAdapter adapter = new OrderDetailsListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                goToActivityForResult(new Intent(context, OrderDetailsActivity.class), new BaseActivity.OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {
                        if (resultCode == Activity.RESULT_OK) {
                        }
                    }
                });
            }
        });

        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 1, RecyclerView.VERTICAL, false);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setDrawingCacheEnabled(true);
        binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setAdapter(adapter);*/
    }
}