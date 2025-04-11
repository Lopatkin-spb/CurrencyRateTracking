package com.example.currencyratetracking.presentation


sealed interface MainUserEvent {

    data object OnColdClose : MainUserEvent
}
