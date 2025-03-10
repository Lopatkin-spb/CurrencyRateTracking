package com.example.currencyratetracking.di.app.activity

import com.example.currencyratetracking.common.ActivityScope
import com.example.currencyratetracking.currencies.di.CurrenciesComponent
import com.example.currencyratetracking.presentation.MainActivity
import com.example.currencyratetracking.currencies.presentation.CurrenciesDaggerContainer
import com.example.currencyratetracking.presentation.favorites.FavoritesDaggerContainer
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)
//    fun inject(container: CurrenciesDaggerContainer)
    fun inject(container: FavoritesDaggerContainer)

    fun currenciesComponent(): CurrenciesComponent.Factory


}