package com.saket.threadpoolsample.viewmodel

import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModel
import java.lang.Exception
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

/**
 * Data in Viewmodel drives the UI.
 *
 * All the logic goes here.
 */
class CounterViewModel(private val executorService: ExecutorService) : ViewModel() {

    /*
        Passing a consumer callback called currValue which updates the currvalue.
        But since this callback is called on the background thread it cannot update the UI.
        To do so, it has to be called on the main thread.
        To communicate with the main thread i pass the handler which has reference of mainthread looper.
        Using this handler's post() i can execute the code inside the callback on the main thread
        to update the UI.
     */
    fun startCounters(
        upperLimit: Int,
        currValue: (updateValue: Int, counterNumber: Int) -> Unit,
        handler: Handler
    ) {

        //The execute(Runnable) method does not return any value. It simply fires and forgets.
        executorService.execute {
            val delayMillis1 = 100L
            //Handle interruptedException since thread may get interrupted after shutdownNow()
            try {
                //Thread 1
                for (i in 0..upperLimit) {
                    Thread.sleep(delayMillis1)
                    Log.v("CounterViewModel", "Counter 1 : $i")
                    handler.post { currValue(i, 1) }
                }
            } catch (ex: InterruptedException) {
                //executorService.shutdown()
                println("Thread 1 interrupted")
            }
        }

        executorService.execute {
            val delayMillis2 = 200L
            //Handle interruptedException since thread may get interrupted after shutdownNow()
            try {
                //Thread 2
                for (i in 0..upperLimit) {
                    Thread.sleep(delayMillis2)
                    Log.v("CounterViewModel", "Counter 2 : $i")
                    handler.post { currValue(i, 2) }
                }
            } catch (ex: InterruptedException) {
                println("Thread 2 interrupted")
            }
        }

        executorService.execute {
            val delayMillis3 = 400L
            //Handle interruptedException since thread may get interrupted after shutdownNow()
            try {
                //Thread 3
                for (i in 0..upperLimit) {
                    Thread.sleep(delayMillis3)
                    Log.v("CounterViewModel", "Counter 3 : $i")
                    handler.post { currValue(i, 3) }
                }
            } catch (ex: InterruptedException) {
                println("Thread 3 interrupted")
            }
        }

        executorService.execute {
            val delayMillis4 = 800L
            //Handle interruptedException since thread may get interrupted after shutdownNow()
            try {
                //Thread 4
                for (i in 0..upperLimit) {
                    Thread.sleep(delayMillis4)
                    Log.v("CounterViewModel", "Counter 4 : $i")
                    handler.post { currValue(i, 4) }
                }
            } catch (ex: InterruptedException) {
                println("Thread 4 interrupted")
            }
        }
    }

    /*
    Unlike fire and forget ExecutorService.execute(), the submit method returns a Future instance.
    This Future instance can be used to return result of the task.
     */
    fun sayHello(name: String): Future<String> {
        return executorService.submit(Callable<String> {
            Thread.sleep(2000)
            return@Callable "Hello, $name"
        })
    }

    //Arguments passed to a function are val. To modify them, you can create a local copy and work
    //with it.
    private fun incrementCounterVal(counterValue: Int, delayMillis: Long): Int {
        var currCounterVal = counterValue
        //Introduce delay
        Thread.sleep(delayMillis)
        return currCounterVal++
    }

    /*
    ThreadpoolExecutor does not handle activity lifecycle changes by itself.
    So it has to be handled by the app.
    Depending on the work the task is doing in the background, it can be useful to
    see if it should continue after configuration changes...
    ViewModels is one good place to execute the threadpool. It survives temp configuration
    changes of the lifecycleowner activity. So when activity gets the viewmodel instance
    after rotation, its data still persists.
    Only when the activity is permanently destroyed, then we can choose to shut down the
    threadpool, like in this case...
     */
    override fun onCleared() {
        super.onCleared()
        println("MainActivity ViewModel onCleared")
        executorService.shutdown()  //This is the recommended way.
        //executorService.shutdownNow()   //Tries to interrupt current tasks. But no guarantee it will
        //immediately stop execution.
        //executorService.awaitTermination()  //Blocks until all tasks have completed execution after a shutdown
    // request or timeout occurs or current thread is interrupted. Whichever happens first.
    }
}