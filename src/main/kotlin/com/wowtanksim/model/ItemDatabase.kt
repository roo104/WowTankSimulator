package com.wowtanksim.model

enum class ContentTier(val displayName: String, val maxIlvl: Int) {
    T4("T4", 120),
    T5("T5", 133),
    T6("T6", Int.MAX_VALUE),
}

data class ItemOption(
    val item: Item,
    val source: String = "",
)

object ItemDatabase {

    fun itemsForSlot(slot: EquipSlot): List<ItemOption> = when (slot) {
        EquipSlot.HEAD -> headItems
        EquipSlot.NECK -> neckItems
        EquipSlot.SHOULDER -> shoulderItems
        EquipSlot.BACK -> backItems
        EquipSlot.CHEST -> chestItems
        EquipSlot.WRIST -> wristItems
        EquipSlot.HANDS -> handsItems
        EquipSlot.WAIST -> waistItems
        EquipSlot.LEGS -> legsItems
        EquipSlot.FEET -> feetItems
        EquipSlot.RING1, EquipSlot.RING2 -> ringItems
        EquipSlot.TRINKET1, EquipSlot.TRINKET2 -> trinketItems
        EquipSlot.MAINHAND -> weaponItems
        EquipSlot.IDOL -> idolItems
    }

    private val headItems = listOf(
        ItemOption(
            Item(id = 29098, name = "Stag-Helm of Malorne", icon = "inv_helmet_15", quality = ItemQuality.EPIC, ilvl = 120, stamina = 30, agility = 33, strength = 41, armor = 490, socketTypes = listOf(GemColor.META, GemColor.YELLOW), setId = 640),
            source = "T4 - Prince Malchezaar",
        ),
        ItemOption(
            Item(id = 30228, name = "Nordrassil Headdress", icon = "inv_helmet_15", quality = ItemQuality.EPIC, ilvl = 133, stamina = 54, agility = 34, strength = 20, armor = 363, defenseRating = 25, socketTypes = listOf(GemColor.META, GemColor.BLUE), setId = 651),
            source = "T5 - Lady Vashj",
        ),
        ItemOption(
            Item(id = 31039, name = "Thunderheart Cover", icon = "inv_helmet_94", quality = ItemQuality.EPIC, ilvl = 146, stamina = 67, agility = 42, strength = 28, armor = 392, defenseRating = 29, dodgeRating = 23, socketTypes = listOf(GemColor.META, GemColor.RED), setId = 652),
            source = "T6 - Archimonde",
        ),
        ItemOption(
            Item(id = 32235, name = "Cursed Vision of Sargeras", icon = "inv_misc_bandana_03", quality = ItemQuality.EPIC, ilvl = 151, stamina = 52, agility = 52, armor = 296, hitRating = 21, expertiseRating = 24),
            source = "Illidan Stormrage",
        ),
    )

    private val neckItems = listOf(
        ItemOption(
            Item(id = 28509, name = "Worgen Claw Necklace", icon = "inv_jewelry_necklace_22", quality = ItemQuality.EPIC, ilvl = 115, stamina = 21, agility = 20, hitRating = 17, attackPower = 42),
            source = "Karazhan - Attumen",
        ),
        ItemOption(
            Item(id = 29381, name = "Choker of Vile Intent", icon = "inv_jewelry_necklace_04", quality = ItemQuality.EPIC, ilvl = 110, stamina = 18, agility = 20, hitRating = 18, attackPower = 42),
            source = "Badge of Justice (25)",
        ),
        ItemOption(
            Item(id = 30022, name = "Pendant of the Perilous", icon = "inv_jewelry_necklace_36", quality = ItemQuality.EPIC, ilvl = 128, stamina = 33, agility = 24, dodgeRating = 20),
            source = "Serpentshrine Cavern",
        ),
        ItemOption(
            Item(id = 33281, name = "Brooch of Deftness", icon = "inv_jewelry_necklace_36", quality = ItemQuality.EPIC, ilvl = 141, stamina = 27, agility = 30, hitRating = 22, expertiseRating = 20),
            source = "Badge of Justice (35)",
        ),
        ItemOption(
            Item(id = 32362, name = "Pendant of Titans", icon = "inv_jewelry_necklace_36", quality = ItemQuality.EPIC, ilvl = 141, stamina = 54, defenseRating = 21, dodgeRating = 19),
            source = "Hyjal Summit",
        ),
    )

