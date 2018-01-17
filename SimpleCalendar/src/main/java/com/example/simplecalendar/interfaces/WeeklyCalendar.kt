package com.example.simplecalender.interfaces

import com.example.simplecalender.models.Event

interface WeeklyCalendar {
    fun updateWeeklyCalendar(events: ArrayList<Event>)
}
