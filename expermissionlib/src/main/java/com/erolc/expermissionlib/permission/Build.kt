package com.erolc.expermissionlib.permission

import androidx.annotation.ColorInt
import androidx.annotation.StringRes


/**
 * 引导用户到系统设置权限页面设置权限的可修改的部分参数
 */
object Build {

    //引导界面的title
    @Volatile
    @StringRes
    var title: Int? = null

    //引导界面的部分内容
    @Volatile
    @StringRes
    var content: Int? = null

    //引导界面的确定按钮文本
    @Volatile
    @StringRes
    var sureButtonText: Int? = null

    //引导界面的title颜色
    @Volatile
    @ColorInt
    var titleTextColor: Int? = null

    //引导界面的正文颜色
    @Volatile
    @ColorInt
    var contentTextColor: Int? = null
    internal var showDialog: ((permissions: MutableList<String>) -> Boolean)? = null

    /**
     * 该弹框最好的实现就是说明app需要该权限做什么
     * 如果系统弹框样式无法满足你的时候，你可以根据自己的情况，设置该弹框，可以调用[toSettingPermission]直接去到应用信息页面，也可以通过引导性语言引导用户通过设置找到设置权限页面，并授予权限。
     *
     * 可以使用[PermissionTable.getPermissionName]方法通过权限获取对应的说明，以便更好的告知用户需要通过的权限是什么
     * 也更好的让用户知道我们为什么需要这个权限
     * @param body 当返回值为false时，使用默认的引导弹框
     */
    fun showDialog(body: (permissions: MutableList<String>) -> Boolean) {
        showDialog = body
    }
}