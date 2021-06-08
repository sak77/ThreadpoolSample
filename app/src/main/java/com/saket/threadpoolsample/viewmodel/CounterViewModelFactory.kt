package com.saket.threadpoolsample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.concurrent.ExecutorService

class CounterViewModelFactory(ex: ExecutorService) : ViewModelProvider.Factory {
    var executorService: ExecutorService = ex
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CounterViewModel(executorService) as T
    }
}