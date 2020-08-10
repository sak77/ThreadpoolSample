package com.saket.threadpoolsample

import android.app.Application
import android.os.Looper
import androidx.core.os.HandlerCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.logging.Handler

/**
 * Creating threads is expensive, so you should create a thread pool only once as your app initializes.
 */
class MyApplication : Application() {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val myHandler = HandlerCompat.createAsync(Looper.getMainLooper())

    fun getMyExecutor() : ExecutorService {
        return executorService
    }

    fun getMyHandler() : android.os.Handler {
        return myHandler
    }

}