package com.example.currencyratetracking.core

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*


/**
 * collected emits to list & emits lists in order arrival
 * (->1->2->3) => ->[]->[1]->[1,2]->[1,2,3]
 */
fun <T> Flow<T>.withHistory(): Flow<List<T>> {
    return flow {
        var history = listOf<T>() //must be var & listOf
        emit(history)
        collect {
            history += it
            emit(history)
        }
    }
}

/**
 * collected emits to list & emit only list next
 * (->1->2->3) => ->[1,2,3]
 *
 * for list size < 10 => need tests
 */
fun <T> Flow<T>.transformToList(): Flow<List<T>> {
    return flow {
        var container = listOf<T>()
        collect { item ->
            container += item
        }
        emit(container)
    }
}

/**
 * catch only CancellationException & apply action, after action rethrow all Throwable to downstream
 *
 * => need tests
 */
fun <T> Flow<T>.catchCancellation(action: suspend FlowCollector<T>.(Throwable) -> Unit): Flow<T> {
    return this.catch { error ->
        if (error is CancellationException) {
            action(error)
        }
        throw error
    }
}

/**
 * catch any Exception (without CancellationException) & apply action,
 * after action rethrow Error & CancellationException to downstream
 *
 * => need tests
 */
fun <T> Flow<T>.catchException(action: suspend FlowCollector<T>.(Throwable) -> Unit): Flow<T> {
    return this.catch { error ->
        when (error) {
            is CancellationException -> throw error
            is Exception -> action(error)
            else -> throw error
        }
    }
}