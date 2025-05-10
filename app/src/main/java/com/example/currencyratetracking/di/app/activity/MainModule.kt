package com.example.currencyratetracking.di.app.activity

import com.example.currencyratetracking.common.ActivityScope
import com.example.currencyratetracking.common_android.AppTag
import com.example.currencyratetracking.common_android.Tag
import com.example.currencyratetracking.currencies.di.CurrenciesComponent
import com.example.currencyratetracking.data.RepositoryImpl
import com.example.currencyratetracking.data.locale.LocaleDataSource
import com.example.currencyratetracking.data.locale.LocaleDataSourceImpl
import com.example.currencyratetracking.domain.ClearUserSessionByLiveCycleUseCase
import com.example.currencyratetracking.domain.repository.Repository
import com.example.currencyratetracking.domain.usecase.ClearUserSessionByLiveCycleUseCaseImpl
import com.example.currencyratetracking.favorites.di.FavoritesComponent
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module(
    subcomponents = [
        CurrenciesComponent::class,
        FavoritesComponent::class,
    ],
    includes = [
        InternalMainDataModule::class,
        InternalMainDomainModule::class,
    ]
)
internal interface MainModule {

    companion object {

        @ActivityScope
        @[Provides AppTag]
        fun provideTag(): Tag {
            return Tag(appAcronym = "crt", moduleName = "app")
        }
    }
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
