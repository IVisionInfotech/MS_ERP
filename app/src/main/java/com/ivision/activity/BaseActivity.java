package com.ivision.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.multidex.MultiDex;

import com.ivision.R;
import com.ivision.utils.LocaleHelper;
import com.ivision.utils.Session;
import com.ivision.utils.TopExceptionHandler;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "okhttp";
    public static int splashTimeOut = 1000;
    private static long back;
    public Context context;
    public Session session;
    protected ImageView ivBack;
    protected TextView tvTitle;
    public int flag = 0;
    public boolean updateFlag = false;
    private OnActivityResultLauncher resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        session = new Session(context);

        // Initialize the SDK
//        Places.initialize(getApplicationContext(), getResources().getString(R.string.placeAPIKey));

        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler("/mnt/sdcard/", context));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
        MultiDex.install(this);
    }

    protected void setToolbar(String title) {

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(title);
    }

    protected void gotoBack() {
        if (back + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
        } else {
            Toast.makeText(this, "Press once again to exit...", Toast.LENGTH_SHORT).show();
        }
        back = System.currentTimeMillis();
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public interface OnActivityResultLauncher {
        void onActivityResultData(Intent data, int resultCode);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            resultLauncher.onActivityResultData(data, result.getResultCode());
        }
    });

    public void goToActivity(Context context, Intent intent) {
        startActivity(intent);
    }

    public void goToActivity(Context context, Class aClass) {
        startActivity(new Intent(context, aClass));
    }

    public void goToActivity(Context context, Class aClass, String id, String title) {
        Intent intent = new Intent(context, aClass);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    public void goToActivityForResult(Intent intent, OnActivityResultLauncher resultLauncher) {
        activityResultLauncher.launch(intent);
        this.resultLauncher = resultLauncher;
    }

    public void goToActivityForResult(Context context, Class aClass, OnActivityResultLauncher resultLauncher) {
        Intent intent = new Intent(context, aClass);
        activityResultLauncher.launch(intent);
        this.resultLauncher = resultLauncher;
    }

    public void goToActivityForResult(Context context, Class aClass, String data, OnActivityResultLauncher resultLauncher) {
        Intent intent = new Intent(context, aClass);
        intent.putExtra("id", data);
        activityResultLauncher.launch(intent);
        this.resultLauncher = resultLauncher;
    }

    public void startImageSelectionForResult(Activity activity, boolean isCamera, boolean isGallery, boolean isCompress, boolean isCrop, OnActivityResultLauncher resultLauncher) {
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, isCamera);
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, isGallery);
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, isCompress);
        intent.putExtra(ImageSelectActivity.FLAG_CROP, isCrop);
        activityResultLauncher.launch(intent);
        this.resultLauncher = resultLauncher;
    }

    public void startImageSelectionForResult(Fragment fragment, boolean isCamera, boolean isGallery, boolean isCompress, boolean isCrop, OnActivityResultLauncher resultLauncher) {
        Intent intent = new Intent(fragment.getContext(), ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, isCamera);
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, isGallery);
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, isCompress);
        intent.putExtra(ImageSelectActivity.FLAG_CROP, isCrop);
        activityResultLauncher.launch(intent);
        this.resultLauncher = resultLauncher;
    }

    public void setResultOfActivity(int resultCode, boolean finishStatus) {
        Intent intent = new Intent();
        setResult(resultCode, intent);
        intent.putExtra("result", resultCode);
        setResult(Activity.RESULT_OK, intent);
        if (finishStatus) finish();
    }

    public void setResultOfActivity(Intent intent, int resultCode, boolean finishStatus) {
        setResult(resultCode, intent);
        intent.putExtra("result", resultCode);
        setResult(Activity.RESULT_OK, intent);
        if (finishStatus) finish();
    }

    public void setResultOfActivity(int resultCode, int position) {
        Intent intent = new Intent();
        setResult(resultCode, intent);
        intent.putExtra("result", resultCode);
        intent.putExtra("position", position);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void setCancelResultOfActivity(int resultCode) {
        Intent intent = new Intent();
        setResult(resultCode, intent);
        intent.putExtra("result", resultCode);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
