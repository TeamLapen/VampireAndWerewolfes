package de.teamlapen.werewolves.entities.werewolf;

import de.teamlapen.vampirism.api.entity.EntityClassType;
import de.teamlapen.vampirism.api.entity.actions.EntityActionTier;
import de.teamlapen.vampirism.entity.goals.LookAtClosestVisibleGoal;
import de.teamlapen.vampirism.entity.hunter.HunterBaseEntity;
import de.teamlapen.vampirism.entity.vampire.VampireBaseEntity;
import de.teamlapen.werewolves.config.WerewolvesConfig;
import de.teamlapen.werewolves.core.ModEntities;
import de.teamlapen.werewolves.player.WerewolfForm;
import de.teamlapen.werewolves.util.Helper;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class HumanWerewolfEntity extends CreatureEntity implements WerewolfTransformable {
    private static final DataParameter<Integer> FORM = EntityDataManager.createKey(HumanWerewolfEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(HumanWerewolfEntity.class, DataSerializers.VARINT);

    private final EntityClassType classType;
    private final EntityActionTier actionTier;

    protected int rage;

    public HumanWerewolfEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.classType = EntityClassType.getRandomClass(this.getRNG());
        this.actionTier = EntityActionTier.Medium;
    }

    public static boolean spawnPredicateHumanWerewolf(EntityType<? extends CreatureEntity> entityType, IServerWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        if (world.getDifficulty() == net.minecraft.world.Difficulty.PEACEFUL) return false;
        if (!MobEntity.canSpawnOn(entityType, world, spawnReason, blockPos, random)) return false;
        if (random.nextInt(3) != 0) return false;
        if (world.canBlockSeeSky(blockPos) && MonsterEntity.isValidLightLevel(world, blockPos, random))  {
            return true;
        }
        return Helper.isInWerewolfBiome(world, blockPos) && blockPos.getY() >= world.getSeaLevel();
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.getDataManager().register(FORM, -1);
        this.getDataManager().register(TYPE, -1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.2));

        this.goalSelector.addGoal(9, new RandomWalkingGoal(this, 0.7));
        this.goalSelector.addGoal(10, new LookAtClosestVisibleGoal(this, PlayerEntity.class, 20F, 0.6F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, HunterBaseEntity.class, 17F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, VampireBaseEntity.class, 17F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (super.attackEntityFrom(source, amount)) {
            this.rage += amount * 10;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (this.rage > 50) {
            WerewolfTransformable werewolf = this.transformToWerewolf();
            ((MobEntity) werewolf).setRevengeTarget(this.getAttackTarget());
        }
    }

    @Override
    public void reset() {
        this.rage = 0;
    }

    public static AttributeModifierMap.MutableAttribute getAttributeBuilder() {
        return CreatureEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, WerewolvesConfig.BALANCE.MOBPROPS.human_werewolf_speed.get())
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 48.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, WerewolvesConfig.BALANCE.MOBPROPS.human_werewolf_attack_damage.get())
                .createMutableAttribute(Attributes.MAX_HEALTH, WerewolvesConfig.BALANCE.MOBPROPS.human_werewolf_max_health.get());
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("form")) {
            int t = compound.getInt("form");
            this.getDataManager().set(FORM, t < 2 && t >= 0 ? t : -1);
        }
        if (compound.contains("type")) {
            int t = compound.getInt("type");
            this.getDataManager().set(TYPE, t < 126 && t >= 0 ? t : -1);
        }
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("form", this.getDataManager().get(FORM));
        compound.putInt("type", this.getDataManager().get(TYPE));
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.getDataManager().get(FORM) == -1) {
            this.getDataManager().set(FORM, this.getRNG().nextInt(2));
        }
        if (this.getDataManager().get(TYPE) == -1) {
            this.getDataManager().set(TYPE, this.getRNG().nextInt(126));
        }
    }

    @Override
    public int getEntityTextureType() {
        int i = this.getDataManager().get(TYPE);
        return Math.max(i, 0);
    }

    @Override
    public int getEyeTextureType() {
        return 0;
    }

    @Override
    public BasicWerewolfEntity _transformToWerewolf() {
        EntityType<? extends BasicWerewolfEntity> type;
        if (this.getDataManager().get(FORM) == 0) {
            type = ModEntities.werewolf_beast;
        } else {
            type = ModEntities.werewolf_survivalist;
        }
        BasicWerewolfEntity werewolf = WerewolfTransformable.copyData(type, this);
        werewolf.setSourceEntity(this);
        return werewolf;
    }

    @Override
    public EntityActionTier getEntityTier() {
        return this.actionTier;
    }

    @Override
    public EntityClassType getEntityClass() {
        return this.classType;
    }

    @Override
    public WerewolfTransformable _transformBack() {
        return this;
    }

    @Override
    public boolean canTransform() {
        return true;
    }

    @Nonnull
    @Override
    public WerewolfForm getWerewolfForm() {
        switch (this.getDataManager().get(FORM)) {
            case 0:
                return WerewolfForm.BEAST;
            case 1:
                return WerewolfForm.SURVIVALIST;
            default:
                throw new IllegalStateException("Werewolf form is not set");
        }
    }
}
