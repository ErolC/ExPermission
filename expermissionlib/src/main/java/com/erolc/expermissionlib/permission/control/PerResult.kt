package com.erolc.expermissionlib.permission.control

sealed class PerResult {
     fun isGranted(function: (Granted) -> Unit) {
        if (this is Granted){
            function(this)
        }
    }

    fun isDenied(function: (Denied) -> Unit) {
        if (this is Denied){
            function(this)
        }
    }
}

class Denied(val requestCode:Int, val permissions: Array<out String>):PerResult()
class Granted(val requestCode:Int, val permissions: Array<out String>):PerResult()