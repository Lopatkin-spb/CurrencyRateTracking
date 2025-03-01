package com.example.currencyratetracking.common_android.di

import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.common_android.LogcatBaseLogger
import dagger.Binds
import dagger.Module


@Module(includes = [LoggerModule::class])
interface CommonAndroidModule


@Module
internal interface LoggerModule {
    @Binds
    fun bindBaseLogger(logger: LogcatBaseLogger): BaseLogger
}