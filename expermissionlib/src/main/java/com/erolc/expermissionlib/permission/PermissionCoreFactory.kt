package com.erolc.expermissionlib.permission

/**
 * 权限处理核心类工厂，给每一个界面生成一个独立的权限处理核心类，以防所需申请权限混乱
 *
 */
internal class PermissionCoreFactory private constructor() {
    val map:MutableMap<String, PermissionCore> = mutableMapOf()

    companion object{
        private val factory: PermissionCoreFactory by lazy{ PermissionCoreFactory() }

        /**
         * @param clazzName fragment或activity的名字，说明该权限核心类是属于该页面的
         */
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