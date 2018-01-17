package com.example.simplecalender.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.simplecalender.extensions.notifyRunningEvents
import com.example.simplecalender.extensions.scheduleAllEvents

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, arg1: Intent) {
        context.scheduleAllEvents()
        context.notifyRunningEvents()
    }
}
