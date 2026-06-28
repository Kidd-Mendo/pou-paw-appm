package com.pou.paw.domain.usecase

import com.pou.paw.data.model.PouEntity
import com.pou.paw.data.model.Pet
import com.pou.paw.data.model.Plant

class GetEntityCountsUseCase {
    operator fun invoke(entities: List<PouEntity>): Pair<Int, Int> {
        val pets = entities.count { it is Pet || it.type == "Gato" || it.type == "Perro" }
        val plants = entities.count { it is Plant || it.type == "Planta" }
        return Pair(pets, plants)
    }
}
