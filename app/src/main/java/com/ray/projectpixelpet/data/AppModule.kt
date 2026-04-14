package com.ray.projectpixelpet.data

import android.content.Context
import androidx.room.Room
import com.ray.projectpixelpet.data.local.ProjectPixelPetDatabase
import com.ray.projectpixelpet.domain.AssetMapper
import com.ray.projectpixelpet.domain.DefaultAssetMapper
import com.ray.projectpixelpet.domain.DefaultPetRepository
import com.ray.projectpixelpet.domain.PetActionUseCase
import com.ray.projectpixelpet.domain.PetRepository
import com.ray.projectpixelpet.domain.PetStateCalculator
import com.ray.projectpixelpet.widget.ReminderScheduler
import com.ray.projectpixelpet.widget.WidgetUpdater
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ProjectPixelPetDatabase =
        Room.databaseBuilder(
            context,
            ProjectPixelPetDatabase::class.java,
            "project_pixelpet.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideAssetMapper(): AssetMapper = DefaultAssetMapper()

    @Provides
    @Singleton
    fun provideRepository(
        db: ProjectPixelPetDatabase,
        calculator: PetStateCalculator,
        assetMapper: AssetMapper
    ): PetRepository = DefaultPetRepository(
        petDao = db.petDao(),
        activityLogDao = db.activityLogDao(),
        calculator = calculator,
        assetMapper = assetMapper
    )

    @Provides
    @Singleton
    fun provideReminderScheduler(@ApplicationContext context: Context): ReminderScheduler =
        ReminderScheduler(context)

    @Provides
    @Singleton
    fun provideWidgetUpdater(@ApplicationContext context: Context): WidgetUpdater =
        WidgetUpdater(context)

    @Provides
    @Singleton
    fun providePetActionUseCase(
        repository: PetRepository,
        widgetUpdater: WidgetUpdater,
        reminderScheduler: ReminderScheduler
    ): PetActionUseCase = PetActionUseCase(repository, widgetUpdater, reminderScheduler)
}

