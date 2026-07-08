package com.pou.paw.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Interfaz común para que el filtro y las listas funcionen con ambos tipos
interface PouEntity {
    val id: Long
    val name: String
    val type: String
    val imageUrl: String?
}

data class Need(
    val name: String,
    val level: Float
)

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    override val name: String,
    override val type: String,
    val breed: String,
    val age: String,
    override val imageUrl: String? = null,
    val needs: List<Need> = emptyList()
) : PouEntity

@Entity(tableName = "plants")
data class PlantEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    override val name: String,
    override val type: String, // "Planta"
    val species: String,
    val wateringFrequency: String,
    override val imageUrl: String? = null,
    val needs: List<Need> = emptyList()
) : PouEntity

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val targetId: Long,
    val category: String,
    val action: String,
    val frequency: String,
    val message: String,
    val nextOccurrence: Long,
    val imageUri: String? = null
)

data class UserStats(
    val streakDays: Int = 0,
    val totalTasksCompleted: Int = 0,
    val achievements: List<String> = emptyList()
)
