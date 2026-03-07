package com.wowtanksim.model

data class TalentDef(
    val id: String,
    val name: String,
    val tree: String,
    val row: Int,       // 0-based tier (row 0 = tier 1)
    val col: Int,       // 0-based column (0..3)
    val maxPoints: Int,
    val icon: String,   // wowhead/zamimg icon name
    val prerequisiteId: String? = null,
    val description: List<String> = emptyList(), // one per rank
)

data class TalentTreeDef(
    val name: String,
    val talents: List<TalentDef>,
)

object TalentTrees {
    // ─── Balance ───
    val balance = TalentTreeDef(
        name = "Balance",
        talents = listOf(
            TalentDef("starlight_wrath", "Starlight Wrath", "Balance", 0, 0, 5, "spell_nature_abolishmagic",
                description = (1..5).map { "Reduces the cast time of your Wrath and Starfire spells by ${it * 0.1} sec." }),
            TalentDef("natures_grasp", "Nature's Grasp", "Balance", 0, 1, 1, "spell_nature_natureswrath",
                description = listOf("While active, any time an enemy strikes the caster they have a 35% chance to become afflicted by Entangling Roots.")),
            TalentDef("improved_natures_grasp", "Improved Nature's Grasp", "Balance", 0, 2, 4, "spell_nature_natureswrath",
                prerequisiteId = "natures_grasp",
                description = (1..4).map { "Increases the chance for your Nature's Grasp to entangle an enemy by ${it * 15}%." }),
            TalentDef("control_of_nature", "Control of Nature", "Balance", 1, 0, 3, "spell_nature_healingtouch",
                description = (1..3).map { "Gives you a ${it * 33}% chance to avoid interruption caused by damage while casting Entangling Roots and Cyclone." }),
            TalentDef("focused_starlight", "Focused Starlight", "Balance", 1, 1, 2, "spell_arcane_starfire",
                description = (1..2).map { "Increases the critical strike chance of your Wrath and Starfire spells by ${it * 2}%." }),
            TalentDef("improved_moonfire", "Improved Moonfire", "Balance", 1, 2, 2, "spell_nature_starfall",
                description = (1..2).map { "Increases the damage and critical strike chance of your Moonfire spell by ${it * 5}%." }),
            TalentDef("brambles", "Brambles", "Balance", 2, 0, 3, "spell_nature_thorns",
                description = (1..3).map { "Increases damage caused by your Thorns and Entangling Roots spells by ${it * 25}%." }),
            // Insect Swarm is row 2 col 1 but requires no prereq
            TalentDef("insect_swarm", "Insect Swarm", "Balance", 2, 2, 1, "spell_nature_insectswarm",
                description = listOf("The enemy target is swarmed by insects, decreasing their chance to hit by 2% and causing 792 Nature damage over 12 sec.")),
            TalentDef("natures_reach", "Nature's Reach", "Balance", 2, 3, 2, "spell_nature_naturetouchgrow",
                description = (1..2).map { "Increases the range of your Balance spells and Faerie Fire (Feral) ability by ${it * 10}%." }),
            TalentDef("vengeance", "Vengeance", "Balance", 3, 1, 5, "spell_nature_purge",
                description = (1..5).map { "Increases the critical strike damage bonus of your Starfire, Moonfire, and Wrath spells by ${it * 20}%." }),
            TalentDef("celestial_focus", "Celestial Focus", "Balance", 3, 2, 3, "spell_arcane_starfire",
                description = (1..3).map { "Gives your Starfire spell a ${it * 5}% chance to stun the target for 3 sec and increases your total spell haste by ${it}%." }),
            TalentDef("lunar_guidance", "Lunar Guidance", "Balance", 4, 0, 3, "spell_nature_sentinal",
                description = (1..3).map { "Increases your spell damage and healing by ${it * 8}% of your total Intellect." }),
            TalentDef("natures_grace", "Nature's Grace", "Balance", 4, 1, 1, "spell_nature_naturesblessing",
                description = listOf("All spell criticals grace you with a blessing of nature, reducing the casting time of your next spell by 0.5 sec.")),
            TalentDef("moonglow", "Moonglow", "Balance", 4, 2, 3, "spell_nature_sentinal",
                description = (1..3).map { "Reduces the Mana cost of your Moonfire, Starfire, Wrath, Healing Touch, Regrowth and Rejuvenation spells by ${it * 3}%." }),
            TalentDef("moonfury", "Moonfury", "Balance", 5, 1, 5, "spell_nature_moonglow",
                description = (1..5).map { "Increases the damage done by your Starfire, Moonfire, and Wrath spells by ${it * 2}%." }),
            TalentDef("balance_of_power", "Balance of Power", "Balance", 5, 2, 2, "spell_nature_moonkey",
                description = (1..2).map { "Increases your chance to hit with all spells by ${it * 2}% and reduces the chance you are hit by spells by ${it * 2}%." }),
            TalentDef("dreamstate", "Dreamstate", "Balance", 6, 0, 3, "spell_nature_lightning",
                description = (1..3).map { "Regenerate mana equal to ${it * 4}% of your Intellect every 5 sec, even while casting." }),
            TalentDef("moonkin_form", "Moonkin Form", "Balance", 6, 1, 1, "spell_nature_forceofnature",
                description = listOf("Shapeshift into Moonkin Form. While in this form the armor contribution from items is increased by 400%, and all party members within 30 yards have their spell critical chance increased by 5%. The Moonkin can only cast Balance spells while shapeshifted.")),
            TalentDef("improved_faerie_fire", "Improved Faerie Fire", "Balance", 6, 2, 3, "spell_nature_faeriefire",
                description = (1..3).map { "Your Faerie Fire spell also increases the chance the target will be hit by melee and ranged attacks by ${it}%." }),
            TalentDef("wrath_of_cenarius", "Wrath of Cenarius", "Balance", 7, 1, 5, "spell_arcane_starfire",
                description = (1..5).map { "Your Starfire spell gains an additional ${it * 4}% and your Wrath gains an additional ${it * 2}% of your bonus damage effects." }),
            TalentDef("force_of_nature", "Force of Nature", "Balance", 8, 1, 1, "ability_druid_forceofnature",
                prerequisiteId = "moonkin_form",
                description = listOf("Summons 3 treants to attack enemy targets for 30 sec.")),
        ),
    )

