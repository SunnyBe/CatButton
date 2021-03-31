package com.buchi.buttoned.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
suspend fun <T> Flow<T>.getValueForTest(scope: CoroutineScope): T? {
    var value: T? = null
    scope.launch {
        collectLatest {
            println("This is the value: $it")
            value = it
        }
    }
    return value
}