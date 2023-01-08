package com.example.mobiledevfinal.viewmodels


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.mobiledevfinal.models.Medication
import com.example.mobiledevfinal.models.Quote
import com.example.mobiledevfinal.repositories.MedicationRepository
import com.example.mobiledevfinal.repositories.QuotesRepository
import com.example.mobiledevfinal.repositories.UserRepository
import java.util.Calendar


class MedScreenState{
    val _meds = mutableStateListOf<Medication>()
    val meds: List<Medication> get() = _meds
    var haveMeds by mutableStateOf(false)
    var loading by mutableStateOf(true)
    var missedMeds by mutableStateOf(false)
    var quote by mutableStateOf<Quote?>(null)
    var time by mutableStateOf(0)
    var isLaunched by mutableStateOf(false)
    var quoteLoaded by mutableStateOf(false)
}

class MedViewModel(application: Application): AndroidViewModel(application) {
    val uiState = MedScreenState()
    suspend fun getMed() {
        val meds = MedicationRepository.getMeds()
        uiState._meds.clear()
        meds.sortedBy { it.time }.forEach {
            if(it.time != Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
                uiState._meds.add(it)
            }else{
                uiState._meds.add(0, it)
            }
        }
    }

    suspend fun checkForMissedMeds(){
        val meds = MedicationRepository.getMeds().filter { it.missed == true }
        uiState.missedMeds = meds.isNotEmpty()
    }

    suspend fun addOldMeds(){
        MedicationRepository.createMed(
            medName = "Missed Meds",
            notes = "Take 2 pills every 4 hours",
            quantity = 10,
            dosage = 2,
            streak = 6,
            time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)-1,
            dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1,
            dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1,
            month = Calendar.getInstance().get(Calendar.MONTH),
            year = Calendar.getInstance().get(Calendar.YEAR),
            missed = false
        )
    }

    suspend fun addTakeNowMeds(){
        MedicationRepository.createMed(
            medName = "Take Now Meds",
            notes = "Take 2 pills every 4 hours",
            quantity = 10,
            dosage = 2,
            streak = 6,
            time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1,
            dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1,
            month = Calendar.getInstance().get(Calendar.MONTH),
            year = Calendar.getInstance().get(Calendar.YEAR),
            missed = false
        )
    }

    suspend fun takeMissedMeds(){
        val meds = MedicationRepository.getMeds().filter { it.missed == true }
        meds.forEach {
            val medication = Medication(
                dosage = it.dosage,
                notes = it.notes,
                quantity  = it.quantity,
                medName = it.medName,
                id = it.id,
                streak = 1,
                time = it.time,
                userId = UserRepository.getCurrentUserId(),
                dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
                dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                month = Calendar.getInstance().get(Calendar.MONTH),
                year = Calendar.getInstance().get(Calendar.YEAR),
                missed = false
            )
            uiState._meds[uiState._meds.indexOf(it)] = medication
            MedicationRepository.updateMed(medication)
        }

    }

    suspend fun checkIfHaveMeds(){
        var noMeds = true
        if(MedicationRepository.haveMedsForTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))){
            val meds = MedicationRepository.getMeds().filter {
                it.time == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            }
            meds.forEach {
                if(it.dayOfWeek == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) && it.dayOfMonth == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && it.year == Calendar.getInstance().get(Calendar.YEAR) && it.month == Calendar.getInstance().get(Calendar.MONTH)){
                    return@forEach
                }else{
                    noMeds = false
                    uiState.haveMeds = true
                }
            }
        }
        if(noMeds) {
            uiState.haveMeds = false
        }
    }
    suspend fun loadDailyQuote(){
        this.checkIfHaveMeds()
        uiState.time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        uiState.quoteLoaded = true
        val quote = QuotesRepository.getDailyQuote()
        uiState.quote = quote
        uiState.quoteLoaded = false
    }

    suspend fun updateMissedMeds(time: Int){
        val meds = MedicationRepository.getMeds()
        meds.forEach {
            if(it.time!! < time) {
                if (it.dayOfWeek == Calendar.getInstance()
                        .get(Calendar.DAY_OF_WEEK) && it.dayOfMonth == Calendar.getInstance()
                        .get(Calendar.DAY_OF_MONTH) && it.year == Calendar.getInstance()
                        .get(Calendar.YEAR) && it.month == Calendar.getInstance()
                        .get(Calendar.MONTH)
                ) {
                    return@forEach
                } else {
                    val medication = Medication(
                        dosage = it.dosage,
                        notes = it.notes,
                        quantity = it.quantity,
                        medName = it.medName,
                        id = it.id,
                        streak = it.streak,
                        time = it.time,
                        userId = UserRepository.getCurrentUserId(),
                        dayOfWeek = it.dayOfWeek,
                        dayOfMonth = it.dayOfMonth,
                        month = it.month,
                        year = it.year,
                        missed = true
                    )
                    uiState._meds[uiState._meds.indexOf(it)] = medication
                    MedicationRepository.updateMed(medication)
                }
            }
        }

    }

    suspend fun takeMeds(time: Int){
        val meds = MedicationRepository.getMeds()
        meds.forEach {
            var endStreak = true;
            if(it.time!! <= time){
                if(it.dayOfWeek == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) && it.dayOfMonth == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && it.year == Calendar.getInstance().get(Calendar.YEAR) && it.month == Calendar.getInstance().get(Calendar.MONTH)){
                    return@forEach
                }
                else if(it.time < time){

                }
                else if(it.dayOfMonth == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1 || Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 1){
                    if(it.dayOfWeek == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1 || (it.dayOfWeek == 7 && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1)){
                        if(it.month == Calendar.getInstance().get(Calendar.MONTH) || (it.month == 11 && Calendar.getInstance().get(Calendar.MONTH) == 0) || it.month == Calendar.getInstance().get(Calendar.MONTH)-1){
                            if(it.year == Calendar.getInstance().get(Calendar.YEAR) || it.year == Calendar.getInstance().get(Calendar.YEAR)-1){
                                val medication = Medication(
                                    dosage = it.dosage,
                                    notes = it.notes,
                                    quantity  = it.quantity,
                                    medName = it.medName,
                                    id = it.id,
                                    streak = it.streak?.plus(1),
                                    time = it.time,
                                    userId = UserRepository.getCurrentUserId(),
                                    dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
                                    dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                                    month = Calendar.getInstance().get(Calendar.MONTH),
                                    year = Calendar.getInstance().get(Calendar.YEAR),
                                    missed = false
                                )
                                uiState._meds[uiState._meds.indexOf(it)] = medication
                                MedicationRepository.updateMed(medication)
                                endStreak = false
                            }
                        }
                    }
                }
                if(endStreak){
                    val medication = Medication(
                        dosage = it.dosage,
                        notes = it.notes,
                        quantity  = it.quantity,
                        medName = it.medName,
                        id = it.id,
                        streak = 1,
                        time = it.time,
                        userId = UserRepository.getCurrentUserId(),
                        dayOfWeek = it.dayOfWeek,
                        dayOfMonth = it.dayOfMonth,
                        month = it.month,
                        year = it.year,
                        missed = true
                    )
                    uiState._meds[uiState._meds.indexOf(it)] = medication
                    MedicationRepository.updateMed(medication)
                }
            }
        }

    }
}
