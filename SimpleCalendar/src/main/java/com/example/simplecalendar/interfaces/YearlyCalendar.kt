package com.example.simplecalender.interfaces

import android.util.SparseArray
import java.util.*

interface YearlyCalendar {
    fun updateYearlyCalendar(events: SparseArray<ArrayList<Int>>)
}
