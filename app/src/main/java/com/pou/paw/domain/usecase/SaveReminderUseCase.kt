package com.pou.paw.domain.usecase

import com.pou.paw.data.model.PetEntity
import com.pou.paw.data.model.PlantEntity
import com.pou.paw.data.model.ReminderEntity
import com.pou.paw.data.repository.IPetPlantRepository
import com.pou.paw.data.repository.IReminderRepository
import java.time.LocalDate
import java.time.ZoneOffset
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
        // 1. Guardar la Entidad (Mascota o Planta) y obtener su ID generado
        val generatedId = if (category == "Mascota") {
            val newPet = PetEntity(
                name = name,
                type = "Mascota",
                breed = breedOrType,
                age = "0", // Campo requerido por la rúbrica
                imageUrl = imageUri
            )
            petPlantRepository.addPet(newPet)
        } else {
            val newPlant = PlantEntity(
                name = name,
                type = "Planta",
                species = breedOrType,
                wateringFrequency = "$frequencyType $frequencyValue",
                imageUrl = imageUri
            )
            petPlantRepository.addPlant(newPlant)
        }

        // 2. Guardar el Recordatorio vinculado al ID de la entidad
        val reminder = ReminderEntity(
            targetId = generatedId,
            category = category,
            action = action,
            frequency = "$frequencyType $frequencyValue",
            message = message,
            nextOccurrence = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            imageUri = imageUri
        )
        reminderRepository.addReminder(reminder)
    }
}
