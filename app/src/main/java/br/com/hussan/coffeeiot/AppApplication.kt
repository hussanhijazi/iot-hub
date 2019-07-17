package br.com.hussan.coffeeiot

import android.app.Application
import com.google.firebase.FirebaseApp


class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
