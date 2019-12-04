package com.erolc.expermissionlib.permission.control

import com.erolc.expermissionlib.model.Result
import com.erolc.expermissionlib.permission.PermissionCore
import com.erolc.expermissionlib.permission.anno.permission
import com.erolc.expermissionlib.permission.has
import java.lang.RuntimeException
import java.lang.ref.SoftReference
import java.lang.reflect.Method

class AnnotationManager internal constructor(private val any: SoftReference<Any>, core: PermissionCore) {
    private val map = mutableMapOf<Int, Pair<Array<String>, Method>>()
    private var method: Method? = null

    init {
        val methods = any.get()?.javaClass?.methods?.filter { it -> it.annotations.has { it is permission } }?.toList()
        methods?.forEach { method ->
            method.annotations.forEach {
                if (it is permission) {
                    map[it.requestCode] = it.value to method
                }
            }
        }
        core.hasPermission ?: core.setNeedPermissions {
            val pair = map[it]
            method = pair?.second
            pair?.first ?: throw RuntimeException("找不到该请求码对应的权限，请检查")
        }
        if (core.manager == null) {
            core.manager = this
        }
    }

    fun granted(result: Result) {
        method?.invoke(any.get(),Granted(result.requestCode,result.permissions))
    }

    fun denied(result: Result) {
        method?.invoke(any.get(),Denied(result.requestCode,result.permissions))
    }


}