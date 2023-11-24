package com.example.media3

import android.app.Application


class MainApplication : Application() {
//    private val appModule = module {
//        viewModel { MainViewModel() }
//    }


    override fun onCreate() {
        super.onCreate()
//        startKoin {
//            androidContext(this@MainApplication)
//            modules(appModule)
//        }
    }
}