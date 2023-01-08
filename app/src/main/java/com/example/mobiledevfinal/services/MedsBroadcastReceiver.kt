package com.example.mobiledevfinal.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mobiledevfinal.MainActivity
import com.example.mobiledevfinal.R
import com.example.mobiledevfinal.models.Medication
import com.example.mobiledevfinal.repositories.MedicationRepository
import com.example.mobiledevfinal.repositories.UserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar


class MedsBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null) return
        if(!UserRepository.isUserLoggedIn()) return
        val rightNow = Calendar.getInstance()
        val hour = rightNow.get(Calendar.HOUR_OF_DAY)
        GlobalScope.launch {
            var meds = MedicationRepository.getMeds().filter {
                it.time == hour || it.time.toString() == hour.toString()
            }
            val medsToTake : MutableList<Medication> = arrayListOf()
            meds.forEach {
                if(it.dayOfWeek == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) && it.dayOfMonth == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && it.year == Calendar.getInstance().get(Calendar.YEAR) && it.month == Calendar.getInstance().get(Calendar.MONTH)) {
                    return@forEach
                }else{
                    medsToTake.add(it)
                }
            }
            meds = medsToTake
            if(meds.isNotEmpty()){
                //display notification
                val stringBuilder = StringBuilder()
                meds.forEach{
                    stringBuilder.append("${it.medName}\n")
                }
                val intent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                val builder = NotificationCompat
                    .Builder(context, "meds")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("It is time to take your medication")
                    .setContentText(stringBuilder.toString())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setContentIntent(pendingIntent)

                NotificationManagerCompat.from(context).notify(
                    0,
                    builder.build()
                )
            }
        }
    }
}