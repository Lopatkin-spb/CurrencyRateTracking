package com.example.currencyratetracking.core.di

import androidx.lifecycle.ViewModelProvider
import com.example.currencyratetracking.common.ApplicationScope
import com.example.currencyratetracking.core.BaseCoroutineDispatcher
import com.example.currencyratetracking.core.BaseCoroutineDispatcherImpl
import com.example.currencyratetracking.core.presentation.MultiViewModelFactory
import dagger.Binds
import dagger.Module


@Module(includes = [InternalCoreModule::class])
interface CoreModule


@Module
internal interface InternalCoreModule {

    @ApplicationScope
    @Binds
    fun bindBaseCoroutineDispatcher(impl: BaseCoroutineDispatcherImpl): BaseCoroutineDispatcher

    @ApplicationScope
    @Binds
    fun bindViewModelProviderFactory(impl: MultiViewModelFactory): ViewModelProvider.Factory

}