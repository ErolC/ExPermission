package com.erolc.expermission

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.erolc.expermissionlib.utils.loge
import com.erolc.expermissionlib.utils.showToast
import com.erolc.expermissionlib.permission.*
import com.erolc.expermissionlib.permission.anno.permission
import com.erolc.expermissionlib.permission.control.Denied
import com.erolc.expermissionlib.permission.control.Granted
import com.erolc.expermissionlib.permission.control.PerResult


const val REQUEST_CAMERA_CODE = 0X1
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun requestCamera(view: View) {
        requestPermission(REQUEST_CAMERA_CODE)//申请权限
    }

    fun requestLocation(view:View){
        requestPermission()
    }

    @permission([Manifest.permission.CAMERA],REQUEST_CAMERA_CODE)
    fun cameraRsult(result: PerResult) {
        result.isGranted{
            //申请成功
            showToast("success")
        }

        result.isDenied {
            showToast("${it.requestCode}-denied")
        }
    }

}
