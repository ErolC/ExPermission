package com.erolc.expermissionlib.model

/**
 * Int 是请求码，第二个参数是权限集合，如果是通过的函数，那么就是通过的权限集合，反之亦然
 */
data class Result(val requestCode:Int, val permissions: Array<out String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Result

        if (requestCode != other.requestCode) return false
        if (!permissions.contentEquals(other.permissions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = requestCode
        result = 31 * result + permissions.contentHashCode()
        return result
    }
}