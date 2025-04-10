package com.example.currencyratetracking.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel


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