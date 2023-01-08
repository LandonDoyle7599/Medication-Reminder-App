package com.example.mobiledevfinal.viewmodels


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.mobiledevfinal.repositories.MedicationRepository
import java.util.Calendar
import kotlin.Exception

class MedModScreenState {
    var dosage by mutableStateOf("1")
    var medName by mutableStateOf("")
    var quantity by mutableStateOf("1")
    var streak by mutableStateOf("1")
    var notes by mutableStateOf("")
    var dosageError by mutableStateOf(false)
    var medNameError by mutableStateOf(false)
    var time by mutableStateOf("0")
    var quantityError by mutableStateOf(false)
    var timeError by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var saveSuccess by mutableStateOf(false)
    var deleteSuccess by mutableStateOf(false)
    var showDelete by mutableStateOf(false)
}

class MedModViewModel(application: Application): AndroidViewModel(application) {
    val uiState = MedModScreenState()
    var id: String? = null

    suspend fun setupInitialState(id: String?) {
        uiState.showDelete = false
        if (id == null || id == "new") return
        this.id = id
        val sheet = MedicationRepository.getMeds().find { it.id == id }
            ?: return //note handle med that doesn't exist
        uiState.medName = sheet.medName ?: ""
        uiState.notes = sheet.notes ?: ""
        uiState.quantity = "${sheet.quantity ?: "1"}"
        uiState.dosage = "${sheet.dosage ?: "1"}"
        uiState.streak = "${sheet.streak ?: "1"}"
        uiState.time = "${sheet.time ?: "0"}"
        uiState.showDelete = true
    }

    fun updateTime(input: String){
        uiState.timeError = false
        uiState.errorMessage = ""
        try {
            val i = input.filter { !it.isWhitespace() }.toInt()
            if(i <0 || i > 23){
                throw Exception()
            }
        } catch (e: Exception) {
            uiState.timeError = true
            uiState.errorMessage = "Time must be a whole number between 0-23."
        } finally {
            uiState.time = input.filter { !it.isWhitespace() }
        }
    }

    fun updateDosage(input: String) {
        uiState.dosageError = false
        uiState.errorMessage = ""
        try {
            val i = input.filter { !it.isWhitespace() }.toInt()
            if(i < 1){
                throw Exception()
            }
        } catch (e: Exception) {
            uiState.dosageError = true
            uiState.errorMessage = "Dosage must be greater than 0."
        } finally {
            uiState.dosage = input.filter { !it.isWhitespace() }
        }
    }

    fun updateQuantity(input: String) {
        uiState.quantityError = false
        uiState.errorMessage = ""
        try {
            val i = input.filter { !it.isWhitespace() }.toInt()
            if(i<1){
                throw Exception()
            }
        } catch (e: Exception) {
            uiState.quantityError = true
            uiState.errorMessage = "Quantity must be greater than 0."
        } finally {
            uiState.quantity = input.filter { !it.isWhitespace() }
        }
    }


    suspend fun saveMed() {
        // we handle the updates of completion time in real time
        if (uiState.dosageError) return
        if (uiState.quantityError) return

        uiState.errorMessage = ""
        uiState.medNameError = false


        if (uiState.medName.isEmpty()) {
            uiState.medNameError = true
            uiState.errorMessage = "Medication name cannot be blank."
            return
        }

        if (id == null) {//save new
            // TODO: handle new char sheet creation and modification
            MedicationRepository.createMed(
                medName = uiState.medName,
                notes = uiState.notes,
                quantity = uiState.quantity.toInt(),
                dosage = uiState.dosage.toInt(),
                streak = uiState.streak.toInt(),
                time = uiState.time.toInt(),
                dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
                dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                month = Calendar.getInstance().get(Calendar.MONTH),
                year = Calendar.getInstance().get(Calendar.YEAR),
                missed = false
            )
        } else {//update
            val sheet = MedicationRepository.getMeds().find { it.id == id } ?: return
            MedicationRepository.updateMed(
                sheet.copy(
                    medName = uiState.medName,
                    notes = uiState.notes,
                    dosage = uiState.dosage.toInt(),
                    quantity = uiState.quantity.toInt(),
                    streak = uiState.streak.toInt(),
                    time = uiState.time.toInt()
                )
            )
        }
        uiState.saveSuccess = true
    }

    suspend fun deleteMed() {
        MedicationRepository.deleteMed(id ?: return)
        uiState.deleteSuccess = true
    }
}