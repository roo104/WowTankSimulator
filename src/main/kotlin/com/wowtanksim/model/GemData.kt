package com.wowtanksim.model

/** Quality tier for sorting: higher = better */
enum class GemQuality(val label: String) {
    COMMON("Common"),
    UNCOMMON("Uncommon"),
    RARE("Rare"),
    EPIC("Epic"),
}

data class GemOption(
    val gem: Gem,
    val note: String? = null,
    val source: String = "Jewelcrafting",
    val quality: GemQuality = GemQuality.RARE,
)

object GemData {

    fun gemOptionsForColor(color: GemColor): List<GemOption> = when (color) {
        GemColor.RED -> redGems
        GemColor.BLUE -> blueGems
        GemColor.YELLOW -> yellowGems
        GemColor.META -> metaGems
    }

    // ── Red gems (pure red + hybrids matching red) ────────────────
    private val redGems = listOf(
        // Common (Tourmaline) — vendor
        GemOption(Gem(id = 28459, name = "Delicate Tourmaline", icon = "inv_misc_gem_ruby_03", color = GemColor.RED, agility = 4), note = "+4 Agility", source = "Vendor (~2g)", quality = GemQuality.COMMON),
        GemOption(Gem(id = 28458, name = "Bold Tourmaline", icon = "inv_misc_gem_ruby_03", color = GemColor.RED, strength = 4), note = "+4 Strength", source = "Vendor (~2g)", quality = GemQuality.COMMON),
        GemOption(Gem(id = 28462, name = "Bright Tourmaline", icon = "inv_misc_gem_ruby_03", color = GemColor.RED, attackPower = 8), note = "+8 AP", source = "Vendor (~2g)", quality = GemQuality.COMMON),
        GemOption(Gem(id = 28460, name = "Teardrop Tourmaline", icon = "inv_misc_gem_ruby_03", color = GemColor.RED), note = "+9 Heal / +3 SP", source = "Vendor (~2g)", quality = GemQuality.COMMON),
        GemOption(Gem(id = 28461, name = "Runed Tourmaline", icon = "inv_misc_gem_ruby_03", color = GemColor.RED), note = "+5 Spell Damage", source = "Vendor (~2g)", quality = GemQuality.COMMON),
        // Uncommon (Blood Garnet)
        GemOption(Gem(id = 23097, name = "Delicate Blood Garnet", icon = "inv_misc_gem_bloodgem_02", color = GemColor.RED, agility = 6), note = "+6 Agility", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23095, name = "Bold Blood Garnet", icon = "inv_misc_gem_bloodgem_02", color = GemColor.RED, strength = 6), note = "+6 Strength", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 28595, name = "Bright Blood Garnet", icon = "inv_misc_gem_bloodgem_02", color = GemColor.RED, attackPower = 12), note = "+12 AP", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23094, name = "Teardrop Blood Garnet", icon = "inv_misc_gem_bloodgem_02", color = GemColor.RED), note = "+13 Healing / +5 SP", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23096, name = "Runed Blood Garnet", icon = "inv_misc_gem_bloodgem_02", color = GemColor.RED), note = "+7 Spell Damage", quality = GemQuality.UNCOMMON),
        // Rare (Living Ruby)
        GemOption(Gem(id = 24028, name = "Delicate Living Ruby", icon = "inv_jewelcrafting_livingruby_03", color = GemColor.RED, agility = 8), note = "+8 Agility"),
        GemOption(Gem(id = 24036, name = "Bold Living Ruby", icon = "inv_jewelcrafting_livingruby_03", color = GemColor.RED, strength = 8), note = "+8 Strength"),
        GemOption(Gem(id = 24030, name = "Subtle Living Ruby", icon = "inv_jewelcrafting_livingruby_03", color = GemColor.RED, dodgeRating = 8), note = "+8 Dodge Rating"),
        // Epic (Crimson Spinel)
        GemOption(Gem(id = 32194, name = "Delicate Crimson Spinel", icon = "inv_jewelcrafting_crimsonspinel_02", color = GemColor.RED, agility = 10), note = "+10 Agility", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32198, name = "Subtle Crimson Spinel", icon = "inv_jewelcrafting_crimsonspinel_02", color = GemColor.RED, dodgeRating = 10), note = "+10 Dodge Rating", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32193, name = "Bold Crimson Spinel", icon = "inv_jewelcrafting_crimsonspinel_02", color = GemColor.RED, strength = 10), note = "+10 Strength", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32197, name = "Bright Crimson Spinel", icon = "inv_jewelcrafting_crimsonspinel_02", color = GemColor.RED, attackPower = 20), note = "+20 AP", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        // PvP (Honor)
        GemOption(Gem(id = 28362, name = "Bold Ornate Ruby", icon = "inv_misc_gem_ruby_02", color = GemColor.RED, attackPower = 20), note = "+20 AP", source = "8,500 Honor", quality = GemQuality.EPIC),
        // JC-only BoP
        GemOption(Gem(id = 33131, name = "Crimson Sun", icon = "inv_jewelcrafting_crimsonspinel_02", color = GemColor.RED, attackPower = 32), note = "+32 AP", source = "JC Only (BoP)", quality = GemQuality.EPIC),
        // Orange (match red+yellow) — duplicated in yellowGems
        GemOption(Gem(id = 23100, name = "Glinting Flame Spessarite", icon = "inv_misc_gem_flamespessarite_02", color = GemColor.RED, hitRating = 3, agility = 3), note = "+3 Hit / +3 Agi", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 31869, name = "Wicked Flame Spessarite", icon = "inv_misc_gem_flamespessarite_02", color = GemColor.RED, critRating = 3, attackPower = 6), note = "+3 Crit / +6 AP", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23101, name = "Potent Flame Spessarite", icon = "inv_misc_gem_flamespessarite_02", color = GemColor.RED), note = "+3 Spell Crit / +4 SP", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 31866, name = "Veiled Flame Spessarite", icon = "inv_misc_gem_flamespessarite_02", color = GemColor.RED), note = "+3 Spell Hit / +4 SP", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23099, name = "Luminous Flame Spessarite", icon = "inv_misc_gem_flamespessarite_02", color = GemColor.RED, intellect = 3), note = "+3 Int / +7 Heal", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 31106, name = "Glinting Noble Topaz", icon = "inv_jewelcrafting_nobletopaz_03", color = GemColor.RED, hitRating = 4, agility = 4), note = "+4 Hit / +4 Agi"),
        GemOption(Gem(id = 31098, name = "Stalwart Fire Opal", icon = "inv_jewelcrafting_nobletopaz_03", color = GemColor.RED, dodgeRating = 4, stamina = 6), note = "+4 Dodge / +6 Sta"),
        GemOption(Gem(id = 31104, name = "Resolute Fire Opal", icon = "inv_jewelcrafting_nobletopaz_03", color = GemColor.RED, defenseRating = 4, dodgeRating = 4), note = "+4 Def / +4 Dodge"),
        GemOption(Gem(id = 31103, name = "Nimble Fire Opal", icon = "inv_jewelcrafting_nobletopaz_03", color = GemColor.RED, dodgeRating = 4, hitRating = 4), note = "+4 Dodge / +4 Hit"),
        GemOption(Gem(id = 32220, name = "Glinting Pyrestone", icon = "inv_jewelcrafting_pyrestone_02", color = GemColor.RED, hitRating = 5, agility = 5), note = "+5 Hit / +5 Agi", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32222, name = "Wicked Pyrestone", icon = "inv_jewelcrafting_pyrestone_02", color = GemColor.RED, attackPower = 10, critRating = 5), note = "+10 AP / +5 Crit", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32217, name = "Inscribed Pyrestone", icon = "inv_jewelcrafting_pyrestone_02", color = GemColor.RED, strength = 5, critRating = 5), note = "+5 Str / +5 Crit", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 28363, name = "Inscribed Ornate Topaz", icon = "inv_misc_gem_opal_01", color = GemColor.RED, attackPower = 10, critRating = 5), note = "+10 AP / +5 Crit", source = "8,500 Honor", quality = GemQuality.EPIC),
        // Purple (match red+blue) — duplicated in blueGems
        GemOption(Gem(id = 23110, name = "Shifting Shadow Draenite", icon = "inv_misc_gem_ebondraenite_02", color = GemColor.RED, agility = 3, stamina = 4), note = "+3 Agi / +4 Sta", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23111, name = "Sovereign Shadow Draenite", icon = "inv_misc_gem_ebondraenite_02", color = GemColor.RED, strength = 3, stamina = 4), note = "+3 Str / +4 Sta", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23108, name = "Glowing Shadow Draenite", icon = "inv_misc_gem_ebondraenite_02", color = GemColor.RED, stamina = 4), note = "+4 SP / +4 Sta", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23109, name = "Royal Shadow Draenite", icon = "inv_misc_gem_ebondraenite_02", color = GemColor.RED), note = "+7 Heal / +3 SP / +1 MP5", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 31116, name = "Shifting Nightseye", icon = "inv_jewelcrafting_nightseye_03", color = GemColor.RED, agility = 4, stamina = 6), note = "+4 Agi / +6 Sta"),
        GemOption(Gem(id = 31118, name = "Sovereign Nightseye", icon = "inv_jewelcrafting_nightseye_03", color = GemColor.RED, strength = 4, stamina = 6), note = "+4 Str / +6 Sta"),
        GemOption(Gem(id = 32212, name = "Shifting Shadowsong Amethyst", icon = "inv_jewelcrafting_shadowsongamethyst_02", color = GemColor.RED, agility = 5, stamina = 7), note = "+5 Agi / +7 Sta", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32211, name = "Sovereign Shadowsong Amethyst", icon = "inv_jewelcrafting_shadowsongamethyst_02", color = GemColor.RED, strength = 5, stamina = 7), note = "+5 Str / +7 Sta", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32213, name = "Balanced Shadowsong Amethyst", icon = "inv_jewelcrafting_shadowsongamethyst_02", color = GemColor.RED, attackPower = 10, stamina = 7), note = "+10 AP / +7 Sta", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
    )

    // ── Blue gems (pure blue + hybrids matching blue) ──────────────
    private val blueGems = listOf(
        // Common (Zircon) — vendor
        GemOption(Gem(id = 28463, name = "Solid Zircon", icon = "inv_misc_gem_crystal_03", color = GemColor.BLUE, stamina = 6), note = "+6 Stamina", source = "Vendor (~2g)", quality = GemQuality.COMMON),
        GemOption(Gem(id = 28464, name = "Sparkling Zircon", icon = "inv_misc_gem_crystal_03", color = GemColor.BLUE, spirit = 4), note = "+4 Spirit", source = "Vendor (~2g)", quality = GemQuality.COMMON),
        // Uncommon (Azure Moonstone)
        GemOption(Gem(id = 23118, name = "Solid Azure Moonstone", icon = "inv_misc_gem_azuredraenite_02", color = GemColor.BLUE, stamina = 9), note = "+9 Stamina", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23119, name = "Sparkling Azure Moonstone", icon = "inv_misc_gem_azuredraenite_02", color = GemColor.BLUE, spirit = 6), note = "+6 Spirit", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23120, name = "Stormy Azure Moonstone", icon = "inv_misc_gem_azuredraenite_02", color = GemColor.BLUE), note = "+8 Spell Penetration", quality = GemQuality.UNCOMMON),
        // Rare (Star of Elune)
        GemOption(Gem(id = 24033, name = "Solid Star of Elune", icon = "inv_jewelcrafting_starofelune_03", color = GemColor.BLUE, stamina = 12), note = "+12 Stamina"),
        // Epic (Empyrean Sapphire)
        GemOption(Gem(id = 32200, name = "Solid Empyrean Sapphire", icon = "inv_jewelcrafting_empyreansapphire_02", color = GemColor.BLUE, stamina = 15), note = "+15 Stamina", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        // JC-only BoP
        GemOption(Gem(id = 33135, name = "Falling Star", icon = "inv_jewelcrafting_empyreansapphire_02", color = GemColor.BLUE, stamina = 18), note = "+18 Stamina", source = "JC Only (BoP)", quality = GemQuality.EPIC),
        // Purple (match red+blue) — duplicated in redGems
        GemOption(Gem(id = 23110, name = "Shifting Shadow Draenite", icon = "inv_misc_gem_ebondraenite_02", color = GemColor.BLUE, agility = 3, stamina = 4), note = "+3 Agi / +4 Sta", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23111, name = "Sovereign Shadow Draenite", icon = "inv_misc_gem_ebondraenite_02", color = GemColor.BLUE, strength = 3, stamina = 4), note = "+3 Str / +4 Sta", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23108, name = "Glowing Shadow Draenite", icon = "inv_misc_gem_ebondraenite_02", color = GemColor.BLUE, stamina = 4), note = "+4 SP / +4 Sta", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23109, name = "Royal Shadow Draenite", icon = "inv_misc_gem_ebondraenite_02", color = GemColor.BLUE), note = "+7 Heal / +3 SP / +1 MP5", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 31116, name = "Shifting Nightseye", icon = "inv_jewelcrafting_nightseye_03", color = GemColor.BLUE, agility = 4, stamina = 6), note = "+4 Agi / +6 Sta"),
        GemOption(Gem(id = 31118, name = "Sovereign Nightseye", icon = "inv_jewelcrafting_nightseye_03", color = GemColor.BLUE, strength = 4, stamina = 6), note = "+4 Str / +6 Sta"),
        GemOption(Gem(id = 32212, name = "Shifting Shadowsong Amethyst", icon = "inv_jewelcrafting_shadowsongamethyst_02", color = GemColor.BLUE, agility = 5, stamina = 7), note = "+5 Agi / +7 Sta", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32211, name = "Sovereign Shadowsong Amethyst", icon = "inv_jewelcrafting_shadowsongamethyst_02", color = GemColor.BLUE, strength = 5, stamina = 7), note = "+5 Str / +7 Sta", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32213, name = "Balanced Shadowsong Amethyst", icon = "inv_jewelcrafting_shadowsongamethyst_02", color = GemColor.BLUE, attackPower = 10, stamina = 7), note = "+10 AP / +7 Sta", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        // Green (match yellow+blue) — duplicated in yellowGems
        GemOption(Gem(id = 23105, name = "Enduring Deep Peridot", icon = "inv_misc_gem_deepperidot_02", color = GemColor.BLUE, defenseRating = 3, stamina = 4), note = "+3 Def / +4 Sta", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23104, name = "Jagged Deep Peridot", icon = "inv_misc_gem_deepperidot_02", color = GemColor.BLUE, critRating = 3, stamina = 4), note = "+3 Crit / +4 Sta", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23106, name = "Dazzling Deep Peridot", icon = "inv_misc_gem_deepperidot_02", color = GemColor.BLUE, intellect = 3), note = "+3 Int / +1 MP5", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23103, name = "Radiant Deep Peridot", icon = "inv_misc_gem_deepperidot_02", color = GemColor.BLUE, critRating = 3), note = "+3 Spell Crit / +4 Spell Pen", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 31101, name = "Enduring Talasite", icon = "inv_jewelcrafting_talasite_03", color = GemColor.BLUE, defenseRating = 4, stamina = 6), note = "+4 Def / +6 Sta"),
        GemOption(Gem(id = 32223, name = "Enduring Seaspray Emerald", icon = "inv_jewelcrafting_seasprayemerald_02", color = GemColor.BLUE, defenseRating = 5, stamina = 7), note = "+5 Def / +7 Sta", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32226, name = "Jagged Seaspray Emerald", icon = "inv_jewelcrafting_seasprayemerald_02", color = GemColor.BLUE, critRating = 5, stamina = 7), note = "+5 Crit / +7 Sta", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
    )

    // ── Yellow gems (pure yellow + hybrids matching yellow) ────────
    private val yellowGems = listOf(
        // Common (Amber) — vendor
        GemOption(Gem(id = 28470, name = "Thick Amber", icon = "inv_misc_gem_topaz_03", color = GemColor.YELLOW, defenseRating = 4), note = "+4 Defense Rating", source = "Vendor (~2g)", quality = GemQuality.COMMON),
        GemOption(Gem(id = 28468, name = "Rigid Amber", icon = "inv_misc_gem_topaz_03", color = GemColor.YELLOW, hitRating = 4), note = "+4 Hit Rating", source = "Vendor (~2g)", quality = GemQuality.COMMON),
        GemOption(Gem(id = 28467, name = "Smooth Amber", icon = "inv_misc_gem_topaz_03", color = GemColor.YELLOW, critRating = 4), note = "+4 Crit Rating", source = "Vendor (~2g)", quality = GemQuality.COMMON),
        GemOption(Gem(id = 28466, name = "Brilliant Amber", icon = "inv_misc_gem_topaz_03", color = GemColor.YELLOW, intellect = 4), note = "+4 Intellect", source = "Vendor (~2g)", quality = GemQuality.COMMON),
        // Uncommon (Golden Draenite)
        GemOption(Gem(id = 23115, name = "Thick Golden Draenite", icon = "inv_misc_gem_goldendraenite_02", color = GemColor.YELLOW, defenseRating = 6), note = "+6 Defense Rating", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23116, name = "Rigid Golden Draenite", icon = "inv_misc_gem_goldendraenite_02", color = GemColor.YELLOW, hitRating = 6), note = "+6 Hit Rating", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 28290, name = "Smooth Golden Draenite", icon = "inv_misc_gem_goldendraenite_02", color = GemColor.YELLOW, critRating = 6), note = "+6 Crit Rating", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23113, name = "Brilliant Golden Draenite", icon = "inv_misc_gem_goldendraenite_02", color = GemColor.YELLOW, intellect = 6), note = "+6 Intellect", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 31860, name = "Great Golden Draenite", icon = "inv_misc_gem_goldendraenite_02", color = GemColor.YELLOW), note = "+6 Spell Hit Rating", quality = GemQuality.UNCOMMON),
        // Rare (Dawnstone)
        GemOption(Gem(id = 31095, name = "Thick Dawnstone", icon = "inv_jewelcrafting_dawnstone_03", color = GemColor.YELLOW, defenseRating = 8), note = "+8 Defense Rating"),
        // Epic (Lionseye)
        GemOption(Gem(id = 32208, name = "Thick Lionseye", icon = "inv_jewelcrafting_lionseye_02", color = GemColor.YELLOW, defenseRating = 10), note = "+10 Defense Rating", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32206, name = "Rigid Lionseye", icon = "inv_jewelcrafting_lionseye_02", color = GemColor.YELLOW, hitRating = 10), note = "+10 Hit Rating", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32205, name = "Smooth Lionseye", icon = "inv_jewelcrafting_lionseye_02", color = GemColor.YELLOW, critRating = 10), note = "+10 Crit Rating", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32209, name = "Mystic Lionseye", icon = "inv_jewelcrafting_lionseye_02", color = GemColor.YELLOW, resilienceRating = 10), note = "+10 Resilience", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        // PvP (Honor)
        GemOption(Gem(id = 27679, name = "Sublime Mystic Dawnstone", icon = "inv_misc_gem_topaz_01", color = GemColor.YELLOW, resilienceRating = 10), note = "+10 Resilience", source = "8,500 Honor", quality = GemQuality.EPIC),
        // JC-only BoP
        GemOption(Gem(id = 33143, name = "Stone of Blades", icon = "inv_jewelcrafting_lionseye_02", color = GemColor.YELLOW, critRating = 12), note = "+12 Crit Rating", source = "JC Only (BoP)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 33144, name = "Facet of Eternity", icon = "inv_jewelcrafting_lionseye_02", color = GemColor.YELLOW, defenseRating = 12), note = "+12 Defense", source = "JC Only (BoP)", quality = GemQuality.EPIC),
        // Orange (match red+yellow) — duplicated in redGems
        GemOption(Gem(id = 23100, name = "Glinting Flame Spessarite", icon = "inv_misc_gem_flamespessarite_02", color = GemColor.YELLOW, hitRating = 3, agility = 3), note = "+3 Hit / +3 Agi", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 31869, name = "Wicked Flame Spessarite", icon = "inv_misc_gem_flamespessarite_02", color = GemColor.YELLOW, critRating = 3, attackPower = 6), note = "+3 Crit / +6 AP", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23101, name = "Potent Flame Spessarite", icon = "inv_misc_gem_flamespessarite_02", color = GemColor.YELLOW), note = "+3 Spell Crit / +4 SP", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 31866, name = "Veiled Flame Spessarite", icon = "inv_misc_gem_flamespessarite_02", color = GemColor.YELLOW), note = "+3 Spell Hit / +4 SP", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23099, name = "Luminous Flame Spessarite", icon = "inv_misc_gem_flamespessarite_02", color = GemColor.YELLOW, intellect = 3), note = "+3 Int / +7 Heal", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 31106, name = "Glinting Noble Topaz", icon = "inv_jewelcrafting_nobletopaz_03", color = GemColor.YELLOW, hitRating = 4, agility = 4), note = "+4 Hit / +4 Agi"),
        GemOption(Gem(id = 31098, name = "Stalwart Fire Opal", icon = "inv_jewelcrafting_nobletopaz_03", color = GemColor.YELLOW, dodgeRating = 4, stamina = 6), note = "+4 Dodge / +6 Sta"),
        GemOption(Gem(id = 31104, name = "Resolute Fire Opal", icon = "inv_jewelcrafting_nobletopaz_03", color = GemColor.YELLOW, defenseRating = 4, dodgeRating = 4), note = "+4 Def / +4 Dodge"),
        GemOption(Gem(id = 31103, name = "Nimble Fire Opal", icon = "inv_jewelcrafting_nobletopaz_03", color = GemColor.YELLOW, dodgeRating = 4, hitRating = 4), note = "+4 Dodge / +4 Hit"),
        GemOption(Gem(id = 32220, name = "Glinting Pyrestone", icon = "inv_jewelcrafting_pyrestone_02", color = GemColor.YELLOW, hitRating = 5, agility = 5), note = "+5 Hit / +5 Agi", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32222, name = "Wicked Pyrestone", icon = "inv_jewelcrafting_pyrestone_02", color = GemColor.YELLOW, attackPower = 10, critRating = 5), note = "+10 AP / +5 Crit", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32217, name = "Inscribed Pyrestone", icon = "inv_jewelcrafting_pyrestone_02", color = GemColor.YELLOW, strength = 5, critRating = 5), note = "+5 Str / +5 Crit", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 28363, name = "Inscribed Ornate Topaz", icon = "inv_misc_gem_opal_01", color = GemColor.YELLOW, attackPower = 10, critRating = 5), note = "+10 AP / +5 Crit", source = "8,500 Honor", quality = GemQuality.EPIC),
        // Green (match yellow+blue) — duplicated in blueGems
        GemOption(Gem(id = 23105, name = "Enduring Deep Peridot", icon = "inv_misc_gem_deepperidot_02", color = GemColor.YELLOW, defenseRating = 3, stamina = 4), note = "+3 Def / +4 Sta", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23104, name = "Jagged Deep Peridot", icon = "inv_misc_gem_deepperidot_02", color = GemColor.YELLOW, critRating = 3, stamina = 4), note = "+3 Crit / +4 Sta", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23106, name = "Dazzling Deep Peridot", icon = "inv_misc_gem_deepperidot_02", color = GemColor.YELLOW, intellect = 3), note = "+3 Int / +1 MP5", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 23103, name = "Radiant Deep Peridot", icon = "inv_misc_gem_deepperidot_02", color = GemColor.YELLOW, critRating = 3), note = "+3 Spell Crit / +4 Spell Pen", quality = GemQuality.UNCOMMON),
        GemOption(Gem(id = 31101, name = "Enduring Talasite", icon = "inv_jewelcrafting_talasite_03", color = GemColor.YELLOW, defenseRating = 4, stamina = 6), note = "+4 Def / +6 Sta"),
        GemOption(Gem(id = 32223, name = "Enduring Seaspray Emerald", icon = "inv_jewelcrafting_seasprayemerald_02", color = GemColor.YELLOW, defenseRating = 5, stamina = 7), note = "+5 Def / +7 Sta", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
        GemOption(Gem(id = 32226, name = "Jagged Seaspray Emerald", icon = "inv_jewelcrafting_seasprayemerald_02", color = GemColor.YELLOW, critRating = 5, stamina = 7), note = "+5 Crit / +7 Sta", source = "JC (15 Badges raw)", quality = GemQuality.EPIC),
    )

    // ── Meta gems ──────────────────────────────────────────────────
    private val metaGems = listOf(
        // Earthstorm Diamonds
        GemOption(
            Gem(id = 25896, name = "Powerful Earthstorm Diamond", icon = "inv_misc_gem_diamond_06", color = GemColor.META, stamina = 18),
            note = "+18 Sta, 5% stun resist", source = "Jewelcrafting",
        ),
        GemOption(
            Gem(id = 25898, name = "Tenacious Earthstorm Diamond", icon = "inv_misc_gem_diamond_06", color = GemColor.META, defenseRating = 12),
            note = "+12 Def, chance restore health on hit", source = "Jewelcrafting",
        ),
        GemOption(
            Gem(id = 32409, name = "Relentless Earthstorm Diamond", icon = "inv_misc_gem_diamond_06", color = GemColor.META, agility = 12),
            note = "+12 Agi, +3% crit dmg", source = "Jewelcrafting",
        ),
        GemOption(
            Gem(id = 35501, name = "Eternal Earthstorm Diamond", icon = "inv_misc_gem_diamond_06", color = GemColor.META, defenseRating = 12),
            note = "+12 Def, +10% shield block value", source = "Jewelcrafting",
        ),
        GemOption(
            Gem(id = 25899, name = "Brutal Earthstorm Diamond", icon = "inv_misc_gem_diamond_06", color = GemColor.META),
            note = "+3 melee dmg, chance stun target", source = "Jewelcrafting",
        ),
        GemOption(
            Gem(id = 25897, name = "Bracing Earthstorm Diamond", icon = "inv_misc_gem_diamond_06", color = GemColor.META),
            note = "+26 Heal / +9 SP, 2% reduced threat", source = "Jewelcrafting",
        ),
        GemOption(
            Gem(id = 25901, name = "Insightful Earthstorm Diamond", icon = "inv_misc_gem_diamond_06", color = GemColor.META, intellect = 12),
            note = "+12 Int, chance restore mana on cast", source = "Jewelcrafting",
        ),
        // Skyfire Diamonds
        GemOption(
            Gem(id = 34220, name = "Chaotic Skyfire Diamond", icon = "inv_misc_gem_diamond_07", color = GemColor.META, critRating = 12),
            note = "+12 Crit, +3% crit dmg", source = "Jewelcrafting",
        ),
        GemOption(
            Gem(id = 25894, name = "Swift Skyfire Diamond", icon = "inv_misc_gem_diamond_07", color = GemColor.META, attackPower = 24),
            note = "+24 AP, minor run speed", source = "Jewelcrafting",
        ),
        GemOption(
            Gem(id = 25895, name = "Enigmatic Skyfire Diamond", icon = "inv_misc_gem_diamond_07", color = GemColor.META, critRating = 12),
            note = "+12 Crit, 5% snare/root resist", source = "Jewelcrafting",
        ),
        GemOption(
            Gem(id = 32410, name = "Thundering Skyfire Diamond", icon = "inv_misc_gem_diamond_07", color = GemColor.META),
            note = "Chance haste proc on hit", source = "Jewelcrafting",
        ),
        GemOption(
            Gem(id = 25890, name = "Destructive Skyfire Diamond", icon = "inv_misc_gem_diamond_07", color = GemColor.META),
            note = "+14 Spell Crit, 1% spell reflect", source = "Jewelcrafting",
        ),
        GemOption(
            Gem(id = 25893, name = "Mystical Skyfire Diamond", icon = "inv_misc_gem_diamond_07", color = GemColor.META),
            note = "Chance spell haste proc on cast", source = "Jewelcrafting",
        ),
        GemOption(
            Gem(id = 35503, name = "Ember Skyfire Diamond", icon = "inv_misc_gem_diamond_07", color = GemColor.META),
            note = "+14 Spell Dmg, +2% Intellect", source = "Jewelcrafting",
        ),
        // Unstable Diamonds (BoP from SSC/TK trash)
        GemOption(
            Gem(id = 32640, name = "Potent Unstable Diamond", icon = "inv_misc_gem_diamond_06", color = GemColor.META, attackPower = 24),
            note = "+24 AP, 5% stun resist", source = "SSC/TK Trash (BoP)", quality = GemQuality.EPIC,
        ),
        GemOption(
            Gem(id = 32641, name = "Imbued Unstable Diamond", icon = "inv_misc_gem_diamond_07", color = GemColor.META),
            note = "+14 Spell Dmg, 5% stun resist", source = "SSC/TK Trash (BoP)", quality = GemQuality.EPIC,
        ),
    )
}
