package com.erolc.expermission;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.erolc.expermissionlib.permission.PermissionHandle;
import com.erolc.expermissionlib.utils.TranF;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PermissionHandle build = new PermissionHandle.Build()
                .with(this)
                .pageUserPermissions(integer -> new String[]{})
                .setPermissionResult(TranF._Unit(permissionResult -> {

                }))
                .build();

        build.request(0);
    }
}
