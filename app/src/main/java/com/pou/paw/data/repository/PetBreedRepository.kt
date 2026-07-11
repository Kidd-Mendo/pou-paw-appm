package com.pou.paw.data.repository

import com.pou.paw.data.local.BreedDao
import com.pou.paw.data.model.BreedEntity
import com.pou.paw.data.remote.PetApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetBreedRepository @Inject constructor(
    private val apiService: PetApiService,
    private val breedDao: BreedDao
) : IPetBreedRepository {

    override val breeds: Flow<List<String>> = breedDao.getAllBreeds()

    override suspend fun refreshBreeds() {
        try {
            val response = apiService.getAllBreeds()
            if (response.status == "success") {
                val breedEntities = response.breeds.keys.map { BreedEntity(it) }
                breedDao.insertBreeds(breedEntities)
            }
        } catch (e: Exception) {
            // Manejar error de red: la UI seguirá viendo los datos locales previos
        }
    }

    override suspend fun getRandomImage(breed: String?): String? {
        return try {
            val response = if (breed != null) {
                apiService.getRandomImageByBreed(breed)
            } else {
                apiService.getRandomImage()
            }
            if (response.status == "success") {
                response.imageUrl
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
