package com.ray.projectpixelpet

import android.app.Application
import com.ray.projectpixelpet.widget.ReminderScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ProjectPixelPetApp : Application() {

    @Inject
    lateinit var reminderScheduler: ReminderScheduler

    override fun onCreate() {
        super.onCreate()
        reminderScheduler.ensureRefreshWork()
    }
}

