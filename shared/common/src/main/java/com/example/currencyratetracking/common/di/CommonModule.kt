package com.example.currencyratetracking.common.di

import com.example.currencyratetracking.common.ApplicationScope
import com.example.currencyratetracking.common.SerializationManager
import com.example.currencyratetracking.common.SerializationManagerImpl
import dagger.Binds
import dagger.Module


@Module(includes = [InternalCommonModule::class])
public interface CommonModule


@Module
internal interface InternalCommonModule {

    @ApplicationScope
    @Binds
    fun bindSerializationManager(impl: SerializationManagerImpl): SerializationManager
}