package com.pou.paw.data.local

import androidx.room.*
import com.pou.paw.data.model.PetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pets")
    fun getAllPets(): Flow<List<PetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity): Long

    @Update
    suspend fun updatePet(pet: PetEntity)

    @Delete
    suspend fun deletePet(pet: PetEntity)
}
