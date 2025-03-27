package com.example.currencyratetracking.di.app.activity

import com.example.currencyratetracking.api_locale.storage.application.DatabaseApiManager
import com.example.currencyratetracking.api_remote.api.ApiRemoteManager
import com.example.currencyratetracking.common.ActivityScope
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.BaseCoroutineDispatcher
import com.example.currencyratetracking.currencies.di.CurrenciesComponent
import com.example.currencyratetracking.data.RepositoryImpl
import com.example.currencyratetracking.data.locale.LocaleDataSource
import com.example.currencyratetracking.data.locale.LocaleDataSourceImpl
import com.example.currencyratetracking.domain.ClearUserSessionByLiveCycleUseCase
import com.example.currencyratetracking.domain.usecase.ClearUserSessionByLiveCycleUseCaseImpl
import com.example.currencyratetracking.domain.repository.Repository
import com.example.currencyratetracking.presentation.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module(
    subcomponents = [CurrenciesComponent::class],
    includes = [
        InternalMainDataModule::class,
        InternalMainDomainModule::class,
    ]
)
interface MainModule {

    companion object {

        @ActivityScope
        @Provides
        fun provideViewModelFactory(
            apiRemoteManager: ApiRemoteManager,
            logger: BaseLogger,
            databaseApiManager: DatabaseApiManager,
            dispatcher: BaseCoroutineDispatcher,
            clearUserSessionByLiveCycleUseCase: ClearUserSessionByLiveCycleUseCase,
        ): ViewModelFactory {
            return ViewModelFactory(
                logger = logger,
                api = apiRemoteManager.getRatesApi(),
                favoriteCurrencyPairApi = databaseApiManager.getFavoriteCurrencyPairApi(),
                dispatcher = dispatcher,
                clearUserSessionByLiveCycleUseCase = clearUserSessionByLiveCycleUseCase,
            )
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
