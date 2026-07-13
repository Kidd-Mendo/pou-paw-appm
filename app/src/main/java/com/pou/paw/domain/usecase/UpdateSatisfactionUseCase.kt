package com.pou.paw.domain.usecase

import com.pou.paw.data.model.PetEntity
import com.pou.paw.data.model.PlantEntity
import com.pou.paw.data.model.PouEntity
import com.pou.paw.data.repository.IPetPlantRepository
import javax.inject.Inject

class UpdateSatisfactionUseCase @Inject constructor(
    private val petPlantRepository: IPetPlantRepository
) {
    suspend operator fun invoke(entity: PouEntity, needName: String) {
        when (entity) {
            is PetEntity -> {
                val updatedNeeds = entity.needs.map { need ->
                    if (need.name == needName) {
                        need.copy(level = (need.level + 0.2f).coerceAtMost(1.0f))
                    } else need
                }
                petPlantRepository.updatePet(entity.copy(needs = updatedNeeds))
            }
            is PlantEntity -> {
                val updatedNeeds = entity.needs.map { need ->
                    if (need.name == needName) {
                        need.copy(level = (need.level + 0.2f).coerceAtMost(1.0f))
                    } else need
                }
                petPlantRepository.updatePlant(entity.copy(needs = updatedNeeds))
            }
        }
    }
}
