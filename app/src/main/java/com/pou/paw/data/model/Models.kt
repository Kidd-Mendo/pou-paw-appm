package com.pou.paw.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

sealed class PouEntity {
    abstract val id: String
    abstract val name: String
    abstract val type: String
    abstract val imageUrl: String?
}

data class Pet(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val type: String, // e.g. "Gato", "Perro"
    val breed: String,
    override val imageUrl: String? = null,
    val needs: List<Need> = listOf(
        Need("Comida", 0.5f),
        Need("Agua", 0.7f),
        Need("Limpieza", 0.3f)
    )
) : PouEntity()

data class Plant(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val type: String, // e.g. "Helecho"
    val species: String,
    override val imageUrl: String? = null,
    val isToxicForPets: Boolean = false,
    val needs: List<Need> = listOf(
        Need("Agua", 0.75f),
        Need("Luz", 0.9f),
        Need("Nutriente", 0.4f)
    )
) : PouEntity()

data class Need(
    val name: String,
    val level: Float // 0.0 to 1.0
)

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val targetId: String, // Pet or Plant Name
    val category: String, // "Mascota" or "Planta"
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
