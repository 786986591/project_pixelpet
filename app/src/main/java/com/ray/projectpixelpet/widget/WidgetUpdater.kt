package com.ray.projectpixelpet.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.runBlocking

class WidgetUpdater(private val context: Context) {
    fun refresh() {
        runBlocking {
            PetWidget().updateAll(context)
        }
    }
}

