package com.erolc.expermissionlib.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import com.erolc.expermissionlib.R
import com.erolc.expermissionlib.model.Result
import org.jetbrains.anko.alert
import java.lang.RuntimeException

/**
 * 动态权限授权的核心类
 *
 * [ActivityCompat.shouldShowRequestPermissionRationale]说明：
 * 如果没有请求过就调用，会返回false
 * 如果请求过，但是被拒绝，会返回true
 * 如果请求过，但被拒绝并勾选了"不再提示",会返回false
 * 如果请求过，并且被授予了，会返回false
 * 但这并非不能该改变，通过源码可以得知，在系统版本23以后，这个的返回值将由activity管理，当然如果请求在fragment，那么将由fragment管理
 */
internal class PermissionCore {

    internal var hasPermission: ((Int) -> Array<String>)? = null

    internal val result = PermissionResult()

    //请求的所有权限
    private var requestPermission: Array<String>? = null


    /**
     * 在申请权限之前，检查权限是否未授权，将授权和未授权的权限分开
     * @param context 上下文
     * @param requestCode 请求码
     * @return 处理之后的权限，第一位为已授权部分，第二位为未授权部分
     */
    private fun checkSelfPermission(
        context: Context,
        requestCode: Int
    ): Pair<Array<String>, Array<String>> {
        requestPermission = hasPermission?.invoke(requestCode)//获取需要请求的权限
        val dpers = mutableListOf<String>()//所有未授权的权限
        val grantPers = mutableListOf<String>()//所有已授权的权限
        requestPermission?.all {
            val checkSelfPermission = checkSelfPermission(context, it)
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {//检查是否已经同意，如果没有，则保留下来
                dpers.add(it)
            } else {
                grantPers.add(it)
            }
        }
        if (requestPermission == null) {
            throw RuntimeException("请先设置需要的权限之后再请求权限")
        }
        return Pair(grantPers.toTypedArray(), dpers.toTypedArray())
    }

    /**
     * Activity的请求方法
     */
    fun requestPermission(activity: Activity, requestCode: Int) {
        val dpers = checkSelfPermission(activity, requestCode)
        if (dpers.second.isEmpty()) {
            //当未授权部分的的大小为0，则说明全部都已经同意了。
            granted(Result(requestCode, dpers.first))
            return
        }
        ActivityCompat.requestPermissions(activity, dpers.second, requestCode)
    }

    /**
     * Fragment的请求方法
     */
    fun requestPermission(fragment: Fragment, requestCode: Int) {
        val dpers = checkSelfPermission(fragment.requireContext(), requestCode)
        if (dpers.second.isEmpty()) {
            //当未授权部分的的大小为0，则说明全部都已经同意了。
            granted(Result(requestCode, dpers.first))
            return
        }
        fragment.requestPermissions(dpers.second, requestCode)
    }

    /**
     * 请求结果处理
     */
    fun permissionResult(
        activity: Activity,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val grantPermission = getGrantPermission(permissions, grantResults)
        val deniedPermission = getDeniedPermission(permissions, grantResults)
        if (deniedPermission.isEmpty()) {//如果不同意的权限的集合是空的，那么全部权限都同意
            granted(Result(requestCode, grantPermission))
            return
        }

        var shouldShowRequestPermission = false
        //如果存在有一个是不再提示，则直接引导用户到设置界面设置
        val per = mutableListOf<String>()
        for (permission in deniedPermission) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                if (!shouldShowRequestPermission)
                    shouldShowRequestPermission = true
                per.add(permission)
            }
        }

        if (shouldShowRequestPermission) {
            //引导用户到设置界面
            val showDialog = Build.showDialog
            if (showDialog == null || !showDialog.invoke(per)){
                showAlert(activity, per)
            }
        } else {
            result(activity, requestCode, grantPermission, deniedPermission)
        }
    }

    /**
     * 弹出引导弹框
     */
    private fun showAlert(activity: Activity, per: MutableList<String>) {
        val contentText = activity.resources.getString(Build.content ?: R.string.settingMessage)
        val titleText = activity.resources.getString(Build.title ?: R.string.settingTitle)

        val permissionsContent = StringBuffer()
        for (permission in per) {
            val permissionName = PermissionTable.getPermissionName(activity.resources, permission)
            if (permissionsContent.isNotEmpty()) {
                permissionsContent.append(",")
            }
            permissionsContent.append(permissionName)
        }
        val str = if (per.size > 1) activity.resources.getString(R.string.and_order) else ""

        val contentStr =
            activity.resources.getString(
                R.string.apply_for_a_permission,
                permissionsContent.append(str)
            )

        val content = SpannableStringBuilder(contentStr)
        content.append(contentText)
        val contentColor =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                activity.getColor(Build.contentTextColor ?: R.color.contentColor)
            } else {
                activity.resources.getColor(Build.contentTextColor ?: R.color.contentColor)
            }

        val color = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activity.getColor(Build.titleTextColor ?: R.color.titleColor)
        } else {
            activity.resources.getColor(
                Build.titleTextColor ?: R.color.titleColor
            )
        }
        content.setSpan(
            ForegroundColorSpan(contentColor),
            0,
            content.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )

        val title = SpannableString(titleText)
        title.setSpan(
            ForegroundColorSpan(color),
            0,
            title.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )

        activity.alert(content, title) {
            negativeButton(Build.sureButtonText ?: R.string.setting) {
                activity.toSettingPermission()
            }
        }.show()//弹出引导用户去设置授权界面的弹框
    }

    /**
     * 结果发放，将授权结果返回出去
     */
    private fun result(
        activity: Activity,
        requestCode: Int,
        grantPermission: Array<out String>,
        deniedPermission: Array<out String>
    ) {
        if (grantPermission.isEmpty() && requestPermission?.size != deniedPermission.size) {
            val permissions = checkSelfPermission(activity, requestCode)
            if (permissions.first.isNotEmpty()) {
                granted(Result(requestCode, permissions.first))

            }
        }
        if (deniedPermission.isNotEmpty()) {
            denied(Result(requestCode, deniedPermission))
        }
    }

    /**
     * 获取授权成功的权限集合
     */
    private fun getGrantPermission(
        permissions: Array<out String>,
        grantResults: IntArray
    ): Array<String> {
        val permission = mutableListOf<String>()
        for (i in permissions.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                permission.add(permissions[i])
            }
        }
        return permission.toTypedArray()
    }

    /**
     * 获取未授权的权限集合
     */
    private fun getDeniedPermission(
        permissions: Array<out String>,
        grantResults: IntArray
    ): Array<String> {
        val permission = mutableListOf<String>()
        for (i in permissions.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permission.add(permissions[i])
            }
        }
        return permission.toTypedArray()
    }

    private fun granted(r: Result) {
        result.granted?.invoke(r)
    }

    private fun denied(r: Result) {
        result.denied?.invoke(r)
    }
}


/**
 * 去权限管理页面设置权限,由于无法直接去到权限设置页面，所以只能去到应用信息页面
 */
fun Activity.toSettingPermission() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri = Uri.fromParts("package", this.packageName, null)
    intent.data = uri
    this.startActivity(intent)
}
