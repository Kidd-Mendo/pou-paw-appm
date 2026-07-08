package com.pou.paw.domain.usecase

import com.pou.paw.data.repository.IPetBreedRepository
import javax.inject.Inject

class GetBreedsUseCase @Inject constructor(
    private val repository: IPetBreedRepository
) {
    suspend operator fun invoke(): List<String> = repository.getBreeds()
}
