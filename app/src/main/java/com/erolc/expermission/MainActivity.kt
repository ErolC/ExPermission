package com.erolc.expermission

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.erolc.expermissionlib.permission.PermissionHandle


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun expand(view: View) {
        Expand.start(this)
    }

    fun link(view: View) {
        Link.start(this)
    }


}