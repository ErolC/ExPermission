package com.erolc.expermissionlib.permission

import androidx.annotation.ColorInt
import androidx.annotation.StringRes

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

    //是否需要展示引导界面
    @Volatile
    var isShow = true

}