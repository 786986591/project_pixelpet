package com.ray.projectpixelpet.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.FilledButton
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.ray.projectpixelpet.MainActivity
import com.ray.projectpixelpet.R
import com.ray.projectpixelpet.domain.PetAction
import com.ray.projectpixelpet.domain.WidgetSizeVariant
import dagger.hilt.android.EntryPointAccessors

class PetWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Responsive(
        setOf(
            androidx.compose.ui.unit.DpSize(140.dp, 140.dp),
            androidx.compose.ui.unit.DpSize(260.dp, 140.dp)
        )
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(context, WidgetEntryPoint::class.java)
        val dashboard = entryPoint.repository().getDashboard(WidgetSizeVariant.WIDE)

        provideContent {
            WidgetContent(
                frame = dashboard.uiModel.petFrame,
                bubble = dashboard.uiModel.moodBubble,
                summary = dashboard.uiModel.statusSummary,
                cta = dashboard.uiModel.cta,
                isWide = LocalSize.current.width >= 220.dp
            )
        }
    }
}

@Composable
private fun WidgetContent(
    frame: String,
    bubble: String,
    summary: String,
    cta: PetAction,
    isWide: Boolean
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(androidx.glance.ImageProvider(R.drawable.widget_background_light))
            .padding(16.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        Text("Pocket Tama", style = TextStyle(fontWeight = FontWeight.Bold))
        Spacer(GlanceModifier.height(8.dp))
        Text(frame, style = TextStyle(fontFamily = FontFamily.Monospace))
        Spacer(GlanceModifier.height(8.dp))
        Text(bubble, maxLines = if (isWide) 2 else 1)
        if (isWide) {
            Spacer(GlanceModifier.height(8.dp))
            Text(summary, maxLines = 2)
        }
        Spacer(GlanceModifier.height(10.dp))
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            FilledButton(
                text = actionLabel(cta),
                onClick = actionRunCallback<PetWidgetActionCallback>(
                    actionParametersOf(ActionKey to cta.name)
                )
            )
            Spacer(GlanceModifier.width(8.dp))
            FilledButton(
                text = "详情",
                onClick = actionStartActivity<MainActivity>()
            )
        }
    }
}

private val ActionKey = ActionParameters.Key<String>("pet_action")

private fun actionLabel(action: PetAction): String = when (action) {
    PetAction.FEED -> "喂食"
    PetAction.PLAY -> "玩耍"
    PetAction.CLEAN -> "清洁"
    PetAction.SLEEP -> "睡觉"
}

class PetWidgetActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val action = parameters[ActionKey]?.let(PetAction::valueOf) ?: PetAction.FEED
        val entryPoint = EntryPointAccessors.fromApplication(context, WidgetEntryPoint::class.java)
        entryPoint.petActionUseCase().invoke(action)
        PetWidget().updateAll(context)
    }
}

