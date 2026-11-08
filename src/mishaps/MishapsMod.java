package mishaps;

import arc.*;
import arc.graphics.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.units.*;
import mindustry.world.blocks.units.UnitFactory.*;
import mindustry.world.meta.*;
import mishaps.content.*;
import mishaps.gen.*;
import mishaps.graphics.*;

import static mindustry.type.ItemStack.with;

@SuppressWarnings("unused")
public class MishapsMod extends Mod{

    public MishapsMod(){
        Events.on(ClientLoadEvent.class, e -> Time.runTask(10f, () -> {
            assignColor(MishapBlocks.hotCarbonStone, Color.valueOf("5f433d"));
            assignColor(MishapBlocks.magmaCarbonStone, Color.valueOf("855443"));
            assignColor(MishapBlocks.shallowSlag, Color.valueOf("cc5035"));
            assignColor(MishapBlocks.ceriseStoneWall, Color.valueOf("DB959F"));
        }));

        Events.on(FileTreeInitEvent.class, e -> Core.app.post(() -> {
            MishapSounds.load();
        }));

    }
    @Override
    public void loadContent(){
        EntityRegistry.register();
        MishapStatusEffects.load();
        MishapItems.load();
        MishapUnitTypes.load();
        MishapBlocks.load();
        MishapSectorPresets.load();
        loadOther();
        progMatsCompat();
        MishapTechTree.load();
    }

    public void loadOther(){
        Blocks.crystallineStoneWall.attributes.set(Attribute.sand, 0.7f);
        Blocks.arkyicWall.attributes.set(Attribute.sand, 1.1f);
        Blocks.redStone.asFloor().attributes.set(Attribute.water, -0.1f);
        Blocks.denseRedStone.asFloor().attributes.set(Attribute.water, -0.1f);
        Blocks.carbonStone.asFloor().attributes.set(Attribute.water, -0.1f);

        BulletType diseaseSpectreBullet = new BasicBulletType(7f, 65){{
            hitSize = 5;
            width = 15f;
            height = 21f;
            reloadMultiplier = 0.5f;
            frontColor = MishapPal.dreadRust;
            backColor = MishapPal.darkDreadRust;
            status = MishapStatusEffects.dreadRust;
            shootEffect = Fx.shootBig;
            statusDuration = 270;
            pierceCap = 2;
            pierceBuilding = true;
            knockback = 0.6f;
            ammoMultiplier = 4;
        }};

        BulletType aquaSpectreBullet = new BasicBulletType(7f, 58){{
            hitSize = 5;
            width = 15f;
            height = 21f;
            frontColor = MishapPal.aquaBright;
            backColor = MishapPal.aquaMedium;
            shootEffect = Fx.shootBig;
            pierceCap = 3;
            pierceBuilding = true;
            knockback = 1.2f;
            ammoMultiplier = 4;
        }};

        BulletType diseaseFuseBullet = new ShrapnelBulletType(){{
            length = 100;
            damage = 66f;
            ammoMultiplier = 5f;
            width = 17f;
            reloadMultiplier = 0.5f;
            toColor = MishapPal.dreadRust;
            shootEffect = smokeEffect = MishapFx.dreadShoot;
            status = MishapStatusEffects.dreadRust;
            statusDuration = 270;
        }};

        ((ItemTurret)Blocks.spectre).ammoTypes.put(MishapItems.diseaseVector, diseaseSpectreBullet);

        ((ItemTurret)Blocks.spectre).ammoTypes.put(MishapItems.aquamarine, aquaSpectreBullet);

        ((ItemTurret)Blocks.fuse).ammoTypes.put(MishapItems.diseaseVector, diseaseFuseBullet);

        ((UnitFactory)Blocks.groundFactory).plans.add(
            new UnitPlan(MishapUnitTypes.relayer, 60f * 30, with(Items.silicon, 20, Items.copper, 30))
        );

        ((UnitFactory)Blocks.airFactory).plans.add(new UnitPlan(
            MishapUnitTypes.aphid, 60f * 30, with(Items.silicon, 30, Items.metaglass, 30))
        );

        ((Reconstructor)Blocks.additiveReconstructor).upgrades.add(
            new UnitType[]{MishapUnitTypes.relayer, MishapUnitTypes.announcer},
            new UnitType[]{MishapUnitTypes.aphid, MishapUnitTypes.acyrtho}
        );

        ((Reconstructor)Blocks.multiplicativeReconstructor).upgrades.add(
            new UnitType[]{MishapUnitTypes.announcer, MishapUnitTypes.agent},
            new UnitType[]{MishapUnitTypes.acyrtho, MishapUnitTypes.mindarus}
        );
    
        ((Reconstructor)Blocks.exponentialReconstructor).upgrades.add(
            new UnitType[]{MishapUnitTypes.agent, MishapUnitTypes.attorney},
            new UnitType[]{MishapUnitTypes.mindarus, MishapUnitTypes.rhophalo}
        );

        ((Reconstructor)Blocks.tetrativeReconstructor).upgrades.add(
            new UnitType[]{MishapUnitTypes.rhophalo, MishapUnitTypes.toxoptera},
            new UnitType[]{MishapUnitTypes.attorney, MishapUnitTypes.magistrate}
        );
    }
    
    public void progMatsCompat(){
        if(Vars.mods.locateMod("prog-mats") == null) return;
        Log.info("[MishapsMod] Meepscellaneous Concepts detected. Loading compatibility...");
        try {
            Item tenelium = Vars.content.item("prog-mats-techtanite");
            MishapBlocks.densityProjector.requirements = ItemStack.with(
                Items.metaglass, 225,
                Items.thorium, 100,
                Items.silicon, 250,
                tenelium, 25
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to load compatibility.", e);
        }
    }

    public static void assignColor(Block block, Color color){
        if(block != null && color != null){
            block.mapColor = color;
        }
    }
}
