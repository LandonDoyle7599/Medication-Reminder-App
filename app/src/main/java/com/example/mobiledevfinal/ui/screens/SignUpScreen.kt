package com.example.mobiledevfinal.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobiledevfinal.navigation.Routes
import com.example.mobiledevfinal.ui.components.FormField
import com.example.mobiledevfinal.viewmodels.SignUpViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navHostController: NavHostController) {
    val viewModel: SignUpViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val state = viewModel.uiState

    LaunchedEffect(state.signUpSuccess){
        if (state.signUpSuccess){
            navHostController.navigate(Routes.medNavigation.route) {
                popUpTo(0)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround) {
        Surface(elevation = 2.dp) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Text(text = "Create Account", style = MaterialTheme.typography.body1)
                FormField(
                    value = state.email,
                    onValueChange = { state.email = it },
                    placeholder = { Text("Email", style = MaterialTheme.typography.body2) },
                    error = state.emailError
                )
                FormField(
                    value = state.emailConfirmation,
                    onValueChange = { state.emailConfirmation = it},
                    placeholder = { Text("Email Confirmation", style = MaterialTheme.typography.body2) },
                    error = state.emailConfirmationError
                )
                FormField(
                    value = state.password,
                    onValueChange = { state.password = it },
                    placeholder = { Text("Password", style = MaterialTheme.typography.body2) },
                    error = state.passwordError,
                    password = true
                )
                FormField(
                    value = state.passwordConfirmation,
                    onValueChange = { state.passwordConfirmation = it },
                    placeholder = { Text("Password Confirmation", style = MaterialTheme.typography.body2) },
                    error = state.passwordConfirmationError,
                    password = true
                )
                Row (
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Button(colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFF6FF8C)
                    ), onClick = { navHostController.popBackStack() }) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFF6FF8C)
                    ), onClick = { scope.launch { viewModel.signUp() } }) {
                        Text(text = "Create Account")
                    }
                }
                Text(
                    text = state.errorMessage,
                    style = TextStyle(color = MaterialTheme.colors.error),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}