    private val shoulderItems = listOf(
        ItemOption(
            Item(id = 29100, name = "Shoulderguards of Malorne", icon = "inv_shoulder_14", quality = ItemQuality.EPIC, ilvl = 120, stamina = 25, agility = 27, strength = 33, armor = 410, socketTypes = listOf(GemColor.BLUE, GemColor.BLUE), setId = 640),
            source = "T4 - High King Maulgar",
        ),
        ItemOption(
            Item(id = 30230, name = "Nordrassil Feral-Mantle", icon = "inv_shoulder_14", quality = ItemQuality.EPIC, ilvl = 133, stamina = 42, agility = 28, strength = 18, armor = 336, defenseRating = 22, socketTypes = listOf(GemColor.RED, GemColor.YELLOW), setId = 651),
            source = "T5 - Void Reaver",
        ),
        ItemOption(
            Item(id = 31048, name = "Thunderheart Pauldrons", icon = "inv_shoulder_58", quality = ItemQuality.EPIC, ilvl = 146, stamina = 48, agility = 36, strength = 26, armor = 363, defenseRating = 21, dodgeRating = 19, socketTypes = listOf(GemColor.YELLOW, GemColor.BLUE), setId = 652),
            source = "T6 - Mother Shahraz",
        ),
    )

    private val backItems = listOf(
        ItemOption(
            Item(id = 28660, name = "Gilded Thorium Cloak", icon = "inv_misc_cape_20", quality = ItemQuality.EPIC, ilvl = 115, stamina = 30, armor = 385, defenseRating = 24),
            source = "Karazhan - Illhoof",
        ),
        ItemOption(
            Item(id = 28672, name = "Drape of the Dark Reavers", icon = "inv_misc_cape_naxxramas_03", quality = ItemQuality.EPIC, ilvl = 115, stamina = 28, agility = 22, armor = 86),
            source = "Karazhan - Shade of Aran",
        ),
        ItemOption(
            Item(id = 29994, name = "Thalassian Wildercloak", icon = "inv_misc_cape_naxxramas_03", quality = ItemQuality.EPIC, ilvl = 128, stamina = 33, agility = 25, armor = 93, dodgeRating = 17),
            source = "Tempest Keep",
        ),
        ItemOption(
            Item(id = 32590, name = "Shadowmoon Destroyer's Drape", icon = "inv_misc_cape_naxxramas_03", quality = ItemQuality.EPIC, ilvl = 141, stamina = 43, agility = 24, armor = 100, dodgeRating = 22),
            source = "Black Temple",
        ),
        ItemOption(
            Item(id = 33592, name = "Slikk's Cloak of Placation", icon = "inv_misc_cape_20", quality = ItemQuality.EPIC, ilvl = 141, stamina = 37, agility = 28, armor = 100, hitRating = 18, dodgeRating = 20),
            source = "Badge of Justice (35)",
        ),
    )

