package com.pou.paw.domain.usecase

import com.pou.paw.data.model.PouEntity
import com.pou.paw.data.model.PetEntity
import com.pou.paw.data.model.PlantEntity
import javax.inject.Inject

class FilterEntitiesUseCase @Inject constructor() {
    operator fun invoke(entities: List<PouEntity>, filter: String): List<PouEntity> {
        return when (filter) {
            "Mascotas" -> entities.filter { it is PetEntity || it.type == "Gato" || it.type == "Perro" }
            "Plantas" -> entities.filter { it is PlantEntity || it.type == "Planta" }
            else -> entities
        }
    }
}
