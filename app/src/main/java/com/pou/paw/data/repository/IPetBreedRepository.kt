package com.pou.paw.data.repository

import kotlinx.coroutines.flow.Flow

interface IPetBreedRepository {
    val breeds: Flow<List<String>>
    suspend fun refreshBreeds()
    suspend fun getRandomImage(breed: String? = null): String?
}
