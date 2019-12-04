package com.erolc.expermissionlib.permission.anno


@Target(AnnotationTarget.FUNCTION)
@Retention
annotation class permission(val value:Array<String>,val requestCode:Int = 0)