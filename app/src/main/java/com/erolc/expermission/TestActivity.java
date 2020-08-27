package com.erolc.expermission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.erolc.expermission.databinding.ActivityExpandBinding;
import com.erolc.expermissionlib.utils.PermissionUtils;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityExpandBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_expand);
        binding.setData("请求相机权限");

    }

    public void requestPermission(View view){
        PermissionUtils.requestPermission(this);
    }


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, TestActivity.class);
        activity.startActivity(intent);
    }
}
