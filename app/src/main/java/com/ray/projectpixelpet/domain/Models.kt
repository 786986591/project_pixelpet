package com.ray.projectpixelpet.domain

import androidx.compose.ui.graphics.Color

enum class PetStage {
    EGG,
    BABY,
    CHILD,
    ADULT
}

enum class PetAction {
    FEED,
    PLAY,
    CLEAN,
    SLEEP
}

enum class WidgetSizeVariant {
    SMALL,
    WIDE
}

data class PetState(
    val petId: String = "main_pet",
    val userId: String = "local_user",
    val cloudSyncToken: String? = null,
    val stage: PetStage,
    val hunger: Int,
    val mood: Int,
    val cleanliness: Int,
    val energy: Int,
    val bondExp: Int,
    val lastUpdatedAt: Long,
    val lastSleptAt: Long
)

data class ActivityLog(
    val id: Long,
    val action: PetAction,
    val createdAt: Long,
    val note: String
)

data class WidgetUiModel(
    val petFrame: String,
    val moodBubble: String,
    val statusSummary: String,
    val cta: PetAction,
    val sizeVariant: WidgetSizeVariant,
    val accentColor: Long
)

data class PetDashboard(
    val state: PetState,
    val uiModel: WidgetUiModel,
    val recentLogs: List<ActivityLog>
)

data class MeterUi(
    val label: String,
    val value: Int,
    val color: Color
)

