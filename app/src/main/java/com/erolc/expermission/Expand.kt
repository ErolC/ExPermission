package com.erolc.expermission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.erolc.expermission.databinding.ActivityExpandBinding
import com.erolc.expermissionlib.compat.PermissionCompat
import com.erolc.expermissionlib.permission.onGranted
import com.erolc.expermissionlib.permission.pageUsePermissions
import com.erolc.expermissionlib.permission.requestPermission
import com.erolc.expermissionlib.utils.loge
import com.erolc.expermissionlib.utils.showToast
import java.lang.reflect.Method


const val REQUEST_CAMERA_CODE = 0X1

class Expand : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityExpandBinding>(this, R.layout.activity_expand)
        binding.data = "请求相机权限"

        pageUsePermissions {
            arrayOf(Manifest.permission.CAMERA)
        }

        onGranted {
            showToast("${it.requestCode} success")

        } onDenied {
            showToast("${it.requestCode} fail")

        }
    }

    fun requestPermission(view: View) {
        requestPermission(REQUEST_CAMERA_CODE)
//        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
    }



    companion object {
        fun start(activity: AppCompatActivity) {
            val intent = Intent(activity, Expand::class.java)
            activity.startActivity(intent)
        }
    }
}