package com.pou.paw.data.repository

import com.pou.paw.data.local.ReminderDao
import com.pou.paw.data.model.ReminderEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReminderRepository @Inject constructor(private val reminderDao: ReminderDao) : IReminderRepository {
    override val reminders: Flow<List<ReminderEntity>> = reminderDao.getAllReminders()

    override suspend fun addReminder(reminder: ReminderEntity) {
        reminderDao.insertReminder(reminder)
    }

    override suspend fun deleteReminder(reminder: ReminderEntity) {
        reminderDao.deleteReminder(reminder)
    }
}
