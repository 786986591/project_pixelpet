package com.ray.projectpixelpet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {

    @Query("SELECT * FROM pet_state WHERE petId = :petId LIMIT 1")
    fun observePet(petId: String = "main_pet"): Flow<PetEntity?>

    @Query("SELECT * FROM pet_state WHERE petId = :petId LIMIT 1")
    suspend fun getPet(petId: String = "main_pet"): PetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: PetEntity)
}

