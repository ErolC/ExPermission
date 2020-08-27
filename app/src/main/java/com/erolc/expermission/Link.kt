package com.erolc.expermission

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.erolc.expermission.databinding.ActivityLinkBinding
import com.erolc.expermissionlib.permission.PermissionHandle
import com.erolc.expermissionlib.utils.showToast

class Link : AppCompatActivity() {
    lateinit var handle:PermissionHandle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityLinkBinding>(this, R.layout.activity_link)
        binding.data = "请求位置权限"




        handle = PermissionHandle.Build()
            .with(this)
            .pageUserPermissions {
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .setPermissionResult {
                onDenied {
                    showToast("${it.requestCode} fail")
                }
                onGranted {
                    showToast("${it.requestCode} success")
                }
            }.build()

    }

    fun requestPermission(view: View){
        handle.request()
    }



    companion object{
        fun start(activity: AppCompatActivity) {
            val intent = Intent(activity, Link::class.java)
            activity.startActivity(intent)
        }
    }
}