package com.pou.paw.data.repository

import com.pou.paw.data.local.PetDao
import com.pou.paw.data.local.PlantDao
import com.pou.paw.data.model.Pet
import com.pou.paw.data.model.Plant
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

    override val petPlants: Flow<List<PouEntity>> = combine(
        petDao.getAllPets(),
        plantDao.getAllPlants()
    ) { pets, plants ->
        pets + plants
    }

    override suspend fun addPet(pet: Pet) = withContext(Dispatchers.IO) {
        petDao.insertPet(pet)
    }

    override suspend fun addPlant(plant: Plant) = withContext(Dispatchers.IO) {
        plantDao.insertPlant(plant)
    }
}
