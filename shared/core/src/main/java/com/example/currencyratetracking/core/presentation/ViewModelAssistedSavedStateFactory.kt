package com.example.currencyratetracking.core.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


interface ViewModelAssistedSavedStateFactory<VM : ViewModel> {
    fun create(handle: SavedStateHandle): VM
}