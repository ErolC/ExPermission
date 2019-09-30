package com.erolc.expermissionlib.permission

/**
 * 权限处理核心类工厂，给每一个界面生成一个独立的权限处理核心类，以防所需申请权限混乱
 */
internal class PermissionCoreFactory private constructor() {
    val map:MutableMap<String, PermissionCore> = mutableMapOf()

    companion object{
        private val factory: PermissionCoreFactory by lazy{ PermissionCoreFactory() }

        fun create(clazzName:String): PermissionCore {
            var core = factory.map[clazzName]
            if (core == null){
                synchronized(PermissionCoreFactory::class.java){
                    factory.map[clazzName] = PermissionCore()
                    core = factory.map[clazzName]
                }
            }

            return core!!
        }
    }
}