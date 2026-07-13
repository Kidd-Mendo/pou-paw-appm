package com.pou.paw.domain.usecase

import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.pou.paw.data.model.PetEntity
import com.pou.paw.data.model.PlantEntity
import com.pou.paw.data.model.ReminderEntity
import com.pou.paw.data.repository.IPetPlantRepository
import com.pou.paw.data.repository.IReminderRepository
import com.pou.paw.worker.ReminderWorker
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class SaveReminderUseCase @Inject constructor(
    private val reminderRepository: IReminderRepository,
    private val petPlantRepository: IPetPlantRepository,
    private val workManager: WorkManager
) {
    suspend operator fun invoke(
        name: String,
        category: String,
        breedOrType: String,
        imageUri: String?,
        action: String,
        frequencyType: String,
        frequencyValue: Int,
        date: LocalDate,
        message: String
    ) {
        // 1. Guardar la Entidad
        val generatedId = if (category == "Mascota") {
            val newPet = PetEntity(
                name = name,
                type = "Mascota",
                breed = breedOrType,
                age = "0",
                imageUrl = imageUri,
                needs = listOf(
                    com.pou.paw.data.model.Need("Comida", 0.5f),
                    com.pou.paw.data.model.Need("Agua", 0.7f),
                    com.pou.paw.data.model.Need("Limpieza", 0.3f)
                )
            )
            petPlantRepository.addPet(newPet)
        } else {
            val newPlant = PlantEntity(
                name = name,
                type = "Planta",
                species = breedOrType,
                wateringFrequency = "$frequencyType $frequencyValue",
                imageUrl = imageUri,
                needs = listOf(
                    com.pou.paw.data.model.Need("Agua", 0.6f),
                    com.pou.paw.data.model.Need("Luz", 0.8f),
                    com.pou.paw.data.model.Need("Nutriente", 0.4f)
                )
            )
            petPlantRepository.addPlant(newPlant)
        }

        // 2. Guardar el Recordatorio
        val targetTime = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        val reminder = ReminderEntity(
            targetId = generatedId,
            category = category,
            action = action,
            frequency = "$frequencyType $frequencyValue",
            message = message,
            nextOccurrence = targetTime,
            imageUri = imageUri
        )
        reminderRepository.addReminder(reminder)

        // 3. Programar Notificación
        val delay = Duration.ofMillis(targetTime - System.currentTimeMillis())
        if (!delay.isNegative) {
            val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(delay)
                .setInputData(
                    Data.Builder()
                        .putString("targetName", name)
                        .putString("message", "$action: $message")
                        .build()
                )
                .build()
            workManager.enqueue(workRequest)
        }
    }
}
