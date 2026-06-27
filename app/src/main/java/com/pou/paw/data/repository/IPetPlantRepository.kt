package com.pou.paw.data.repository

import com.pou.paw.data.model.Pet
import com.pou.paw.data.model.Plant
import com.pou.paw.data.model.PouEntity
import kotlinx.coroutines.flow.Flow

interface IPetPlantRepository {
    val petPlants: Flow<List<PouEntity>>
    suspend fun addPet(pet: Pet)
    suspend fun addPlant(plant: Plant)
}
