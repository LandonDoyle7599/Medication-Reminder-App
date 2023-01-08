package com.example.mobiledevfinal.models

import java.time.DayOfWeek


data class Medication(
    val id: String? = null,
    val userId: String? = null,
    val deleteMe: Boolean? = null,
    val dosage: Int? = null,
    val medName: String? = null,
    val quantity: Int? = null,
    val notes: String? = null,
    val streak: Int? = null,
    val time: Int? = null,
    val dayOfWeek: Int? = null,
    val dayOfMonth: Int? = null,
    val month: Int? = null,
    val year: Int? = null,
    val missed: Boolean? = null,
)
