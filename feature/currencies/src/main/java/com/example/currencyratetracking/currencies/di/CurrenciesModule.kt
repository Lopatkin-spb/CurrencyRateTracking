package com.example.currencyratetracking.currencies.di

import com.example.currencyratetracking.api_locale.storage.application.DatabaseApiManager
import com.example.currencyratetracking.api_remote.api.ApiRemoteManager
import com.example.currencyratetracking.common.FragmentScope
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.currencies.presentation.ViewModelFactory
import dagger.Module
import dagger.Provides


@Module(includes = [InternalCurrenciesModule::class])
interface CurrenciesModule


@Module
internal interface InternalCurrenciesModule {

    companion object {

        @FragmentScope
        @Provides
        fun provideViewModelFactory(
            apiRemoteManager: ApiRemoteManager,
            logger: BaseLogger,
            databaseApiManager: DatabaseApiManager,
        ): ViewModelFactory {
            return ViewModelFactory(
                logger = logger,
                apiRemoteManager = apiRemoteManager,
                databaseApiManager = databaseApiManager,
            )
        }
    }

}