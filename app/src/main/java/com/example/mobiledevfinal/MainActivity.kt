package com.example.mobiledevfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mobiledevfinal.ui.App
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationManagerCompat
import com.example.mobiledevfinal.services.MedsBroadcastReceiver
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
            }

    //more notification stuff
        val alarmManager: AlarmManager =  getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MedsBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_HOUR,
            pendingIntent
        )

        val channel = NotificationChannel(
            "meds",
            "Meds Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "It is time to take your meds" }
        NotificationManagerCompat
            .from(this)
            .createNotificationChannel(channel)
    }
}

