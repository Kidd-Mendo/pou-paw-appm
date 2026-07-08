package com.pou.paw.data.remote.model

import com.google.gson.annotations.SerializedName

data class DogImageResponse(
    @SerializedName("message")
    val imageUrl: String,
    @SerializedName("status")
    val status: String
)

data class DogBreedsResponse(
    @SerializedName("message")
    val breeds: Map<String, List<String>>,
    @SerializedName("status")
    val status: String
)
