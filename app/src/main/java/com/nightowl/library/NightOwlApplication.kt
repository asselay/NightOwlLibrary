package com.nightowl.library

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.nightowl.library.di.AppContainer

class NightOwlApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

fun CreationExtras.nightOwlApplication(): NightOwlApplication =
    checkNotNull(this[APPLICATION_KEY]) as NightOwlApplication