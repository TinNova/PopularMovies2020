package com.tin.popularmovies.di.modules

import com.tin.popularmovies.ui.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun bindHomeActivity(): HomeActivity

}