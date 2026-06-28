package com.pou.paw.domain.usecase

import com.pou.paw.data.model.Pet
import com.pou.paw.data.model.Plant
import com.pou.paw.data.model.Reminder
import com.pou.paw.data.repository.IPetPlantRepository
import com.pou.paw.data.repository.IReminderRepository
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.UUID

import javax.inject.Inject

class SaveReminderUseCase @Inject constructor(
    private val reminderRepository: IReminderRepository,
    private val petPlantRepository: IPetPlantRepository
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
        // 1. Guardar la Entidad (Mascota o Planta)
        if (category == "Mascota") {
            val newPet = Pet(
                name = name,
                type = "Mascota",
                breed = breedOrType,
                imageUrl = imageUri
            )
            petPlantRepository.addPet(newPet)
        } else {
            val newPlant = Plant(
                name = name,
                type = "Planta",
                species = breedOrType,
                imageUrl = imageUri
            )
            petPlantRepository.addPlant(newPlant)
        }

        // 2. Guardar el Recordatorio
        val reminder = Reminder(
            id = UUID.randomUUID().toString(),
            targetId = name,
            category = category,
            breedOrType = breedOrType,
            action = action,
            frequency = "$frequencyType $frequencyValue",
            message = message,
            nextOccurrence = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            imageUri = imageUri
        )
        reminderRepository.addReminder(reminder)
    }
}
