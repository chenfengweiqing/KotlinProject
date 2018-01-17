package com.example.simplecalender.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.simplecalender.R
import com.example.simplecalender.adapters.EventTypeAdapter
import com.example.simplecalender.dialogs.NewEventTypeDialog
import com.example.simplecalender.extensions.dbHelper
import com.example.simplecalender.helpers.DBHelper
import com.example.simplecalender.interfaces.DeleteEventTypesListener
import com.example.simplecalender.models.EventType
import com.simplemobiletools.commons.extensions.toast
import com.simplemobiletools.commons.extensions.updateTextColors
import kotlinx.android.synthetic.main.activity_manage_event_types.*
import java.util.*

class ManageEventTypesActivity : SimpleActivity(), DeleteEventTypesListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_event_types)

        getEventTypes()
        updateTextColors(manage_event_types_list)
    }

    private fun showEventTypeDialog(eventType: EventType? = null) {
        NewEventTypeDialog(this, eventType) {
            getEventTypes()
        }
    }

    private fun getEventTypes() {
        dbHelper.getEventTypes {
            runOnUiThread {
                manage_event_types_list.adapter = EventTypeAdapter(this, it, this) {
                    showEventTypeDialog(it)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_event_types, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_event_type -> showEventTypeDialog()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun deleteEventTypes(ids: ArrayList<Int>, deleteEvents: Boolean) {
        if (ids.contains(DBHelper.REGULAR_EVENT_TYPE_ID)) {
            toast(R.string.cannot_delete_default_type)
        }

        dbHelper.deleteEventTypes(ids, deleteEvents) {
            if (it > 0) {
                getEventTypes()
            } else {
                toast(R.string.unknown_error_occurred)
            }
        }
    }
}
