package com.wowtanksim.model

enum class EnchantTier(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
}

enum class EnchantRole(val label: String) {
    TANK("Tank"),
    MELEE_DPS("Melee DPS"),
    CASTER_DPS("Caster DPS"),
    HEALER("Healer"),
}

data class EnchantOption(
    val enchant: Enchant,
    val note: String? = null,
    val tier: EnchantTier = EnchantTier.HIGH,
    val roles: Set<EnchantRole> = setOf(EnchantRole.TANK),
    val source: String = "Enchanting",
    val materials: String = "",
)

object EnchantData {

    fun enchantOptionsForSlot(slot: EquipSlot): List<EnchantOption> = when (slot) {
        EquipSlot.HEAD -> headEnchants
        EquipSlot.SHOULDER -> shoulderEnchants
        EquipSlot.BACK -> backEnchants
        EquipSlot.CHEST -> chestEnchants
        EquipSlot.WRIST -> wristEnchants
        EquipSlot.HANDS -> handsEnchants
        EquipSlot.LEGS -> legsEnchants
        EquipSlot.FEET -> feetEnchants
        EquipSlot.MAINHAND -> mainhandEnchants
        EquipSlot.RING1, EquipSlot.RING2 -> ringEnchants
        else -> emptyList()
    }

    private val TANK = EnchantRole.TANK
    private val MELEE = EnchantRole.MELEE_DPS
    private val CASTER = EnchantRole.CASTER_DPS
    private val HEALER = EnchantRole.HEALER
    private val ALL_ROLES = setOf(TANK, MELEE, CASTER, HEALER)
    private val PHYSICAL = setOf(TANK, MELEE)

    // ──────────────────────────── HEAD (Glyphs - Reputation vendors) ────────────────────────────

    private val headEnchants = listOf(
        EnchantOption(
            Enchant(id = 35452, name = "Glyph of the Defender", defenseRating = 16, dodgeRating = 17),
            note = "Revered - Keepers of Time", roles = setOf(TANK),
            source = "Reputation", materials = "100g",
        ),
        EnchantOption(
            Enchant(id = 30846, name = "Glyph of the Gladiator", stamina = 18, resilienceRating = 20),
            note = "Shattered Sun - Phase 5", roles = setOf(TANK),
            source = "Reputation", materials = "100g",
        ),
        EnchantOption(
            Enchant(id = 29186, name = "Glyph of Ferocity", attackPower = 34, hitRating = 16),
            note = "Revered - Cenarion Expedition", roles = setOf(MELEE),
            source = "Reputation", materials = "100g",
        ),
        EnchantOption(
            Enchant(id = 29191, name = "Glyph of Power", spellPower = 22, spellHitRating = 14),
            note = "Revered - Sha'tar", roles = setOf(CASTER),
            source = "Reputation", materials = "100g",
        ),
        EnchantOption(
            Enchant(id = 29189, name = "Glyph of Renewal", healingPower = 35, spellPower = 12, mp5 = 7),
            note = "Revered - Honor Hold/Thrallmar", roles = setOf(HEALER),
            source = "Reputation", materials = "100g",
        ),
    )

    // ──────────────────────────── SHOULDER (Inscriptions - Reputation vendors) ────────────────────────────

