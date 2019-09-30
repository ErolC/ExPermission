package com.erolc.expermission

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class TestWorker(context: Context,pa: WorkerParameters):Worker(context,pa) {

    override fun doWork(): Result {
        val workDataOf = workDataOf("out" to 2)
        return Result.success(workDataOf)
    }

}