package com.example.simplecalender.helpers

import android.content.Context
import com.example.simplecalender.extensions.dbHelper
import com.example.simplecalender.interfaces.WeeklyCalendar
import com.example.simplecalender.models.Event
import java.util.*

class WeeklyCalendarImpl(val mCallback: WeeklyCalendar, val mContext: Context) {
    var mEvents = ArrayList<Event>()

    fun updateWeeklyCalendar(weekStartTS: Int) {
        val startTS = weekStartTS
        val endTS = startTS + WEEK_SECONDS
        mContext.dbHelper.getEvents(startTS, endTS) {
            mEvents = it as ArrayList<Event>
            mCallback.updateWeeklyCalendar(it)
        }
    }
}
