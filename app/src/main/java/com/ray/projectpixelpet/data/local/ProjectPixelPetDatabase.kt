package com.ray.projectpixelpet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PetEntity::class, ActivityLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProjectPixelPetDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun activityLogDao(): ActivityLogDao
}

