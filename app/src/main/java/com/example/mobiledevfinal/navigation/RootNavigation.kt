package com.example.mobiledevfinal.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.mobiledevfinal.viewmodels.RootNavigationViewModel
import kotlinx.coroutines.launch
import com.example.mobiledevfinal.ui.screens.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RootNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewModel: RootNavigationViewModel = viewModel()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (currentDestination?.hierarchy?.none { it.route == Routes.launchNavigation.route || it.route == Routes.splashScreen.route } == true) {
                TopAppBar(backgroundColor = Color.White, elevation = 0.dp) {
                    if (currentDestination?.route == Routes.meds.route) {
                        IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu button")
                        }
                    } else {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                    Text(text = "Medications")
                }
            }
        },
        drawerContent = {
            if (currentDestination?.hierarchy?.none { it.route == Routes.launchNavigation.route || it.route == Routes.splashScreen.route } == true) {
                DropdownMenuItem(onClick = {
                    // TODO Log the user out
                    scope.launch {
                        viewModel.signOutUser()
                        scaffoldState.drawerState.snapTo(DrawerValue.Closed)
                    }
                    navController.navigate(Routes.launchNavigation.route) {
                        popUpTo(0) // clear back stack and basically start the app from scratch
                    }
                }) {
                    Icon(Icons.Default.ExitToApp, "Logout")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Logout", style = MaterialTheme.typography.body1)
                }
            }
        },
        floatingActionButton = {
            if (currentDestination?.route == Routes.meds.route) {
                FloatingActionButton(backgroundColor = Color(0xFFEBEBEB), onClick = {navController.navigate(Routes.editMeds.route)}) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Medication")
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.splashScreen.route,
            modifier = Modifier.padding(paddingValues = it)
        ) {
            navigation(route = Routes.launchNavigation.route, startDestination = Routes.launch.route) {
                composable(route = Routes.launch.route) { LaunchScreen(navController) }
                composable(route = Routes.signIn.route) { SignInScreen(navController) }
                composable(route = Routes.signUp.route) { SignUpScreen(navController) }
            }
            navigation(route = Routes.medNavigation.route, startDestination = Routes.meds.route) {
                composable(
                    route = "editmeds?id={id}",
                    arguments = listOf(navArgument("id") {defaultValue = "new"})
                ) { navBackStackEntry ->
                    MedModScreen(navController, navBackStackEntry.arguments?.get("id").toString())
                }
                composable(route = Routes.meds.route) { MedScreen(navController) }
            }
            composable(route = Routes.splashScreen.route) { SplashScreen(navController) }
        }
    }
}