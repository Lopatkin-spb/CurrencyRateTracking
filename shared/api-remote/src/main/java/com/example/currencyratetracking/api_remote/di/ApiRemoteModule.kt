package com.example.currencyratetracking.api_remote.di

import com.example.currencyratetracking.api_remote.api.*
import com.example.currencyratetracking.common.ApplicationScope
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module(includes = [InternalApiRemoteModule::class])
public interface ApiRemoteModule


@Module
internal interface InternalApiRemoteModule {

    @ApplicationScope
    @Binds
    fun bindApiRemoteManager(impl: ApiRemoteManagerImpl): ApiRemoteManager

    @ApplicationScope
    @Binds
    fun bindConverterFactoryManager(impl: ConverterFactoryManagerImpl): ConverterFactoryManager

    companion object {

        /**
         * Network.
         */
        @ApplicationScope
        @Provides
        fun provideNetworkManager(
            client: ClientManager,
            converterFactory: ConverterFactoryManager,
            @ApiUrl apiUrl: String,
        ): NetworkManager {
            return NetworkManagerImpl(
                converterFactory = converterFactory,
                client = client,
                urlApi = apiUrl,
            )
        }

        @ApplicationScope
        @Provides
        fun provideClientManager(
            @BuildType buildType: String,
        ): ClientManager {
            return ClientManagerImpl(
                buildType = buildType,
            )
        }

    }

}