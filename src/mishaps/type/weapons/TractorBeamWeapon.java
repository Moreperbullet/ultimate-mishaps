package mishaps.type.weapons;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;

import static mindustry.Vars.*;

public class TractorBeamWeapon extends Weapon{
    public float laserWidth = 0.6f;
    public float shootLength = 5f;
    public float force = 1.7f;
    public float scaledForce = 0f;

    public boolean targetAir = true, targetGround = true, targetBuildings = true;
    public Color laserColor = Color.white;
   
    public String laserSpriteName = "mishaps-purple";

    public TextureRegion laser, laserEnd;

    public TractorBeamWeapon(String name){
        super(name);
    }

    public TractorBeamWeapon(){
    }

    {
        alternate = false;
        //must be >0 to prevent various bugs
        reload = 60f;
        shootCone = 4f;
        predictTarget = false;
        autoTarget = true;
        controllable = false;
        rotate = true;
        mountType = TractorBeamMount::new;
        recoil = 0f;
        useAttackRange = false;
        activeSound = Sounds.beamParallax;
        activeSoundVolume = 0.9f;
    }

    @Override
    public void load(){
        super.load();

        laser = Core.atlas.find(laserSpriteName + "-laser");
        laserEnd = Core.atlas.find(laserSpriteName + "-laser-end");
    }

    @Override
    protected Teamc findTarget(Unit unit, float x, float y, float range, boolean air, boolean ground){
        return Units.closestTarget(unit.team, x, y, range + Math.abs(shootY), u -> u.checkTarget(targetAir, targetGround), t -> targetBuildings && (unit.type.targetUnderBlocks || !t.block.underBullets));
    }

    @Override
    protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation){
      //so it does not actually shoot
    }

    @Override
    public void update(Unit unit, WeaponMount mount){
        super.update(unit, mount);
      
        float
        weaponRotation = unit.rotation - 90,
        wx = unit.x + Angles.trnsx(weaponRotation, x, y),
        wy = unit.y + Angles.trnsy(weaponRotation, x, y);

        TractorBeamMount tractor = (TractorBeamMount)mount;
        boolean canShoot = mount.shoot && Angles.within(mount.rotation + baseRotation, mount.targetRotation, shootCone);

        tractor.any = false;
        if(canShoot && mount.target instanceof Unit u){
            if(bullet.status != StatusEffects.none){
                u.apply(bullet.status, bullet.statusDuration);
            }
            u.impulseNet(Tmp.v1.set(unit).sub(u).limit((force + (1f - u.dst(unit) / bullet.maxRange) * scaledForce)));
        }

        if(canShoot && mount.target instanceof Healthc h){
            tractor.lastX = mount.target.x();
            tractor.lastY = mount.target.y();
            tractor.strength = Mathf.lerpDelta(tractor.strength, 1f, 0.1f);

            if(bullet.damage > 0) h.damageContinuousPierce(bullet.damage * state.rules.unitDamage(unit.team));
            tractor.any = true;
        }else{
            tractor.strength = Mathf.lerpDelta(tractor.strength, 0f, 0.1f);
        }
    }

    @Override
    public void draw(Unit unit, WeaponMount mount){
        super.draw(unit, mount);
        TractorBeamMount tractor = (TractorBeamMount)mount;
      
        if(tractor.any){
            float
                weaponRotation = unit.rotation - 90,
                wx = unit.x + Angles.trnsx(weaponRotation, x, y),
                wy = unit.y + Angles.trnsy(weaponRotation, x, y),
                z = Draw.z();

            float ang = Angles.angle(wx, wy, tractor.lastX, tractor.lastY);

            Draw.z(Layer.flyingUnit + 1f);
            Draw.mixcol(laserColor, Mathf.absin(4f, 0.6f));
            Drawf.laser(laser, laserEnd, laserEnd,
            wx + Angles.trnsx(ang, shootLength), wy + Angles.trnsy(ang, shootLength),
            tractor.lastX, tractor.lastY, tractor.strength * laserWidth);
            Draw.mixcol();
            Draw.z(z);
       }
    }

    public static class TractorBeamMount extends WeaponMount{
        public float lastX, lastY, strength;
        public boolean any;

        public TractorBeamMount(Weapon weapon){
            super(weapon);
        }
    }
}
