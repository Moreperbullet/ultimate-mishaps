package mishaps.world.blocks;

import arc.graphics.g2d.*;
import arc.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.blocks.storage.*;

public class TankVault extends StorageBlock{
    public TextureRegion bottomRegion;
    public float liquidPadding = 0f;

    public TankVault(String name){
        super(name);
        update = true;
        hasLiquids = true;
        noUpdateDisabled = true;
        canOverdrive = false;
        outputsLiquid = true;
        drawCached = false;
        drawDynamic = true;
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{bottomRegion, region};
    }

    @Override
    public void load(){
        super.load();
        bottomRegion = Core.atlas.find(name + "-bottom");
    }

    public class TankVaultBuild extends StorageBuild{
        @Override
        public void updateTile(){
            dumpLiquid(liquids.current());
        }

        @Override
        public void draw(){
            Draw.rect(bottomRegion, x, y);

            if(liquids.currentAmount() > 0.001f){
                LiquidBlock.drawTiledFrames(size, x, y, liquidPadding, liquids.current(), liquids.currentAmount() / liquidCapacity);
            }

            Draw.rect(region, x, y);
            drawTeamTop();
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return (liquids.current() == liquid || liquids.currentAmount() < 0.2f);
        }
    }
}
