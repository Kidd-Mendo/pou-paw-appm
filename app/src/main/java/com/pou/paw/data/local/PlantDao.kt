package com.pou.paw.data.local

import androidx.room.*
import com.pou.paw.data.model.PlantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Query("SELECT * FROM plants")
    fun getAllPlants(): Flow<List<PlantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: PlantEntity): Long

    @Delete
    suspend fun deletePlant(plant: PlantEntity)
}
