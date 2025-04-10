package com.example.currencyratetracking.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel


/**
 * Listener for composable screen lifecycle. Listen single event avoid recompositions.
 * For analytics, logs, and other events.
 */
@Composable
fun OnLifecycleScreen(
    onStart: () -> Unit = {},
    onStop: () -> Unit = {},
) {
    OnLifecycleComposable(
        onStart = onStart,
        onStop = onStop,
    )
}

@Composable
private fun OnLifecycleComposable(
    onStart: () -> Unit,
    onStop: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    // Safely update the current lambdas when a new one is provided
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnStop by rememberUpdatedState(onStop)

    // If `lifecycleOwner` changes, dispose and reset the effect
    DisposableEffect(lifecycleOwner) {

        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                currentOnStart()
            } else if (event == Lifecycle.Event.ON_STOP) {
                currentOnStop()
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}


/**
 * It uses LocalViewModelStoreOwner,
 * which means the owner may be activity, fragment or navBackStackEntry.
 */
@Composable
inline fun <reified VM : ViewModel> daggerViewModel(
    crossinline viewModelInstanceCreator: () -> VM,
): VM {
    val factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = viewModelInstanceCreator() as VM
    }
    return viewModel(
        modelClass = VM::class.java,
        factory = factory,
    )
}


@Composable
inline fun <reified VM : ViewModel> daggerAssistedViewModel(
    noinline viewModelInstanceCreator: (handle: SavedStateHandle) -> VM,
): VM {
    val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current
    return viewModel(
        modelClass = VM::class.java,
        factory = ViewModelLocalAssistedFactory(savedStateRegistryOwner, viewModelInstanceCreator),
    )
}