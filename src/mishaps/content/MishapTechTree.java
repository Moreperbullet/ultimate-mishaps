package mishaps.content;

import arc.func.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.content.TechTree.*;
import mindustry.ctype.*;
import mindustry.game.Objectives.*;
import mindustry.type.*;

import static mindustry.content.Blocks.*;
import static mindustry.content.SectorPresets.*;
import static mindustry.content.UnitTypes.*;
import static mishaps.content.MishapSectorPresets.*;
import static mishaps.content.MishapBlocks.*;
import static mishaps.content.MishapUnitTypes.*;

@SuppressWarnings({"unused", "CodeBlock2Expr"})
public class MishapTechTree{
    static TechTree.TechNode context = null;

    public static void load(){

        vanillaNode(Items.pyratite, () -> {
            node(MishapItems.diseaseFragments, Seq.with(
                new Produce(MishapItems.diseaseFragments)
            ), () -> {
                node(MishapItems.diseaseVector, Seq.with(
                    new Produce(MishapItems.diseaseVector)
                ));
            });
        });

        vanillaNode(Items.thorium, () -> {
            node(MishapItems.aquamarine, Seq.with(
                new Produce(MishapItems.aquamarine)
            ));
        });

        vanillaNode(vault, () -> {
            node(tankVault);
        });

        vanillaNode(surgeWall, () -> {
            node(aquamarineWall, () -> {
                node(aquamarineWallLarge);
            });
        });

        vanillaNode(plastaniumCompressor, () -> {
            node(plastaniumCrusher);
        });

        vanillaNode(pyratiteMixer, () -> {
            node(diseaseExtractor, () -> {
                node(diseaseMixer);
            });
        });

        vanillaNode(surgeTower, () -> {
            node(surgeLink);
        });

        vanillaNode(phaseWall, () -> {
            node(plastaniumDeflectWall, Seq.with(
                new Research(plastaniumWallLarge),

                new Research(segment)
            ), () -> {
                node(deflectWallLarge);
            });
        });

        vanillaNode(forceProjector, () -> {
            node(densityProjector);
        });
    
        vanillaNode(massDriver, () -> {
            node(payloadPropulsionTower, Seq.with(
                new Research(payloadConveyor)
            ));
        });
 
        vanillaNode(payloadConveyor, () -> {
            node(largePayloadConveyor, () -> {
                node(sConstructor, Seq.with(
                    new Research(mega)
                ), () -> {
                    node(sDeconstructor);
                    node(sPayloadLoader, () -> {
                        node(sPayloadUnloader);
                    });
                });
            });
        });

        vanillaNode(meltdown, () -> {
            node(devastation, Seq.with(
                new SectorComplete(overgrowth),
                new Research(plastaniumCrusher)
            ));
        });

        vanillaNode(scorch, () -> {
            node(vampirism);
        });

        vanillaNode(nova, () -> {
            node(relayer, Seq.with(
                new Research(aphid)
            ), () -> {
                node(announcer, () -> {
                    node(agent, () -> {
                        node(attorney, () -> {
                            node(magistrate);
                        });
                    });
                });
            });
        });

        vanillaNode(mono, () -> {
            node(aphid, Seq.with(
                new SectorComplete(windsweptIslands)
            ), () -> {
                node(acyrtho, () -> {
                    node(mindarus, () -> {
                        node(rhophalo, () -> {
                            node(toxoptera, Seq.with(new SectorComplete(mycelialBastion)), () -> {
                            });
                        });
                    });
                });
            });
        });
    }

    private static void vanillaNode(UnlockableContent parent, Runnable children){
        vanillaNode("serpulo", parent, children);
    }

    private static void vanillaNode(String tree, UnlockableContent parent, Runnable children){
        context = findNode(TechTree.roots.find(r -> r.name.equals(tree)), n -> n.content == parent);
        children.run();
    }

    private static TechNode findNode(TechNode root, Boolf<TechNode> filter){
        if(filter.get(root)) return root;
        for(TechNode node : root.children){
            TechNode search = findNode(node, filter);
            if(search != null) return search;
        }
        return null;
    }

    private static void rebaseNode(Content next){
        rebaseNode("serpulo", next);
    }

    /** Moves a node from its parent to the context node. */
    private static void rebaseNode(String tree, Content next){
        TechNode oldNode = findNode(TechTree.roots.find(r -> r.name.equals(tree)), n -> n.content == next);
        oldNode.parent.children.remove(oldNode);
        context.children.add(oldNode);
        oldNode.parent = context;

        if(oldNode.researchCostMultipliers != context.researchCostMultipliers){
            //Reset multipliers
            ItemStack[] req = ItemStack.copy(oldNode.requirements);
            if(oldNode.researchCostMultipliers.size > 0){
                for(ItemStack itemStack : req){
                    itemStack.amount /= (int) oldNode.researchCostMultipliers.get(itemStack.item, 1f);
                }
            }

            //Apply new multipliers
            if(context.researchCostMultipliers.size > 0){
                for(ItemStack itemStack : req){
                    itemStack.amount *= (int) context.researchCostMultipliers.get(itemStack.item, 1f);
                }
            }
            oldNode.requirements = req;
        }
    }

    private static void node(UnlockableContent content, ItemStack[] requirements, Seq<Objective> objectives, Runnable children){
        TechTree.TechNode node = new TechTree.TechNode(context, content, requirements);
        if(objectives != null) node.objectives = objectives;

        TechTree.TechNode prev = context;
        context = node;
        children.run();
        context = prev;
    }

    private static void node(UnlockableContent content, ItemStack[] requirements, Seq<Objective> objectives){
        node(content, requirements, objectives, () -> {});
    }

    private static void node(UnlockableContent content, Seq<Objective> objectives){
        node(content, content.researchRequirements(), objectives, () -> {});
    }

    private static void node(UnlockableContent content, ItemStack[] requirements){
        node(content, requirements, Seq.with(), () -> {});
    }

    private static void node(UnlockableContent content, ItemStack[] requirements, Runnable children){
        node(content, requirements, null, children);
    }

    private static void node(UnlockableContent content, Seq<Objective> objectives, Runnable children){
        node(content, content.researchRequirements(), objectives, children);
    }

    private static void node(UnlockableContent content, Runnable children){
        node(content, content.researchRequirements(), children);
    }

    private static void node(UnlockableContent block){
        node(block, () -> {});
    }
}
