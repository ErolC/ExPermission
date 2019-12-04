package com.erolc.expermissionlib.permission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.lang.ref.SoftReference

class PermissionHandle private constructor(private val code: PermissionCore) {

    private var aReference: SoftReference<FragmentActivity>? = null
    private var fReference: SoftReference<Fragment>? = null
    private var result: (PermissionResult.() -> Unit)? = null
    private var permissionSet:((Int)->Array<String>)? = null


    private fun initialize() {
        //判断所需要的权限是否已经准备好了
        code.hasPermission ?:  permissionSet?.apply { code.setNeedPermissions(this) }
        result?.invoke(code.result)
    }

    fun request(requestCode: Int = DEFAULT_REQUEST_CODE) {
        initialize()
        aReference?.get()?.requestPermission(requestCode)
        fReference?.get()?.requestPermission(requestCode)
    }

    class Build {
        private lateinit var core: PermissionCore
        private var aReference: SoftReference<FragmentActivity>? = null
        private var fReference: SoftReference<Fragment>? = null
        private var result: (PermissionResult.() -> Unit)? = null
        private var permissionSet:((Int)->Array<String>)? = null


        fun with(activity: FragmentActivity): Build {
            core = PermissionCoreFactory.create(activity.javaClass.name)
            aReference = SoftReference(activity)
            return this
        }

        fun with(fragment: Fragment): Build {
            core = PermissionCoreFactory.create(fragment.javaClass.name)
            fReference = SoftReference(fragment)
            return this
        }

        fun pageUserPermissions(body:(Int)->Array<String>):Build{
            permissionSet = body
            return this
        }

        fun setPermissionResult(result: PermissionResult.() -> Unit): Build {
            this.result = result
            return this
        }

        fun build(): PermissionHandle {
            val permissionHandle = PermissionHandle(core)
            permissionHandle.aReference = aReference
            permissionHandle.fReference = fReference
            permissionHandle.result = result
            permissionHandle.permissionSet = permissionSet
            return permissionHandle
        }
    }
}