    // ─── Feral Combat ───
    val feral = TalentTreeDef(
        name = "Feral Combat",
        talents = listOf(
            TalentDef("ferocity", "Ferocity", "Feral Combat", 0, 1, 5, "ability_druid_demoralizingroar",
                description = (1..5).map { "Reduces the cost of your Maul, Swipe, Claw, Rake and Mangle abilities by ${it} Rage or Energy." }),
            TalentDef("feral_aggression", "Feral Aggression", "Feral Combat", 0, 2, 5, "ability_druid_demoralizingroar",
                description = (1..5).map { "Increases the attack power reduction of your Demoralizing Roar by ${it * 8}% and the damage caused by your Ferocious Bite by ${it * 3}%." }),
            TalentDef("feral_instinct", "Feral Instinct", "Feral Combat", 1, 0, 3, "ability_ambush",
                description = (1..3).map { "Increases threat caused in Bear and Dire Bear Form by ${it * 5}% and reduces the chance enemies have to detect you while Prowling." }),
            TalentDef("brutal_impact", "Brutal Impact", "Feral Combat", 1, 1, 2, "ability_druid_bash",
                description = (1..2).map { "Increases the stun duration of your Bash and Pounce abilities by ${it * 0.5} sec." }),
            TalentDef("thick_hide", "Thick Hide", "Feral Combat", 1, 2, 3, "inv_misc_pelt_bear_03",
                description = listOf(
                    "Increases your Armor contribution from cloth and leather items by 4%.",
                    "Increases your Armor contribution from cloth and leather items by 7%.",
                    "Increases your Armor contribution from cloth and leather items by 10%.",
                )),
            TalentDef("feral_swiftness", "Feral Swiftness", "Feral Combat", 2, 0, 2, "ability_druid_challangingroar",
                description = (1..2).map { "Increases your movement speed by ${it * 15}% in Cat Form and increases your chance to dodge while in Cat Form, Bear Form and Dire Bear Form by ${it * 2}%." }),
            TalentDef("feral_charge", "Feral Charge", "Feral Combat", 2, 1, 1, "ability_hunter_pet_bear",
                description = listOf("Causes you to charge an enemy, immobilizing and interrupting any spell being cast for 4 sec.")),
            TalentDef("sharpened_claws", "Sharpened Claws", "Feral Combat", 2, 2, 3, "inv_misc_monsterclaw_04",
                description = (1..3).map { "Increases your critical strike chance while in Bear, Dire Bear, or Cat Form by ${it * 2}%." }),
            TalentDef("shredding_attacks", "Shredding Attacks", "Feral Combat", 3, 0, 2, "spell_shadow_vampiricaura",
                description = (1..2).map { "Reduces the energy cost of your Shred ability by ${it * 9} and the rage cost of your Lacerate ability by ${it}." }),
            TalentDef("predatory_strikes", "Predatory Strikes", "Feral Combat", 3, 1, 3, "ability_hunter_pet_cat",
                description = (1..3).map { "Increases your melee attack power in Cat, Bear, and Dire Bear Forms by ${it * 50}% of your level." }),
            TalentDef("primal_fury", "Primal Fury", "Feral Combat", 3, 2, 2, "ability_racial_cannibalize",
                prerequisiteId = "sharpened_claws",
                description = (1..2).map { "Gives you a ${it * 50}% chance to gain an additional 5 Rage anytime you get a critical strike while in Bear and Dire Bear Form." }),
            TalentDef("savage_fury", "Savage Fury", "Feral Combat", 4, 0, 2, "ability_druid_ravage",
                description = (1..2).map { "Increases the damage caused by your Claw, Rake, Mangle (Cat), Mangle (Bear), and Maul abilities by ${it * 10}%." }),
            TalentDef("faerie_fire_feral", "Faerie Fire (Feral)", "Feral Combat", 4, 2, 1, "spell_nature_faeriefire",
                description = listOf("Decrease the armor of the target by 610 for 40 sec. While affected, the target cannot stealth or turn invisible.")),
            TalentDef("nurturing_instinct", "Nurturing Instinct", "Feral Combat", 4, 3, 2, "ability_druid_healinginstincts",
                description = (1..2).map { "Increases your healing spells by up to ${it * 25}% of your Agility." }),
            TalentDef("heart_of_the_wild", "Heart of the Wild", "Feral Combat", 5, 1, 5, "spell_holy_blessingofagility",
                description = (1..5).map { "Increases your Intellect by ${it * 4}%. In addition, while in Bear or Dire Bear Form your Stamina is increased by ${it * 4}% and while in Cat Form your attack power is increased by ${it * 2}%." }),
            TalentDef("survival_of_the_fittest", "Survival of the Fittest", "Feral Combat", 5, 2, 3, "ability_druid_enrage",
                description = (1..3).map { "Increases all attributes by ${it}% and reduces the chance you'll be critically hit by melee attacks by ${it}%." }),
            TalentDef("leader_of_the_pack", "Leader of the Pack", "Feral Combat", 6, 1, 1, "spell_nature_unyeildingstamina",
                description = listOf("While in Cat, Bear or Dire Bear Form, the Leader of the Pack increases ranged and melee critical chance of all party members within 45 yards by 5%.")),
            TalentDef("improved_leader_of_the_pack", "Improved Leader of the Pack", "Feral Combat", 6, 2, 2, "spell_nature_unyeildingstamina",
                prerequisiteId = "leader_of_the_pack",
                description = (1..2).map { "Your Leader of the Pack ability also causes affected targets to have a ${it * 2}% chance to heal themselves for ${it * 2}% of their total health when they critically hit." }),
            TalentDef("predatory_instincts", "Predatory Instincts", "Feral Combat", 7, 0, 3, "ability_druid_predatoryinstincts",
                description = (1..3).map { "Increases your critical strike damage bonus by ${if (it == 1) 3 else if (it == 2) 7 else 10}% while in Cat or Bear Form." }),
            TalentDef("mangle", "Mangle", "Feral Combat", 8, 1, 1, "ability_druid_mangle2",
                description = listOf("Mangle the target, inflicting damage and causing the target to take additional damage from bleed effects for 12 sec. This ability can be used in Cat and Bear Form.")),
        ),
    )

