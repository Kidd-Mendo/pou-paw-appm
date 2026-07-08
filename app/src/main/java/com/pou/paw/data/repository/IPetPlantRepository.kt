package com.pou.paw.data.repository

import com.pou.paw.data.model.PetEntity
import com.pou.paw.data.model.PlantEntity
import com.pou.paw.data.model.PouEntity
import kotlinx.coroutines.flow.Flow

interface IPetPlantRepository {
    val allPets: Flow<List<PetEntity>>
    val allPlants: Flow<List<PlantEntity>>
    val petPlants: Flow<List<PouEntity>>
    suspend fun addPet(pet: PetEntity): Long
    suspend fun addPlant(plant: PlantEntity): Long
}
