package com.ivision.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.JsonObject;
import com.ivision.R;
import com.ivision.databinding.ActivityMainBinding;
import com.ivision.utils.BroadCastManager;
import com.ivision.utils.Common;
import com.ivision.utils.CommonAPI;
import com.ivision.utils.Constant;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private LocalReceiver localReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = MainActivity.this;

        Common.appUpdateDialog(context);

        init();

        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.refreshCount);
            localReceiver = new LocalReceiver();
            BroadCastManager.getInstance().registerReceiver(this, localReceiver, filter);//Registered broadcast recipient
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        binding.ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, CartListActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {
                        if (resultCode == Activity.RESULT_OK) {
                        }
                    }
                });
            }
        });

        binding.tvCount.setSolidColor(context.getResources().getColor(R.color.colorOneLight));
        binding.tvCount2.setSolidColor(context.getResources().getColor(R.color.colorTwoLight));
        binding.tvCount3.setSolidColor(context.getResources().getColor(R.color.colorThreeLight));
        binding.tvCount4.setSolidColor(context.getResources().getColor(R.color.colorFourLight));

        binding.cvBox1.setOnClickListener(clickListener);
        binding.cvBox2.setOnClickListener(clickListener);
        binding.cvBox3.setOnClickListener(clickListener);
        binding.cvBox4.setOnClickListener(clickListener);
        binding.cvProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, ProductListActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });
            }
        });
        binding.cvMill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, MillListActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });
            }
        });
        binding.cvVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, VendorListActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });
            }
        });
        binding.cvCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, CustomerListActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });
            }
        });
        binding.cvTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, TransportListActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });
            }
        });
        binding.cvFranchise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, FranchiseListActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });
            }
        });
        binding.cvPriceCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, SizeListActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });
            }
        });
        binding.cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, ProfileActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCounts();
    }

    @Override
    public void onBackPressed() {
        gotoBack();
    }

    @Override
    protected void onDestroy() {
        BroadCastManager.getInstance().unregisterReceiver(this, localReceiver);//Logout broadcast recipient
        super.onDestroy();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String type = "";
            if (view.getId() == R.id.cvBox1) {
                type = "0";
            } else if (view.getId() == R.id.cvBox2) {
                type = "1";
            } else if (view.getId() == R.id.cvBox3) {
                type = "2";
            } else if (view.getId() == R.id.cvBox4) {
                type = "3";
            }
            goToActivityForResult(new Intent(context, OrderListActivity.class).putExtra("type", type), new OnActivityResultLauncher() {
                @Override
                public void onActivityResultData(Intent data, int resultCode) {

                }
            });
        }
    };

    private void getCounts() {
        FrameLayout flCart = findViewById(R.id.flCart);
        flCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, CartListActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });
            }
        });
        /*FrameLayout flNotification = findViewById(R.id.flNotification);
        flNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityForResult(context, NotificationActivity.class, new OnActivityResultLauncher() {
                    @Override
                    public void onActivityResultData(Intent data, int resultCode) {

                    }
                });
            }
        });*/

        CommonAPI.getCounts(context, new CommonAPI.OnResponseObject() {
            @Override
            public void OnResponse(JsonObject jsonObject) {
//                CircularTextView tvCartCount = findViewById(R.id.tvCartCount);
                binding.tvCartCount.setVisibility(View.GONE);
                binding.tvCartCount.setText(jsonObject.get("cartCount").getAsString());
                if (!jsonObject.get("cartCount").getAsString().isEmpty() && !jsonObject.get("cartCount").getAsString().equals("0")) {
                    binding.tvCartCount.setSolidColor(getResources().getColor(R.color.colorAccent));
                    binding.tvCartCount.setVisibility(View.VISIBLE);
                }
                binding.tvCount.setText(jsonObject.get("count1").getAsString());
                binding.tvCount2.setText(jsonObject.get("count2").getAsString());
                binding.tvCount3.setText(jsonObject.get("count3").getAsString());
                binding.tvCount4.setText(jsonObject.get("count4").getAsString());

                /*CircularTextView tvNotificationCount = findViewById(R.id.tvNotificationCount);
                tvNotificationCount.setVisibility(View.GONE);
                tvNotificationCount.setText(jsonObject.get("notificationCount").getAsString());
                if (!jsonObject.get("notificationCount").getAsString().isEmpty() && !jsonObject.get("notificationCount").getAsString().equals("0")) {
                    tvNotificationCount.setSolidColor(getResources().getColor(R.color.colorAccent));
                    tvNotificationCount.setVisibility(View.VISIBLE);
                }*/
                /*if (!jsonObject.get("quotationMainCount").getAsString().isEmpty() && !jsonObject.get("quotationMainCount").getAsString().equals("0")) {
                    Common.showBadge(context, bottomNavigationView, R.id.mQuotation, jsonObject.get("quotationMainCount").getAsString());
                }
                if (!jsonObject.get("orderMainCount").getAsString().isEmpty() && !jsonObject.get("orderMainCount").getAsString().equals("0")) {
                    Common.showBadge(context, bottomNavigationView, R.id.mOrder, jsonObject.get("orderMainCount").getAsString());
                }*/
            }
        });
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Received the process after broadcast
            if (intent.hasExtra(Constant.refreshCount)) {
                if (intent.getStringExtra(Constant.refreshCount).equals(Constant.refreshCount)) {
//                    getCounts();
                }
            }
        }
    }
}