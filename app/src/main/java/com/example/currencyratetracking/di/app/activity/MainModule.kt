package com.example.currencyratetracking.di.app.activity


import com.example.currencyratetracking.api_locale.storage.application.DatabaseApiManager
import com.example.currencyratetracking.api_remote.api.ApiRemoteManager
import com.example.currencyratetracking.common.ActivityScope
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.presentation.ViewModelFactory
import dagger.Module
import dagger.Provides


@Module
interface MainModule {

    companion object {

        @ActivityScope
        @Provides
        fun provideViewModelFactory(
            apiRemoteManager: ApiRemoteManager,
            logger: BaseLogger,
            databaseApiManager: DatabaseApiManager,
        ): ViewModelFactory {
            return ViewModelFactory(
                logger = logger,
                api = apiRemoteManager.getRatesApi(),
                favoriteCurrencyPairApi = databaseApiManager.getFavoriteCurrencyPairApi(),
            )
        }
    }

}
