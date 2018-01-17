package com.example.simplecalender.extensions
import org.joda.time.DateTime

fun DateTime.seconds() = (millis / 1000).toInt()
