package com.example.currencyratetracking.core.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*





//--------------------


//    public fun <T> Flow<T>.onStartT(
//        action: suspend FlowCollector<T>.() -> Unit
//    ): Flow<T> {
//        return emptyFlow()
//    }
//
//    public fun <T> Flow<T>.stateIn(
//        scope: CoroutineScope,
//        started: SharingStarted,
//        initialValue: T
//    ): StateFlow<T> {
//       return emptyFlow<>()
//    }

public fun <T> StateFlow<T>.onStartState(
    initialValue: T,
    action: suspend FlowCollector<T>.() -> Unit
): StateFlow<T> {
    return onStartState(
//        scope = viewModelScope,
        initialValue = initialValue,
        action = action,
    )
}

//----------------

fun <T, K> StateFlow<T>.mapState(
    transform: (data: T) -> K
): StateFlow<K> {
    return mapState(
//        scope = viewModelScope,
        transform = transform,
    )
}

fun <T, K> StateFlow<T>.mapState(
    initialValue: K,
    transform: suspend (data: T) -> K
): StateFlow<K> {
    return mapState(
//        scope = viewModelScope,
        initialValue = initialValue,
        transform = transform,
    )
}

fun <T, K> StateFlow<T>.mapState(
    scope: CoroutineScope,
    transform: (data: T) -> K
): StateFlow<K> {
    return mapLatest { transform(it) }
        .stateIn(scope, SharingStarted.Eagerly, transform(value))
}

fun <T, K> StateFlow<T>.mapState(
    scope: CoroutineScope,
    initialValue: K,
    transform: suspend (data: T) -> K
): StateFlow<K> {
    return mapLatest { transform(it) }
        .stateIn(scope, SharingStarted.Eagerly, initialValue)
}


//----------------


/**
 * It is a shorthand for onStart{}.stateIn()
 */
public fun <T> StateFlow<T>.onStartState(
    scope: CoroutineScope,
    initialValue: T,
    action: suspend FlowCollector<T>.() -> Unit
): StateFlow<T> {
    return onStart { action() }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = initialValue,
        )
}

public fun <T> StateFlow<T>.onStartT(
    action: suspend FlowCollector<T>.() -> Unit
): Flow<T> {
    return this
        .onStart { action() }

}