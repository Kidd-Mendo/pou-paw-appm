package com.pou.paw.data.repository

import com.pou.paw.data.model.ReminderEntity
import kotlinx.coroutines.flow.Flow

interface IReminderRepository {
    val reminders: Flow<List<ReminderEntity>>
    suspend fun addReminder(reminder: ReminderEntity)
    suspend fun deleteReminder(reminder: ReminderEntity)
}
