package com.pou.paw.data.remote

import com.pou.paw.data.remote.model.DogBreedsResponse
import com.pou.paw.data.remote.model.DogImageResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PetApiService {
    @GET("breeds/list/all")
    suspend fun getAllBreeds(): DogBreedsResponse

    @GET("breed/{breed}/images/random")
    suspend fun getRandomImageByBreed(
        @Path("breed") breed: String
    ): DogImageResponse

    @GET("breeds/image/random")
    suspend fun getRandomImage(): DogImageResponse
}
