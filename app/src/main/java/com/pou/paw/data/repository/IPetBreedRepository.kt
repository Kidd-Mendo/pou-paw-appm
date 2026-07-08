package com.pou.paw.data.repository

import kotlinx.coroutines.flow.Flow

interface IPetBreedRepository {
    suspend fun getBreeds(): List<String>
    suspend fun getRandomImage(breed: String? = null): String?
}
