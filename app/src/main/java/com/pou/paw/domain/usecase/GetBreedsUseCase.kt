package com.pou.paw.domain.usecase

import com.pou.paw.data.repository.IPetBreedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBreedsUseCase @Inject constructor(
    private val repository: IPetBreedRepository
) {
    operator fun invoke(): Flow<List<String>> = repository.breeds

    suspend fun refresh() {
        repository.refreshBreeds()
    }
}
