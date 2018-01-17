package com.example.simplecalender.models

class ListSection(val title: String, val isToday: Boolean = false) : ListItem() {
    override fun toString() = "ListSection {title=$title, isToday=$isToday}"
}
