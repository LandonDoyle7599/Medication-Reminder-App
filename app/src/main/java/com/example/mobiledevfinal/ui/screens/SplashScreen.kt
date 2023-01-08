package com.example.mobiledevfinal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.mobiledevfinal.navigation.Routes
import com.example.mobiledevfinal.viewmodels.SplashScreenViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun SplashScreen(navHostController: NavHostController) {
    val viewModel: SplashScreenViewModel = viewModel()
    LaunchedEffect(true) {
        val loginStatusCheck = async {
            // TODO: check to see if user is logged in

        }
        // wait for 3 seconds or until the login check is
        // done before navigating
        val cal = Calendar.getInstance()
        println(cal.get(Calendar.HOUR_OF_DAY))
        delay(1000)
        // TODO: if logged in the skip the launch
        //       flow and go straight to the application
        navHostController.navigate(
            if(viewModel.isUserLoggedIn()){
                Routes.medNavigation.route
            } else{
                Routes.launchNavigation.route
            }) {
            // makes it so that we can't get back to the
            // splash screen by pushing the back button
            popUpTo(navHostController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Mindful Meds",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center
        )
    }
}