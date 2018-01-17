package com.example.simplecalender.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.simplecalender.R
import com.example.simplecalender.activities.EventActivity
import com.example.simplecalender.activities.SimpleActivity
import com.example.simplecalender.adapters.EventListAdapter
import com.example.simplecalender.extensions.config
import com.example.simplecalender.extensions.dbHelper
import com.example.simplecalender.extensions.getFilteredEvents
import com.example.simplecalender.extensions.seconds
import com.example.simplecalender.helpers.*
import com.example.simplecalender.helpers.Formatter
import com.example.simplecalender.interfaces.DeleteEventsListener
import com.example.simplecalender.models.Event
import com.example.simplecalender.models.ListEvent
import com.example.simplecalender.models.ListItem
import com.example.simplecalender.models.ListSection
import com.simplemobiletools.commons.extensions.beGoneIf
import com.simplemobiletools.commons.extensions.beVisibleIf
import kotlinx.android.synthetic.main.fragment_event_list.view.*
import org.joda.time.DateTime
import java.util.*

class EventListFragment : Fragment(), DBHelper.EventUpdateListener, DeleteEventsListener {
    private var mEvents: List<Event> = ArrayList()
    private var prevEventsHash = 0
    lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(R.layout.fragment_event_list, container, false)
        val placeholderText = String.format(getString(R.string.two_string_placeholder), "${getString(R.string.no_upcoming_events)}\n", getString(R.string.add_some_events))
        mView.calendar_empty_list_placeholder.text = placeholderText
        return mView
    }

    override fun onResume() {
        super.onResume()
        checkEvents()
    }

    private fun checkEvents() {
        val fromTS = DateTime().seconds() - context.config.displayPastEvents * 60
        val toTS = DateTime().plusYears(1).seconds()
        context.dbHelper.getEvents(fromTS, toTS) {
            receivedEvents(it)
        }
    }

    override fun gotEvents(events: MutableList<Event>) {
        receivedEvents(events)
    }

    private fun receivedEvents(events: MutableList<Event>) {
        if (context == null || activity == null)
            return

        val filtered = context.getFilteredEvents(events)
        val hash = filtered.hashCode()
        if (prevEventsHash == hash)
            return

        prevEventsHash = hash
        mEvents = filtered
        val listItems = ArrayList<ListItem>(mEvents.size)
        val sorted = mEvents.sortedWith(compareBy({ it.startTS }, { it.endTS }, { it.title }, { it.description }))
        val sublist = sorted.subList(0, Math.min(sorted.size, 100))
        var prevCode = ""
        sublist.forEach {
            val code = Formatter.getDayCodeFromTS(it.startTS)
            if (code != prevCode) {
                val day = Formatter.getDayTitle(context, code)
                listItems.add(ListSection(day))
                prevCode = code
            }
            listItems.add(ListEvent(it.id, it.startTS, it.endTS, it.title, it.description, it.isAllDay))
        }

        val eventsAdapter = EventListAdapter(activity as SimpleActivity, listItems, this) {
            editEvent(it)
        }
        activity?.runOnUiThread {
            mView.calendar_events_list.apply {
                this@apply.adapter = eventsAdapter
            }
            checkPlaceholderVisibility()
        }
    }

    private fun checkPlaceholderVisibility() {
        mView.calendar_empty_list_placeholder.beVisibleIf(mEvents.isEmpty())
        mView.calendar_events_list.beGoneIf(mEvents.isEmpty())
        if (activity != null)
            mView.calendar_empty_list_placeholder.setTextColor(activity.config.textColor)
    }

    private fun editEvent(event: ListEvent) {
        Intent(activity.applicationContext, EventActivity::class.java).apply {
            putExtra(EVENT_ID, event.id)
            putExtra(EVENT_OCCURRENCE_TS, event.startTS)
            startActivity(this)
        }
    }

    override fun deleteItems(ids: ArrayList<Int>) {
        val eventIDs = Array(ids.size, { i -> (ids[i].toString()) })
        DBHelper.newInstance(activity.applicationContext, this).deleteEvents(eventIDs)
    }

    override fun addEventRepeatException(parentIds: ArrayList<Int>, timestamps: ArrayList<Int>) {
        parentIds.forEachIndexed { index, value ->
            context.dbHelper.addEventRepeatException(value, timestamps[index])
        }
        checkEvents()
    }

    override fun eventInserted(event: Event) {
        checkEvents()
    }

    override fun eventUpdated(event: Event) {
        checkEvents()
    }

    override fun eventsDeleted(cnt: Int) {
        checkEvents()
        checkPlaceholderVisibility()
    }
}