    private val shoulderEnchants = listOf(
        // Aldor
        EnchantOption(
            Enchant(id = 35417, name = "Greater Inscription of Warding", dodgeRating = 15, defenseRating = 10),
            note = "Exalted - Aldor", roles = setOf(TANK),
            source = "Reputation", materials = "Holy Dust x8",
        ),
        EnchantOption(
            Enchant(id = 35406, name = "Inscription of Warding", dodgeRating = 13),
            note = "Honored - Aldor", roles = setOf(TANK),
            source = "Reputation", materials = "Holy Dust x2", tier = EnchantTier.MEDIUM,
        ),
        EnchantOption(
            Enchant(id = 35433, name = "Greater Inscription of Vengeance", attackPower = 30, critRating = 10),
            note = "Exalted - Aldor", roles = setOf(MELEE),
            source = "Reputation", materials = "Holy Dust x8",
        ),
        EnchantOption(
            Enchant(id = 35404, name = "Inscription of Vengeance", attackPower = 26),
            note = "Honored - Aldor", roles = setOf(MELEE),
            source = "Reputation", materials = "Holy Dust x2", tier = EnchantTier.MEDIUM,
        ),
        EnchantOption(
            Enchant(id = 35436, name = "Greater Inscription of Discipline", spellPower = 18, spellCritRating = 10),
            note = "Exalted - Aldor", roles = setOf(CASTER),
            source = "Reputation", materials = "Holy Dust x8",
        ),
        EnchantOption(
            Enchant(id = 35437, name = "Greater Inscription of Faith", healingPower = 33, spellPower = 11, mp5 = 4),
            note = "Exalted - Aldor", roles = setOf(HEALER),
            source = "Reputation", materials = "Holy Dust x8",
        ),
        // Scryers
        EnchantOption(
            Enchant(id = 35439, name = "Greater Inscription of the Knight", defenseRating = 15, dodgeRating = 10),
            note = "Exalted - Scryers", roles = setOf(TANK),
            source = "Reputation", materials = "Arcane Rune x8",
        ),
        EnchantOption(
            Enchant(id = 35441, name = "Greater Inscription of the Blade", critRating = 15, attackPower = 20),
            note = "Exalted - Scryers", roles = setOf(MELEE),
            source = "Reputation", materials = "Arcane Rune x8",
        ),
        EnchantOption(
            Enchant(id = 35443, name = "Greater Inscription of the Orb", spellCritRating = 15, spellPower = 12),
            note = "Exalted - Scryers", roles = setOf(CASTER),
            source = "Reputation", materials = "Arcane Rune x8",
        ),
        EnchantOption(
            Enchant(id = 35445, name = "Greater Inscription of the Oracle", mp5 = 6, healingPower = 22),
            note = "Exalted - Scryers", roles = setOf(HEALER),
            source = "Reputation", materials = "Arcane Rune x8",
        ),
    )

    // ──────────────────────────── BACK ────────────────────────────

    private val backEnchants = listOf(
        EnchantOption(
            Enchant(id = 34004, name = "Enchant Cloak - Greater Agility", agility = 12),
            roles = PHYSICAL, source = "Enchanting",
            materials = "1 Greater Planar Essence, 4 Arcane Dust, 1 Primal Air",
        ),
        EnchantOption(
            Enchant(id = 25086, name = "Enchant Cloak - Dodge", dodgeRating = 12),
            roles = setOf(TANK), source = "Enchanting",
            materials = "3 Small Prismatic Shard, 3 Greater Planar Essence, 8 Primal Earth",
        ),
        EnchantOption(
            Enchant(id = 47051, name = "Enchant Cloak - Steelweave", defenseRating = 12),
            note = "Phase 5", roles = setOf(TANK), source = "Enchanting",
            materials = "8 Greater Planar Essence, 8 Primal Earth",
        ),
        EnchantOption(
            Enchant(id = 27961, name = "Enchant Cloak - Major Armor", armor = 120),
            roles = setOf(TANK), source = "Enchanting",
            materials = "8 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 34003, name = "Enchant Cloak - Spell Penetration", spellPenetration = 20),
            roles = setOf(CASTER), source = "Enchanting",
            materials = "2 Greater Planar Essence, 6 Arcane Dust, 2 Primal Mana",
        ),
    )

    // ──────────────────────────── CHEST ────────────────────────────

    private val chestEnchants = listOf(
        EnchantOption(
            Enchant(id = 27957, name = "Enchant Chest - Exceptional Health", stamina = 15),
            note = "+150 HP", roles = setOf(TANK), source = "Enchanting",
            materials = "8 Arcane Dust, 4 Major Healing Potion, 2 Large Brilliant Shard",
        ),
        EnchantOption(
            Enchant(id = 33990, name = "Enchant Chest - Major Resilience", resilienceRating = 15),
            roles = setOf(TANK), source = "Enchanting",
            materials = "4 Greater Planar Essence, 10 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 46594, name = "Enchant Chest - Defense", defenseRating = 15),
            roles = setOf(TANK), source = "Enchanting",
            materials = "4 Greater Planar Essence, 8 Arcane Dust, 4 Eternium Ore",
        ),
        EnchantOption(
            Enchant(id = 27960, name = "Enchant Chest - Exceptional Stats", stamina = 6, agility = 6, strength = 6, intellect = 6, spirit = 6),
            note = "+6 All Stats", roles = ALL_ROLES, source = "Enchanting",
            materials = "4 Large Prismatic Shard, 4 Greater Planar Essence, 4 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 33991, name = "Enchant Chest - Restore Mana Prime", mp5 = 6),
            roles = setOf(HEALER, CASTER), source = "Enchanting",
            materials = "2 Lesser Planar Essence, 2 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 33992, name = "Enchant Chest - Major Spirit", spirit = 15),
            roles = setOf(HEALER), source = "Enchanting",
            materials = "2 Greater Planar Essence",
        ),
    )

    // ──────────────────────────── WRIST ────────────────────────────

