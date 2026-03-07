package com.wowtanksim.model

data class GemOption(
    val gem: Gem,
    val note: String? = null,
)

object GemData {

    fun gemOptionsForColor(color: GemColor): List<GemOption> = when (color) {
        GemColor.RED -> redGems
        GemColor.BLUE -> blueGems
        GemColor.YELLOW -> yellowGems
        GemColor.META -> metaGems
    }

    // Red gems (and red-matching orange/purple)
    private val redGems = listOf(
        GemOption(
            Gem(id = 24028, name = "Delicate Living Ruby", icon = "inv_jewelcrafting_livingruby_03", color = GemColor.RED, agility = 8),
            note = "+8 Agility",
        ),
        GemOption(
            Gem(id = 32194, name = "Delicate Crimson Spinel", icon = "inv_jewelcrafting_crimsonspinel_02", color = GemColor.RED, agility = 10),
            note = "+10 Agility (T6)",
        ),
        GemOption(
            Gem(id = 24030, name = "Subtle Living Ruby", icon = "inv_jewelcrafting_livingruby_03", color = GemColor.RED, dodgeRating = 8),
            note = "+8 Dodge Rating",
        ),
        GemOption(
            Gem(id = 32196, name = "Subtle Crimson Spinel", icon = "inv_jewelcrafting_crimsonspinel_02", color = GemColor.RED, dodgeRating = 10),
            note = "+10 Dodge Rating (T6)",
        ),
        GemOption(
            Gem(id = 24036, name = "Bold Living Ruby", icon = "inv_jewelcrafting_livingruby_03", color = GemColor.RED, strength = 8),
            note = "+8 Strength",
        ),
    )

    // Blue gems (and blue-matching green/purple)
    private val blueGems = listOf(
        GemOption(
            Gem(id = 24033, name = "Solid Star of Elune", icon = "inv_jewelcrafting_starofelune_03", color = GemColor.BLUE, stamina = 12),
            note = "+12 Stamina",
        ),
        GemOption(
            Gem(id = 32200, name = "Solid Empyrean Sapphire", icon = "inv_jewelcrafting_empyreansapphire_02", color = GemColor.BLUE, stamina = 15),
            note = "+15 Stamina (T6)",
        ),
        GemOption(
            Gem(id = 31116, name = "Shifting Nightseye", icon = "inv_jewelcrafting_nightseye_03", color = GemColor.BLUE, agility = 4, stamina = 6),
            note = "+4 Agi / +6 Sta (Purple)",
        ),
        GemOption(
            Gem(id = 32212, name = "Shifting Shadowsong Amethyst", icon = "inv_jewelcrafting_shadowsongamethyst_02", color = GemColor.BLUE, agility = 5, stamina = 7),
            note = "+5 Agi / +7 Sta (T6 Purple)",
        ),
        GemOption(
            Gem(id = 31118, name = "Sovereign Nightseye", icon = "inv_jewelcrafting_nightseye_03", color = GemColor.BLUE, strength = 4, stamina = 6),
            note = "+4 Str / +6 Sta (Purple)",
        ),
    )

    // Yellow gems (and yellow-matching orange/green)
    private val yellowGems = listOf(
        GemOption(
            Gem(id = 31095, name = "Thick Dawnstone", icon = "inv_jewelcrafting_dawnstone_03", color = GemColor.YELLOW, defenseRating = 8),
            note = "+8 Defense Rating",
        ),
        GemOption(
            Gem(id = 32206, name = "Thick Lionseye", icon = "inv_jewelcrafting_lionseye_02", color = GemColor.YELLOW, defenseRating = 10),
            note = "+10 Defense Rating (T6)",
        ),
        GemOption(
            Gem(id = 31101, name = "Enduring Talasite", icon = "inv_jewelcrafting_talasite_03", color = GemColor.YELLOW, defenseRating = 4, stamina = 6),
            note = "+4 Def / +6 Sta (Green)",
        ),
        GemOption(
            Gem(id = 32218, name = "Enduring Seaspray Emerald", icon = "inv_jewelcrafting_seasprayemerald_02", color = GemColor.YELLOW, defenseRating = 5, stamina = 7),
            note = "+5 Def / +7 Sta (T6 Green)",
        ),
        GemOption(
            Gem(id = 31098, name = "Stalwart Fire Opal", icon = "inv_jewelcrafting_nobletopaz_03", color = GemColor.YELLOW, dodgeRating = 4, stamina = 6),
            note = "+4 Dodge / +6 Sta (Orange)",
        ),
        GemOption(
            Gem(id = 31104, name = "Resolute Fire Opal", icon = "inv_jewelcrafting_nobletopaz_03", color = GemColor.YELLOW, defenseRating = 4, dodgeRating = 4),
            note = "+4 Def / +4 Dodge (Orange)",
        ),
        GemOption(
            Gem(id = 31103, name = "Nimble Fire Opal", icon = "inv_jewelcrafting_nobletopaz_03", color = GemColor.YELLOW, dodgeRating = 4, hitRating = 4),
            note = "+4 Dodge / +4 Hit (Orange)",
        ),
    )

    // Meta gems
    private val metaGems = listOf(
        GemOption(
            Gem(id = 25896, name = "Powerful Earthstorm Diamond", icon = "inv_misc_gem_diamond_07", color = GemColor.META, stamina = 18),
            note = "+18 Sta, 5% stun resist",
        ),
        GemOption(
            Gem(id = 32409, name = "Relentless Earthstorm Diamond", icon = "inv_misc_gem_diamond_07", color = GemColor.META, agility = 12, critRating = 3),
            note = "+12 Agi, +3% crit dmg",
        ),
        GemOption(
            Gem(id = 35501, name = "Eternal Earthstorm Diamond", icon = "inv_misc_gem_diamond_07", color = GemColor.META, defenseRating = 12),
            note = "+12 Def, +5% shield block",
        ),
        GemOption(
            Gem(id = 25897, name = "Bracing Earthstorm Diamond", icon = "inv_misc_gem_diamond_07", color = GemColor.META, stamina = 14),
            note = "+14 Spell Dmg, -2% threat (non-tank)",
        ),
    )
}
