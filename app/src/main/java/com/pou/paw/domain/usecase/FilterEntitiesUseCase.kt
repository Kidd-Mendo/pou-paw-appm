package com.pou.paw.domain.usecase

import com.pou.paw.data.model.PouEntity
import com.pou.paw.data.model.Pet
import com.pou.paw.data.model.Plant

import javax.inject.Inject

class FilterEntitiesUseCase @Inject constructor() {
    operator fun invoke(entities: List<PouEntity>, filter: String): List<PouEntity> {
        return when (filter) {
            "Mascotas" -> entities.filter { it is Pet || it.type == "Gato" || it.type == "Perro" }
            "Plantas" -> entities.filter { it is Plant || it.type == "Planta" }
            else -> entities
        }
    }
}
