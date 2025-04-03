package com.example.currencyratetracking.favorites.di

import androidx.lifecycle.ViewModel
import com.example.currencyratetracking.common.FragmentScope
import com.example.currencyratetracking.core.presentation.ViewModelClassKey
import com.example.currencyratetracking.favorites.data.FavoriteRepositoryImpl
import com.example.currencyratetracking.favorites.data.RateRepositoryImpl
import com.example.currencyratetracking.favorites.data.locale.FavoriteLocaleDataSource
import com.example.currencyratetracking.favorites.data.locale.FavoriteLocaleDataSourceImpl
import com.example.currencyratetracking.favorites.data.remote.RateRemoteDataSource
import com.example.currencyratetracking.favorites.data.remote.RateRemoteDataSourceImpl
import com.example.currencyratetracking.favorites.domain.DeletePairCurrenciesFromFavoriteByCharCodesUseCase
import com.example.currencyratetracking.favorites.domain.GetListFavoritePairsUseCase
import com.example.currencyratetracking.favorites.domain.SetPairCurrenciesToFavoriteUseCase
import com.example.currencyratetracking.favorites.domain.repository.FavoriteRepository
import com.example.currencyratetracking.favorites.domain.repository.RateRepository
import com.example.currencyratetracking.favorites.domain.usecase.DeletePairCurrenciesFromFavoriteByCharCodesUseCaseImpl
import com.example.currencyratetracking.favorites.domain.usecase.GetListFavoritePairsUseCaseImpl
import com.example.currencyratetracking.favorites.domain.usecase.SetPairCurrenciesToFavoriteUseCaseImpl
import com.example.currencyratetracking.favorites.presentation.FavoritesViewModel
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
interface FavoritesModule


@Module
internal interface InternalPresentationModule {

    @Binds
    @[IntoMap ViewModelClassKey(FavoritesViewModel::class)]
    fun bindFavoritesViewModel(impl: FavoritesViewModel): ViewModel

}


@Module
internal interface InternalDomainModule {

    @FragmentScope
    @Binds
    fun bindGetListFavoritePairsUseCase(impl: GetListFavoritePairsUseCaseImpl): GetListFavoritePairsUseCase

    @FragmentScope
    @Binds
    fun bindDeletePairCurrenciesFromFavoriteByCharCodesUseCase(
        impl: DeletePairCurrenciesFromFavoriteByCharCodesUseCaseImpl
    ): DeletePairCurrenciesFromFavoriteByCharCodesUseCase

    @FragmentScope
    @Binds
    fun bindSetPairCurrenciesToFavoriteUseCase(impl: SetPairCurrenciesToFavoriteUseCaseImpl): SetPairCurrenciesToFavoriteUseCase

}

@Module
internal interface InternalDataModule {

    @FragmentScope
    @Binds
    fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @FragmentScope
    @Binds
    fun bindRateRemoteDataSource(impl: RateRemoteDataSourceImpl): RateRemoteDataSource

    @FragmentScope
    @Binds
    fun bindFavoriteLocalDataSource(impl: FavoriteLocaleDataSourceImpl): FavoriteLocaleDataSource

    @FragmentScope
    @Binds
    fun bindRateRepository(impl: RateRepositoryImpl): RateRepository
}