package com.ray.projectpixelpet.domain

interface AssetMapper {
    fun toWidgetModel(state: PetState, sizeVariant: WidgetSizeVariant): WidgetUiModel
}

class DefaultAssetMapper : AssetMapper {
    override fun toWidgetModel(state: PetState, sizeVariant: WidgetSizeVariant): WidgetUiModel {
        val primaryAction = listOf(
            PetAction.FEED to state.hunger,
            PetAction.PLAY to state.mood,
            PetAction.CLEAN to state.cleanliness,
            PetAction.SLEEP to state.energy
        ).minBy { it.second }.first

        val petFrame = when (state.stage) {
            PetStage.EGG -> "( .. )"
            PetStage.BABY -> if (state.mood < 40) "( ; ;)" else "( ^.^)"
            PetStage.CHILD -> if (state.energy < 35) "( -.-)" else "( o.o)"
            PetStage.ADULT -> if (state.cleanliness < 35) "( > <)" else "( uwu)"
        }
        val bubble = when {
            state.hunger < 35 -> "肚子咕咕叫"
            state.energy < 35 -> "想睡一会"
            state.cleanliness < 35 -> "需要洗香香"
            state.mood < 35 -> "陪我玩一下"
            else -> "今天状态不错"
        }
        return WidgetUiModel(
            petFrame = petFrame,
            moodBubble = bubble,
            statusSummary = "饱腹${state.hunger} / 心情${state.mood} / 清洁${state.cleanliness} / 精力${state.energy}",
            cta = primaryAction,
            sizeVariant = sizeVariant,
            accentColor = when (state.stage) {
                PetStage.EGG -> 0xFFFFE6A8
                PetStage.BABY -> 0xFFFFB347
                PetStage.CHILD -> 0xFF75BFFF
                PetStage.ADULT -> 0xFF7BC8A4
            }
        )
    }
}

