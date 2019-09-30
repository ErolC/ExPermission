package com.erolc.expermissionlib.permission

import com.erolc.expermissionlib.model.Result

/**
 * 修改：回调中只有两个参数，一个是requestCode，就是该次请求的请求码
 * 一个是permissions，如果在onGranted方法中回调的就是通过的权限。在onDenied中则是未通过的权限
 *      requestCode:Int, permissions: Array<out String>
 */
class PermissionResult {

    internal lateinit var denied: (Result) -> Unit
    internal lateinit var granted: (Result) -> Unit
    /**
     * 一次中请求的所有权限
     * 对应权限是否申请通过 0--通过，-1--不通过
     */
    infix fun onDenied(denied: (Result) -> Unit) {
        this.denied = denied
    }

    fun onGranted(granted: (Result) -> Unit) {
        this.granted = granted
    }


}

