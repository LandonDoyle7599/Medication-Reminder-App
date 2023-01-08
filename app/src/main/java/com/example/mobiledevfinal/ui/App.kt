package com.example.mobiledevfinal.ui

import androidx.compose.runtime.Composable
import com.example.mobiledevfinal.navigation.RootNavigation
import com.example.mobiledevfinal.ui.theme.MobileDevFinalTheme

@Composable
fun App() {
    MobileDevFinalTheme {
        RootNavigation()
    }
}