package com.saket.threadpoolsample

import android.app.Application
import android.os.Looper
import androidx.core.os.HandlerCompat
import java.util.concurrent.*
import java.util.logging.Handler

/**
 * Creating threads is expensive, so you should create a thread pool only once as your app initializes.
 */
class MyApplication : Application() {

    /*
    Threadpool can be created using the built-in methods provided by Executors class from Java's
    Executor Framework.

    Or if you want more detailed control, then you can create your own instance of ThreadpoolExecutor
    class, as shown below.
     */
    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val myHandler = HandlerCompat.createAsync(Looper.getMainLooper())

    //Note: Named arguments are not allowed for non-Kotlin functions...
    private val myScheduledThreadPoolExecutor = ThreadPoolExecutor(
        4,
        5000,
        4,
        TimeUnit.MILLISECONDS,
        LinkedBlockingQueue<Runnable>()
    )

    fun getMyExecutor() : ExecutorService {
        return executorService
    }

    fun getMainThreadHandler() : android.os.Handler {
        return myHandler
    }

}