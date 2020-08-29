package com.erolc.expermissionlib.compat

import android.app.Activity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.erolc.expermissionlib.permission.PermissionCore
import com.erolc.expermissionlib.permission.PermissionCoreFactory
import com.erolc.expermissionlib.permission.PermissionResult

/**
 * 权限请求兼容类，兼容了使用委托，fragment，以及原生请求方式。
 */
object PermissionCompat {
    private val delegateImpl = PermissionDelegateImpl()

    init {
        ActivityCompat.setPermissionCompatDelegate(delegateImpl)
    }

    fun pageUsePermissions(
        activity: Activity,
        usePermissions: (requestCode: Int) -> Array<String>
    ) {
        getPermissionCode(activity).hasPermission = usePermissions
    }

    fun pageUsePermissions(
        fragment: Fragment,
        usePermissions: (requestCode: Int) -> Array<String>
    ) {
        getPermissionCode(fragment).hasPermission = usePermissions
    }

    fun requestPermission(activity: Activity, requestCode: Int) {
        getPermissionCode(activity).requestPermission(activity, requestCode)
    }

    fun requestPermission(fragment: Fragment, requestCode: Int) {
        getPermissionCode(fragment).requestPermission(fragment.requireActivity(), requestCode)
    }

    fun requestPermissionsResult(
        fragment: Fragment,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        getPermissionCode(fragment).permissionResult(
            fragment.requireActivity(),
            requestCode,
            permissions,
            grantResults
        )
    }

    fun requestPermissionsResult(
        activity: Activity,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        getPermissionCode(activity).permissionResult(
            activity,
            requestCode,
            permissions,
            grantResults
        )
    }

    private fun getPermissionCode(fragment: Fragment): PermissionCore {
        return PermissionCoreFactory.create(fragment.javaClass.name)

    }

    private fun getPermissionCode(activity: Activity): PermissionCore {
        return PermissionCoreFactory.create(activity.javaClass.name)

    }

    /**
     * 获取权限结果
     */
    internal fun permissionResult(activity: Activity): PermissionResult {
        return getPermissionCode(activity).result
    }


    internal fun permissionResult(fragment: Fragment): PermissionResult {
        return getPermissionCode(fragment).result
    }

}