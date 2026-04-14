package com.ray.projectpixelpet.domain

import com.ray.projectpixelpet.data.local.ActivityLogDao
import com.ray.projectpixelpet.data.local.ActivityLogEntity
import com.ray.projectpixelpet.data.local.PetDao
import com.ray.projectpixelpet.data.local.PetEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

interface PetRepository {
    fun observeDashboard(sizeVariant: WidgetSizeVariant = WidgetSizeVariant.WIDE): Flow<PetDashboard>
    suspend fun getDashboard(sizeVariant: WidgetSizeVariant = WidgetSizeVariant.WIDE): PetDashboard
    suspend fun performAction(action: PetAction): PetDashboard
    suspend fun refresh()
}

@Singleton
class DefaultPetRepository @Inject constructor(
    private val petDao: PetDao,
    private val activityLogDao: ActivityLogDao,
    private val calculator: PetStateCalculator,
    private val assetMapper: AssetMapper
) : PetRepository {

    override fun observeDashboard(sizeVariant: WidgetSizeVariant): Flow<PetDashboard> {
        return combine(
            petDao.observePet(),
            activityLogDao.observeRecent()
        ) { entity, logs ->
            val state = entity?.toDomain()?.let { calculator.derive(it, now()) } ?: defaultState()
            PetDashboard(
                state = state,
                uiModel = assetMapper.toWidgetModel(state, sizeVariant),
                recentLogs = logs.map { it.toDomain() }
            )
        }
    }

    override suspend fun getDashboard(sizeVariant: WidgetSizeVariant): PetDashboard {
        val state = loadOrCreateDerived()
        return PetDashboard(
            state = state,
            uiModel = assetMapper.toWidgetModel(state, sizeVariant),
            recentLogs = activityLogDao.observeRecent().first().map { it.toDomain() }
        )
    }

    override suspend fun performAction(action: PetAction): PetDashboard {
        val now = now()
        val updated = calculator.applyAction(loadOrCreateDerived(), action, now)
        petDao.upsert(updated.toEntity())
        activityLogDao.insert(
            ActivityLogEntity(
                action = action.name,
                createdAt = now,
                note = actionLabel(action)
            )
        )
        return getDashboard()
    }

    override suspend fun refresh() {
        petDao.upsert(loadOrCreateDerived().toEntity())
    }

    private suspend fun loadOrCreateDerived(): PetState {
        val existing = petDao.getPet()?.toDomain() ?: defaultState()
        val derived = calculator.derive(existing, now())
        petDao.upsert(derived.toEntity())
        return derived
    }

    private fun actionLabel(action: PetAction): String = when (action) {
        PetAction.FEED -> "喂食成功，宠物更有精神了"
        PetAction.PLAY -> "玩耍完成，亲密度提升"
        PetAction.CLEAN -> "清洁完成，闪闪发亮"
        PetAction.SLEEP -> "睡眠补满，准备继续冒险"
    }

    private fun defaultState(now: Long = now()) = PetState(
        stage = PetStage.EGG,
        hunger = 72,
        mood = 68,
        cleanliness = 74,
        energy = 64,
        bondExp = 0,
        lastUpdatedAt = now,
        lastSleptAt = now
    )

    private fun PetEntity.toDomain() = PetState(
        petId = petId,
        userId = userId,
        cloudSyncToken = cloudSyncToken,
        stage = PetStage.valueOf(stage),
        hunger = hunger,
        mood = mood,
        cleanliness = cleanliness,
        energy = energy,
        bondExp = bondExp,
        lastUpdatedAt = lastUpdatedAt,
        lastSleptAt = lastSleptAt
    )

    private fun PetState.toEntity() = PetEntity(
        petId = petId,
        userId = userId,
        cloudSyncToken = cloudSyncToken,
        stage = stage.name,
        hunger = hunger,
        mood = mood,
        cleanliness = cleanliness,
        energy = energy,
        bondExp = bondExp,
        lastUpdatedAt = lastUpdatedAt,
        lastSleptAt = lastSleptAt
    )

    private fun ActivityLogEntity.toDomain() = ActivityLog(
        id = id,
        action = PetAction.valueOf(action),
        createdAt = createdAt,
        note = note
    )

    private fun now(): Long = System.currentTimeMillis()
}

