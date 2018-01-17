package com.example.simplecalender.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.example.simplecalender.adapters.EventListWidgetAdapter

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent) = EventListWidgetAdapter(applicationContext, intent)
}
