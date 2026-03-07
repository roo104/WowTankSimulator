package com.wowtanksim.model

data class EnchantOption(
    val enchant: Enchant,
    val note: String? = null,
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

    private val headEnchants = listOf(
        EnchantOption(
            Enchant(id = 35452, name = "Glyph of the Defender", defenseRating = 16, dodgeRating = 17),
            note = "Revered - Keepers of Time",
        ),
        EnchantOption(
            Enchant(id = 30846, name = "Glyph of the Gladiator", stamina = 18, resilienceRating = 20),
            note = "Honor vendor",
        ),
        EnchantOption(
            Enchant(id = 29186, name = "Glyph of Ferocity", attackPower = 34, hitRating = 16),
            note = "Revered - Cenarion Expedition",
        ),
    )

    private val shoulderEnchants = listOf(
        EnchantOption(
            Enchant(id = 35417, name = "Greater Inscription of Warding", dodgeRating = 15, defenseRating = 10),
            note = "Exalted - Aldor",
        ),
        EnchantOption(
            Enchant(id = 35439, name = "Greater Inscription of the Knight", defenseRating = 15, dodgeRating = 10),
            note = "Exalted - Scryers",
        ),
        EnchantOption(
            Enchant(id = 35406, name = "Inscription of Warding", dodgeRating = 13),
            note = "Honored - Aldor",
        ),
        EnchantOption(
            Enchant(id = 35433, name = "Greater Inscription of the Blade", attackPower = 20, critRating = 15),
            note = "Exalted - Aldor",
        ),
    )

    private val backEnchants = listOf(
        EnchantOption(
            Enchant(id = 34004, name = "Enchant Cloak - Greater Agility", agility = 12),
        ),
        EnchantOption(
            Enchant(id = 25086, name = "Enchant Cloak - Dodge", dodgeRating = 12),
        ),
        EnchantOption(
            Enchant(id = 47051, name = "Enchant Cloak - Steelweave", defenseRating = 12),
        ),
        EnchantOption(
            Enchant(id = 27962, name = "Enchant Cloak - Major Armor", armor = 120),
        ),
    )

    private val chestEnchants = listOf(
        EnchantOption(
            Enchant(id = 27957, name = "Enchant Chest - Exceptional Health", stamina = 15),
            note = "+150 HP",
        ),
        EnchantOption(
            Enchant(id = 33990, name = "Enchant Chest - Major Resilience", resilienceRating = 15),
        ),
        EnchantOption(
            Enchant(id = 46594, name = "Enchant Chest - Defense", defenseRating = 15),
        ),
        EnchantOption(
            Enchant(id = 27960, name = "Enchant Chest - Exceptional Stats", stamina = 6, agility = 6, strength = 6, intellect = 6, spirit = 6),
            note = "+6 All Stats",
        ),
    )

    private val wristEnchants = listOf(
        EnchantOption(
            Enchant(id = 27906, name = "Enchant Bracer - Major Defense", defenseRating = 12),
        ),
        EnchantOption(
            Enchant(id = 27899, name = "Enchant Bracer - Brawn", strength = 12),
        ),
        EnchantOption(
            Enchant(id = 34002, name = "Enchant Bracer - Assault", attackPower = 24),
        ),
        EnchantOption(
            Enchant(id = 27914, name = "Enchant Bracer - Fortitude", stamina = 12),
        ),
    )

    private val handsEnchants = listOf(
        EnchantOption(
            Enchant(id = 33996, name = "Enchant Gloves - Assault", attackPower = 26),
        ),
        EnchantOption(
            Enchant(id = 25080, name = "Enchant Gloves - Superior Agility", agility = 15),
        ),
        EnchantOption(
            Enchant(id = 32399, name = "Enchant Gloves - Major Agility (Clefthide)", agility = 15),
            note = "Leatherworking",
        ),
        EnchantOption(
            Enchant(id = 33994, name = "Enchant Gloves - Precision", hitRating = 15),
        ),
    )

    private val legsEnchants = listOf(
        EnchantOption(
            Enchant(id = 35490, name = "Nethercleft Leg Armor", stamina = 40, agility = 12),
        ),
        EnchantOption(
            Enchant(id = 35489, name = "Clefthide Leg Armor", stamina = 30, agility = 10),
        ),
        EnchantOption(
            Enchant(id = 29535, name = "Cobrahide Leg Armor", attackPower = 40, critRating = 10),
        ),
        EnchantOption(
            Enchant(id = 29536, name = "Nethercobra Leg Armor", attackPower = 50, critRating = 12),
        ),
    )

    private val feetEnchants = listOf(
        EnchantOption(
            Enchant(id = 34008, name = "Enchant Boots - Boar's Speed", stamina = 9),
            note = "+Minor Run Speed",
        ),
        EnchantOption(
            Enchant(id = 34007, name = "Enchant Boots - Cat's Swiftness", agility = 6),
            note = "+Minor Run Speed",
        ),
        EnchantOption(
            Enchant(id = 27951, name = "Enchant Boots - Dexterity", agility = 12),
        ),
        EnchantOption(
            Enchant(id = 27950, name = "Enchant Boots - Fortitude", stamina = 12),
        ),
    )

    private val mainhandEnchants = listOf(
        EnchantOption(
            Enchant(id = 27971, name = "Enchant 2H Weapon - Savagery", attackPower = 70),
        ),
        EnchantOption(
            Enchant(id = 27977, name = "Enchant 2H Weapon - Major Agility", agility = 35),
        ),
        EnchantOption(
            Enchant(id = 28004, name = "Enchant Weapon - Mongoose", agility = 25),
            note = "Proc: +120 Agi/Haste",
        ),
        EnchantOption(
            Enchant(id = 42974, name = "Enchant Weapon - Executioner", expertiseRating = 0),
            note = "Proc: +120 ArPen",
        ),
    )

    private val ringEnchants = listOf(
        EnchantOption(
            Enchant(id = 27927, name = "Enchant Ring - Stats", stamina = 4, agility = 4, strength = 4, intellect = 4, spirit = 4),
            note = "Enchanter only",
        ),
    )
}
