package mishaps.type;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mishaps.gen.*;
import mishaps.graphics.*;
import mishaps.world.meta.*;

import static mishaps.MishapGlobals.*;

public class MishapStatusEffect extends StatusEffect{

    /** Damage percentage per second. Heals if negative.*/
    public float damagePercentage;
    /** Flag for interfered visual effect.*/
    public boolean interferedFlag;

    public boolean blocksInvisibility;

    public MishapStatusEffect(String name) {
        super(name);
    }

    @Override
    public void setStats(){
        super.setStats();
        if(damagePercentage > 0) stats.addPercent(MishapStats.damagePercent, damagePercentage);
        if(damagePercentage < 0) stats.addPercent(MishapStats.healPercent, -damagePercentage);
        if(blocksInvisibility) stats.add(MishapStats.blocksInvisibility, blocksInvisibility);
    }

    @Override
    public void draw(Unit unit){
        if(interferedFlag) drawInterfered(unit);
    }

    public void drawInterfered(Unit unit){
        Draw.z(Layer.flyingUnit + 0.1f);
        Draw.blend(Blending.additive);
        float rand = Mathf.random() / 2;
        Draw.mixcol(color, 1f);
        Draw.alpha(rand);
        Mechc mech = unit instanceof Mechc m ? m : null;
        if(mech != null){
            //copied code from UnitType's draw function
            mechVector.trns(mech.baseRotation(), 0f, Mathf.lerp(Mathf.sin(mech.walkExtend(true), 2f/Mathf.PI, 1) * unit.type.mechSideSway, 0f, unit.elevation));
            mechVector.add(Tmp.v1.trns(mech.baseRotation() + 90, 0f, Mathf.lerp(Mathf.sin(mech.walkExtend(true), 1f/Mathf.PI, 1) * unit.type.mechFrontSway, 0f, unit.elevation)));

            unit.trns(mechVector.x, mechVector.y);
        }
        Draw.rect(unit.type.region, unit.x, unit.y, unit.rotation - 90);
        for(WeaponMount mount : unit.mounts){
            mount.weapon.drawOutline(unit, mount);
        }
        Draw.blend();
        Draw.mixcol();
        if(mech != null) unit.trns(-mechVector.x, -mechVector.y);
        Draw.reset();
    }

    @Override
    public void update(Unit unit, StatusEntry entry){
        super.update(unit, entry);
        if(damagePercentage > 0){
            unit.damageContinuousPierce(unit.maxHealth * damagePercentage / 100);
        }else if(damagePercentage < 0){
            unit.heal(unit.maxHealth * (-1f * damagePercentage) / 100 * Time.delta);
        }

        if(unit instanceof Invisiblec i && blocksInvisibility) i.addDisabledTime(30f);
    }
}
