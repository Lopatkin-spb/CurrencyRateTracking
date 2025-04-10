package com.example.currencyratetracking.core.presentation

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner


/**
 * Factory for single vm.
 * SavedStateRegistryOwner - saving dependency for restore state.
 * SavedStateHandle - passing & storing values to/in vm when creating & restoring.
 */
class ViewModelLocalAssistedFactory<VM : ViewModel>(
    savedStateRegistryOwner: SavedStateRegistryOwner,
    private val viewModelInstanceCreator: (handle: SavedStateHandle) -> VM,
) : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, null) {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(key: String, modelClass: Class<VM>, handle: SavedStateHandle): VM {
        return viewModelInstanceCreator(handle) as VM
    }
}