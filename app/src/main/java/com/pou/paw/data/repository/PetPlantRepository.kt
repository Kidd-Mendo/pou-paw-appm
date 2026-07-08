package com.pou.paw.data.repository

import com.pou.paw.data.local.PetDao
import com.pou.paw.data.local.PlantDao
import com.pou.paw.data.model.PetEntity
import com.pou.paw.data.model.PlantEntity
import com.pou.paw.data.model.PouEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PetPlantRepository @Inject constructor(
    private val petDao: PetDao,
    private val plantDao: PlantDao
) : IPetPlantRepository {

    override val allPets: Flow<List<PetEntity>> = petDao.getAllPets()
    override val allPlants: Flow<List<PlantEntity>> = plantDao.getAllPlants()

    override val petPlants: Flow<List<PouEntity>> = combine(allPets, allPlants) { pets, plants ->
        pets + plants
    }

    override suspend fun addPet(pet: PetEntity): Long = withContext(Dispatchers.IO) {
        petDao.insertPet(pet)
    }

    override suspend fun addPlant(plant: PlantEntity): Long = withContext(Dispatchers.IO) {
        plantDao.insertPlant(plant)
    }
}
