package com.example.mobiledevfinal.navigation

data class Screen(val route: String)

object Routes {
    val launchNavigation = Screen("launchnavigation")
    val medNavigation = Screen("medNavigation")
    val launch = Screen("launch")
    val signIn = Screen("signin")
    val signUp = Screen("signup")
    val meds = Screen("meds")
    val editMeds = Screen("editmeds")
    val splashScreen = Screen("splashscreen")
}