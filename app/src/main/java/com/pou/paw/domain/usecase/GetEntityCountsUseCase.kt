package com.pou.paw.domain.usecase

import com.pou.paw.data.model.PouEntity
import com.pou.paw.data.model.PetEntity
import com.pou.paw.data.model.PlantEntity

import javax.inject.Inject

class GetEntityCountsUseCase @Inject constructor() {
    operator fun invoke(entities: List<PouEntity>): Pair<Int, Int> {
        val pets = entities.count { it is PetEntity || it.type == "Gato" || it.type == "Perro" }
        val plants = entities.count { it is PlantEntity || it.type == "Planta" }
        return Pair(pets, plants)
    }
}
