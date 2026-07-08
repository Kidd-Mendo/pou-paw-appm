package com.pou.paw.domain.usecase

import com.pou.paw.data.repository.IPetBreedRepository
import javax.inject.Inject

class GetRandomPetImageUseCase @Inject constructor(
    private val repository: IPetBreedRepository
) {
    suspend operator fun invoke(breed: String? = null): String? = repository.getRandomImage(breed)
}
