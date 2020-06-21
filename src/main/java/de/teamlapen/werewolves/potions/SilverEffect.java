package de.teamlapen.werewolves.potions;

import de.teamlapen.werewolves.config.WerewolvesConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.EffectType;

import javax.annotation.Nonnull;

public class SilverEffect extends WerewolvesEffect {
    private static final String MOVEMENT_SPEED = "8ffcfde9-4799-4120-8714-4f479cc6e23e";
    private static final String ARMOR = "19435a2e-9f5b-4d3b-952e-b1f561e06cab";

    public SilverEffect() {
        super("silver", EffectType.HARMFUL, 0xC0C0C0);
        this.addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED, MOVEMENT_SPEED, WerewolvesConfig.BALANCE.POTIONS.silverStatsReduction.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributesModifier(SharedMonsterAttributes.ARMOR, ARMOR, WerewolvesConfig.BALANCE.POTIONS.silverStatsReduction.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void performEffect(@Nonnull LivingEntity entityLivingBaseIn, int amplifier) {
    }
}