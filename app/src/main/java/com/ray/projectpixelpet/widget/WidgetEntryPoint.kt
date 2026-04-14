package com.ray.projectpixelpet.widget

import com.ray.projectpixelpet.domain.PetActionUseCase
import com.ray.projectpixelpet.domain.PetRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun repository(): PetRepository
    fun petActionUseCase(): PetActionUseCase
}

