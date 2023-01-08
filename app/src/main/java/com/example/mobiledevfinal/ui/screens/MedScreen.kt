package com.example.mobiledevfinal.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobiledevfinal.ui.components.Loader
import com.example.mobiledevfinal.ui.components.MedListItem
import com.example.mobiledevfinal.ui.theme.custom
import com.example.mobiledevfinal.viewmodels.MedViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar


@Composable
fun MedScreen(navHostController: NavHostController) {
    // TODO: Implement once we have logged in a user
    val viewModel: MedViewModel = viewModel()
    val state = viewModel.uiState
    val scope = rememberCoroutineScope()


    LaunchedEffect(true) {
        val loadingMeds = async { viewModel.getMed() }
        delay(3000)
        loadingMeds.await()
        val updatingMissed = async { viewModel.updateMissedMeds(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) }
        updatingMissed.await()
        viewModel.checkIfHaveMeds()
        viewModel.checkForMissedMeds()
        if(!state.isLaunched){
            val gettingQuote = async{viewModel.loadDailyQuote()}
            gettingQuote.await()
            state.isLaunched = true
        }
        state.loading = false
    }






    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF6FF8C))
            .padding(16.dp), ) {
        if (state.loading) {
            Spacer(Modifier.height(16.dp))
            Loader()
        } else {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.quoteLoaded) {
                } else if (state.quote != null) {
                    Text(
                        text = state.quote?.content ?: "",
                        style = MaterialTheme.typography.body1,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = state.quote?.author ?: "",
                        style = MaterialTheme.typography.body2,
                    )
                }
            }
        }
        if (state.missedMeds) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFEBEBEB)
                ), onClick = {
                    scope.launch {
                        viewModel.takeMissedMeds()
                        delay(300)
                        viewModel.checkForMissedMeds()
                    }
                }) {
                    Text(text = "Take All Missed Meds", style = MaterialTheme.typography.button)
                }
            }
        }

            ///Add missed and take now meds
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Button(colors = ButtonDefaults.buttonColors(
//                backgroundColor = Color(0xFFEBEBEB)
//            ),onClick = {
//                scope.launch {
//                    viewModel.addOldMeds()
//                }
//            }) {
//                Text(text = "Add Missed Meds", style = MaterialTheme.typography.button)
//            }
//        }
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Button(colors = ButtonDefaults.buttonColors(
//                    backgroundColor = Color(0xFFEBEBEB)
//                ),onClick = {
//                scope.launch {
//                    viewModel.addTakeNowMeds()
//                }
//            }) {
//                Text(text = "Add Take Now Meds", style = MaterialTheme.typography.button)
//            }
//        }
            ///End of add missed and take now meds


        if (state.haveMeds) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFEBEBEB)
                ), onClick = {
                    scope.launch {
                        viewModel.takeMeds(
                            Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                        )
                        delay(300)
                        viewModel.checkIfHaveMeds()
                    }
                }) {
                    Text(text = "Take All ${state.time}:00 meds", style = MaterialTheme.typography.button)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            items(state.meds, key = { it.id!! }) { med ->
                MedListItem(
                    med = med,
                    onEditPressed = { navHostController.navigate("editmeds?id=${med.id}") })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
}