package com.example.currencyratetracking.di.app.activity

import com.example.currencyratetracking.common.ActivityScope
import com.example.currencyratetracking.currencies.di.CurrenciesComponent
import com.example.currencyratetracking.favorites.di.FavoritesComponent
import com.example.currencyratetracking.presentation.MainActivity
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)

    fun currenciesComponent(): CurrenciesComponent.Factory

    fun favoritesComponent(): FavoritesComponent.Factory
}