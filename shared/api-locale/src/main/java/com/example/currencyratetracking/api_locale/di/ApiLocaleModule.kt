package com.example.currencyratetracking.api_locale.di

import com.example.currencyratetracking.api_locale.api.PersonalizationApi
import com.example.currencyratetracking.api_locale.api.PreferencesPersonalizationApi
import com.example.currencyratetracking.api_locale.api.favorite.FavoriteCurrencyPairApi
import com.example.currencyratetracking.api_locale.api.rate.CurrencyPairRateApi
import com.example.currencyratetracking.api_locale.storage.application.*
import com.example.currencyratetracking.common.ApplicationScope
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module(includes = [InternalApiLocaleModule::class])
interface ApiLocaleModule


@Module
internal interface InternalApiLocaleModule {

    companion object {

        @ApplicationScope
        @Provides
        fun provideFavoriteCurrencyPairApi(apiManager: DatabaseApiManager): FavoriteCurrencyPairApi {
            return apiManager.getFavoriteCurrencyPairApi()
        }

        @ApplicationScope
        @Provides
        fun provideCurrencyPairRateApi(apiManager: DatabaseApiManager): CurrencyPairRateApi {
            return apiManager.getCurrencyPairRateApi()
        }
    }

    @ApplicationScope
    @Binds
    fun bindResourceManager(impl: AndroidResourceManager): ResourceManager

    @ApplicationScope
    @Binds
    fun bindAssetManager(impl: AndroidAssetManager): AssetManager

    @ApplicationScope
    @Binds
    fun bindPrefsManager(impl: AndroidPrefsManager): PrefsManager

    @ApplicationScope
    @Binds
    fun bindPersonalizationApi(impl: PreferencesPersonalizationApi): PersonalizationApi

    @ApplicationScope
    @Binds
    fun bindDatabaseApiManager(impl: DatabaseApiManagerImpl): DatabaseApiManager
}