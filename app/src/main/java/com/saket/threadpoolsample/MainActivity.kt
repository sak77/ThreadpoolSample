package com.saket.threadpoolsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.saket.threadpoolsample.databinding.ActivityMainBinding
import com.saket.threadpoolsample.viewmodel.CounterViewModel
import com.saket.threadpoolsample.viewmodel.CounterViewModelFactory

/**
 *
 * The purpose of this app to perform immediate background tasks using Threadpool
 * with Executorservice and ThreadpoolExecutor.
 *
 * This app displays 4 counters.
 * The user will provide the upper limit for the counters and hit the start button.
 * Each counter will start counting from 0 to the upper limit albeit at different speeds.
 * They will all stop at the upper limit.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myApplication: MyApplication = applicationContext as MyApplication

        val ex = myApplication.getMyExecutor()
        val handler = myApplication.getMainThreadHandler()
        println("MainActivity onCreate called ")
        //val counterViewModel = CounterViewModel(ex)
        //MainActivity is the lifecycle owner of the counterViewModel
        val counterViewModel: CounterViewModel by viewModels {
            CounterViewModelFactory(ex)
        }

        counterViewModel.startCounters(
            50,
            handler = handler,
            currValue = { updateValue, counterNumber ->
                when (counterNumber) {
                    1 -> binding.txtCounter1.text = updateValue.toString()
                    2 -> binding.txtCounter2.text = updateValue.toString()
                    3 -> binding.txtCounter3.text = updateValue.toString()
                    4 -> binding.txtCounter4.text = updateValue.toString()
                }
            })

        //demonstrating how executorService.submit(Callable) can be used to return a
        //Future instance as a result. We can use the .get() method to get result from the
        // callable task..
        //Note that .get() method blocks execution until background task is completed.
        //So it is important to first check if background task is complete using Future.isDone()
        //and then get the result using .get().
        val hello = counterViewModel.sayHello("Saket")
        while(!hello.isDone) {
            //Log.v("MainActivity", "Background task is executing....")
        }
        Log.v("MainActivity", hello.get())
    }


    override fun onDestroy() {
        super.onDestroy()
        println("MainActivity OnDestroy called ")
    }
}