package com.erolc.expermission;

import android.Manifest;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.erolc.expermissionlib.permission.PermissionHandle;
import com.erolc.expermissionlib.permission.PermissionKt;
import com.erolc.expermissionlib.permission.anno.permission;
import com.erolc.expermissionlib.permission.control.Denied;
import com.erolc.expermissionlib.permission.control.Granted;
import com.erolc.expermissionlib.permission.control.PerResult;
import com.erolc.expermissionlib.utils.TranF;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PermissionHandle build = new PermissionHandle.Build()
                .with(this)
                .pageUserPermissions(integer -> new String[]{})
                .setPermissionResult(TranF._Unit(permissionResult -> {
                    permissionResult.onDenied(TranF._Unit(result -> {

                    }));
                }))
                .build();

        build.request(0);

        PermissionKt.requestPermission(this,100);
    }

    @permission(value = {Manifest.permission.CAMERA},requestCode = 100)
    public void result(PerResult result) {
        if (result instanceof Granted) {
            //成功
        }
        if (result instanceof Denied){
            //失败
        }
    }
}
