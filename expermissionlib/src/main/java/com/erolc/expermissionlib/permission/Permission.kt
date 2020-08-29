package com.erolc.expermissionlib.permission

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.erolc.expermissionlib.compat.PermissionCompat
import com.erolc.expermissionlib.model.Result

/**

 *
 * 最后通过permissionResult()方法得到动态权限申请结果的回调
 *
 * 注意：pageUsePermissions()方法和permissionResult()方法必须在requestPermission()方法前调用
 *
 *
 * example:
 *          pageUsePermissions {//设置页面所需权限方法
 *              if (it == 100)//it是请求码，默认为0，根据不同请求码在同一页面可以请求不同的权限
 *                  arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
 *              else
 *                  arrayOf(Manifest.permission.CAMERA)
 *          }
 *
 *          find.setOnClickListener {
 *              requestPermission()//检查是否具有该请求码的权限，默认为0
 *          }
 *
 *          onGranted {                        //权限同意时回调
 *              toast("$it 请求码的权限已同意")
 *          }  onDenied {        //权限拒绝时回调
 *              toast("$it 请求码的权限被拒绝")    //it是请求码
 *          }
 *
 */


/**
 * Activity需要的所有权限,可根据requestCode进行分组
 */
const val DEFAULT_REQUEST_CODE = 0


fun Activity.pageUsePermissions(usePermissions: (requestCode: Int) -> Array<String>) {
    PermissionCompat.pageUsePermissions(this, usePermissions)
}

fun Fragment.pageUsePermissions(usePermissions: (requestCode: Int) -> Array<String>) {
    PermissionCompat.pageUsePermissions(this, usePermissions)
}


/**
 * 检查是否具有requestCode 分组的权限
 */
fun Activity.requestPermission(requestCode: Int = DEFAULT_REQUEST_CODE) {
    PermissionCompat.requestPermission(this, requestCode)
}

fun Fragment.requestPermission(requestCode: Int = DEFAULT_REQUEST_CODE) {
    PermissionCompat.requestPermission(this, requestCode)
}

/**
 * 该方法设置在Activity的onRequestPermissionsResult回调中。
 */

fun Activity.requestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    PermissionCompat.requestPermissionsResult(this, requestCode, permissions, grantResults)
}

internal fun Fragment.requestPermissionResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    PermissionCompat.requestPermissionsResult(this, requestCode, permissions, grantResults)
}

fun Activity.onGranted(granted: (Result) -> Unit): PermissionResult {
    val result = PermissionCompat.permissionResult(this)
    result.onGranted(granted)
    return result
}


fun Fragment.onGranted(granted: (Result) -> Unit): PermissionResult {
    val result = PermissionCompat.permissionResult(this)
    result.onGranted(granted)
    return result
}