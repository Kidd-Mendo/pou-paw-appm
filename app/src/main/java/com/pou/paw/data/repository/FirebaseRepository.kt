package com.pou.paw.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.pou.paw.data.model.Pet
import com.pou.paw.data.model.Plant
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun savePet(pet: Pet) {
        db.collection("pets").document(pet.id).set(pet).await()
    }

    suspend fun savePlant(plant: Plant) {
        db.collection("plants").document(plant.id).set(plant).await()
    }

    suspend fun getPets(): List<Pet> {
        return db.collection("pets").get().await().toObjects(Pet::class.java)
    }
}
