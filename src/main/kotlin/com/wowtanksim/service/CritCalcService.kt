package com.wowtanksim.service

import com.wowtanksim.model.TankStats

data class CritImmunityResult(
    val baseCritChance: Double,         // 5.6% for boss 73 vs player 70
    val sotfReduction: Double,          // from Survival of the Fittest talent
    val critToReduce: Double,           // remaining crit to become immune
    val critFromDefense: Double,        // crit reduction from defense rating
    val critFromResilience: Double,     // crit reduction from resilience
    val totalCritReduction: Double,     // defense + resilience contribution
    val isCritImmune: Boolean,
    val remainingCritGap: Double,       // how much more crit reduction needed (0 if immune)
    val defenseSkill: Double,
) {
    val progressPercent: Double get() = if (critToReduce <= 0.0) 100.0
        else ((totalCritReduction / critToReduce) * 100.0).coerceIn(0.0, 100.0)
}

object CritCalcService {

    private const val BOSS_CRIT_CHANCE = 5.6 // level 73 boss vs level 70 player

    fun calculate(stats: TankStats, sotfPoints: Int): CritImmunityResult {
        val sotfReduction = sotfPoints.coerceIn(0, 3) * 1.0 // 1% per point
        val critToReduce = (BOSS_CRIT_CHANCE - sotfReduction).coerceAtLeast(0.0)
        val critFromDef = stats.critReductionFromDefense
        val critFromRes = stats.critReductionFromResilience
        val total = critFromDef + critFromRes
        val gap = (critToReduce - total).coerceAtLeast(0.0)

        return CritImmunityResult(
            baseCritChance = BOSS_CRIT_CHANCE,
            sotfReduction = sotfReduction,
            critToReduce = critToReduce,
            critFromDefense = critFromDef,
            critFromResilience = critFromRes,
            totalCritReduction = total,
            isCritImmune = total >= critToReduce,
            remainingCritGap = gap,
            defenseSkill = stats.defenseSkill,
        )
    }
}
