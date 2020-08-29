package com.erolc.expermissionlib.compat

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.erolc.expermissionlib.permission.requestPermission
import com.erolc.expermissionlib.permission.requestPermissionDelegate
import com.erolc.expermissionlib.permission.requestPermissionResult
import com.erolc.expermissionlib.permission.requestPermissionsResult
import com.erolc.expermissionlib.utils.log
import com.erolc.expermissionlib.utils.loge
import com.erolc.expermissionlib.utils.logi
import com.erolc.expermissionlib.utils.logw
import java.lang.reflect.Method

/**
 * 权限请求的委托,通过这个委托，权限请求的结果将不走activity或fragment的onRequestPermissionResult方法
 */
class PermissionDelegateImpl : ActivityCompat.PermissionCompatDelegate {

    var fragment: Fragment? = null

    override fun requestPermissions(
        activity: Activity,
        permissions: Array<out String>,
        requestCode: Int
    ): Boolean {
        try {
            val method: Method = activity.packageManager.javaClass.getMethod(
                "buildRequestPermissionsIntent",
                Array<String>::class.java
            )
            val intent = method.invoke(
                activity.packageManager,
                permissions
            ) as Intent?
            if (intent != null) {
                activity.startActivityForResult(intent, requestCode)
                logi("正在通过委托类请求权限")
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (fragment != null) {
            fragment?.requestPermissionDelegate(requestCode)
            log("fragment正在通过fragment请求权限")
            return true
        }
        if (activity is FragmentActivity) {
            activity.requestPermission(requestCode)
            log("activity正在通过fragment请求权限")
            return true
        }
        logw("由于您使用的是普通的activity，无法自动获取到权限请求的结果，所以需要您重写onRequestPermissionResult方法以获得权限结果")
        logw("只需在onResultPermissionResult方法中调用requestPermissionsResult方法即可继续使用该库解析权限结果")
        return false
    }

    override fun onActivityResult(
        activity: Activity,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ): Boolean {
        val permissions = if (data != null) data.getStringArrayExtra(
            "android.content.pm.extra.REQUEST_PERMISSIONS_NAMES"
        ) else arrayOfNulls(0)
        val grantResults = if (data != null) data.getIntArrayExtra(
            "android.content.pm.extra.REQUEST_PERMISSIONS_RESULTS"
        ) else IntArray(0)
        if (fragment != null) {
            fragment?.requestPermissionResult(requestCode, permissions, grantResults)
            return true
        }
        activity.requestPermissionsResult(requestCode, permissions, grantResults)
        return true
    }


}