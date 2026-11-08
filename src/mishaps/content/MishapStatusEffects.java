package mishaps.content;

import arc.graphics.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mishaps.graphics.*;
import mishaps.type.*;

public class MishapStatusEffects{

    public static StatusEffect dreadRust, hellburnt, interfered, giga;

    public static void load(){

        dreadRust = new MishapStatusEffect("dread-rust"){{
            color = MishapPal.dreadRust;
            hideDetails = false;
            damagePercentage = 0.04f;
            healthMultiplier = 0.85f;
            reloadMultiplier = 0.85f;
            speedMultiplier = 0.75f;

            effect = MishapFx.dreadRusting;
        }};

        hellburnt = new MishapStatusEffect("hellburnt"){{
            color = Color.valueOf("FFB7FD");
            damage = 0.167f;
            effect = MishapFx.hellburnt;
            transitionDamage = 8f;

            init(() -> affinity(StatusEffects.tarred, (unit, result, time) -> {
                unit.damagePierce(transitionDamage);
                MishapFx.hellburnt.at(unit.x + Mathf.range(unit.bounds() / 2f), unit.y + Mathf.range(unit.bounds() / 2f));
                result.set(hellburnt, Math.min(time + result.time, 300f));
            }));
        }};

        interfered = new MishapStatusEffect("interfered"){{
            color = Pal.techBlue;
            interferedFlag = true;
            reloadMultiplier = 0.6f;
            speedMultiplier = 0.6f;
            buildSpeedMultiplier = 0.6f;
            blocksInvisibility = true;

            init(() -> {
                affinity(StatusEffects.electrified, (unit, result, time) -> result.set(interfered, time + result.time));
            });
        }};

        giga = new MishapStatusEffect("giga"){{
            color = Team.crux.color;
            permanent = true;
            damageMultiplier = 2f;
            healthMultiplier = 2f;
            effectChance = 0.07f;
            effect = Fx.overdriven;
        }};
    }
}
