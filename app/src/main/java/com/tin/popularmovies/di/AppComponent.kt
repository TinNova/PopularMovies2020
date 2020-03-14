package com.tin.popularmovies.di

import android.app.Application
import com.tin.popularmovies.App
import com.tin.popularmovies.di.modules.ActivityModule
import com.tin.popularmovies.di.modules.ApiModule
import com.tin.popularmovies.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ApiModule::class,
        ActivityModule::class]
)

interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}