package com.example.nocrastinate

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.Calendar

class NotificationHelper(private val fragment: Fragment) {

    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun canScheduleExactAlarms(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = fragment.requireContext()
                .getSystemService(Context.ALARM_SERVICE) as AlarmManager
            return alarmManager.canScheduleExactAlarms()
        }
        return true
    }

    fun showAlarmPermissionDialog() {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("Wymagane uprawnienie")
            .setMessage("Aby ustawić alerty, musisz zezwolić aplikacji na powiadomienia Push.")
            .setPositiveButton("Otwórz ustawienia") { _, _ ->
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                fragment.startActivity(intent)
            }
            .setNegativeButton("Anuluj", null)
            .show()
    }

    fun showTimePickerDialog(task: Task, onTimeSelected: (Int, Int) -> Unit) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            fragment.requireContext(),
            { _, hour, minute ->
                onTimeSelected(hour, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    fun scheduleNotification(task: Task, hour: Int, minute: Int): Boolean {
        val context = fragment.requireContext()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(context, "Brak uprawnień", Toast.LENGTH_SHORT).show()
            return false
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("TASK_NAME", task.name)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        return try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Toast.makeText(
                context,
                "Przypomnienie ustawione na $hour:${String.format("%02d", minute)}",
                Toast.LENGTH_SHORT
            ).show()
            true
        } catch (e: SecurityException) {
            Toast.makeText(context, "Błąd: ${e.message}", Toast.LENGTH_LONG).show()
            false
        }
    }

    fun askForReminder(task: Task, onScheduled: (Long) -> Unit) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("Alert")
            .setMessage("Czy chcesz ustawić przypomnienie?")
            .setPositiveButton("Tak") { _, _ ->
                if (!canScheduleExactAlarms()) {
                    showAlarmPermissionDialog()
                    return@setPositiveButton
                }

                showTimePickerDialog(task) { hour, minute ->
                    if (scheduleNotification(task, hour, minute)) {
                        val calendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, 0)
                            if (timeInMillis <= System.currentTimeMillis()) {
                                add(Calendar.DAY_OF_YEAR, 1)
                            }
                        }
                        onScheduled(calendar.timeInMillis)
                    }
                }
            }
            .setNegativeButton("Nie", null)
            .show()
    }
}