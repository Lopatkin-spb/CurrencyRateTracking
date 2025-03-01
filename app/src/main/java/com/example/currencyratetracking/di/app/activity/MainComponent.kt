package com.example.currencyratetracking.di.app.activity

import com.example.currencyratetracking.presentation.MainActivity
import com.example.currencyratetracking.presentation.currencies.CurrenciesDaggerContainer
import com.example.currencyratetracking.presentation.favorites.FavoritesDaggerContainer
import dagger.Subcomponent


@Subcomponent
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)
    fun inject(container: CurrenciesDaggerContainer)
    fun inject(container: FavoritesDaggerContainer)

}