package com.saket.threadpoolsample

import android.app.Application
import android.os.Looper
import android.util.Log
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
    //A thread pool of 4 threads and a shared unbounded queue.
    private val executorService: ThreadPoolExecutor = Executors.newFixedThreadPool(4) as ThreadPoolExecutor

    //Thread pool of single thread and an unbounded queue.
    //Unlike newFixedThreadPool(1), the executorService here cannot be reconfigured to add new threads.
    private val singleExecutorService: ExecutorService = Executors.newSingleThreadExecutor()

    //Creates new thread as needed. But will reusue previously created threads when available.
    //Improves performance of pool with many short-lived async tasks.
    //An idle thread in the pool will be terminated after 60 seconds. So no resource wastage here.
    private val cachedExecutorService = Executors.newCachedThreadPool()

    private val workStealingExecutorService = Executors.newWorkStealingPool()
    private val myHandler = HandlerCompat.createAsync(Looper.getMainLooper())

    private val myCallable: Callable<Int> = Callable { Log.d("", "") }

    //Note: Named arguments are not allowed for non-Kotlin functions...
    private val myScheduledThreadPoolExecutor = ThreadPoolExecutor(
        4,
        5000,
        4,
        TimeUnit.MILLISECONDS,
        LinkedBlockingQueue<Runnable>()
    )

    fun getMyExecutor() : ExecutorService {
        val myRunnable = MyRunnable()
        Thread(myRunnable).start()

        return workStealingExecutorService
    }


    private class MyRunnable() : Runnable {
        override fun run() {
            println("Running task from MyRunnable")
        }
    }

    fun getMainThreadHandler() : android.os.Handler {
        return myHandler
    }

}