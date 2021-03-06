package de.teamlapen.werewolves.world;

import de.teamlapen.vampirism.api.entity.CaptureEntityEntry;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.api.event.VampirismVillageEvent;
import de.teamlapen.vampirism.core.ModBlocks;
import de.teamlapen.werewolves.entities.werewolf.IVillagerTransformable;
import de.teamlapen.werewolves.entities.werewolf.WerewolfTransformable;
import de.teamlapen.werewolves.util.WReference;
import net.minecraft.entity.MobEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class WWorldEventHandler {

    @SubscribeEvent
    public void onVillageCaptureFinish(VampirismVillageEvent.VillagerCaptureFinish.Pre event) {
        World world = ((TileEntity) event.getTotem()).getWorld();
        List<MobEntity> werewolves = world.getEntitiesWithinAABB(MobEntity.class, event.getVillageArea(), entity -> entity instanceof WerewolfTransformable);
        if (WReference.WEREWOLF_FACTION.equals(event.getControllingFaction())) {
            werewolves.forEach(e -> {
                if (e instanceof IVillagerTransformable) {
                    ((IVillagerTransformable) e).setWerewolfFaction(true);
                } else {
                    if (((WerewolfTransformable) e).canTransform()) {
                        ((WerewolfTransformable) e).transformBack();
                    } else if (event.isForced()) {
                        spawnEntity(world, getCaptureEntity(event.getCapturingFaction(), world), e, true);
                    }
                }
            });
        } else {
            for (MobEntity werewolf : werewolves) {
                if (werewolf instanceof IVillagerTransformable) {
                    ((IVillagerTransformable) werewolf).transformBack();
                    ((IVillagerTransformable) werewolf).setWerewolfFaction(false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onVillageSpawnNewVillager(VampirismVillageEvent.SpawnNewVillager event) {
        if (event.getControllingFaction() == WReference.WEREWOLF_FACTION) {
            ((IVillagerTransformable) event.getNewVillager()).setWerewolfFaction(true);
        }
    }
    @SubscribeEvent
    public void onVillageReplaceBlock(VampirismVillageEvent.ReplaceBlock event) {
        if (event.getState().getBlock() == ModBlocks.cursed_earth) {
            ((TileEntity) event.getTotem()).getWorld().setBlockState(event.getBlockPos(), ((TileEntity) event.getTotem()).getWorld().getBiome(event.getBlockPos()).getGenerationSettings().getSurfaceBuilderConfig().getTop());
        }
    }
    @SubscribeEvent
    public void onVillageMakeAggressive(VampirismVillageEvent.MakeAggressive event) {
        if (event.getControllingFaction() == WReference.WEREWOLF_FACTION) {
            event.setCanceled(true);
            ((IVillagerTransformable) event.getOldVillager()).transformToWerewolf();
        }
    }

    private void spawnEntity(World world, MobEntity newEntity, MobEntity oldEntity, boolean replaceOld) {
        newEntity.copyDataFromOld(oldEntity);
        newEntity.setUniqueId(MathHelper.getRandomUUID());
        if (replaceOld) oldEntity.remove();
        world.addEntity(newEntity);
    }

    private MobEntity getCaptureEntity(IFaction<?> faction, World world) {
        List<CaptureEntityEntry> entry = faction.getVillageData().getCaptureEntries();
        return WeightedRandom.getRandomItem(world.getRandom(), entry).getEntity().create(world);
    }
}
