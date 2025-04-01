package com.example.currencyratetracking.di.app.activity

import androidx.lifecycle.ViewModel
import com.example.currencyratetracking.common.ActivityScope
import com.example.currencyratetracking.core.presentation.ViewModelClassKey
import com.example.currencyratetracking.currencies.di.CurrenciesComponent
import com.example.currencyratetracking.data.RepositoryImpl
import com.example.currencyratetracking.data.locale.LocaleDataSource
import com.example.currencyratetracking.data.locale.LocaleDataSourceImpl
import com.example.currencyratetracking.domain.ClearUserSessionByLiveCycleUseCase
import com.example.currencyratetracking.domain.repository.Repository
import com.example.currencyratetracking.domain.usecase.ClearUserSessionByLiveCycleUseCaseImpl
import com.example.currencyratetracking.presentation.MainViewModel
import com.example.currencyratetracking.presentation.favorites.FavoritesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module(
    subcomponents = [CurrenciesComponent::class],
    includes = [
        InternalMainDataModule::class,
        InternalMainDomainModule::class,
    ]
)
interface MainModule {

    @Binds
    @[IntoMap ViewModelClassKey(MainViewModel::class)]
    fun bindMainViewModel(impl: MainViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelClassKey(FavoritesViewModel::class)]
    fun bindFavoritesViewModel(impl: FavoritesViewModel): ViewModel

}


@Module
internal interface InternalMainDomainModule {

    @ActivityScope
    @Binds
    fun bindClearUserSessionByLiveCycleUseCase(
        impl: ClearUserSessionByLiveCycleUseCaseImpl
    ): ClearUserSessionByLiveCycleUseCase
}


@Module
internal interface InternalMainDataModule {

    @ActivityScope
    @Binds
    fun bindRepository(impl: RepositoryImpl): Repository

    @ActivityScope
    @Binds
    fun bindLocaleDataSource(impl: LocaleDataSourceImpl): LocaleDataSource
}
