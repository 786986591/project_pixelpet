package com.ray.projectpixelpet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet_state")
data class PetEntity(
    @PrimaryKey val petId: String = "main_pet",
    val userId: String = "local_user",
    val cloudSyncToken: String? = null,
    val stage: String,
    val hunger: Int,
    val mood: Int,
    val cleanliness: Int,
    val energy: Int,
    val bondExp: Int,
    val lastUpdatedAt: Long,
    val lastSleptAt: Long
)

