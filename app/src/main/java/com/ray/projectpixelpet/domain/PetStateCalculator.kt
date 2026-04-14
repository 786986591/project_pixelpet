package com.ray.projectpixelpet.domain

import javax.inject.Inject
import kotlin.math.max

class PetStateCalculator @Inject constructor() {

    fun derive(current: PetState, now: Long): PetState {
        val minutesElapsed = max(0L, (now - current.lastUpdatedAt) / 60_000L).toInt()
        if (minutesElapsed == 0) return current

        val updated = current.copy(
            hunger = (current.hunger - (minutesElapsed / 24)).coerceIn(0, 100),
            mood = (current.mood - (minutesElapsed / 30)).coerceIn(0, 100),
            cleanliness = (current.cleanliness - (minutesElapsed / 28)).coerceIn(0, 100),
            energy = (current.energy - (minutesElapsed / 20)).coerceIn(0, 100),
            lastUpdatedAt = now
        )
        return updated.copy(stage = stageForBond(updated.bondExp))
    }

    fun applyAction(current: PetState, action: PetAction, now: Long): PetState {
        val normalized = derive(current, now)
        val boosted = when (action) {
            PetAction.FEED -> normalized.copy(
                hunger = (normalized.hunger + 28).coerceAtMost(100),
                cleanliness = (normalized.cleanliness - 4).coerceAtLeast(0),
                bondExp = normalized.bondExp + 8
            )
            PetAction.PLAY -> normalized.copy(
                mood = (normalized.mood + 24).coerceAtMost(100),
                energy = (normalized.energy - 10).coerceAtLeast(0),
                hunger = (normalized.hunger - 6).coerceAtLeast(0),
                bondExp = normalized.bondExp + 10
            )
            PetAction.CLEAN -> normalized.copy(
                cleanliness = (normalized.cleanliness + 35).coerceAtMost(100),
                mood = (normalized.mood + 6).coerceAtMost(100),
                bondExp = normalized.bondExp + 6
            )
            PetAction.SLEEP -> normalized.copy(
                energy = 100,
                mood = (normalized.mood + 8).coerceAtMost(100),
                lastSleptAt = now,
                bondExp = normalized.bondExp + 5
            )
        }
        return boosted.copy(stage = stageForBond(boosted.bondExp), lastUpdatedAt = now)
    }

    private fun stageForBond(bondExp: Int): PetStage = when {
        bondExp < 20 -> PetStage.EGG
        bondExp < 60 -> PetStage.BABY
        bondExp < 140 -> PetStage.CHILD
        else -> PetStage.ADULT
    }
}

