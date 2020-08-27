package com.erolc.expermissionlib.utils

import android.content.Context
import android.widget.Toast
import android.system.Os.accept
import androidx.fragment.app.Fragment


fun  makeToast(context: Context,toast: String,length:Int):Toast{
    return Toast.makeText(context, toast,length)
}

inline fun <R> Fragment.showToast(toast: R) {
    context?.showToast(toast)
}

inline fun <R> Context.showToast(toast: R) {
    makeToast(this,toast.toString(),Toast.LENGTH_SHORT).show()
}

inline fun <R> Fragment.showLongToast(toast: R) {
    context?.showLongToast(toast)
}

inline fun <R> Context.showLongToast(toast: R) {
    makeToast(this,toast.toString(), Toast.LENGTH_LONG).show()
}
