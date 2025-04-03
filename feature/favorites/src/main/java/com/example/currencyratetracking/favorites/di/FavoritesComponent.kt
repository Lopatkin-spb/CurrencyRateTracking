package com.example.currencyratetracking.favorites.di

import com.example.currencyratetracking.common.FragmentScope
import com.example.currencyratetracking.favorites.presentation.FavoritesDaggerContainer
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = [FavoritesModule::class])
interface FavoritesComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): FavoritesComponent
    }

    fun inject(container: FavoritesDaggerContainer)

}