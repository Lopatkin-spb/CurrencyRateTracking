package com.example.currencyratetracking.presentation.favorites


internal sealed interface FavoritesUserEvent {
    data object OnScreenOpen : FavoritesUserEvent
    data object OnScreenClose : FavoritesUserEvent
}