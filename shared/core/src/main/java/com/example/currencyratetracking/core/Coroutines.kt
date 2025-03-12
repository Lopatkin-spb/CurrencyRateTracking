package com.example.currencyratetracking.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


interface BaseCoroutineDispatcher {

    fun main(): CoroutineDispatcher

    fun default(): CoroutineDispatcher

    fun io(): CoroutineDispatcher

    fun unconfined(): CoroutineDispatcher

}


internal class BaseCoroutineDispatcherImpl @Inject constructor() : BaseCoroutineDispatcher {
    private val main: CoroutineDispatcher = Dispatchers.Main
    private val default: CoroutineDispatcher = Dispatchers.Default
    private val io: CoroutineDispatcher = Dispatchers.IO
    private val unconfined: CoroutineDispatcher = Dispatchers.Unconfined

    override fun main(): CoroutineDispatcher {
        return main
    }

    override fun default(): CoroutineDispatcher {
        return default
    }

    override fun io(): CoroutineDispatcher {
        return io
    }

    override fun unconfined(): CoroutineDispatcher {
        return unconfined
    }
}