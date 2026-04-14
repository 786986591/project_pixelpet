package com.ray.projectpixelpet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ray.projectpixelpet.domain.MeterUi
import com.ray.projectpixelpet.domain.PetAction

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Pocket Tama")
                        Text(
                            text = "桌面常驻的像素宠物原型",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { HeroCard(state.title, state.petFrame, state.bubble) }
            item { ActionRow(onAction = viewModel::onAction) }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    state.meters.forEach { meter -> MeterCard(meter) }
                }
            }
            item { HintCard() }
            item {
                Text(
                    text = "今日互动",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            if (state.logs.isEmpty()) {
                item { Text("还没有互动记录，先喂食看看。") }
            } else {
                items(state.logs) { log -> LogCard(log) }
            }
            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun HeroCard(title: String, petFrame: String, bubble: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = petFrame,
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
            Text(bubble, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun ActionRow(onAction: (PetAction) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        ActionChip("喂食", Icons.Outlined.Fastfood) { onAction(PetAction.FEED) }
        ActionChip("玩耍", Icons.Outlined.SportsEsports) { onAction(PetAction.PLAY) }
        ActionChip("清洁", Icons.Outlined.CleaningServices) { onAction(PetAction.CLEAN) }
        ActionChip("睡觉", Icons.Outlined.NightsStay) { onAction(PetAction.SLEEP) }
    }
}

@Composable
private fun ActionChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = label) }
    )
}

@Composable
private fun MeterCard(meter: MeterUi) {
    Card(shape = RoundedCornerShape(22.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = meter.color)
                Spacer(Modifier.width(8.dp))
                Text("${meter.label} ${meter.value}", style = MaterialTheme.typography.titleMedium)
            }
            LinearProgressIndicator(
                progress = { meter.value / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = meter.color
            )
        }
    }
}

@Composable
private fun HintCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        shape = RoundedCornerShape(22.dp)
    ) {
        Text(
            text = "状态会根据你上次互动后的时间自动衰减。长按桌面并添加 Pocket Tama Widget，就能把它常驻在主屏。",
            modifier = Modifier.padding(18.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LogCard(log: String) {
    Card(shape = RoundedCornerShape(18.dp)) {
        Text(
            text = log,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

