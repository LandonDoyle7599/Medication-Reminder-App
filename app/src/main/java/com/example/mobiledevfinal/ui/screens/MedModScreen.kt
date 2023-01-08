package com.example.mobiledevfinal.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobiledevfinal.ui.components.FormField
import com.example.mobiledevfinal.viewmodels.MedModViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.launch

@Composable
fun MedModScreen(navHostController: NavHostController, id: String? ) {
    val viewModel: MedModViewModel = viewModel()
    val state = viewModel.uiState
    val scope = rememberCoroutineScope()

    LaunchedEffect(true){
        viewModel.setupInitialState(id)
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            navHostController.popBackStack()
        }
    }
    LaunchedEffect(state.deleteSuccess) {
        println(id)
        if (state.deleteSuccess) {
            navHostController.popBackStack()
        }
    }
    Column(modifier = Modifier
        .background(Color.White)
        .padding(16.dp)) {
        FormField(
            value = state.medName,
            onValueChange = { state.medName = it },
            placeholder = { Text(text = "Medication Name", style = MaterialTheme.typography.body1) }
        )
        Spacer(modifier = Modifier.height(4.dp))
        FormField(
            value = state.notes,
            onValueChange = { state.notes = it },
            placeholder = { Text(text = "Notes", style = MaterialTheme.typography.body1) },
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ){
            OutlinedTextField(
                modifier = Modifier.size(175.dp, 80.dp),
                value = state.dosage,
                onValueChange = {
                    viewModel.updateDosage(it)
                },
                isError = state.dosageError,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
                label = { Text(text = "Dosage", style = MaterialTheme.typography.body1) },
                trailingIcon = { Text(text = "mg")},
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(20.dp))
            OutlinedTextField(
                modifier = Modifier.size(175.dp, 80.dp),
                value = state.quantity,
                onValueChange = {
                    viewModel.updateQuantity(it)
                },
                isError = state.quantityError,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
                label = { Text(text = "Quantity", style = MaterialTheme.typography.body1) },
                trailingIcon = { Text(text = "Pills")},
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()){
            OutlinedTextField(
                modifier = Modifier.size(200.dp, 80.dp),
                value = state.time,
                onValueChange = {
                    viewModel.updateTime(it)
                },
                isError = state.timeError,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
                label = { Text(text = "Time", style = MaterialTheme.typography.body1) },
                trailingIcon = { Text(text = "Hour (24h)  ")},
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        Text(
            text = state.errorMessage,
            style = TextStyle(color = MaterialTheme.colors.error),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

            Button(colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF6FF8C)
            ), onClick = { navHostController.popBackStack() }) {
                Text(text = "Cancel", style = MaterialTheme.typography.button)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF6FF8C)
            ),onClick = {
                scope.launch {
                    viewModel.saveMed()
                }
            }) {
                Text(text = "Save", style = MaterialTheme.typography.button)
            }
            Spacer(modifier = Modifier.width(8.dp))
            if(state.showDelete) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFF6FF8C),
                        contentColor = Color.Black
                    ), onClick = {
                        scope.launch {
                            viewModel.deleteMed()
                        }
                    }) {
                    Text(text = "Delete", style = MaterialTheme.typography.button)
                }
            }
        }
        Row(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                AndroidView(modifier = Modifier.fillMaxWidth(),
                    factory = { context ->
                        AdView(context).apply {
                            adUnitId = "ca-app-pub-3940256099942544/6300978111"
                            setAdSize(AdSize.BANNER)
                            loadAd(AdRequest.Builder().build())
                        }
                    }
                )
            }

            }
        }


    }