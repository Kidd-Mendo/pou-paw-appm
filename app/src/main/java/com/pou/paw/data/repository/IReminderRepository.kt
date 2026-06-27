package com.pou.paw.data.repository

import com.pou.paw.data.model.Reminder
import kotlinx.coroutines.flow.Flow

interface IReminderRepository {
    val reminders: Flow<List<Reminder>>
    suspend fun addReminder(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
}