    private val chestItems = listOf(
        ItemOption(
            Item(id = 28264, name = "Wastewalker Tunic", icon = "inv_chest_leather_27", quality = ItemQuality.EPIC, ilvl = 109, stamina = 33, agility = 31, armor = 285, hitRating = 14, critRating = 20),
            source = "Shattered Halls",
        ),
        ItemOption(
            Item(id = 29096, name = "Breastplate of Malorne", icon = "inv_chest_leather_05", quality = ItemQuality.EPIC, ilvl = 120, stamina = 36, agility = 34, strength = 33, armor = 659, socketTypes = listOf(GemColor.RED, GemColor.BLUE, GemColor.YELLOW), setId = 640),
            source = "T4 - Magtheridon",
        ),
        ItemOption(
            Item(id = 30222, name = "Nordrassil Chestguard", icon = "inv_chest_leather_05", quality = ItemQuality.EPIC, ilvl = 133, stamina = 55, agility = 34, strength = 22, armor = 440, defenseRating = 31, socketTypes = listOf(GemColor.RED, GemColor.YELLOW, GemColor.YELLOW), setId = 651),
            source = "T5 - Kael'thas",
        ),
        ItemOption(
            Item(id = 31042, name = "Thunderheart Chestguard", icon = "inv_chest_leather_12", quality = ItemQuality.EPIC, ilvl = 146, stamina = 61, agility = 44, strength = 30, armor = 476, defenseRating = 32, dodgeRating = 24, socketTypes = listOf(GemColor.RED, GemColor.RED, GemColor.BLUE), setId = 652),
            source = "T6 - Illidan",
        ),
    )

    private val wristItems = listOf(
        ItemOption(
            Item(id = 29263, name = "Forestheart Bracers", icon = "inv_bracer_02", quality = ItemQuality.EPIC, ilvl = 110, stamina = 27, agility = 18, strength = 18, armor = 237),
            source = "Shattered Halls",
        ),
        ItemOption(
            Item(id = 28445, name = "General's Dragonhide Bracers", icon = "inv_bracer_07", quality = ItemQuality.EPIC, ilvl = 113, stamina = 22, agility = 13, strength = 16, armor = 213, resilienceRating = 11, socketTypes = listOf(GemColor.RED)),
            source = "PvP",
        ),
        ItemOption(
            Item(id = 29246, name = "Nightfall Wristguards", icon = "inv_bracer_07", quality = ItemQuality.EPIC, ilvl = 115, stamina = 22, agility = 22, armor = 164, dodgeRating = 13),
            source = "Karazhan - Nightbane",
        ),
        ItemOption(
            Item(id = 32810, name = "Wristbands of the Swarming Nightmare", icon = "inv_bracer_07", quality = ItemQuality.RARE, ilvl = 128, stamina = 24, agility = 27, armor = 180),
            source = "Shadow Labyrinth - Heroic",
        ),
        ItemOption(
            Item(id = 33540, name = "Guardian's Band of Abjuration", icon = "inv_bracer_17", quality = ItemQuality.EPIC, ilvl = 141, stamina = 37, armor = 200, defenseRating = 18, dodgeRating = 20),
            source = "Badge of Justice (35)",
        ),
        ItemOption(
            Item(id = 32280, name = "Gronnstalker's Bracers of the Defender", icon = "inv_bracer_07", quality = ItemQuality.EPIC, ilvl = 141, stamina = 34, agility = 22, armor = 200, dodgeRating = 24),
            source = "Black Temple",
        ),
    )

    private val handsItems = listOf(
        ItemOption(
            Item(id = 29097, name = "Gauntlets of Malorne", icon = "inv_gauntlets_29", quality = ItemQuality.EPIC, ilvl = 120, stamina = 28, agility = 24, strength = 32, armor = 475, setId = 640),
            source = "T4 - The Curator",
        ),
        ItemOption(
            Item(id = 30223, name = "Nordrassil Handgrips", icon = "inv_gauntlets_29", quality = ItemQuality.EPIC, ilvl = 133, stamina = 42, agility = 30, strength = 17, armor = 303, defenseRating = 20, socketTypes = listOf(GemColor.RED, GemColor.YELLOW), setId = 651),
            source = "T5 - Leotheras",
        ),
        ItemOption(
            Item(id = 31034, name = "Thunderheart Gauntlets", icon = "inv_gauntlets_61", quality = ItemQuality.EPIC, ilvl = 146, stamina = 48, agility = 32, strength = 24, armor = 329, defenseRating = 22, dodgeRating = 18, socketTypes = listOf(GemColor.RED), setId = 652),
            source = "T6 - Azgalor",
        ),
    )

