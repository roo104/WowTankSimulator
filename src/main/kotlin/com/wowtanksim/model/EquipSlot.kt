package com.wowtanksim.model

enum class EquipSlot(val displayName: String, val isArmor: Boolean = false) {
    HEAD("Head", isArmor = true),
    NECK("Neck"),
    SHOULDER("Shoulder", isArmor = true),
    BACK("Back"),
    CHEST("Chest", isArmor = true),
    WRIST("Wrist", isArmor = true),
    HANDS("Hands", isArmor = true),
    WAIST("Waist", isArmor = true),
    LEGS("Legs", isArmor = true),
    FEET("Feet", isArmor = true),
    RING1("Ring 1"),
    RING2("Ring 2"),
    TRINKET1("Trinket 1"),
    TRINKET2("Trinket 2"),
    MAINHAND("Main Hand"),
    IDOL("Idol/Relic"),
}
