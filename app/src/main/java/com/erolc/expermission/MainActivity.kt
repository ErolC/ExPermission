package com.erolc.expermission

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.erolc.expermissionlib.utils.loge
import com.erolc.expermissionlib.utils.showToast
import com.erolc.expermissionlib.permission.*


const val REQUEST_CAMERA_CODE = 0X1


class MainActivity : AppCompatActivity() {
    private var ipAddress = 0
    private lateinit var build: PermissionHandle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //设置本页需要用到的权限，it是请求码，根据不同的请求码，在同一个页面可以请求多组权限
        pageUsePermissions {
            when (it) {
                REQUEST_CAMERA_CODE -> arrayOf(Manifest.permission.CAMERA)
                else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
//
        //当权限通过是被调用
        onGranted {
            when (it.requestCode) {
                REQUEST_CAMERA_CODE -> showToast("申请照相成功")
                else -> {
                    for (per in it.permissions)
                        loge("${per} success")
                    showToast("申请定位与权限成功")
                }
            }
        } onDenied {//当权限被拒绝是被调用，这两个方法和setPermissionResult也是一样的，可以二者取其一
            when (it.requestCode) {
                REQUEST_CAMERA_CODE -> showToast("申请照相失败")
                else -> {
                    showToast("申请定位权限失败")
                    for (per in it.permissions)
                        loge("${per} fails")

                }
            }
        }

        /**-------------------------------------------------上面是扩展的方式--------------下面是链式的方式---------------------------------------------------------------------*/

        //链式设置请求权限
        build = PermissionHandle.Build()
            .with(this)//设置用到权限请求的位置，通常是activity或者是fragment
            .pageUserPermissions {
                when (it) {
                    REQUEST_CAMERA_CODE -> arrayOf(Manifest.permission.CAMERA)
                    else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
            .setPermissionResult {//权限申请的结果反馈
                onDenied {//当拒绝权限时被调用
                    showToast("denied")
                }
                onGranted {//当权限通过时被调用
                    showToast("success")
                }

            }
            .build()

    }

    fun requestCamera(view: View) {
        requestPermission(REQUEST_CAMERA_CODE)//扩展请求方式
//        build.request(REQUEST_CAMERA_CODE)//链式请求方式
    }

    fun requestLocation(view: View) {
        build.request(DEFAULT_REQUEST_CODE)//DEFAULT_REQUEST_CODE，默认的请求方式，可以省略
    }
}
