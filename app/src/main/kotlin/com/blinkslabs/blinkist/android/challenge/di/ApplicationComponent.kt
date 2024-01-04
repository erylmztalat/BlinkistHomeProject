package com.blinkslabs.blinkist.android.challenge.di

import android.content.Context
import com.blinkslabs.blinkist.android.challenge.data.api.BooksApiModule
import com.blinkslabs.blinkist.android.challenge.data.api.BooksServiceModule
import com.blinkslabs.blinkist.android.challenge.ui.activity.BooksActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [BooksApiModule::class, BooksServiceModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    fun inject(activity: BooksActivity)
}
