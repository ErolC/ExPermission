package com.erolc.expermissionlib.utils

import android.util.Log

inline fun <reified T, R> T.loge(log: R) {
    Log.e(T::class.java.name, log.toString())
}

inline fun <reified T, R> T.log(log: R) {
    Log.d(T::class.java.name, log.toString())
}

inline fun <reified T, R> T.logv(log: R) {
    Log.v(T::class.java.name, log.toString())
}

inline fun <reified T, R> T.logi(log: R) {
    Log.i(T::class.java.name, log.toString())
}

inline fun <reified T, R> T.logw(log: R) {
    Log.w(T::class.java.name, log.toString())
}

inline fun <reified T, R> T.logwtf(log: R) {
    Log.wtf(T::class.java.name, log.toString())
}