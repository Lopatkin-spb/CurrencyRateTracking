package com.example.currencyratetracking.core.di

import com.example.currencyratetracking.common.ApplicationScope
import com.example.currencyratetracking.core.BaseCoroutineDispatcher
import com.example.currencyratetracking.core.BaseCoroutineDispatcherImpl
import dagger.Binds
import dagger.Module


@Module(includes = [InternalCoreModule::class])
interface CoreModule


@Module
internal interface InternalCoreModule {

    @ApplicationScope
    @Binds
    fun bindBaseCoroutineDispatcher(impl: BaseCoroutineDispatcherImpl): BaseCoroutineDispatcher

}