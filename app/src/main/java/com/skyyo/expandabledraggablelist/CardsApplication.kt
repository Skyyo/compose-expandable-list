package com.skyyo.expandabledraggablelist

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class CardsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            Venom.createInstance(this).apply {
//                initialize()
//                start()
//            }
    }
}