    private val waistItems = listOf(
        ItemOption(
            Item(id = 29264, name = "Tree-Mender's Belt", icon = "inv_belt_22", quality = ItemQuality.EPIC, ilvl = 110, stamina = 22, agility = 27, strength = 27, armor = 406),
            source = "Shattered Halls",
        ),
        ItemOption(
            Item(id = 28828, name = "Gronn-Stitched Girdle", icon = "inv_belt_26", quality = ItemQuality.EPIC, ilvl = 125, stamina = 27, armor = 222, critRating = 25, attackPower = 72, socketTypes = listOf(GemColor.BLUE, GemColor.YELLOW)),
            source = "Gruul's Lair",
        ),
        ItemOption(
            Item(id = 28750, name = "Girdle of Treachery", icon = "inv_belt_24", quality = ItemQuality.EPIC, ilvl = 115, stamina = 28, agility = 30, armor = 226, critRating = 22),
            source = "Karazhan - Moroes",
        ),
        ItemOption(
            Item(id = 30106, name = "Belt of Natural Power", icon = "inv_belt_24", quality = ItemQuality.EPIC, ilvl = 128, stamina = 30, agility = 32, strength = 30, armor = 248, socketTypes = listOf(GemColor.RED, GemColor.YELLOW, GemColor.BLUE)),
            source = "Leatherworking",
        ),
        ItemOption(
            Item(id = 33878, name = "Belt of the Guardian", icon = "inv_belt_26", quality = ItemQuality.EPIC, ilvl = 141, stamina = 52, agility = 32, armor = 268, defenseRating = 20, dodgeRating = 24),
            source = "Badge of Justice (60)",
        ),
        ItemOption(
            Item(id = 32365, name = "Veil of Turning Leaves", icon = "inv_belt_24", quality = ItemQuality.EPIC, ilvl = 141, stamina = 46, agility = 34, armor = 268, dodgeRating = 26, socketTypes = listOf(GemColor.YELLOW)),
            source = "Hyjal Summit",
        ),
    )

    private val legsItems = listOf(
        ItemOption(
            Item(id = 28741, name = "Skulker's Greaves", icon = "inv_pants_leather_21", quality = ItemQuality.EPIC, ilvl = 115, stamina = 33, agility = 38, armor = 310, hitRating = 16, critRating = 24),
            source = "Karazhan - Moroes",
        ),
        ItemOption(
            Item(id = 29099, name = "Greaves of Malorne", icon = "inv_pants_leather_05", quality = ItemQuality.EPIC, ilvl = 120, stamina = 39, agility = 32, strength = 42, armor = 640, setId = 640),
            source = "T4 - Gruul",
        ),
        ItemOption(
            Item(id = 30229, name = "Nordrassil Feral-Kilt", icon = "inv_pants_leather_05", quality = ItemQuality.EPIC, ilvl = 133, stamina = 52, agility = 34, strength = 24, armor = 396, defenseRating = 28, socketTypes = listOf(GemColor.RED, GemColor.BLUE), setId = 651),
            source = "T5 - Fathom-Lord Karathress",
        ),
        ItemOption(
            Item(id = 31044, name = "Thunderheart Leggings", icon = "inv_pants_leather_16", quality = ItemQuality.EPIC, ilvl = 146, stamina = 63, agility = 44, strength = 30, armor = 427, defenseRating = 28, dodgeRating = 20, socketTypes = listOf(GemColor.RED, GemColor.YELLOW, GemColor.BLUE), setId = 652),
            source = "T6 - The Illidari Council",
        ),
    )

