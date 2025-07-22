package com.ivision.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ivision.R;
import com.ivision.databinding.ActivitySplashBinding;
import com.ivision.utils.Common;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding binding;
    private Animation animationTop, animationBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        animationTop = AnimationUtils.loadAnimation(context, R.anim.splash_image_animation);
        animationBottom = AnimationUtils.loadAnimation(context, R.anim.splash_title_animation);

        binding.ivImage.startAnimation(animationTop);

        animationTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getData(Common.handleIntent(getIntent()));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void getData(String data) {
        final Handler handler = new Handler();
        if (!data.isEmpty()) {
            String id = data.split(", ")[0];
            String type = data.split(", ")[1];
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoNext(id, type);
                }
            }, splashTimeOut);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoNext();
                }
            }, splashTimeOut);
        }
    }

    private void gotoNext(String id, String type) {
        if (session.getLoginStatus()) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("type", type);
            startActivity(intent);
        } else {
            goToActivity(context, LoginActivity.class);
        }
        finish();
    }

    private void gotoNext() {
        if (session.getLoginStatus()) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        } else {
            goToActivity(context, LoginActivity.class);
        }
        finish();
    }
}