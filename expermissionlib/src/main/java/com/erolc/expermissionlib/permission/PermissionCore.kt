package com.erolc.expermissionlib.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import com.erolc.expermissionlib.R
import com.erolc.expermissionlib.model.Result
import org.jetbrains.anko.alert


internal class PermissionCore {

    internal var hasPermission: ((Int) -> Array<String>)? = null
    internal val result = PermissionResult()
    //请求的所有权限
    private var requestPermission: Array<String>? = null

    /**
     * 根据requestCode请求需要的权限
     */
    fun setNeedPermissions(needPermissions: (Int) -> Array<String>) {
        hasPermission = needPermissions
    }

    /**
     * 在申请权限之前，检查权限是否未授权，将授权和未授权的权限分开
     * @param context 上下文
     * @param requestCode 请求码
     * @return 处理之后的权限，第一位为以授权部分，第二位为未授权部分
     */
    private fun checkSelfPermission(context: Context, requestCode: Int): Pair<Array<String>, Array<String>> {
        requestPermission = hasPermission?.invoke(requestCode)//获取需要请求的权限
        if (requestPermission == null) {
            throw RuntimeException("请先设置需要的权限之后再请求权限")
        }
        val dpers = mutableListOf<String>()//所有未授权的权限
        val grantPers = mutableListOf<String>()//所有已授权的权限
        requestPermission!!.all {
            if (checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {//检查是否已经同意，如果没有，则保留下来
                dpers.add(it)
            } else {
                grantPers.add(it)
            }
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
            result.granted(Result(requestCode, dpers.first))
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
            result.granted(Result(requestCode, dpers.first))
            return
        }
        fragment.requestPermissions(dpers.second, requestCode)
    }

    /**
     * 请求结果处理
     */
    fun permissionResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val grantPermission = getGrantPermission(permissions, grantResults)
        val deniedPermission = getDeniedPermission(permissions, grantResults)
        if (deniedPermission.isEmpty()) {//如果不同意的权限的集合是空的，那么全部权限都同意
            result.granted(Result(requestCode, grantPermission))
        }

        var boolean = true
        //如果存在有一个是不再提示，则直接引导用户到设置界面设置
        var per = mutableListOf<String>()
        for (permission in deniedPermission) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                if (boolean)
                    boolean = false
                per.add(permission)
            }
        }

        if (!boolean && Build.isShow) {
            //引导用户到设置界面
            showAlert(activity,per)
        } else {
            result(activity, requestCode, grantPermission, deniedPermission)
        }
    }

    /**
     * 弹出引导弹框
     */
    private fun showAlert(activity: Activity,per: MutableList<String>) {
        val contentText = activity.resources.getString(Build.content ?: R.string.settingMessage)
        val titleText = activity.resources.getString(Build.title ?: R.string.settingTitle)

        var permissionsContent = StringBuffer()
        for (permission in per) {
            val permissionName = PermissionTable.getPermissionName(activity.resources, permission)
            if (permissionsContent.isNotEmpty()) {
                permissionsContent.append(",")
            }
            permissionsContent.append(permissionName)
        }
        val str = if (per.size > 1) activity.resources.getString(R.string.and_order) else ""

        val contentStr =
            activity.resources.getString(R.string.apply_for_a_permission, permissionsContent.append(str))

        val content = SpannableStringBuilder(contentStr)
        content.append(contentText)
        content.setSpan(
            ForegroundColorSpan(activity.resources.getColor(Build.contentTextColor ?: R.color.contentColor)),
            0,
            content.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )

        val title = SpannableString(titleText)
        title.setSpan(
            ForegroundColorSpan(activity.resources.getColor(Build.titleTextColor ?: R.color.titleColor)),
            0,
            title.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )

        activity.alert(content, title) {
            negativeButton(Build.sureButtonText ?: R.string.setting) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
        }.show()//弹出引导用户去设置授权界面的弹框
    }

    /**
     * 结果发放
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
                result.granted(Result(requestCode, permissions.first))
            }
        }
        if (deniedPermission.isNotEmpty()) {
            result.denied(Result(requestCode, deniedPermission))
        }
    }

    /**
     * 获取授权成功的权限集合
     */
    private fun getGrantPermission(permissions: Array<out String>, grantResults: IntArray): Array<String> {
        val permission = mutableListOf<String>()
        for (i in 0 until permissions.size) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                permission.add(permissions[i])
            }
        }
        return permission.toTypedArray()
    }

    /**
     * 获取未授权的权限集合
     */
    private fun getDeniedPermission(permissions: Array<out String>, grantResults: IntArray): Array<String> {
        val permission = mutableListOf<String>()
        for (i in 0 until permissions.size) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permission.add(permissions[i])
            }
        }
        return permission.toTypedArray()
    }
}


