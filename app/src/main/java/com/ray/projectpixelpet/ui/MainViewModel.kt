package com.ray.projectpixelpet.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ray.projectpixelpet.domain.MeterUi
import com.ray.projectpixelpet.domain.PetAction
import com.ray.projectpixelpet.domain.PetActionUseCase
import com.ray.projectpixelpet.domain.PetDashboard
import com.ray.projectpixelpet.domain.PetRepository
import com.ray.projectpixelpet.domain.PetStage
import com.ray.projectpixelpet.domain.WidgetSizeVariant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PetRepository,
    private val petActionUseCase: PetActionUseCase
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = repository.observeDashboard(WidgetSizeVariant.WIDE)
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MainUiState())

    fun onAction(action: PetAction) {
        viewModelScope.launch {
            petActionUseCase(action)
        }
    }

    private fun PetDashboard.toUiState(): MainUiState = MainUiState(
        title = when (state.stage) {
            PetStage.EGG -> "孵化期"
            PetStage.BABY -> "幼体"
            PetStage.CHILD -> "成长期"
            PetStage.ADULT -> "成熟体"
        },
        petFrame = uiModel.petFrame,
        bubble = uiModel.moodBubble,
        meters = listOf(
            MeterUi("饱腹", state.hunger, Color(0xFFFFB347)),
            MeterUi("心情", state.mood, Color(0xFFF497B6)),
            MeterUi("清洁", state.cleanliness, Color(0xFF7BC8A4)),
            MeterUi("精力", state.energy, Color(0xFF75BFFF))
        ),
        logs = recentLogs.map { it.note }
    )
}

data class MainUiState(
    val title: String = "孵化期",
    val petFrame: String = "( .. )",
    val bubble: String = "欢迎领养你的第一只像素宠物",
    val meters: List<MeterUi> = emptyList(),
    val logs: List<String> = emptyList()
)