    private val feetItems = listOf(
        ItemOption(
            Item(id = 28545, name = "Edgewalker Longboots", icon = "inv_boots_08", quality = ItemQuality.EPIC, ilvl = 115, stamina = 24, agility = 30, armor = 242, hitRating = 18),
            source = "Karazhan - Moroes",
        ),
        ItemOption(
            Item(id = 29483, name = "Zierhut's Lost Treads", icon = "inv_boots_08", quality = ItemQuality.EPIC, ilvl = 115, stamina = 31, agility = 26, armor = 242, defenseRating = 14, dodgeRating = 16),
            source = "Badge of Justice (60)",
        ),
        ItemOption(
            Item(id = 30104, name = "Boots of Natural Grace", icon = "inv_boots_08", quality = ItemQuality.EPIC, ilvl = 128, stamina = 30, agility = 32, armor = 266, defenseRating = 24, socketTypes = listOf(GemColor.YELLOW, GemColor.BLUE)),
            source = "Leatherworking",
        ),
        ItemOption(
            Item(id = 32366, name = "Shadowmaster's Boots", icon = "inv_boots_08", quality = ItemQuality.EPIC, ilvl = 141, stamina = 46, agility = 34, armor = 290, dodgeRating = 20, hitRating = 18),
            source = "Hyjal Summit",
        ),
    )

    private val ringItems = listOf(
        ItemOption(
            Item(id = 29279, name = "Violet Signet of the Great Protector", icon = "inv_jewelry_ring_62", quality = ItemQuality.EPIC, ilvl = 130, stamina = 37, armor = 392, defenseRating = 19),
            source = "Karazhan Rep - Exalted",
        ),
        ItemOption(
            Item(id = 28792, name = "A'dal's Signet of Defense", icon = "inv_jewelry_ring_55", quality = ItemQuality.EPIC, ilvl = 125, stamina = 34, armor = 367, defenseRating = 20),
            source = "Magtheridon Quest",
        ),
        ItemOption(
            Item(id = 30834, name = "Shapeshifter's Signet", icon = "inv_jewelry_ring_62", quality = ItemQuality.EPIC, ilvl = 128, stamina = 33, agility = 24, dodgeRating = 22),
            source = "Serpentshrine Cavern",
        ),
        ItemOption(
            Item(id = 32261, name = "Band of the Abyssal Lord", icon = "inv_jewelry_ring_62", quality = ItemQuality.EPIC, ilvl = 141, stamina = 42, armor = 220, defenseRating = 22),
            source = "Black Temple",
        ),
        ItemOption(
            Item(id = 33496, name = "Ring of Unyielding Force", icon = "inv_jewelry_ring_62", quality = ItemQuality.EPIC, ilvl = 141, stamina = 45, defenseRating = 15, dodgeRating = 28),
            source = "Badge of Justice (60)",
        ),
    )

