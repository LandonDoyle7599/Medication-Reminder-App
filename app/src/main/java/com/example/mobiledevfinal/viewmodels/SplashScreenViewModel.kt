package com.example.mobiledevfinal.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mobiledevfinal.repositories.UserRepository

class SplashScreenViewModel(application: Application): AndroidViewModel(application) {
    fun isUserLoggedIn() = UserRepository.isUserLoggedIn()
}