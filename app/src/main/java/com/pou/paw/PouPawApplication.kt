package com.pou.paw

import android.app.Application
import com.pou.paw.data.repository.ReminderRepository

class PouPawApplication : Application() {
    val repository by lazy { ReminderRepository(this) }
}
