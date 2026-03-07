package com.wowtanksim.model

data class TalentState(
    val points: Map<String, Int> = DEFAULT_FERAL_TANK,
) {
    companion object {
        /** Default feral tank spec: 0/44/17 */
        val DEFAULT_FERAL_TANK: Map<String, Int> = mapOf(
            // Feral Combat (47 points)
            "ferocity" to 5,
            "feral_instinct" to 3,
            "brutal_impact" to 2,
            "thick_hide" to 3,
            "feral_swiftness" to 2,
            "feral_charge" to 1,
            "sharpened_claws" to 3,
            "shredding_attacks" to 2,
            "predatory_strikes" to 3,
            "primal_fury" to 2,
            "savage_fury" to 2,
            "faerie_fire_feral" to 1,
            "heart_of_the_wild" to 5,
            "survival_of_the_fittest" to 3,
            "leader_of_the_pack" to 1,
            "improved_leader_of_the_pack" to 2,
            "predatory_instincts" to 3,
            "mangle" to 1,
            // Restoration (17 points)
            "furor" to 5,
            "naturalist" to 5,
            "natural_shapeshifter" to 3,
            "intensity" to 3,
            "omen_of_clarity" to 1,
        )
    }
    val totalPoints: Int get() = points.values.sum()

    fun pointsInTree(treeName: String): Int {
        val treeIds = TalentTrees.allTrees
            .first { it.name == treeName }
            .talents.map { it.id }.toSet()
        return points.entries.filter { it.key in treeIds }.sumOf { it.value }
    }

    // Convenience accessors for stat-relevant talents
    val survivalOfTheFittest: Int get() = points["survival_of_the_fittest"] ?: 0
    val thickHide: Int get() = points["thick_hide"] ?: 0
    val heartOfTheWild: Int get() = points["heart_of_the_wild"] ?: 0

    /** Try to add a point to the given talent. Returns new state or null if invalid. */
    fun addPoint(talentId: String): TalentState? {
        val def = TalentTrees.byId[talentId] ?: return null
        val current = points[talentId] ?: 0
        if (current >= def.maxPoints) return null
        if (totalPoints >= 61) return null

        // Row requirement: need 5 points per tier below this talent's row, within the same tree
        val tree = TalentTrees.allTrees.first { it.name == def.tree }
        val treePointsBelow = tree.talents
            .filter { it.row < def.row }
            .sumOf { points[it.id] ?: 0 }
        if (treePointsBelow < def.row * 5) return null

        // Prerequisite check
        if (def.prerequisiteId != null) {
            val prereqDef = TalentTrees.byId[def.prerequisiteId] ?: return null
            val prereqPts = points[def.prerequisiteId] ?: 0
            if (prereqPts < prereqDef.maxPoints) return null
        }

        return copy(points = points + (talentId to current + 1))
    }

    /** Try to remove a point from the given talent. Returns new state or null if invalid. */
    fun removePoint(talentId: String): TalentState? {
        val def = TalentTrees.byId[talentId] ?: return null
        val current = points[talentId] ?: 0
        if (current <= 0) return null

        val candidatePoints = points + (talentId to current - 1)

        // Check that removing this point doesn't break dependents
        val tree = TalentTrees.allTrees.first { it.name == def.tree }

        // Check prerequisite dependents: if any talent requires this one maxed, can't go below max
        val dependents = tree.talents.filter { it.prerequisiteId == talentId }
        for (dep in dependents) {
            if ((candidatePoints[dep.id] ?: 0) > 0 && (current - 1) < def.maxPoints) {
                return null
            }
        }

        // Check row requirements: talents in higher rows still need enough points in lower rows
        for (talent in tree.talents) {
            val pts = candidatePoints[talent.id] ?: 0
            if (pts <= 0) continue
            val pointsBelowRow = tree.talents
                .filter { it.row < talent.row }
                .sumOf { candidatePoints[it.id] ?: 0 }
            if (pointsBelowRow < talent.row * 5) return null
        }

        val finalPts = if (current - 1 == 0) points - talentId else points + (talentId to current - 1)
        return copy(points = finalPts)
    }

    /** Reset all talents in a specific tree. */
    fun resetTree(treeName: String): TalentState {
        val treeIds = TalentTrees.allTrees
            .first { it.name == treeName }
            .talents.map { it.id }.toSet()
        return copy(points = points.filterKeys { it !in treeIds })
    }

    /** Reset all talents. */
    fun resetAll(): TalentState = TalentState()

    /** Check if a talent is available to have points added. */
    fun canAddPoint(talentId: String): Boolean {
        val def = TalentTrees.byId[talentId] ?: return false
        val current = points[talentId] ?: 0
        if (current >= def.maxPoints) return false
        if (totalPoints >= 61) return false

        val tree = TalentTrees.allTrees.first { it.name == def.tree }
        val treePointsBelow = tree.talents
            .filter { it.row < def.row }
            .sumOf { points[it.id] ?: 0 }
        if (treePointsBelow < def.row * 5) return false

        if (def.prerequisiteId != null) {
            val prereqDef = TalentTrees.byId[def.prerequisiteId] ?: return false
            val prereqPts = points[def.prerequisiteId] ?: 0
            if (prereqPts < prereqDef.maxPoints) return false
        }
        return true
    }
}
