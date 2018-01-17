package com.example.simplecalender.interfaces

import com.example.simplecalender.models.Day

interface MonthlyCalendar {
    fun updateMonthlyCalendar(month: String, days: List<Day>)
}
