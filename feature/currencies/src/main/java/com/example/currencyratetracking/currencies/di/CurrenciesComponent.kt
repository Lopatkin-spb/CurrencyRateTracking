package com.example.currencyratetracking.currencies.di

import com.example.currencyratetracking.common.FragmentScope
import com.example.currencyratetracking.currencies.presentation.CurrenciesViewModel
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = [CurrenciesModule::class])
interface CurrenciesComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CurrenciesComponent
    }

    fun getViewModel(): CurrenciesViewModel.Factory

}