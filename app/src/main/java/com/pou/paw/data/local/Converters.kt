package com.pou.paw.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pou.paw.data.model.Need

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromNeedList(value: List<Need>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toNeedList(value: String): List<Need> {
        val listType = object : TypeToken<List<Need>>() {}.type
        return gson.fromJson(value, listType)
    }
}
