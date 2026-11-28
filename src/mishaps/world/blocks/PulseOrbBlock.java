package mishaps.world.blocks;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.graphics.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import mishaps.world.meta.*;

import static mindustry.Vars.*;

public class PulseOrbBlock extends Block{
    protected static final Seq<Unit> targets = new Seq<>();

    protected static final IntFloatMap orbMap = new IntFloatMap();

    private static long lastUpdateFrame = -1;

    public float minRange = 7f * 8f,
           maxRange = 20f * 8f,
           growSpeed = 0.4f,
           damage = 11f,
           itemUseTime = 100f;

    public DrawBlock drawer = new DrawDefault();

    public Color orbColor = Color.valueOf("8ca9e8");

    public Sound shootSound = Sounds.beamParallax;
    public float shootSoundVolume = 0.9f;

    public PulseOrbBlock(String name){
        super(name);
        update = true;
        solid = true;
        hasPower = true;
        envEnabled |= Env.space;
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public boolean outputsItems(){
        return false;
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
    }

    @Override
    public void load(){
        super.load();
        drawer.load(this);
    }

    @Override
    public void setStats(){
        stats.timePeriod = itemUseTime;
        super.setStats();

        stats.add(MishapStats.special, Core.bundle.format("special.varying-range", minRange / 8, maxRange / 8));
        if(damage > 0) stats.add(Stat.damage, damage * 60f, StatUnit.perSecond);
        stats.add(Stat.productionTime, itemUseTime / 60f, StatUnit.seconds);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);

        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, minRange, orbColor);
        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, maxRange, player.team().color);
    }

    public class PulseOrbBuild extends Building implements Ranged{
        public float baseRange = minRange, rangeScl, hit, warmup, accumulator;
        public boolean expanding, touched;

        @Override
        public float range(){
            return realRange();
        }

        @Override
        public float warmup(){
            return warmup;
        }

        @Override
        public void updateTile(){
            targets.clear();

            if(accumulator >= itemUseTime){
                consume();
                accumulator -= itemUseTime;
            }

            rangeScl = Mathf.lerpDelta(rangeScl, checkSuppression() ? 0f : warmup, 0.05f);

            warmup = Mathf.lerpDelta(warmup, efficiency, 0.1f);

            Units.nearbyEnemies(team, x, y, realRange(), enemy -> {
                if(enemy.targetable(team)) targets.add(enemy);
            });

            touched = targets.size > 0;

            if(touched){
                hit = 1f;
                if(baseRange < maxRange) expanding = true;
            }else{
                if(hit < 0.09f) expanding = false;
            }

            if(touched && expanding){
                baseRange = Math.min(maxRange, baseRange + growSpeed * Time.delta);
            }else if(!expanding && hit < 0.09f){
                baseRange = Math.max(minRange, baseRange - growSpeed * Time.delta);
            }

            if(hit > 0){
                hit -= 1f / 6f * Time.delta;
            }

            if(efficiency > 0){
                for(int i = 0; i < targets.size; i++){
                    Unit other = targets.get(i);
                    if(other.dst(x, y) < realRange()){               
                        int id = other.id;
                        float rawDamage = damage * state.rules.blockDamage(team);
                        orbMap.put(id, Math.max(orbMap.get(id), rawDamage));
                    }
                }
            }
            accumulator += delta() * efficiency;

            if(targets.size > 0 && !headless) control.sound.loop(shootSound, this, shootSoundVolume);

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
        public boolean shouldConsume(){
            return touched;
        }

        public float realRange(){
            return baseRange * rangeScl;
        }

        @Override
        public void draw(){
            drawer.draw(this);
            drawOrb();
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

        public void drawOrb(){
            Draw.color(orbColor, Color.white, Mathf.clamp(hit));
            Draw.z(Layer.flyingUnit + 1f);
            Lines.stroke(1.5f);
            Draw.alpha(0.09f + Mathf.clamp(0.08f * hit));
            Fill.circle(x, y, realRange());
            Draw.alpha(1f);
            Lines.circle(x, y, realRange());
            Draw.reset();
        }

        @Override
        public void write(Writes write){
            super.write(write);
            Log.info("if this doesn't work idk what will");
            write.f(baseRange);
            write.f(rangeScl);
            write.f(warmup);
        }

        @Override
        public void read(Reads read){
            super.read(read);
            Log.info("if this doesn't work idk what will");
            baseRange = read.f();
            rangeScl = read.f();
            warmup = read.f();
        }
    }
}