    private val wristEnchants = listOf(
        EnchantOption(
            Enchant(id = 27906, name = "Enchant Bracer - Major Defense", defenseRating = 12),
            roles = setOf(TANK), source = "Enchanting",
            materials = "2 Small Prismatic Shard, 10 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 27914, name = "Enchant Bracer - Fortitude", stamina = 12),
            roles = setOf(TANK), source = "Enchanting",
            materials = "1 Large Prismatic Shard, 10 Greater Planar Essence, 20 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 27905, name = "Enchant Bracer - Stats", stamina = 4, agility = 4, strength = 4, intellect = 4, spirit = 4),
            note = "+4 All Stats", roles = ALL_ROLES, source = "Enchanting",
            materials = "6 Arcane Dust, 6 Lesser Planar Essence",
        ),
        EnchantOption(
            Enchant(id = 34002, name = "Enchant Bracer - Assault", attackPower = 24),
            roles = setOf(MELEE), source = "Enchanting",
            materials = "6 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 27899, name = "Enchant Bracer - Brawn", strength = 12),
            roles = setOf(MELEE), source = "Enchanting",
            materials = "6 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 27917, name = "Enchant Bracer - Spellpower", spellPower = 15),
            roles = setOf(CASTER), source = "Enchanting",
            materials = "6 Large Prismatic Shard, 6 Primal Fire, 6 Primal Water",
        ),
        EnchantOption(
            Enchant(id = 27911, name = "Enchant Bracer - Superior Healing", healingPower = 30, spellPower = 10),
            roles = setOf(HEALER), source = "Enchanting",
            materials = "4 Greater Planar Essence, 4 Primal Life",
        ),
        EnchantOption(
            Enchant(id = 27913, name = "Enchant Bracer - Restore Mana Prime", mp5 = 6),
            roles = setOf(HEALER, CASTER), source = "Enchanting",
            materials = "8 Greater Planar Essence",
        ),
        EnchantOption(
            Enchant(id = 34001, name = "Enchant Bracer - Major Intellect", intellect = 12),
            roles = setOf(CASTER, HEALER), source = "Enchanting",
            materials = "3 Lesser Planar Essence",
        ),
    )

    // ──────────────────────────── HANDS ────────────────────────────

    private val handsEnchants = listOf(
        EnchantOption(
            Enchant(id = 25080, name = "Enchant Gloves - Superior Agility", agility = 15),
            roles = PHYSICAL, source = "Enchanting",
            materials = "3 Small Prismatic Shard, 3 Greater Planar Essence, 2 Primal Air",
        ),
        EnchantOption(
            Enchant(id = 33996, name = "Enchant Gloves - Assault", attackPower = 26),
            roles = setOf(MELEE), source = "Enchanting",
            materials = "8 Arcane Dust, 2 Arcane Crystal",
        ),
        EnchantOption(
            Enchant(id = 33995, name = "Enchant Gloves - Major Strength", strength = 15),
            roles = setOf(MELEE), source = "Enchanting",
            materials = "12 Arcane Dust, 1 Greater Planar Essence",
        ),
        EnchantOption(
            Enchant(id = 33994, name = "Enchant Gloves - Spell Strike", spellHitRating = 15),
            roles = setOf(CASTER), source = "Enchanting",
            materials = "2 Large Prismatic Shard, 8 Greater Planar Essence, 2 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 33993, name = "Enchant Gloves - Blasting", spellCritRating = 10),
            roles = setOf(CASTER), source = "Enchanting",
            materials = "1 Lesser Planar Essence, 4 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 33997, name = "Enchant Gloves - Major Spellpower", spellPower = 20),
            roles = setOf(CASTER), source = "Enchanting",
            materials = "6 Large Prismatic Shard, 6 Greater Planar Essence, 6 Primal Mana",
        ),
        EnchantOption(
            Enchant(id = 33999, name = "Enchant Gloves - Major Healing", healingPower = 35, spellPower = 12),
            roles = setOf(HEALER), source = "Enchanting",
            materials = "6 Large Prismatic Shard, 6 Greater Planar Essence, 6 Primal Life",
        ),
        EnchantOption(
            Enchant(id = 32399, name = "Glove Reinforcements", armor = 240),
            note = "Leatherworking", roles = setOf(TANK),
            source = "Leatherworking",
            materials = "4 Heavy Knothide Leather, 3 Primal Earth, 1 Rune Thread",
        ),
    )

    // ──────────────────────────── LEGS ────────────────────────────

