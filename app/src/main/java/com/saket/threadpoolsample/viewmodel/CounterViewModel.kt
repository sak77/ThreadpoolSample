package com.saket.threadpoolsample.viewmodel

import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.concurrent.ExecutorService

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
    fun startCounters(upperLimit: Int, currValue: (updateValue: Int, counterNumber: Int) -> Unit, handler: Handler) {
        //Create 4 counters upto the upper limit

        executorService.execute {
            val delayMillis1 = 100L
            //Thread 1
            for (i in 0..upperLimit) {
                Thread.sleep(delayMillis1)
                //Log.v("CounterViewModel", "Counter 1 : $i")
                handler.post { currValue(i, 1) }
            }
        }

        executorService.execute {
            val delayMillis2 = 200L
            //Thread 2
            for (i in 0..upperLimit) {
                Thread.sleep(delayMillis2)
                //Log.v("CounterViewModel", "Counter 2 : $i")
                handler.post { currValue(i, 2) }
            }
        }

        executorService.execute {
            val delayMillis3 = 400L
            //Thread 3
            for (i in 0..upperLimit) {
                Thread.sleep(delayMillis3)
                //Log.v("CounterViewModel", "Counter 3 : $i")
                handler.post { currValue(i, 3) }
            }
        }

        executorService.execute {
            val delayMillis4 = 800L
            //Thread 4
            for (i in 0..upperLimit) {
                Thread.sleep(delayMillis4)
                //Log.v("CounterViewModel", "Counter 4 : $i")
                handler.post { currValue(i, 4) }
            }
        }
    }

    //Arguments passed to a function are val. To modify them, you can create a local copy and work
    //with it.
    private fun incrementCounterVal(counterValue: Int, delayMillis: Long): Int {
        var currCounterVal = counterValue
        //Introduce delay
        Thread.sleep(delayMillis)
        return currCounterVal++
    }
}