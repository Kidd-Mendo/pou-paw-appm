package com.pou.paw.data.repository

import com.pou.paw.data.local.ReminderDao
import com.pou.paw.data.model.Reminder
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val reminderDao: ReminderDao) {
    val reminders: Flow<List<Reminder>> = reminderDao.getAllReminders()

    suspend fun addReminder(reminder: Reminder) {
        reminderDao.insertReminder(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder)
    }
}
