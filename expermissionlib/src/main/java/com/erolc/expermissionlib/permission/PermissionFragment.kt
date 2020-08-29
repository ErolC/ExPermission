package com.erolc.expermissionlib.permission

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * 权限请求用的的fragment，当权限委托无法使用的时候将通过这个fragment链接请求和结果
 */
class PermissionFragment : Fragment() {
    private var parentClassName: String? = null

    companion object {
        private lateinit var result: (PermissionFragment) -> Unit
        fun getInstance(
            className: String, requestCode: Int,
            outResult: (PermissionFragment) -> Unit
        ): PermissionFragment {
            val bundle = Bundle()
            bundle.putInt("requestCode", requestCode)
            bundle.putString("className", className)
            val fragment = PermissionFragment()
            result = outResult
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val requestCode = arguments?.getInt("requestCode")
        parentClassName = arguments?.getString("className")
        val core = parentClassName?.let { PermissionCoreFactory.create(it) }
        requestCode?.let { core?.requestPermission(this, it) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        parentClassName?.let {
            requestPermissionsResult(it, requestCode, permissions, grantResults)
            result(this)
        }

    }

    /**
     * 辅助碎片的辅助权限回调方法
     */
    private fun Fragment.requestPermissionsResult(
        className: String,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val core = PermissionCoreFactory.create(className)
        core.permissionResult(this.requireActivity(), requestCode, permissions, grantResults)
    }

}


/**
 * 在FragmentActivity中的检查权限方法，该检查方法不需要设置requestPermissionsResult()
 */
internal fun FragmentActivity.requestPermission(requestCode: Int = DEFAULT_REQUEST_CODE) {
    val result: (PermissionFragment) -> Unit = {
        supportFragmentManager.beginTransaction().remove(it).commit()
    }
    val fragment = PermissionFragment.getInstance(this.javaClass.name, requestCode, result)
    supportFragmentManager.beginTransaction().add(fragment, "tags").commit()
}

/**
 * 在Fragment中的检查权限方法，该检查方法不需要设置requestPermissionsResult()
 */

internal fun Fragment.requestPermissionDelegate(requestCode: Int = DEFAULT_REQUEST_CODE) {
    val create = PermissionCoreFactory.create(this.javaClass.name)

    val result: (PermissionFragment) -> Unit =
        { childFragmentManager.beginTransaction().remove(it).commit() }

    val fragment = PermissionFragment.getInstance(this.javaClass.name, requestCode, result)

    childFragmentManager.beginTransaction().add(fragment, "tags").commit()
//    requireActivity().requestPermission(requestCode)
}