package com.example.currencyratetracking.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.currencyratetracking.R


internal sealed class AppDestination(val uniqueTag: String, @StringRes val titleId: Int, @DrawableRes val iconId: Int) {

    data object Currencies : AppDestination(
        uniqueTag = "Currencies",
        titleId = R.string.action_currencies,
        iconId = R.drawable.ic_currencies_24,
    )

    data object Favorites : AppDestination(
        uniqueTag = "Favorites",
        titleId = R.string.action_favorites,
        iconId = R.drawable.ic_favorites_on_24,
    )

    companion object {
        val toList = listOf(Currencies, Favorites)
    }
}