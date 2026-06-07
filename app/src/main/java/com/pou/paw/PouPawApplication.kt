package com.pou.paw

import android.app.Application
import com.pou.paw.data.local.AppDatabase
import com.pou.paw.data.repository.ReminderRepository

class PouPawApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ReminderRepository(database.reminderDao()) }
}