    private val trinketItems = listOf(
        ItemOption(
            Item(id = 23207, name = "Mark of Tyranny", icon = "inv_jewelry_talisman_08", quality = ItemQuality.RARE, ilvl = 63, armor = 180, dodgeRating = 0),
            source = "UBRS Quest",
        ),
        ItemOption(
            Item(id = 28121, name = "Icon of Unyielding Courage", icon = "inv_misc_gem_bloodgem_01", quality = ItemQuality.EPIC, ilvl = 112, stamina = 21, defenseRating = 19, dodgeRating = 17),
            source = "Karazhan - Maiden",
        ),
        ItemOption(
            Item(id = 28528, name = "Moroes' Lucky Pocket Watch", icon = "inv_misc_pocketwatch_01", quality = ItemQuality.EPIC, ilvl = 115, dodgeRating = 38),
            source = "Karazhan - Moroes",
        ),
        ItemOption(
            Item(id = 29370, name = "Badge of Tenacity", icon = "inv_jewelry_talisman_08", quality = ItemQuality.EPIC, ilvl = 110, agility = 28, stamina = 0, defenseRating = 0, dodgeRating = 0),
            source = "Blade's Edge Ogri'la",
        ),
        ItemOption(
            Item(id = 28830, name = "Dragonspine Trophy", icon = "inv_misc_bone_03", quality = ItemQuality.EPIC, ilvl = 125, attackPower = 40),
            source = "Gruul's Lair",
        ),
        ItemOption(
            Item(id = 29383, name = "Bloodlust Brooch", icon = "inv_misc_monsterscales_15", quality = ItemQuality.EPIC, ilvl = 110, attackPower = 72),
            source = "Badge of Justice (41)",
        ),
        ItemOption(
            Item(id = 30629, name = "Scarab of Displacement", icon = "inv_misc_gem_variety_01", quality = ItemQuality.EPIC, ilvl = 128, dodgeRating = 38),
            source = "Tempest Keep - Void Reaver",
        ),
        ItemOption(
            Item(id = 33830, name = "Figurine of the Colossus", icon = "inv_trinket_naxxramas06", quality = ItemQuality.EPIC, ilvl = 141, stamina = 33, defenseRating = 23),
            source = "Badge of Justice (41)",
        ),
    )

    private val weaponItems = listOf(
        ItemOption(
            Item(id = 28658, name = "Terestian's Stranglestaff", icon = "inv_staff_23", quality = ItemQuality.EPIC, ilvl = 115, stamina = 48, agility = 36, attackPower = 60, hitRating = 22, armor = 0),
            source = "Karazhan - Illhoof",
        ),
        ItemOption(
            Item(id = 29171, name = "Earthwarden", icon = "inv_mace_34", quality = ItemQuality.EPIC, ilvl = 115, stamina = 40, agility = 27, defenseRating = 24, armor = 0),
            source = "Cenarion Expedition - Exalted",
        ),
        ItemOption(
            Item(id = 30883, name = "Pillar of Ferocity", icon = "inv_staff_23", quality = ItemQuality.EPIC, ilvl = 128, stamina = 54, strength = 35, hitRating = 24, expertiseRating = 22, armor = 0),
            source = "Tempest Keep",
        ),
        ItemOption(
            Item(id = 33716, name = "Vengeful Gladiator's Maul", icon = "inv_mace_49", quality = ItemQuality.EPIC, ilvl = 146, stamina = 52, agility = 35, resilienceRating = 27, critRating = 18, armor = 0),
            source = "Arena Season 3",
        ),
        ItemOption(
            Item(id = 32014, name = "Wildfury Greatstaff", icon = "inv_staff_73", quality = ItemQuality.EPIC, ilvl = 151, stamina = 58, agility = 43, armor = 0, attackPower = 90, hitRating = 18, expertiseRating = 20),
            source = "Black Temple - Reliquary of Souls",
        ),
    )

    private val idolItems = listOf(
        ItemOption(
            Item(id = 23198, name = "Idol of Brutality", icon = "inv_relics_idolofferocity", quality = ItemQuality.RARE, ilvl = 65, armor = 0),
            source = "Stratholme",
        ),
        ItemOption(
            Item(id = 28372, name = "Idol of the Wild", icon = "inv_relics_idolofrejuvenation", quality = ItemQuality.RARE, ilvl = 100, armor = 0, agility = 0),
            source = "Sethekk Halls",
        ),
        ItemOption(
            Item(id = 33509, name = "Idol of Terror", icon = "inv_relics_idolofrejuvenation", quality = ItemQuality.EPIC, ilvl = 128, armor = 0, agility = 0),
            source = "Badge of Justice (20)",
        ),
        ItemOption(
            Item(id = 32387, name = "Idol of the Raven Goddess", icon = "inv_relics_idolofrejuvenation", quality = ItemQuality.EPIC, ilvl = 128, armor = 0),
            source = "Sethekk Halls - Heroic",
        ),
    )
}
