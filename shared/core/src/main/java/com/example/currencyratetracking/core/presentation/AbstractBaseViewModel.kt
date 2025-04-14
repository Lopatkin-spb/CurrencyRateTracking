package com.example.currencyratetracking.core.presentation

import androidx.lifecycle.ViewModel


abstract class AbstractBaseViewModel<in E : UiEvent> : ViewModel() {

    abstract fun handle(new: E)
}