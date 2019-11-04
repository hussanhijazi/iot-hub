package br.com.hussan.coffeeiot

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    fun getUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }
}
