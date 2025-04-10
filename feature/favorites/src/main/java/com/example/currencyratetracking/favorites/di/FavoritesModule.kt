package com.example.currencyratetracking.favorites.di

import com.example.currencyratetracking.common.FragmentScope
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
import dagger.Binds
import dagger.Module


@Module(
    includes = [
        InternalDataModule::class,
        InternalDomainModule::class,
    ]
)
interface FavoritesModule
//todo: set internal

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