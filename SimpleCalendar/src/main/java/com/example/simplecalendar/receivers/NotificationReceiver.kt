package com.example.simplecalender.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.simplecalender.extensions.dbHelper
import com.example.simplecalender.extensions.notifyEvent
import com.example.simplecalender.extensions.scheduleAllEvents
import com.example.simplecalender.extensions.updateListWidget
import com.example.simplecalender.helpers.EVENT_ID
import com.example.simplecalender.helpers.Formatter

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.updateListWidget()
        val id = intent.getIntExtra(EVENT_ID, -1)
        if (id == -1)
            return

        val event = context.dbHelper.getEventWithId(id)
        if (event == null || event.getReminders().isEmpty())
            return

        if (!event.ignoreEventOccurrences.contains(Formatter.getDayCodeFromTS(event.startTS).toInt())) {
            context.notifyEvent(event)
        }
        context.scheduleAllEvents()
    }
}
