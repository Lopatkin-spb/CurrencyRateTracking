package com.example.currencyratetracking.di.app


import com.example.currencyratetracking.BuildConfig
import com.example.currencyratetracking.api_remote.di.ApiRemoteModule
import com.example.currencyratetracking.api_remote.di.ApiUrl
import com.example.currencyratetracking.api_remote.di.BuildType
import com.example.currencyratetracking.common.ApplicationScope
import com.example.currencyratetracking.common.di.CommonModule
import com.example.currencyratetracking.common_android.di.CommonAndroidModule
import com.example.currencyratetracking.di.app.activity.MainComponent
import dagger.Module
import dagger.Provides


@Module(
    subcomponents = [MainComponent::class],
    includes = [
        CommonAndroidModule::class,
        ApiRemoteModule::class,
        CommonModule::class,
    ]
)
interface AppModule {

    companion object {

        @ApplicationScope
        @Provides
        @ApiUrl
        fun provideApiUrl(): String {
            return BuildConfig.API_URL
        }

        @ApplicationScope
        @Provides
        @BuildType
        fun provideBuildType(): String {
            return BuildConfig.BUILD_TYPE
        }

    }

}
