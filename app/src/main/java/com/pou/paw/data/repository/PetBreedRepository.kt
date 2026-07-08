package com.pou.paw.data.repository

import com.pou.paw.data.remote.PetApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetBreedRepository @Inject constructor(
    private val apiService: PetApiService
) : IPetBreedRepository {

    override suspend fun getBreeds(): List<String> {
        return try {
            val response = apiService.getAllBreeds()
            if (response.status == "success") {
                response.breeds.keys.toList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
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
