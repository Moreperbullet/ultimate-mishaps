package mishaps.content;

import arc.graphics.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import mishaps.graphics.*;
import mishaps.world.blocks.*;

import static mindustry.type.ItemStack.*;

public class MishapBlocks{
    public static Block
    //enviroment
    hotCarbonStone, magmaCarbonStone, graphiticFloor, ceriseStone, redIceStone, shallowSlag, sunkenCoreZone, carbonPebbles, beryllicVent,
    redIceVent, ceriseVent, ceriseStoneWall, redGraphiticWall, ceriseBoulder, oreDiseaseFragments, oreAquamarine,
    //payloads
    largePayloadConveyor, sConstructor, sDeconstructor, sPayloadLoader, sPayloadUnloader, payloadPropulsionTower,
    //crafters
    plastaniumCrusher, diseaseExtractor, diseaseMixer, aquamarinator,
    //storage
    tankVault,
    //other
    surgeLink, plastaniumDeflectWall, deflectWallLarge, aquamarineWall, aquamarineWallLarge, densityProjector,
    //turrets
    vampirism, devastation;

    public static void load(){

        hotCarbonStone = new Floor("hotcarbon-stone"){{
            hasColor = true;
            mapColor = Color.valueOf("5f433d");
            attributes.set(Attribute.heat, 0.5f);
            attributes.set(Attribute.water, -0.5f);
            wall = Blocks.carbonWall;
            blendGroup = Blocks.carbonStone;
            decoration = Blocks.carbonBoulder;
            variants = 3;
            emitLight = true;
            lightRadius = 30f;
            lightColor = Color.orange.cpy().a(0.15f);
        }};

        magmaCarbonStone = new Floor("magmacarbon-stone"){{
            hasColor = true;
            mapColor = Color.valueOf("855443");
            attributes.set(Attribute.heat, 0.75f);
            attributes.set(Attribute.water, -0.75f);
            wall = Blocks.carbonWall;
            blendGroup = Blocks.carbonStone;
            decoration = Blocks.carbonBoulder;
            variants = 3;
            emitLight = true;
            lightRadius = 50f;
            lightColor = Color.orange.cpy().a(0.3f);
        }};

        graphiticFloor = new Floor("graphitic-floor"){{
            mapColor = Color.valueOf("444c67");
            itemDrop = Items.graphite;
            playerUnmineable = true;
            attributes.set(Attribute.water, -0.1f);
            wall = Blocks.carbonWall;
            blendGroup = Blocks.carbonStone;
            decoration = Blocks.carbonBoulder;
            variants = 3;
        }};

        ceriseStone = new Floor("cerise-stone"){{
            variants = 4;
        }};

        redIceStone = new Floor("red-ice-stone"){{
            dragMultiplier = 0.6f;
            variants = 3;
            attributes.set(Attribute.water, 0.3f);
            albedo = 0.6f;
            decoration = Blocks.redIceBoulder;
            wall = Blocks.redIceWall;
        }};

        shallowSlag = new Floor("shallow-slag"){{
            hasColor = true;
            mapColor = Color.valueOf("cc5035");
            status = StatusEffects.melting;
            statusDuration = 240f;
            speedMultiplier = 0.19f;
            variants = 0;
            liquidDrop = Liquids.slag;
            isLiquid = true;
            cacheLayer = CacheLayer.slag;
            liquidMultiplier = 0.5f;
            attributes.set(Attribute.heat, 0.8f);

            emitLight = true;
            lightRadius = 30f;
            lightColor = Color.orange.cpy().a(0.38f);
        }};

        sunkenCoreZone = new Floor("sunken-core-zone"){{
            speedMultiplier = 0.8f;
            statusDuration = 50f;
            albedo = 0.9f;
            supportsOverlay = true;

            status = StatusEffects.wet;
            cacheLayer = CacheLayer.water;
            isLiquid = true;
            liquidDrop = Liquids.water;
            variants = 0;
            allowCorePlacement = true;
        }};

        carbonPebbles = new OverlayFloor("carbon-pebbles");

        beryllicVent = new SteamVent("beryllic-vent"){{
            parent = blendGroup = Blocks.beryllicStone;
            attributes.set(Attribute.steam, 1f);
        }};

        redIceVent = new SteamVent("red-ice-vent"){{
            parent = blendGroup = Blocks.redIce;
            attributes.set(Attribute.steam, 1f);
        }};

        ceriseVent = new SteamVent("cerise-vent"){{
            parent = blendGroup = ceriseStone;
            attributes.set(Attribute.steam, 1f);
        }};

        ceriseStoneWall = new StaticWall("cerise-stone-wall"){{
            ceriseStone.asFloor().wall = this;
            attributes.set(Attribute.sand, 1.1f);
        }};

        redGraphiticWall = new StaticWall("red-graphitic-wall"){{
            itemDrop = Items.graphite;
            variants = 3;
        }};

        ceriseBoulder = new Prop("cerise-boulder"){{
            variants = 2;
            ceriseStone.asFloor().decoration = this;
        }};

        oreDiseaseFragments = new OreBlock("ore-disease-fragments", MishapItems.diseaseFragments){{
            variants = 3;
        }};

        oreAquamarine = new OreBlock("ore-aquamarine", MishapItems.aquamarine){{
            variants = 4;
        }};

        largePayloadConveyor = new PayloadConveyor("large-payload-conveyor"){{
            requirements(Category.units, with(Items.graphite, 30, Items.copper, 30, Items.titanium, 12));
            canOverdrive = false;
            moveTime = 75f;
            payloadLimit = 4.5f;
            size = 5;
        }};

        sConstructor = new Constructor("s-constructor"){{
            requirements(Category.units, with(Items.silicon, 50, Items.graphite, 120, Items.thorium, 60));
            hasPower = true;
            buildSpeed = 0.8f;
            consumePower(3f);
            size = 3;
            filter = Seq.with(
                Blocks.scrapWallLarge,
                Blocks.copperWallLarge,
                Blocks.titaniumWallLarge,
                Blocks.thoriumWallLarge,
                Blocks.plastaniumWallLarge,
                Blocks.surgeWallLarge,
                Blocks.liquidContainer,
                Blocks.container,
                Blocks.powerNode,
                Blocks.mender,
                Blocks.mendProjector,
                Blocks.cultivator
            );
        }};

        sDeconstructor = new PayloadDeconstructor("s-deconstructor"){{
            requirements(Category.units, with(Items.silicon, 50, Items.metaglass, 100, Items.thorium, 60, Items.graphite, 80));
            itemCapacity = 100;
            consumePower(3f);
            size = 3;
            deconstructSpeed = 2f;
        }};

        sPayloadLoader = new PayloadLoader("s-payload-loader"){{
            requirements(Category.units, with(Items.silicon, 50, Items.titanium, 70, Items.graphite, 50));
            hasPower = true;
            consumePower(3f);
            size = 3;
        }};

        sPayloadUnloader = new PayloadUnloader("s-payload-unloader"){{
            requirements(Category.units, with(Items.silicon, 50, Items.titanium, 70, Items.graphite, 50));
            hasPower = true;
            consumePower(3f);
            size = 3;
        }};

        payloadPropulsionTower = new PayloadMassDriver("payload-propulsion-tower"){{
            requirements(Category.units, with(Items.thorium, 300, Items.silicon, 200, Items.plastanium, 200, Items.phaseFabric, 50));
            size = 5;
            reload = 130f;
            chargeTime = 100f;
            range = 1000f;
            maxPayloadSize = 3.5f;
            consumePower(6f);
        }};

        plastaniumCrusher = new GenericCrafter("plastanium-crusher"){{
            requirements(Category.crafting, with(Items.silicon, 120, Items.metaglass, 150, Items.graphite, 100, Items.titanium, 100, Items.plastanium, 30));
            hasItems = true;
            itemCapacity = 20;
            liquidCapacity = 60f;
            craftTime = 60f * 1.5f;
            outputItem = new ItemStack(Items.plastanium, 4);
            size = 3;
            health = 400;
            hasPower = hasLiquids = true;
            craftEffect = Fx.formsmoke;
            updateEffect = Fx.plasticburn;
            drawer = new DrawMulti(new DrawDefault(), new DrawFade());

            consumeLiquid(Liquids.oil, 0.5f);
            consumePower(4f);
            consumeItems(with(Items.titanium, 6, Items.coal, 2));
        }};
        
        diseaseExtractor = new Separator("disease-extractor"){{
            requirements(Category.production, with(Items.copper, 25, Items.titanium, 25, Items.silicon, 10));
            results = with(
                Items.sand, 9,
                MishapItems.diseaseFragments, 1
            );
            craftTime = 45f;
            size = 2;
            drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-rotator"){{
                spinSprite = true;
                rotateSpeed = 6;
                layer = Layer.block + 0.2f;
            }}, new DrawRegion("-top"){{
                layer = Layer.block + 0.3f;
            }});
            hasLiquids = true;
            hasPower = true;
            hasItems = true;

