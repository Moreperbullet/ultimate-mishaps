package mishaps.world.blocks;

import arc.*;
import mindustry.gen.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.meta.*;
import mishaps.world.meta.*;

public class ReflectionWall extends Wall{
    public float reflectMod = 0.2f, reflectCap = 280f;

    public ReflectionWall(String name){
        super(name);
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(MishapStats.special, Core.bundle.get("special.reflection"));
        stats.addPercent(MishapStats.reflectModNum, reflectMod);
        if(reflectCap > 0) stats.add(MishapStats.reflectCapNum, reflectCap, StatUnit.none);
    }

    public class ReflectionWallBuild extends WallBuild{
        @Override
        public boolean collision(Bullet bullet){
            super.collision(bullet);
            float finalDamage = bullet.damage() * reflectMod;

            if(reflectCap > 0) finalDamage = Math.min(finalDamage, reflectCap);

            if(finalDamage > 0f && bullet.owner instanceof Healthc c) {
                c.damage(finalDamage);
            }
            return true;
        }
    }
}
