package com.pou.paw.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.pou.paw.data.model.Reminder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class ReminderRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("reminders_prefs", Context.MODE_PRIVATE)
    private val _reminders = MutableStateFlow<List<Reminder>>(loadReminders())
    val reminders: StateFlow<List<Reminder>> = _reminders.asStateFlow()

    fun addReminder(reminder: Reminder) {
        val newList = _reminders.value + reminder
        _reminders.value = newList
        saveReminders(newList)
    }

    private fun saveReminders(list: List<Reminder>) {
        val array = JSONArray()
        list.forEach { reminder ->
            val obj = JSONObject()
            obj.put("id", reminder.id)
            obj.put("targetId", reminder.targetId)
            obj.put("category", reminder.category)
            obj.put("action", reminder.action)
            obj.put("frequency", reminder.frequency)
            obj.put("message", reminder.message)
            obj.put("nextOccurrence", reminder.nextOccurrence)
            obj.put("imageUri", reminder.imageUri ?: "")
            array.put(obj)
        }
        prefs.edit().putString("reminders_json", array.toString()).apply()
    }

    private fun loadReminders(): List<Reminder> {
        val json = prefs.getString("reminders_json", null) ?: return emptyList()
        val list = mutableListOf<Reminder>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(Reminder(
                    id = obj.getString("id"),
                    targetId = obj.getString("targetId"),
                    category = obj.getString("category"),
                    action = obj.getString("action"),
                    frequency = obj.getString("frequency"),
                    message = obj.getString("message"),
                    nextOccurrence = obj.getLong("nextOccurrence"),
                    imageUri = obj.optString("imageUri", "").ifBlank { null }
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
