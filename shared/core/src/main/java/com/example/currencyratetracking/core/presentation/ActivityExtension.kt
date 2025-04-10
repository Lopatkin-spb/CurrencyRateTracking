package com.example.currencyratetracking.core.presentation

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


inline fun <reified VM : ViewModel> ComponentActivity.lazyDaggerAssistedViewModel(
    noinline viewModelInstanceCreator: (handle: SavedStateHandle) -> VM,
): Lazy<VM> {
    return viewModels<VM> { ViewModelLocalAssistedFactory(this, viewModelInstanceCreator) }
}