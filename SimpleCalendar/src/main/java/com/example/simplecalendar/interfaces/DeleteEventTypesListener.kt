package com.example.simplecalender.interfaces

import java.util.*

interface DeleteEventTypesListener {
    fun deleteEventTypes(ids: ArrayList<Int>, deleteEvents: Boolean)
}
