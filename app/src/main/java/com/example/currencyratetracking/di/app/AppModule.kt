package com.example.currencyratetracking.di.app

import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.common_android.di.CommonAndroidModule
import com.example.currencyratetracking.di.app.activity.MainComponent
import com.example.currencyratetracking.presentation.ViewModelFactory
import dagger.Module
import dagger.Provides


@Module(
    subcomponents = [MainComponent::class],
    includes = [CommonAndroidModule::class]
)
interface AppModule {

    companion object {

        @Provides
        fun provideViewModelFactory(
            logger: BaseLogger,
        ): ViewModelFactory {
            return ViewModelFactory(
                logger = logger,
            )
        }
    }

}
