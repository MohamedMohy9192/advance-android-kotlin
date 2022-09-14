/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.eggtimernotifications.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.android.eggtimernotifications.MainActivity
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.receiver.SnoozeReceiver

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    // PendingIntent grants rights to another application or the system to perform an operation on behalf of your application.
    // A PendingIntent itself is simply a reference to a token maintained by the system
    // describing the original data used to retrieve it.
    // This means that, even if its owning application's process is killed,
    // the PendingIntent itself will remain usable from other processes it has been given to.
    // In this case, the system will use the pending intent to open the app on behalf of you,
    // regardless of whether or not the timer app is running.
    // TODO: Step 1.12 create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        // The PendingIntent flag specifies the option to create a new PendingIntent or use an existing one.
        // set PendingIntent.FLAG_UPDATE_CURRENT as the flag since you do not want to create a new notification
        // if there is an existing one. This way you will be modifying the current PendingIntent which is associated with the intent you are supplying.
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    // TODO: Step 2.0 add style
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.cooked_egg
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        // Set bigLargeIcon() to null so that the large icon goes away when the notification is expanded.
        .bigLargeIcon(null)
    // TODO: Step 2.2 add snooze action
    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        PendingIntent.FLAG_ONE_SHOT
    )

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.egg_notification_channel_id)
    )

    // TODO: Step 1.8 use the new 'breakfast' notification channel

    // TODO: Step 1.3 set title, text and icon to builder
    builder.setSmallIcon(R.drawable.cooked_egg)
    builder.setContentTitle(applicationContext.getString(R.string.notification_title))
    builder.setContentText(messageBody)

    // TODO: Step 1.13 set content intent
    builder.setContentIntent(contentPendingIntent)
    // set setAutoCancel() to true, so that when the user taps on the notification,
    // the notification dismisses itself as it takes them to the app.
    builder.setAutoCancel(true)

    // TODO: Step 2.1 add style to builder
    builder.setStyle(bigPicStyle)
    // Set the large icon with setLargeIcon() to the eggImage, so the image will be displayed
    // as a smaller icon when notification is collapsed.
    builder.setLargeIcon(eggImage)

    // TODO: Step 2.3 add snooze action
    builder.addAction(
        R.drawable.egg_icon,
        applicationContext.getString(R.string.snooze),
        snoozePendingIntent
    )

    // TODO: Step 2.5 set priority
    // To support devices running Android 7.1 (API level 25) or lower,
    // you must also call setPriority() for each notification,
    // using a priority constant from the NotificationCompat class.
    builder.priority = NotificationCompat.PRIORITY_HIGH

    // TODO: Step 1.4 call notify
    // This ID represents the current notification instance and is needed for updating
    // or canceling this notification. Since your app will only have one active notification
    // at a given time, you can use the same ID for all your notifications.

    // Notice that you can directly call notify() since you are performing the call from an extension function on the same class.
    notify(NOTIFICATION_ID, builder.build())

}

// TODO: Step 1.14 Cancel all notifications
/**
 * Cancels all notifications.
 *
 * If you set the timer, get a notification, and set the timer again,
 * the previous notification stays on the status bar while the new timer is running.
 * This can confuse your user if the app is in the background, and may result in undercooked eggs.
 * To fix this, you need to clear the previous notification when you start a new timer.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}