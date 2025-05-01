package com.example.currencyratetracking.presentation

import com.example.currencyratetracking.core.presentation.UiEvent


sealed interface MainUserEvent : UiEvent {

    data object OnColdClose : MainUserEvent
}
