package mishaps.world.meta;

import mindustry.world.meta.*;

public class MishapStats {
    public static final Stat
    dodge = new Stat("mishaps-dodge", StatCat.general),

    damagePercent = new Stat("mishaps-damage-percent", StatCat.function),

    healPercent = new Stat("mishaps-heal-percent", StatCat.function),

    blocksInvisibility = new Stat("mishaps-blocks-invisibility", StatCat.function),

    special = new Stat("mishaps-special", StatCat.function),

    reflectModNum = new Stat("mishaps-reflect-modifier", StatCat.function),

    reflectCapNum = new Stat("mishaps-reflect-cap", StatCat.function),

    armorPhases = new Stat("mishaps-armor-phases", StatCat.function),

    baseShieldArmor = new Stat("mishaps-base-shield-armor", StatCat.function);
}
