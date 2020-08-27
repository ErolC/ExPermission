package com.erolc.expermissionlib.permission

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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
    val core = PermissionCoreFactory.create(this.javaClass.name)
    core.setNeedPermissions(usePermissions)
}

fun Fragment.pageUsePermissions(usePermissions: (requestCode: Int) -> Array<String>) {
    val core = PermissionCoreFactory.create(this.javaClass.name)
    core.setNeedPermissions(usePermissions)
}


/**
 * 检查是否具有requestCode 分组的权限
 */
fun Activity.requestPermission(requestCode: Int = DEFAULT_REQUEST_CODE) {
    val core = PermissionCoreFactory.create(this.javaClass.name)
    core.requestPermission(this, requestCode)
}

/**
 * 该方法设置在Activity的onRequestPermissionsResult回调中。如果你使用了Activity.requestPermission方法，那么必须设置该方法才能正常获取结果
 */
fun Activity.requestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    val core = PermissionCoreFactory.create(this.javaClass.name)
    core.permissionResult(this, requestCode, permissions, grantResults)
}


/**
 * 在FragmentActivity中的检查权限方法，该检查方法不需要设置requestPermissionsResult()
 */
fun FragmentActivity.requestPermission(requestCode: Int = DEFAULT_REQUEST_CODE) {

    val create = PermissionCoreFactory.create(this.javaClass.name)


    val result: (TempFragment) -> Unit = {
        supportFragmentManager.beginTransaction().remove(it).commit()
    }
    val fragment = TempFragment.getInstance(this.javaClass.name, requestCode, result)
    supportFragmentManager.beginTransaction().add(fragment, "tags").commit()
}

/**
 * 在Fragment中的检查权限方法，该检查方法不需要设置requestPermissionsResult()
 */
fun Fragment.requestPermission(requestCode: Int = DEFAULT_REQUEST_CODE) {
    val create = PermissionCoreFactory.create(this.javaClass.name)

    val result: (TempFragment) -> Unit =
        { childFragmentManager.beginTransaction().remove(it).commit() }

    val fragment = TempFragment.getInstance(this.javaClass.name, requestCode, result)

    childFragmentManager.beginTransaction().add(fragment, "tags").commit()
//    requireActivity().requestPermission(requestCode)
}

/**
 * 获取权限结果
 */
private fun Activity.permissionResult(): PermissionResult {
    val core = PermissionCoreFactory.create(this.javaClass.name)
    return core.result
}

fun Activity.onGranted(granted: (Result) -> Unit): PermissionResult {
    val result = permissionResult()
    result.onGranted(granted)
    return result
}

private fun Fragment.permissionResult(): PermissionResult {
    val core = PermissionCoreFactory.create(this.javaClass.name)
    return core.result
}

fun Fragment.onGranted(granted: (Result) -> Unit): PermissionResult {
    val result = permissionResult()
    result.onGranted(granted)
    return result
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


/**
 * 辅助碎片
 */
class TempFragment : Fragment() {
    private var parentClassName: String? = null

    companion object {
        private lateinit var result: (TempFragment) -> Unit
        fun getInstance(
            className: String, requestCode: Int,
            outResult: (TempFragment) -> Unit
        ): TempFragment {
            val bundle = Bundle()
            bundle.putInt("requestCode", requestCode)
            bundle.putString("className", className)
            val fragment = TempFragment()
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

}

fun <T> Array<T>.has(body: (T) -> Boolean): Boolean {
    forEach {
        if (body(it)) {
            return true
        }
    }
    return false
}

/**
 * 却权限管理页面设置权限
 */
fun Activity.toSettingPermission() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri = Uri.fromParts("package", this.packageName, null)
    intent.data = uri
    this.startActivity(intent)
}