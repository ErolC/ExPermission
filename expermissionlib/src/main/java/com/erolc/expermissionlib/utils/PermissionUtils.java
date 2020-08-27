package com.erolc.expermissionlib.utils;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.erolc.expermissionlib.permission.PermissionKt;

public class PermissionUtils {

    public static void requestPermission(AppCompatActivity activity) {
        requestPermission(activity, 0);
    }

    public static void requestPermission(AppCompatActivity activity, int requestCode) {
        PermissionKt.requestPermission(activity, requestCode);
    }

    public static void requestPermission(Fragment fragment) {
        requestPermission(fragment, 0);
    }

    public static void requestPermission(Fragment fragment, int requestCode) {
        PermissionKt.requestPermission(fragment, requestCode);
    }


}
