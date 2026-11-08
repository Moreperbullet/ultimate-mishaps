package mishaps.entities.comp;

import arc.math.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mishaps.entities.abilities.*;

@EntityComponent
abstract class InvisibleComp implements Unitc{
    boolean invisible;
    @ReadOnly float disabledTime = 120f;
    @ReadOnly transient Interval scanInterval = new Interval(2);
    @SyncLocal @SyncField(true) float alphaLerp;

    @Import UnitType type;
    @Import Team team;
    @Import float x, y, hitSize, health, maxHealth;
    @Import boolean isShooting;
    @Import Ability[] abilities;

    public transient InvisibleAbility invisibleA;

    @Override
    public void update(){
        disabledTime = Math.max(disabledTime - Time.delta, 0f);

        if(scanInterval.get(0, 5f) && invisibleA.scanBullets){
            hitbox(Tmp.r1);
            Groups.bullet.intersect(Tmp.r1.x, Tmp.r1.y, Tmp.r1.width, Tmp.r1.height, b -> {
                if(b.team != team && b.type.buildingDamage(b) < 1f) addDisabledTime(1.2f * 60);
            });
        }
        if(scanInterval.get(1, 30f)){
            float size = hitSize * 2.5f;
            if(invisibleA.scanUnits){
                Tmp.r1.setCentered(x, y, size * 2f);
                Groups.unit.intersect(Tmp.r1.x, Tmp.r1.y, Tmp.r1.width, Tmp.r1.height, u -> {
                    if(u.team != team && Mathf.within(x, y, u.x, u.y, size)){
                        addDisabledTime(1.2f * 60);
                    }
                });
            }
            if(invisibleA.scanBuilds){
                Building enemy = Units.findEnemyTile(team, x, y, size, b -> b.tile.solid());
                if(enemy != null) addDisabledTime(1.2f * 60);
            }
        }

        updateInvisibility(!isShooting && disabledTime <= 0f);
    }

    void updateInvisibility(boolean visible){
        alphaLerp = Mathf.approachDelta(alphaLerp, visible ? 1f : 0f, invisibleA.invisibleSpeed);
        invisible = alphaLerp >= 0.5f;
    }

    public void addDisabledTime(float amount){
        float clamped = Math.max(amount, disabledTime);
        disabledTime = clamped;
    }

    InvisibleAbility findMyAbility(){
        for(var a : abilities){
            if(a instanceof InvisibleAbility i) return i;
        }
        return null;
    }

    @Override
    public void setType(UnitType type){
	if(invisibleA == null || findMyAbility() != invisibleA) invisibleA = findMyAbility();
    }

    @Replace(10)
    @Override
    public boolean targetable(Team targeter){
        return !invisible && type.targetable(self(), targeter);
    }

    @Replace(10)
    @Override
    public boolean hittable(){
        return !invisible && type.hittable(self());
    }
}
