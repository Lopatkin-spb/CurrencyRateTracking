package com.example.currencyratetracking.currencies.di

import androidx.lifecycle.ViewModel
import com.example.currencyratetracking.common.FragmentScope
import com.example.currencyratetracking.core.presentation.ViewModelClassKey
import com.example.currencyratetracking.currencies.data.FavoriteRepositoryImpl
import com.example.currencyratetracking.currencies.data.PersonalizationRepositoryImpl
import com.example.currencyratetracking.currencies.data.RateRepositoryImpl
import com.example.currencyratetracking.currencies.data.locale.FavoriteLocaleDataSourceImpl
import com.example.currencyratetracking.currencies.data.locale.PersonalizationLocaleDataSourceImpl
import com.example.currencyratetracking.currencies.data.locale.dataSource.FavoriteLocaleDataSource
import com.example.currencyratetracking.currencies.data.locale.dataSource.PersonalizationLocaleDataSource
import com.example.currencyratetracking.currencies.data.locale.dataSource.RateLocaleDataSource
import com.example.currencyratetracking.currencies.data.locale.rate.RateLocaleDataSourceImpl
import com.example.currencyratetracking.currencies.data.remote.RateRemoteDataSourceImpl
import com.example.currencyratetracking.currencies.data.remote.dataSource.RateRemoteDataSource
import com.example.currencyratetracking.currencies.domain.*
import com.example.currencyratetracking.currencies.domain.repository.FavoriteRepository
import com.example.currencyratetracking.currencies.domain.repository.PersonalizationRepository
import com.example.currencyratetracking.currencies.domain.repository.RateRepository
import com.example.currencyratetracking.currencies.domain.usecase.*
import com.example.currencyratetracking.currencies.presentation.CurrenciesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module(
    includes = [
        InternalPresentationModule::class,
        InternalDataModule::class,
        InternalDomainModule::class,
    ]
)
interface CurrenciesModule


@Module
internal interface InternalPresentationModule {

    @Binds
    @[IntoMap ViewModelClassKey(CurrenciesViewModel::class)]
    fun bindAbstractViewModel(viewModel: CurrenciesViewModel): ViewModel

}


@Module
internal interface InternalDataModule {

    @FragmentScope
    @Binds
    fun bindRateRepository(impl: RateRepositoryImpl): RateRepository

    @FragmentScope
    @Binds
    fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @FragmentScope
    @Binds
    fun bindLocaleDataSource(impl: FavoriteLocaleDataSourceImpl): FavoriteLocaleDataSource

    @FragmentScope
    @Binds
    fun bindRemoteDataSource(impl: RateRemoteDataSourceImpl): RateRemoteDataSource

    @FragmentScope
    @Binds
    fun bindPersonalizationRepository(impl: PersonalizationRepositoryImpl): PersonalizationRepository

    @FragmentScope
    @Binds
    fun bindPersonalizationLocaleDataSource(impl: PersonalizationLocaleDataSourceImpl): PersonalizationLocaleDataSource

    @FragmentScope
    @Binds
    fun bindRateLocaleDataSource(impl: RateLocaleDataSourceImpl): RateLocaleDataSource

}


@Module
internal interface InternalDomainModule {

    @FragmentScope
    @Binds
    fun bindGetListBaseCurrenciesUseCase(impl: GetListBaseCurrenciesUseCaseImpl): GetListBaseCurrenciesUseCase

    @FragmentScope
    @Binds
    fun bindSavePairCurrenciesToFavoriteUseCase(impl: SetPairCurrenciesToFavoriteUseCaseImpl): SetPairCurrenciesToFavoriteUseCase

    @FragmentScope
    @Binds
    fun bindDeletePairCurrenciesFromFavoriteByCharCodesUseCase(
        impl: DeletePairCurrenciesFromFavoriteByCharCodesUseCaseImpl
    ): DeletePairCurrenciesFromFavoriteByCharCodesUseCase

    @FragmentScope
    @Binds
    fun bindGetListActualCurrencyRatesByBaseCharCodeUseCase(
        impl: GetListActualCurrencyRatesByBaseCharCodeUseCaseImpl
    ): GetListActualCurrencyRatesByBaseCharCodeUseCase

    @FragmentScope
    @Binds
    fun bindGetLastOpenBaseCurrencyUseCase(impl: GetUserSelectedBaseCurrencyUseCaseImpl): GetUserSelectedBaseCurrencyUseCase

    @FragmentScope
    @Binds
    fun bindSaveLastOpenedBaseCurrencyUseCase(impl: SetUserSelectedBaseCurrencyUseCaseImpl): SetUserSelectedBaseCurrencyUseCase

    @FragmentScope
    @Binds
    fun bindGetListActualCurrencyRatesWithSortByBaseCharCodeUseCase(
        impl: GetListActualCurrencyRatesWithSortByBaseCharCodeUseCaseImpl
    ): GetListActualCurrencyRatesWithSortByBaseCharCodeUseCase
}