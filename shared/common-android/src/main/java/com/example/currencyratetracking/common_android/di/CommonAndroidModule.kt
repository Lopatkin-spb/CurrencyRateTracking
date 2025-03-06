package com.example.currencyratetracking.common_android.di

import com.example.currencyratetracking.common.ApplicationScope
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.common_android.LogcatBaseLogger
import dagger.Binds
import dagger.Module


@Module(includes = [InternalCommonAndroidModule::class])
interface CommonAndroidModule


@Module
internal interface InternalCommonAndroidModule {

    @ApplicationScope
    @Binds
    fun bindBaseLogger(logger: LogcatBaseLogger): BaseLogger
}