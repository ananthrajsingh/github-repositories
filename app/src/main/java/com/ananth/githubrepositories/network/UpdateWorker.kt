package com.ananth.githubrepositories.network

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UpdateWorker (appContext: Context, params: WorkerParameters): Worker(appContext, params) {
    override fun doWork(): Result {
        try {
            getRepoApiService(context = applicationContext).getRepositories()
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Result.retry()
    }
}