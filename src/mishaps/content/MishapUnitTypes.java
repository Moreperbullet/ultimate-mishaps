package mishaps.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import ent.anno.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mishaps.entities.abilities.*;
import mishaps.gen.*;
import mishaps.type.*;
import mishaps.type.weapons.*;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;

@SuppressWarnings("unused")
public class MishapUnitTypes{

    public static @EntityDef({Unitc.class, Phoenixc.class}) UnitType testUnit;

    public static @EntityPoint(LegsUnit.class) UnitType cyatholipid; //tyrant, capricorn, penumbra, hexadeca, livyatan, ateocina

    public static @EntityDef({Unitc.class, Mechc.class, Dodgec.class}) UnitType relayer, announcer, magistrate;

    public static @EntityDef({Unitc.class, Mechc.class, Invisiblec.class}) UnitType agent, attorney; //undercover;

    //comedy, entertain, partygoer, funseeker, satire, parody,

    public static @EntityPoint(UnitEntity.class) UnitType aphid, acyrtho, mindarus, rhophalo, toxoptera; //neoantalus;

    //junior, officer, deputy, senior, enforcer, sheriff;

    public static void load(){

        if(true) testUnit = new MishapUnitType("test-unit"){{
            health = 500;
            hidden = true;
            flying = true;
        }};

        cyatholipid = new MishapUnitType("cyatholipid"){{
            drag = 0.1f;
            speed = 0.4f;
            hitSize = 26f;
            health = 90000;
            armor = 24f;
            lightRadius = 140f;
            stepSound = Sounds.walkerStep;
            stepSoundVolume = 1.1f;

            rotateSpeed = 1.9f;

            legCount = 8;
            legMoveSpace = 0.8f;
            legPairOffset = 3;
            legLength = 80f;
            legExtension = -20;
            legBaseOffset = 10f;
            stepShake = 1f;
            legLengthScl = 0.93f;
            rippleScale = 3f;
            legSpeed = 0.19f;

            legSplashDamage = 120;
            legSplashRange = 60;

            hovering = true;
            shadowElevation = 0.95f;
            groundLayer = Layer.legUnit;
            
            weapons.add(
            new Weapon("large-purple-mount"){{
                y = 3f;
                x = 14f;
                shootY = 7f;
                reload = 75;
                shake = 4f;
                rotateSpeed = 2f;
                ejectEffect = Fx.casing1;
                shootSound = Sounds.shootToxopidShotgun;
                shootSoundVolume = 0.8f;
                rotate = true;
                shadow = 12f;
                recoil = 3f;

                shoot = new ShootSpread(3, 9f);

                bullet = new ShrapnelBulletType(){{
                    length = 90f;
                    damage = 185f;
                    width = 25f;
                    armorMultiplier = 0.8f;
                    serrationLenScl = 7f;
                    serrationSpaceOffset = 60f;
                    serrationFadeOffset = 0f;
                    serrations = 10;
                    serrationWidth = 6f;
                    fromColor = Pal.sapBullet;
                    toColor = Pal.sapBulletBack;
                    shootEffect = smokeEffect = Fx.sparkShoot;
                }};
            }}
            );

            weapons.add(new Weapon("mishaps-ultra-mount"){{
                y = -2f;
                x = 0f;
                shootY = 4f;
                mirror = false;
                cooldownTime = 180f;
                reload = 210;
                shake = 12f;
                recoil = 12f;
                rotateSpeed = 1f;
                ejectEffect = Fx.none;
                shootSound = Sounds.shootOmura;
                rotate = true;
                shadow = 30f;

                rotationLimit = 80f;

                bullet = new RailBulletType(){{
                    shootEffect = MishapFx.railShootColor;
                    length = 530;
                    damage = 1500;
                    pierceEffect = MishapFx.railHitColor;
                    pointEffect = MishapFx.railTrailColor;
                    hitEffect = Fx.sapExplosion;
                    pierceDamageFactor = 0.6f;
                    trailColor = hitColor = Pal.sapBullet;
                    smokeEffect = Fx.shootBig2;
                    pointEffectSpace = 30f;

                    status = StatusEffects.sapped;
                    statusDuration = 60f * 10;
                }};
            }});
        }};

        //region ground spook
        relayer = new MishapUnitType("relayer"){{
            dodge = 0.5f;
            speed = 0.5f;
            hitSize = 8f;
            health = 100;

            weapons.add(new Weapon(name + "-weapon"){{
                top = false;
                shootY = 2f;
                reload = 36f;
                x = 4f;
                y = 0.5f;
                alternate = false;
                ejectEffect = Fx.none;
                recoil = 2f;
                shootSound = Sounds.shootLaser;

                bullet = new LaserBoltBulletType(2.5f, 12){{
                    smokeEffect = Fx.hitLaserColor;
                    hitEffect = Fx.hitLaserColor;
                    despawnEffect = Fx.hitLaserColor;

                    lifetime = 90f;
                    backColor = lightColor = hitColor = Pal.lancerLaser;
                    frontColor = Color.white;
                }};
            }}
            );
        }};

        announcer = new MishapUnitType("announcer"){{
            speed = 0.5f;
            hitSize = 10f;
            health = 350;
            armor = 4f;
            dodge = 2f / 3;

            immunities.add(StatusEffects.shocked);

            weapons.add(new Weapon(name + "-weapon"){{
                top = false;
                shootSound = Sounds.shootLocus;
                shootY = 2f;
                reload = 36f;
                x = 6f;
                recoil = 1f;
                rotate = true;
                rotationLimit = 50;
                rotateSpeed = 1f;
                shootCone = 60f;
                ejectEffect = Fx.none;

                bullet = new RailBulletType(){{
                    length = 130f;
                    damage = 28;
                    hitColor = Pal.lancerLaser;
                    hitEffect = endEffect = Fx.hitBulletColor;
                    pierceDamageFactor = 0.4f;

                    smokeEffect = Fx.colorSpark;
                    status = StatusEffects.shocked;
                    statusDuration = 10f;

                    endEffect = new Effect(14f, e -> {
                        color(e.color);
                        Drawf.tri(e.x, e.y, e.fout() * 1.5f, 5f, e.rotation);
                    });

                    shootEffect = new Effect(10, e -> {
                        color(e.color);
                        float w = 1.2f + 7 * e.fout();

                        Drawf.tri(e.x, e.y, w, 30f * e.fout(), e.rotation);
                        color(e.color);

                        for(int i : Mathf.signs){
                            Drawf.tri(e.x, e.y, w * 0.9f, 18f * e.fout(), e.rotation + i * 90f);
                        }

                        Drawf.tri(e.x, e.y, w, 4f * e.fout(), e.rotation + 180f);
                    });

                    lineEffect = new Effect(20f, e -> {
                        if(!(e.data instanceof Vec2 v)) return;

                        color(e.color);
                        stroke(e.fout() * 0.9f + 0.6f);

                        Fx.rand.setSeed(e.id);
                        for(int i = 0; i < 7; i++){
                            Fx.v.trns(e.rotation, Fx.rand.random(8f, v.dst(e.x, e.y) - 8f));
                            Lines.lineAngleCenter(e.x + Fx.v.x, e.y + Fx.v.y, e.rotation + e.finpow(), e.foutpowdown() * 20f * Fx.rand.random(0.5f, 1f) + 0.3f);
                        }

                        e.scaled(14f, b -> {
                            stroke(b.fout() * 1.5f);
                            color(e.color);
                            Lines.line(e.x, e.y, v.x, v.y);
                        });
                    });
                }};
            }}
            );
        }};


        agent = new MishapUnitType("agent"){{
            speed = 0.43f;
            hitSize = 13f;
            rotateSpeed = 3f;
            targetAir = false;
            health = 560;
            armor = 9f;
            mechFrontSway = 0.55f;
            abilities.add(new InvisibleAbility(0.01f));

            weapons.add(new Weapon(name + "-weapon"){{
                top = false;
                y = 1f;
                x = 9f;
                reload = 20f;
                inaccuracy = 15;
                recoil = 2f;
                shake = 2f;
                ejectEffect = Fx.none;
                shootSound = Sounds.shootPulsar;
                shoot.shots = 4;
                shoot.shotDelay = 0.5f;

                bullet = new LightningBulletType(){{
                    lightningColor = hitColor = Pal.lancerLaser;
                    damage = 24f;
                    lightningLength = 19;
                    lightningLengthRand = 7;

                    lightningType = new BulletType(0.0001f, 0f){{
                        lifetime = Fx.lightning.lifetime;
                        hitEffect = Fx.hitLancer;
                        despawnEffect = Fx.none;
                        status = StatusEffects.shocked;
                        hittable = false;
                        statusDuration = 10f;
                    }};
                }};
            }});
        }};
        
        attorney = new MishapUnitType("attorney"){{
            speed = 0.4f;
            hitSize = 20f;
            rotateSpeed = 2.1f;
            health = 7900f;
            armor = 19f;
            mechFrontSway = 1f;
            mechStepParticles = true;
            stepShake = 0.15f;
            drownTimeMultiplier = 1.4f;
            
            singleTarget = true;
            stepSound = Sounds.mechStep;
            stepSoundPitch = 0.9f;
            stepSoundVolume = 0.25f;
            abilities.add(new InvisibleAbility(0.01f));
            
            immunities = ObjectSet.with(StatusEffects.shocked, StatusEffects.electrified);
            
            weapons.add(new Weapon(name + "-weapon"){{
                top = false;
                y = 1f;
                x = 16f;
                shootY = 8f;
                reload = 45f;
                recoil = 3f;
                shake = 2f;
                ejectEffect = Fx.casing3;
                shootSound = MishapSounds.shootTera;
                shootCone = 20f;
                inaccuracy = 0.2f;

                shoot = new ShootSpread(9, 4.5f);
                velocityRnd = 0.17f;

                bullet = new BasicBulletType(8f, 71){{
                    lifetime = 15f;
                    knockback = 2f;
                    width = 25f;
                    hitSize = 7f;
                    height = 20f;
                    shootEffect = Fx.shootBigColor;
                    smokeEffect = Fx.shootSmokeSquareSparse;
                    hitColor = backColor = trailColor = Pal.techBlue;
                    frontColor = Pal.lancerLaser;
                    trailWidth = 6f;
                    trailLength = 3;
                    hitEffect = despawnEffect = Fx.hitSquaresColor;

                    status = StatusEffects.shocked;
                    statusDuration = 10f;
                    lightning = 2;
                    lightningLength = 5;
                    lightningColor = Pal.lancerLaser;
                    lightningDamage = 15;
                    despawnSound = Sounds.shockBullet;
                }};
            }});
        }};

        magistrate = new MishapUnitType("magistrate"){{
            health = 21000f;
            dodge = 0.1f;
            speed = 0.4f;
            hitSize = 30f;
            armor = 28f;
            rotateSpeed = 1.7f;
            mechStepParticles = true;
            stepShake = 0.75f;
            drownTimeMultiplier = 1.6f;
            mechFrontSway = 1.9f;
            mechSideSway = 0.6f;
            stepSound = Sounds.mechStepHeavy;
            stepSoundPitch = 0.9f;
            stepSoundVolume = 0.45f;

            weapons.add(
            new Weapon(name + "-weapon"){{
                top = false;
                y = 1f;
                x = 20f;
                shootY = 10f;
                reload = 195f;
                shootSound = Sounds.shootNavanax;

                bullet = new ArtilleryBulletType(3.1f, 410f){{
                    hitEffect = new MultiEffect(Fx.titanExplosion, Fx.titanSmoke);
                    despawnEffect = Fx.none;
                    knockback = 3f;
                    lifetime = 130f;
                    height = 17f;
                    width = 15f;
                    splashDamageRadius = 60f;
                    splashDamage = 410f;
                    scaledSplashDamage = true;
                    backColor = hitColor = trailColor = Pal.lancerLaser;
                    frontColor = Color.white;
                    hitSound = Sounds.explosionTitan;

                    trailLength = 32;
                    trailWidth = 3.35f;
                    trailSinScl = 2.5f;
                    trailSinMag = 0.5f;
                    trailEffect = Fx.none;
                    despawnShake = 7f;

                    shootEffect = Fx.shootTitan;
                    smokeEffect = Fx.shootSmokeTitan;

                    trailInterp = v -> Math.max(Mathf.slope(v), 0.8f);
                    shrinkX = 0.2f;
                    shrinkY = 0.1f;
                }};
            }}
            );
        }};
        //endregion
        
        //region insectoid air
        aphid = new MishapUnitType("aphid"){{
            speed = 3f;
            accel = 0.06f;
            drag = 0.01f;
            flying = true;
            health = 120;
            engineOffset = 5.75f;
            targetFlags = new BlockFlag[]{BlockFlag.generator, BlockFlag.battery, null};
            hitSize = 9;
            itemCapacity = 15;
            rotateSpeed = 5f;
            wreckSoundVolume = 0.7f;

            moveSound = Sounds.loopThruster;
            moveSoundPitchMin = 0.3f;
            moveSoundPitchMax = 1.5f;
            moveSoundVolume = 0.2f;

            weapons.add(new Weapon(){{
                shootSound = Sounds.shootElude;
                y = 1.5f;
                x = 0f;
                mirror = false;
                reload = 50f;

                shootCone = 30f;

                shoot = new ShootSpread(3, 11f);

                bullet = new BasicBulletType(7f, 9){{
                    recoil = 0.1f;
                    homingPower = 0.2f;
                    width = 7f;
                    height = 12f;
                    lifetime = 20f;
                    shootEffect = Fx.sparkShoot;
                    smokeEffect = Fx.shootBigSmoke;
                    hitColor = backColor = trailColor = Pal.sap;
                    frontColor = Color.white;
                    trailWidth = 1.5f;
                    trailLength = 5;
                    hitEffect = despawnEffect = Fx.hitBulletColor;
                }};
            }});
        }};

        acyrtho = new MishapUnitType("acyrtho"){{
            armor = 3f;
            speed = 2.7f;
            accel = 0.08f;
            drag = 0.03f;
            flying = true;
            health = 350;
            engineOffset = 6f;
    
            targetFlags = new BlockFlag[]{BlockFlag.generator, BlockFlag.battery, null};
            hitSize = 11;
            itemCapacity = 15;
    
            weapons.add(new Weapon(name + "-weapon"){{
                reload = 30f;
                x = 4f;
                y = 2f;
                rotate = true;
                shake = 1f;
                inaccuracy = 2f;
                velocityRnd = 0.2f;
                shootSound = Sounds.shootMissile;

                bullet = new MissileBulletType(3f, 7){{
                    splashDamage = 21f;
                    splashDamageRadius = 18f;
                    width = 6f;
                    height = 8f;
                    shrinkY = 0f;
                    drag = -0.003f;
                    homingRange = 60f;
                    scaleKeepVelocity = true;
                    lifetime = 50f;
                    trailColor = backColor = Pal.sapBulletBack;
                    frontColor = Pal.sapBullet;
                    weaveScale = 6f;
                    weaveMag = 1f;
                }};
            }});
        }};

        mindarus = new MishapUnitType("mindarus"){{
            armor = 7f;
            speed = 1.9f;
            accel = 0.09f;
            drag = 0.04f;
            flying = true;
            health = 690;

            engineSize = 2.7f;
            engineOffset = 7.5f;
    
            targetFlags = new BlockFlag[]{BlockFlag.generator, BlockFlag.battery, null};
            hitSize = 20;
            itemCapacity = 30;
    
            weapons.add(
            new Weapon(acyrtho.name + "-weapon"){{
                reload = 70f;
                x = 11f;
                y = 0.5f;
                top = false;
                rotate = false;
                layerOffset = -1f;
                shake = 1f;
                inaccuracy = 2f;
                velocityRnd = 0.2f;
                shootSound = Sounds.shootMissile;

                bullet = new MissileBulletType(3f, 3){{
                    splashDamage = 26f;
                    splashDamageRadius = 25f;
                    width = 7f;
                    height = 9f;
                    shrinkY = 0f;
                    drag = -0.003f;
                    homingRange = 60f;
                    scaleKeepVelocity = true;
                    lifetime = 60f;
                    status = StatusEffects.sapped;
                    statusDuration = 90f;
                    trailColor = backColor = Pal.sapBulletBack;
                    frontColor = Pal.sapBullet;
                    weaveScale = 6f;
                    weaveMag = 1f;
                }};
            }},
            new Weapon(acyrtho.name + "-weapon"){{
                reload = 30f;
                x = 7f;
                y = 8f;
                top = false;
                rotate = false;
                layerOffset = -1f;
                shake = 1f;
                inaccuracy = 2f;
                velocityRnd = 0.2f;
                shootSound = Sounds.shootMissile;

                bullet = new MissileBulletType(5f, 3){{
                    splashDamage = 18f;
                    splashDamageRadius = 18f;
                    width = 6f;
                    height = 8f;
                    shrinkY = 0f;
                    drag = -0.003f;
                    homingRange = 60f;
                    scaleKeepVelocity = true;
                    lifetime = 37f;
                    status = StatusEffects.sapped;
                    statusDuration = 90f;
                    trailColor = backColor = Pal.sapBulletBack;
                    frontColor = Pal.sapBullet;
                    weaveScale = 6f;
                    weaveMag = 1f;
                }};
            }}
            );
        }};

        rhophalo = new MishapUnitType("rhophalo"){{
            speed = 0.9f;
            accel = 0.03f;
            drag = 0.03f;
            rotateSpeed = 1.9f;
            flying = true;
            lowAltitude = true;
            health = 7000;
            armor = 17f;
            engineOffset = 21;
            engineSize = 5.4f;
            hitSize = 38f;
            targetFlags = new BlockFlag[]{BlockFlag.reactor, BlockFlag.generator, BlockFlag.core, null};

            loopSound = Sounds.loopHover;

            weapons.add(
            new Weapon(name + "-salvo"){{
                x = 17f;
                y = 6f;
                reload = 45f;
                shoot.shots = 4;
                shoot.shotDelay = 3f;
                ejectEffect = Fx.casing1;
                rotateSpeed = 8f;
                bullet = new BasicBulletType(3.5f, 28){{
                    width = 9f;
                    height = 12f;
                    lifetime = 50f;

                    hitEffect = despawnEffect = Fx.hitBulletColor;
                    hitColor = backColor = trailColor = Pal.sapBulletBack;
                    frontColor = Pal.sapBullet;
                    status = StatusEffects.sapped;
                    statusDuration = 30f;
                }};
                shootSound = Sounds.shootSalvo;
                rotate = true;
                shadow = 6f;
            }},
            new TractorBeamWeapon(name + "-parallax"){{
                x = 14f;
                y = -4f;
                shootY = 5f;

                //doesn't spawn, but assigns the stats
                bullet = new ContinuousBulletType(){{
                    damage = 0.666667f;
                    damageInterval = 1f;
                    pierce = false;
                    pierceArmor = true;
                    maxRange = 140f;
                    status = StatusEffects.sapped;
                    statusDuration = 15f;
                }};
            }}
            );
        }};

        toxoptera = new MishapUnitType("toxoptera"){{
            speed = 0.7f;
            accel = 0.03f;
            drag = 0.03f;
            rotateSpeed = 1.9f;
            flying = true;
            lowAltitude = true;
            health = 21000;
            armor = 23f;
            engineOffset = 36;
            engineSize = 8.6f;
            hitSize = 58f;
            targetFlags = new BlockFlag[]{BlockFlag.reactor, BlockFlag.generator, BlockFlag.core, null};
            loopSound = Sounds.loopHover;

            BulletType tractorBullet = new ContinuousBulletType(){{
                damage = 0.5f;
                damageInterval = 1f;
                pierce = false;
                pierceArmor = true;
                maxRange = 250f;
                status = StatusEffects.sapped;
                statusDuration = 15f;
            }};

            weapons.add(
                new TractorBeamWeapon(rhophalo.name + "-parallax"){{
                    x = 19f;
                    y = -16f;
                    force = 3f;
                    shootY = 5f;
                    bullet = tractorBullet;
                }},
                new TractorBeamWeapon(rhophalo.name + "-parallax"){{
                    x = 21f;
                    y = 12f;
                    force = 3f;
                    shootY = 5f;
                    bullet = tractorBullet;
                }},
                new Weapon(name + "-mount"){{
                    x = 10f;
                    y = -1f;
                    shootY = 9f;

                    shoot = new ShootAlternate(5f);

                    bullet = new BasicBulletType(7.5f, 42){{
                        hitSize = 4f;
                        width = 10f;
                        height = 16f;
                        shootEffect = Fx.shootBig;
                        knockback = 0.3f;

                        hitEffect = despawnEffect = Fx.hitBulletColor;
                        hitColor = backColor = trailColor = Pal.sapBulletBack;
                        frontColor = Pal.sapBullet;
                    }};
                    shootSound = Sounds.shootSpectre;
                    reload = 8f;
                    rotateSpeed = 2f;
                    rotate = true;
                    recoil = 3f;
                    shake = 2f;
                }}
            );
        }};
        //endregion
        EntityRegistry.registerUnits();
    }
}
