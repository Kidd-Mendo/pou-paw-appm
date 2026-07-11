package com.pou.paw.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pou.paw.data.model.BreedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {
    @Query("SELECT name FROM breeds ORDER BY name ASC")
    fun getAllBreeds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreeds(breeds: List<BreedEntity>)

    @Query("SELECT COUNT(*) FROM breeds")
    suspend fun getCount(): Int
}
