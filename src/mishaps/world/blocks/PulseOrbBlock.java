package mishaps.world.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import mindustry.gen.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class PulseOrbBlock extends Block{
    protected static final Seq<Unit> targets = new Seq<>();

    protected static final IntFloatMap orbMap = new IntFloatMap();

    private static long lastUpdateFrame = -1;

    public float minRange = 5f * 8f,
           maxRange = 20f * 8f,
           growSpeed = 0.4f,
           damage = 2f;

    public Color orbColor = Pal.regen;

    public Effect effect = Fx.regenParticle;

    public PulseOrbBlock(String name){
        super(name);
        update = true;
        solid = true;
        suppressable = true;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);

        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, minRange, orbColor);
        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, maxRange, player.team().color);
    }

    public class PulseOrbBuild extends Building{
        public float realRange = minRange, hit;
        public boolean expanding, touched;

        @Override
        public void updateTile(){
            targets.clear();

            Units.nearbyEnemies(team, x, y, realRange, enemy -> {
                if(enemy.targetable(team)) targets.add(enemy);
            });

            touched = (targets.size > 0) && !checkSuppression();

            if(touched){
                hit = 1f;
                if(realRange < maxRange) expanding = true;
            }else{
                if(hit < 0.09f) expanding = false;
            }

            if(touched && expanding){
                realRange = Math.min(maxRange, realRange + growSpeed * Time.delta);
            }else if(!expanding && hit < 0.09f){
                realRange = Math.max(minRange, realRange - growSpeed * Time.delta);
            }

            if(hit > 0){
                hit -= 1f / 6f * Time.delta;
            }

            if(checkSuppression()) return;

            for(int i = 0; i < targets.size; i++){
                Unit other = targets.get(i);
                if(other.dst(x, y) < realRange){
                    int id = other.id;
                    float rawDamage = damage * state.rules.blockDamage(team);

                    orbMap.put(id, Math.max(orbMap.get(id), rawDamage));
                }
            }

            if(lastUpdateFrame != state.updateId){
                lastUpdateFrame = state.updateId;
                for(var entry : orbMap.entries()){
                    Unit other = Groups.unit.getByID(entry.key);
                    if(other != null && other.isValid()){
                        other.damageContinuous(entry.value);
                    }
                }
                orbMap.clear();
            }
        }

        @Override
        public void draw(){
            Draw.color(orbColor, Color.white, Mathf.clamp(hit));
            Draw.z(Layer.flyingUnit + 1f);
            Lines.stroke(1.5f);
            Draw.alpha(0.09f + Mathf.clamp(0.08f * hit));
            Fill.circle(x, y, realRange);
            Draw.alpha(1f);
            Lines.circle(x, y, realRange);
            Draw.reset();
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(realRange);
        }

        @Override
        public void read(Reads read){
            super.read(read);
            realRange = read.f();
        }
    }
}