    private val legsEnchants = listOf(
        // Leatherworking leg armors
        EnchantOption(
            Enchant(id = 35490, name = "Nethercleft Leg Armor", stamina = 40, agility = 12),
            roles = setOf(TANK), source = "Leatherworking",
            materials = "4 Heavy Knothide Leather, 16 Thick Clefthoof Leather, 8 Primal Earth, 1 Primal Nether",
        ),
        EnchantOption(
            Enchant(id = 35489, name = "Clefthide Leg Armor", stamina = 30, agility = 10),
            roles = setOf(TANK), source = "Leatherworking", tier = EnchantTier.MEDIUM,
            materials = "4 Heavy Knothide Leather, 8 Thick Clefthoof Leather, 4 Primal Earth",
        ),
        EnchantOption(
            Enchant(id = 29536, name = "Nethercobra Leg Armor", attackPower = 50, critRating = 12),
            roles = setOf(MELEE), source = "Leatherworking",
            materials = "4 Heavy Knothide Leather, 4 Cobra Scales, 8 Primal Air, 1 Primal Nether",
        ),
        EnchantOption(
            Enchant(id = 29535, name = "Cobrahide Leg Armor", attackPower = 40, critRating = 10),
            roles = setOf(MELEE), source = "Leatherworking", tier = EnchantTier.MEDIUM,
            materials = "4 Heavy Knothide Leather, 2 Cobra Scales, 4 Primal Air",
        ),
        // Tailoring spellthreads
        EnchantOption(
            Enchant(id = 36316, name = "Runic Spellthread", spellPower = 35, stamina = 20),
            roles = setOf(CASTER), source = "Tailoring",
            materials = "10 Primal Mana, 1 Primal Nether, 1 Rune Thread",
        ),
        EnchantOption(
            Enchant(id = 36315, name = "Mystic Spellthread", spellPower = 25, stamina = 15),
            roles = setOf(CASTER), source = "Tailoring", tier = EnchantTier.MEDIUM,
            materials = "5 Primal Mana, 1 Rune Thread",
        ),
        EnchantOption(
            Enchant(id = 36318, name = "Golden Spellthread", healingPower = 66, spellPower = 22, stamina = 20),
            roles = setOf(HEALER), source = "Tailoring",
            materials = "10 Primal Life, 1 Primal Nether, 1 Rune Thread",
        ),
        EnchantOption(
            Enchant(id = 36317, name = "Silver Spellthread", healingPower = 46, spellPower = 16, stamina = 15),
            roles = setOf(HEALER), source = "Tailoring", tier = EnchantTier.MEDIUM,
            materials = "5 Primal Life, 1 Rune Thread",
        ),
    )

    // ──────────────────────────── FEET ────────────────────────────

    private val feetEnchants = listOf(
        EnchantOption(
            Enchant(id = 34008, name = "Enchant Boots - Boar's Speed", stamina = 9),
            note = "+Minor Run Speed", roles = setOf(TANK), source = "Enchanting",
            materials = "8 Large Prismatic Shard, 8 Primal Earth",
        ),
        EnchantOption(
            Enchant(id = 34007, name = "Enchant Boots - Cat's Swiftness", agility = 6),
            note = "+Minor Run Speed", roles = PHYSICAL, source = "Enchanting",
            materials = "8 Large Prismatic Shard, 8 Primal Air",
        ),
        EnchantOption(
            Enchant(id = 27951, name = "Enchant Boots - Dexterity", agility = 12),
            roles = PHYSICAL, source = "Enchanting",
            materials = "8 Greater Planar Essence, 8 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 27950, name = "Enchant Boots - Fortitude", stamina = 12),
            roles = setOf(TANK), source = "Enchanting",
            materials = "12 Arcane Dust, 4 Arcane Crystal",
        ),
        EnchantOption(
            Enchant(id = 27954, name = "Enchant Boots - Surefooted", hitRating = 10),
            note = "+5% Snare/Root Resist", roles = setOf(MELEE), source = "Enchanting",
            materials = "2 Void Crystal, 4 Large Prismatic Shard, 1 Primal Nether",
        ),
        EnchantOption(
            Enchant(id = 27948, name = "Enchant Boots - Vitality", mp5 = 4),
            note = "+4 HP5, +4 MP5", roles = setOf(HEALER, CASTER), source = "Enchanting",
            materials = "6 Arcane Dust, 4 Major Healing Potion, 4 Major Mana Potion",
        ),
    )

    // ──────────────────────────── WEAPON ────────────────────────────

