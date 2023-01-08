package com.example.mobiledevfinal.repositories

import com.example.mobiledevfinal.models.Medication
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.util.Calendar

object MedicationRepository {

    private val medicationCache = mutableListOf<Medication>()
    private var cacheInitialized = false

    suspend fun haveMedsForTime(time: Int): Boolean{
        val medsAtTime = this.getMeds().filter {
            it.time == time
        }
       if(medsAtTime.isNotEmpty()){
           return true
       }
        return false
    }

    suspend fun checkForMissedMeds(): Boolean{
        return true
    }


    suspend fun getMeds(): List<Medication> {
        if(!cacheInitialized) {
            val snapshot = Firebase.firestore
                .collection("meds")
                .whereEqualTo("userId", UserRepository.getCurrentUserId())
                .get().await()
            medicationCache.addAll(snapshot.toObjects())
            cacheInitialized = true
        }
        return medicationCache
    }

    suspend fun createMed(
        dosage: Int,
        notes: String,
        medName: String,
        quantity: Int,
        streak: Int,
        time: Int,
        dayOfWeek: Int,
        dayOfMonth: Int,
        month: Int,
        year: Int,
        missed: Boolean

        ): Medication {
        val doc = Firebase.firestore.collection("meds").document()
        val medication = Medication(
            dosage = dosage,
            notes = notes,
            quantity  = quantity,
            medName = medName,
            id = doc.id,
            streak = streak,
            time = time,
            userId = UserRepository.getCurrentUserId(),
            dayOfWeek = dayOfWeek,
            dayOfMonth = dayOfMonth,
            month = month,
            year = year,
            missed = missed
        )
        doc.set(medication).await()
        medicationCache.add(medication)
        return medication
    }

    suspend fun updateMed(medication: Medication){
        Firebase.firestore
            .collection("meds")
            .document(medication.id!!)
            .set(medication)
            .await()

        val oldMedIndex = medicationCache.indexOfFirst { it.id == medication.id }
        medicationCache[oldMedIndex] = medication
    }

    suspend fun deleteMed(id: String) {
        Firebase.firestore
            .collection("meds")
            .document(id)
            .delete()
            .await()
        medicationCache.remove(medicationCache.find { it.id == id })
    }

    suspend fun clearCache(){
        medicationCache.clear()
        cacheInitialized = false
    }

}