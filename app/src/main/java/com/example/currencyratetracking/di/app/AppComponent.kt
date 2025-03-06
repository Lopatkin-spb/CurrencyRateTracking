package com.example.currencyratetracking.di.app

import android.content.Context
import com.example.currencyratetracking.common.ApplicationScope
import com.example.currencyratetracking.di.app.activity.MainComponent
import dagger.BindsInstance
import dagger.Component


@ApplicationScope
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }

    fun getMainComponent(): MainComponent.Factory

}