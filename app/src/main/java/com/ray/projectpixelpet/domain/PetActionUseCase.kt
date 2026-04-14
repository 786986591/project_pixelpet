package com.ray.projectpixelpet.domain

import com.ray.projectpixelpet.widget.ReminderScheduler
import com.ray.projectpixelpet.widget.WidgetUpdater

class PetActionUseCase(
    private val repository: PetRepository,
    private val widgetUpdater: WidgetUpdater,
    private val reminderScheduler: ReminderScheduler
) {
    suspend operator fun invoke(action: PetAction) {
        repository.performAction(action)
        widgetUpdater.refresh()
        reminderScheduler.ensureRefreshWork()
    }
}