    private val mainhandEnchants = listOf(
        EnchantOption(
            Enchant(id = 28004, name = "Enchant Weapon - Mongoose", agility = 25),
            note = "Proc: +120 Agi, +2% Haste", roles = PHYSICAL, source = "Enchanting",
            materials = "10 Large Prismatic Shard, 6 Void Crystal, 8 Greater Planar Essence, 40 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 27977, name = "Enchant 2H Weapon - Major Agility", agility = 35),
            roles = PHYSICAL, source = "Enchanting",
            materials = "8 Large Prismatic Shard, 6 Greater Planar Essence, 20 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 27971, name = "Enchant 2H Weapon - Savagery", attackPower = 70),
            roles = setOf(MELEE), source = "Enchanting",
            materials = "4 Large Prismatic Shard, 40 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 42974, name = "Enchant Weapon - Executioner"),
            note = "Proc: Ignore 840 Armor. Phase 4+", roles = setOf(MELEE), source = "Enchanting",
            materials = "10 Large Prismatic Shard, 6 Void Crystal, 6 Greater Planar Essence, 30 Arcane Dust, 3 Elixir of Major Strength",
        ),
        EnchantOption(
            Enchant(id = 42620, name = "Enchant Weapon - Greater Agility", agility = 20),
            roles = PHYSICAL, source = "Enchanting",
            materials = "6 Large Prismatic Shard, 4 Greater Planar Essence, 8 Arcane Dust, 2 Primal Air",
        ),
        EnchantOption(
            Enchant(id = 27972, name = "Enchant Weapon - Potency", strength = 20),
            roles = setOf(MELEE), source = "Enchanting",
            materials = "4 Large Prismatic Shard, 5 Greater Planar Essence, 20 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 27975, name = "Enchant Weapon - Major Spellpower", spellPower = 40),
            roles = setOf(CASTER), source = "Enchanting",
            materials = "8 Large Prismatic Shard, 8 Greater Planar Essence",
        ),
        EnchantOption(
            Enchant(id = 34010, name = "Enchant Weapon - Major Healing", healingPower = 81, spellPower = 27),
            roles = setOf(HEALER), source = "Enchanting",
            materials = "8 Large Prismatic Shard, 8 Primal Water, 8 Primal Life",
        ),
        EnchantOption(
            Enchant(id = 27981, name = "Enchant Weapon - Sunfire", spellPower = 50),
            note = "Arcane/Fire only", roles = setOf(CASTER), source = "Enchanting",
            materials = "8 Large Prismatic Shard, 3 Primal Fire, 3 Primal Mana",
        ),
        EnchantOption(
            Enchant(id = 27982, name = "Enchant Weapon - Soulfrost", spellPower = 54),
            note = "Frost/Shadow only", roles = setOf(CASTER), source = "Enchanting",
            materials = "8 Large Prismatic Shard, 3 Primal Water, 3 Primal Shadow",
        ),
        EnchantOption(
            Enchant(id = 28003, name = "Enchant Weapon - Spellsurge"),
            note = "Proc: 100 Mana to party", roles = setOf(CASTER, HEALER), source = "Enchanting",
            materials = "8 Large Prismatic Shard, 12 Greater Planar Essence, 6 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 28004, name = "Enchant Weapon - Battlemaster"),
            note = "Proc: 180-300 healing to party", roles = setOf(TANK), source = "Enchanting",
            materials = "8 Void Crystal, 8 Large Prismatic Shard, 2 Primal Water",
        ),
    )

    // ──────────────────────────── RINGS (Enchanting only) ────────────────────────────

    private val ringEnchants = listOf(
        EnchantOption(
            Enchant(id = 27927, name = "Enchant Ring - Stats", stamina = 4, agility = 4, strength = 4, intellect = 4, spirit = 4),
            note = "Enchanter only", roles = ALL_ROLES, source = "Enchanting",
            materials = "2 Void Crystal, 2 Large Prismatic Shard",
        ),
        EnchantOption(
            Enchant(id = 27920, name = "Enchant Ring - Spellpower", spellPower = 12),
            note = "Enchanter only", roles = setOf(CASTER), source = "Enchanting",
            materials = "2 Large Prismatic Shard, 2 Greater Planar Essence",
        ),
        EnchantOption(
            Enchant(id = 27926, name = "Enchant Ring - Healing Power", healingPower = 20, spellPower = 7),
            note = "Enchanter only", roles = setOf(HEALER), source = "Enchanting",
            materials = "2 Large Prismatic Shard, 3 Greater Planar Essence, 5 Arcane Dust",
        ),
        EnchantOption(
            Enchant(id = 27924, name = "Enchant Ring - Striking", attackPower = 2),
            note = "Enchanter only", roles = setOf(MELEE), source = "Enchanting",
            materials = "2 Large Prismatic Shard, 6 Arcane Dust",
        ),
    )
}
