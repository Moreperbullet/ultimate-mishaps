package mishaps.entities.comp;

import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mishaps.type.*;

import static ent.anno.Annotations.*;

@EntityComponent
abstract class DodgeComp implements Shieldc{
    @Import float armor, healthMultiplier, hitTime, armorOverride;
    @Import static float hitDuration;
    @Import UnitType type;
    @Import Team team;

    public float damageDodge(float amount){
        return type instanceof MishapUnitType ob && Mathf.chance(ob.dodge) ? 0 : amount;
    }

    public void damageNoDodge(float amount){
        rawDamage(Damage.applyArmor(amount, armorOverride >= 0f ? armorOverride : armor) / healthMultiplier / Vars.state.rules.unitHealth(team));
    }

    public void damageNoDodge(float amount, boolean withEffect){
        float pre = hitTime;

        damageNoDodge(amount);

        if(!withEffect){
            hitTime = pre;
        }
    }

    public void damagePierceNoDodge(float amount, boolean withEffect){
        float pre = hitTime;

        rawDamage(amount / healthMultiplier / Vars.state.rules.unitHealth(team));

        if(!withEffect){
            hitTime = pre;
        }
    }

    public void damageArmorMultNoDodge(float amount, float armorMult, boolean withEffect){
        float pre = hitTime;

        rawDamage(Damage.applyArmor(amount, armorOverride >= 0f ? armorOverride * armorMult : armor * armorMult) / healthMultiplier / Vars.state.rules.unitHealth(team));

        if(!withEffect){
            hitTime = pre;
        }
    }

    @Override
    @Replace(100)
    public void damage(float amount){
        rawDamage(damageDodge(Damage.applyArmor(amount, armorOverride >= 0f ? armorOverride : armor)) / healthMultiplier / Vars.state.rules.unitHealth(team));
    }

    @Override
    @Replace(100)
    public void damagePierce(float amount, boolean withEffect){
        float pre = hitTime;

        rawDamage(damageDodge(amount) / healthMultiplier / Vars.state.rules.unitHealth(team));

        if(!withEffect){
            hitTime = pre;
        }
    }

    @Override
    @Replace(100)
    public void damageArmorMult(float amount, float armorMult, boolean withEffect){
        float pre = hitTime;

        rawDamage(damageDodge(Damage.applyArmor(amount, armorOverride >= 0f ? armorOverride * armorMult : armor * armorMult)) / healthMultiplier / Vars.state.rules.unitHealth(team));

        if(!withEffect){
            hitTime = pre;
        }
    }

    @Override
    @Replace(100)
    public void damageContinuous(float amount){
        damageNoDodge(amount * Time.delta, hitTime <= -10 + hitDuration);
    }

    @Override
    @Replace(100)
    public void damageContinuousPierce(float amount) {
        damagePierceNoDodge(amount * Time.delta, hitTime <= -20 + hitDuration);
    }
    
    @Override
    @Replace(100)
    public void damageContinuousArmorMult(float amount, float armorMult) {
        damageArmorMultNoDodge(amount * Time.delta, armorMult, hitTime <= -20 + hitDuration);
    }
}