    // ─── Restoration ───
    val restoration = TalentTreeDef(
        name = "Restoration",
        talents = listOf(
            TalentDef("improved_mark_of_the_wild", "Improved Mark of the Wild", "Restoration", 0, 1, 5, "spell_nature_regeneration",
                description = (1..5).map { "Increases the effects of your Mark of the Wild and Gift of the Wild spells by ${it * 7}%." }),
            TalentDef("furor", "Furor", "Restoration", 0, 2, 5, "spell_holy_blessingofstamina",
                description = (1..5).map { "Gives you a ${it * 20}% chance to gain 10 Rage when you shapeshift into Bear and Dire Bear Form, and you keep up to ${it * 20} of your Energy when you shapeshift into Cat Form." }),
            TalentDef("naturalist", "Naturalist", "Restoration", 1, 0, 5, "spell_nature_healingtouch",
                description = (1..5).map { "Reduces the cast time of your Healing Touch spell by ${it * 0.1} sec and increases the damage you deal with physical attacks in all forms by ${it * 2}%." }),
            TalentDef("natures_focus", "Nature's Focus", "Restoration", 1, 1, 5, "spell_nature_healingwavelesser",
                description = (1..5).map { "Gives you a ${it * 14}% chance to avoid interruption caused by damage while casting Healing Touch, Regrowth, and Tranquility." }),
            TalentDef("natural_shapeshifter", "Natural Shapeshifter", "Restoration", 1, 2, 3, "spell_nature_wispsplode",
                description = (1..3).map { "Reduces the mana cost of all shapeshifting by ${it * 10}%." }),
            TalentDef("intensity", "Intensity", "Restoration", 2, 0, 3, "spell_frost_windwalkon",
                description = (1..3).map { "Allows ${it * 5}% of your Mana regeneration to continue while casting and causes your Enrage ability to instantly generate ${it * 4} rage." }),
            TalentDef("subtlety", "Subtlety", "Restoration", 2, 1, 5, "ability_eyeoftheowl",
                description = (1..5).map { "Reduces the threat generated by your Restoration spells by ${it * 4}% and reduces the chance your spells will be dispelled by ${it * 6}%." }),
            TalentDef("omen_of_clarity", "Omen of Clarity", "Restoration", 2, 2, 1, "spell_nature_crystalball",
                prerequisiteId = "natural_shapeshifter",
                description = listOf("Each of the Druid's damage, healing spells and auto attacks has a chance of causing the caster to enter a Clearcasting state.")),
            TalentDef("tranquil_spirit", "Tranquil Spirit", "Restoration", 3, 1, 5, "spell_holy_elunesgrace",
                description = (1..5).map { "Reduces the mana cost of your Healing Touch and Tranquility spells by ${it * 2}%." }),
            TalentDef("improved_rejuvenation", "Improved Rejuvenation", "Restoration", 3, 2, 3, "spell_nature_rejuvenation",
                description = (1..3).map { "Increases the effect of your Rejuvenation spell by ${it * 5}%." }),
            TalentDef("natures_swiftness", "Nature's Swiftness", "Restoration", 4, 0, 1, "spell_nature_ravenform",
                description = listOf("When activated, your next Nature spell with a casting time of less than 10 sec becomes an instant cast spell.")),
            TalentDef("gift_of_nature", "Gift of Nature", "Restoration", 4, 1, 5, "spell_nature_protectionformnature",
                description = (1..5).map { "Increases the effect of all healing spells by ${it * 2}%." }),
            TalentDef("improved_tranquility", "Improved Tranquility", "Restoration", 4, 3, 2, "spell_nature_tranquility",
                description = (1..2).map { "Reduces threat caused by Tranquility by ${it * 50}%." }),
            TalentDef("empowered_touch", "Empowered Touch", "Restoration", 5, 0, 2, "spell_nature_healingway",
                description = (1..2).map { "Your Healing Touch spell gains an additional ${it * 10}% of your bonus healing effects." }),
            TalentDef("living_spirit", "Living Spirit", "Restoration", 5, 2, 3, "spell_nature_giftofthewaterspirit",
                description = (1..3).map { "Increases your total Spirit by ${it * 5}%." }),
            TalentDef("swiftmend", "Swiftmend", "Restoration", 6, 0, 1, "inv_relics_idolofrejuvenation",
                description = listOf("Consumes a Rejuvenation or Regrowth effect on a friendly target to instantly heal them.")),
            TalentDef("natural_perfection", "Natural Perfection", "Restoration", 6, 1, 3, "spell_nature_healingtouch",
                description = (1..3).map { "Your critical strike chance with all spells is increased by ${it}% and critical strikes against you give you the Natural Perfection effect reducing all damage taken by ${it * 2}% for 8 sec. Stacks up to 3 times." }),
            TalentDef("empowered_rejuvenation", "Empowered Rejuvenation", "Restoration", 7, 1, 5, "spell_nature_wispheal",
                description = (1..5).map { "The bonus healing effects of your heal over time spells is increased by ${it * 4}%." }),
            TalentDef("tree_of_life", "Tree of Life", "Restoration", 8, 1, 1, "ability_druid_treeoflife",
                prerequisiteId = "empowered_rejuvenation",
                description = listOf("Shapeshift into the Tree of Life. While in this form you increase healing received by 25% of your total Spirit for all party members within 45 yards, your movement speed is reduced by 20%, and you can only cast Restoration spells plus Innervate, Thorns, and Barkskin.")),
        ),
    )

    val allTrees: List<TalentTreeDef> = listOf(balance, feral, restoration)

    /** Flat lookup of all talent definitions by ID. */
    val byId: Map<String, TalentDef> = allTrees.flatMap { it.talents }.associateBy { it.id }
}