            consumePower(130f / 60f);
            consumeLiquid(Liquids.slag, 6f / 60f);
        }};

        diseaseMixer = new GenericCrafter("disease-mixer"){{
            requirements(Category.crafting, with(Items.copper, 50, Items.lead, 30, Items.titanium, 30));
            hasItems = true;
            hasPower = true;
            itemCapacity = 12;
            outputItem = new ItemStack(MishapItems.diseaseVector, 1);

            size = 2;
            craftTime = 75f;

            consumePower(0.3f);
            consumeItem(MishapItems.diseaseFragments, 6);
        }};

        aquamarinator = new AttributeCrafter("aquamarinator"){{
            requirements(Category.crafting, with(Items.titanium, 120, Items.silicon, 100, Items.plastanium, 40, Items.metaglass, 90));
            floating = true;
            craftTime = 150f;
            baseEfficiency = 0f;
            size = 4;
            hasPower = true;
            hasLiquids = true;
            boostScale = 0.05f;
            outputScale = 0.1f;
            liquidCapacity = 80f;
            itemCapacity = 40;
            ambientSound = Sounds.loopHum;
            ambientSoundVolume = 0.06f;

            outputItem = new ItemStack(MishapItems.aquamarine, 2);

            drawer = new DrawMulti(
                    new DrawRegion ("-bottom"),
                    new DrawLiquidTile(Liquids.slag, 2.1f),
                    new DrawDefault(),
                    new DrawGlowRegion(){{
                        color = Pal.slagOrange;
                    }}
            );

            consumePower(12f);
            consumeLiquids(LiquidStack.with(Liquids.cryofluid, 20f / 60f, Liquids.slag, 21f / 60f));
        }};

        tankVault = new TankVault("tank-vault"){{
            requirements(Category.effect, with(Items.titanium, 210, Items.thorium, 120, Items.metaglass, 30));
            size = 3;
            liquidCapacity = 1440f;
            itemCapacity = 720;
            scaledHealth = 60;
            fullOverride = "mishaps-tank-vault-full";
        }};

        surgeLink = new PowerNode("surge-link"){{
            requirements(Category.power, with(Items.lead, 300, Items.silicon, 180, Items.titanium, 120, Items.phaseFabric, 60, Items.surgeAlloy, 60));
            size = 3;
            maxNodes = 2;
            laserRange = 300f;
            autolink = false;
            drawRange = false;
            sameBlockConnection = true;
            laserScale = 0.8f;
            scaledHealth = 140;
            schematicPriority = -15;
        }};

        plastaniumDeflectWall = new ReflectionWall("plastanium-deflect-wall"){{
            requirements(Category.defense, with(Items.phaseFabric, 4, Items.plastanium, 6, Items.metaglass, 4));
            health = 200 * 4;
            insulated = true;
            absorbLasers = true;
        }};

        deflectWallLarge = new ReflectionWall("plastanium-deflect-wall-large"){{
            requirements(Category.defense, mult(plastaniumDeflectWall.requirements, 4));
            health = 200 * 16;
            size = 2;
            insulated = true;
            absorbLasers = true;
        }};

        aquamarineWall = new Wall("aquamarine-wall"){{
            requirements(Category.defense, with(MishapItems.aquamarine, 6, Items.surgeAlloy, 4));
            health = 280 * 4;
        }};

        aquamarineWallLarge = new Wall("aquamarine-wall-large"){{
            requirements(Category.defense, mult(aquamarineWall.requirements, 4));
            health = 280 * 16;
            size = 2;
        }};

        densityProjector = new DensityProjector("density-projector"){{
            requirements(Category.effect, with(Items.metaglass, 225, Items.thorium, 100, Items.silicon, 250, Items.phaseFabric, 75));
            size = 4;
            sides = 8;
            phaseRadiusBoost = 0f;
            phaseShieldBoost = 0f;
            radius = 195f;

            baseForceArmor = 24f;
            shieldHealth = 6000f;
            cooldownNormal = 2.7f;
            cooldownLiquid = 1.4f;
            cooldownBrokenBase = 0.4f;
            fullOverride = "mishaps-density-projector-full";

            itemConsumer = consumeItems(with(Items.phaseFabric, 2, Items.silicon, 4));
            consumePower(12f);
        }};

        vampirism = new ItemTurret("vampirism"){{
            requirements(Category.turret, with(Items.lead, 100, Items.silicon, 70, Items.titanium, 80));
            ammo(
                Items.lead, new SapBulletType(){{
                    sapStrength = 0.3f;
                    length = 85f;
                    damage = 20;
                    shootEffect = Fx.shootSmall;
                    hitColor = color = Color.valueOf("bf92f9");
                    despawnEffect = Fx.none;
                    width = 0.54f;
                    lifetime = 35f;
                    knockback = -1.2f;
                    ammoMultiplier = 2;
                }},
                Items.graphite, new SapBulletType(){{
                    sapStrength = 0.6f;
                    length = 85f;
                    damage = 48;
                    shootEffect = Fx.shootSmall;
                    hitColor = color = MishapPal.graphitic;
                    despawnEffect = Fx.none;
                    width = 1f;
                    lifetime = 35f;
                    knockback = -4f;
                    reloadMultiplier = 0.7f;
                    ammoMultiplier = 4;
                }},
                Items.thorium, new SapBulletType(){{
                    sapStrength = 0.6f;
                    length = 145f;
                    damage = 32;
                    rangeChange = 60f;
                    shootEffect = Fx.shootSmall;
                    hitColor = color = Pal.thoriumAmmoBack;
                    despawnEffect = Fx.none;
                    width = 1f;
                
                    lifetime = 35f;
                
                    knockback = -3f;
                    ammoMultiplier = 2;
                 }},
                 MishapItems.diseaseVector, new SapBulletType(){{
                    sapStrength = 0.6f;
                    length = 85f;
                    damage = 32;
                    shootEffect = Fx.shootSmall;
                    hitColor = color = MishapPal.dreadRust;
                    despawnEffect = Fx.none;
                    width = 1f;
                
                    lifetime = 35f;
                    reloadMultiplier = 0.5f;
                    knockback = -2f;
                    status = MishapStatusEffects.dreadRust;
                    ammoMultiplier = 4;
                 }}
            );
            scaledHealth = 280;
            size = 2;
            range = 85f;
            reload = 25f;
            recoil = 2f;
            shootSound = Sounds.shootSap;
            shootY = 9f;
            researchCostMultiplier = 0.05f;
            coolant = consumeCoolant(0.2f);
        }};

        devastation = new PowerTurret("devastation"){{
            requirements(Category.turret, with(Items.lead, 650, Items.titanium, 350, Items.metaglass, 370, Items.surgeAlloy, 255, Items.silicon, 275));
            recoil = 3.5f;
            reload = 420f;
            shake = 7f;
            range = 390f;
            cooldownTime = reload;

            shoot = new ShootSpread(3, 7.5f);
            shoot.firstShotDelay = MishapFx.orangeLaserCharge.lifetime;

            shootSound = Sounds.shootCorvus;
            chargeSound = Sounds.chargeCorvus;
            scaledHealth = 200;
            size = 4;
            moveWhileCharging = false;
            shootY = 6f;
  
            float brange = range + 10f;

            shootType = new LaserBulletType(460){{
                length = brange;
                width = 50f;

                lifetime = 65f;
                lightColor = Pal.powerLight;
                largeHit = true;
                chargeEffect = MishapFx.orangeLaserCharge;

                sideAngle = 15f;
                sideWidth = 0f;
                sideLength = 0f;
                colors = new Color[]{Color.valueOf("ec7458aa"), Color.valueOf("ff9c5a"), Color.white};
                incendChance = 0.8f;
                incendSpread = 6f;
                incendAmount = 1;
                pierceArmor = true;
                status = StatusEffects.burning;
                statusDuration = 810f;
            }};
            unitSort = UnitSorts.strongest;
            coolantMultiplier = 0.5f;
            coolant = consumeCoolant(1.25f);

            consumePower(11.5f);
        }};
    }
}
