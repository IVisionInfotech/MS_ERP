package com.ivision.activity;

import android.os.Bundle;
import android.view.View;

import com.ivision.R;
import com.ivision.databinding.ActivityProfileBinding;
import com.ivision.model.User;
import com.ivision.utils.Common;
import com.ivision.utils.RealmController;

public class ProfileActivity extends BaseActivity {

    private ActivityProfileBinding binding;
//    private ToolbarPrimaryBinding toolbarBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
//        toolbarBinding = ToolbarPrimaryBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());

        context = ProfileActivity.this;

        init();

        bindData();
    }

    private void init() {
        binding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.confirmationDialog(ProfileActivity.this, getString(R.string.confirmation_logout), "No", "Yes", new Runnable() {
                    @Override
                    public void run() {
                        Common.logout(context, "");
                    }
                });
            }
        });
    }

    private void bindData() {
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            binding.tvName.setText(user.getName());
            binding.tvContact.setText(user.getContact());
            binding.tvCity.setText(user.getCity());
        }
    }
}