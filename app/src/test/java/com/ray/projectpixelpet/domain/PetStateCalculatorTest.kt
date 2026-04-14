package com.ray.projectpixelpet.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PetStateCalculatorTest {

    private val calculator = PetStateCalculator()

    @Test
    fun derive_reduces_stats_over_time() {
        val initial = PetState(
            stage = PetStage.BABY,
            hunger = 100,
            mood = 100,
            cleanliness = 100,
            energy = 100,
            bondExp = 30,
            lastUpdatedAt = 0L,
            lastSleptAt = 0L
        )

        val updated = calculator.derive(initial, 60L * 60L * 1000L)

        assertThat(updated.hunger).isLessThan(100)
        assertThat(updated.energy).isLessThan(100)
    }

    @Test
    fun applyAction_promotes_stage_when_bond_threshold_crossed() {
        val initial = PetState(
            stage = PetStage.BABY,
            hunger = 40,
            mood = 40,
            cleanliness = 40,
            energy = 40,
            bondExp = 58,
            lastUpdatedAt = 0L,
            lastSleptAt = 0L
        )

        val updated = calculator.applyAction(initial, PetAction.PLAY, 0L)

        assertThat(updated.stage).isEqualTo(PetStage.CHILD)
    }
}

