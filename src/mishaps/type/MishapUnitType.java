package mishaps.type;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.environment.*;
import mishaps.world.meta.*;
import mishaps.gen.*;

public class MishapUnitType extends UnitType{

    /** Always status*/
    public @Nullable StatusEffect alwaysStatus;

    /** Fade color, only for {@link mishaps.gen.Invisiblec} units*/
    public Color fadeColor = Pal.lancerLaser.cpy().a(0f);

    /** Dodge chance, only for {@link mishaps.gen.Dodgec} units*/
    public float dodge = 0f;

    static boolean canInvisibleDraw;

    @SuppressWarnings("unchecked")
    public MishapUnitType(String name){
        super(name);
    }

    protected float fade(Invisiblec unit){
        return Mathf.clamp(1f - unit.alphaLerp(), 0.1f, 1f);
    }

    @Override
    public void setStats(){
        super.setStats();
        if (dodge > 0){
            stats.add(MishapStats.dodge, Mathf.round(dodge * 100) + "%");
        }
    }

    @Override
    public void update(Unit unit){
        if(alwaysStatus != null) unit.apply(alwaysStatus);
    }

    @Override
    public void draw(Unit unit){
        canInvisibleDraw = unit instanceof Invisiblec i && i.alphaLerp() > 0.00001f;
        super.draw(unit);
    }

    @Override
    public void drawMech(Mechc mech){
        if(canInvisibleDraw){
            Unit unit = (Unit)mech;
            applyColor(unit);

            float e = unit.elevation;

            float sin = Mathf.lerp(Mathf.sin(mech.walkExtend(true), 2f / Mathf.PI, 1f), 0f, e);
            float extension = Mathf.lerp(mech.walkExtend(false), 0, e);
            float boostTrns = e * 2f;

            Floor floor = unit.isFlying() ? Blocks.air.asFloor() : unit.floorOn();

            if(floor.isLiquid){
                Draw.color(Color.white, floor.mapColor, 0.5f);
                applyColor(unit);
            }

            for(int i : Mathf.signs){
                Draw.mixcol(Tmp.c1.set(mechLegColor).lerp(Color.white, Mathf.clamp(unit.hitTime)), Math.max(Math.max(0, i * extension / mechStride), unit.hitTime));

                Draw.rect(legRegion,
                        unit.x + Angles.trnsx(mech.baseRotation(), extension * i - boostTrns, -boostTrns*i),
                        unit.y + Angles.trnsy(mech.baseRotation(), extension * i - boostTrns, -boostTrns*i),
                        legRegion.width * legRegion.scl() * i,
                        legRegion.height * legRegion.scl() * (1 - Math.max(-sin * i, 0) * 0.5f),
                        mech.baseRotation() - 90 + 35f*i*e);
            }

            Draw.mixcol(Color.white, unit.hitTime);

            if(unit.lastDrownFloor != null){
                Draw.color(Color.white, Tmp.c1.set(unit.lastDrownFloor.mapColor).mul(0.83f), unit.drownTime * 0.9f);
            }else{
                Draw.color(Color.white);
            }

            applyColor(unit);
            Draw.rect(baseRegion, unit, mech.baseRotation() - 90);

            Draw.mixcol();
            Draw.reset();
        }else{
            super.drawMech(mech);
        }
    }

    @Override
    public void drawSoftShadow(Unit unit){
        if(canInvisibleDraw){
            Invisiblec i = unit.as();

            Draw.color(0, 0, 0, 0.4f * fade(i));
            float rad = 1.6f;
            float size = Math.max(region.width, region.height) * Draw.scl;
            Draw.rect(softShadowRegion, unit, size * rad, size * rad);
            Draw.color();
        }else{
            super.drawSoftShadow(unit);
        }
    }

    @Override
    public void drawShadow(Unit unit){
        if(canInvisibleDraw){
            Invisiblec i = unit.as();

            float e = Mathf.clamp(unit.elevation, shadowElevation, 1f) * shadowElevationScl * (1f - unit.drownTime);
            Draw.color(Pal.shadow);
            Draw.alpha(Pal.shadow.a * fade(i));
            Draw.rect(shadowRegion, unit.x + shadowTX * e, unit.y + shadowTY * e, unit.rotation - 90);
            Draw.color();
        }else{
            super.drawShadow(unit);
        }
    }

    @Override
    public void applyColor(Unit unit){
        if(canInvisibleDraw){
            Invisiblec i = unit.as();
            float lerp = fade(i);

            Tmp.c1.set(Color.white).lerp(fadeColor, Mathf.lerp(0f, 0.5f, i.alphaLerp()));
            //Draw.color(Color.white, fadeColor, i.alphaLerp());
            Draw.color(Tmp.c1);
            if(healFlash){
                Tmp.c1.set(Color.white).lerp(healColor, Mathf.clamp(unit.healTime - unit.hitTime));
            }
            Draw.mixcol(Tmp.c1, Math.max(unit.hitTime, !healFlash ? 0f : Mathf.clamp(unit.healTime)));
            Draw.alpha(lerp);

            if(unit.drownTime > 0 && unit.lastDrownFloor != null){
                Draw.mixcol(Tmp.c1.set(unit.lastDrownFloor.mapColor).mul(0.83f), unit.drownTime * 0.9f);
            }
        }else{
            super.applyColor(unit);
        }
    }

    @Override
    public void applyOutlineColor(Unit unit){
        super.applyOutlineColor(unit);
        if(canInvisibleDraw){
            Invisiblec i = unit.as();
            Tmp.c1.set(Draw.getColor()).lerp(fadeColor, i.alphaLerp());
            Draw.color(Tmp.c1);
        }
    }
}
