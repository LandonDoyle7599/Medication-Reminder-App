package com.example.mobiledevfinal.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mobiledevfinal.repositories.UserRepository

class RootNavigationViewModel(application: Application): AndroidViewModel(application) {
    fun signOutUser() = UserRepository.signOutUser()
}