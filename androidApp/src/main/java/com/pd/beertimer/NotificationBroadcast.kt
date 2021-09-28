package com.pd.beertimer

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pd.beertimer.util.CHANNEL_ID
import com.pd.beertimer.util.NOTIFICATION_ID
import com.pd.beertimer.util.scheduleNotificationAtMs
import com.tlapp.beertimemm.drinking.DrinkCoordinator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.ExperimentalTime

@ExperimentalTime
class NotificationBroadcast : BroadcastReceiver(), KoinComponent {
    private val drinkCoordinator: DrinkCoordinator by inject()
    override fun onReceive(context: Context?, p1: Intent?) {
        context?.let { contextNonNull ->

            val intent = Intent(contextNonNull, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            var isLastAlarm = true
            drinkCoordinator.getNextDrinkDrinkingTime()?.let {
                isLastAlarm = false
                val triggerTimeInMs = it.toEpochMilliseconds()
                contextNonNull.scheduleNotificationAtMs(triggerTimeInMs)
            }

            val pendingIntent: PendingIntent = PendingIntent.getActivity(contextNonNull, 0, intent, 0)

            val builder = NotificationCompat.Builder(contextNonNull, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_beer)
                .setContentTitle(contextNonNull.getText(R.string.notification_title))
                .setContentText(
                    // Setting info in PendingIntent was a hassle, so this is a lazy implementation
                    if (isLastAlarm) {
                        contextNonNull.getString(R.string.notification_last)
                    } else {
                        contextNonNull.getString(R.string.notification_text)
                    }
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(contextNonNull)) {
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }
}
