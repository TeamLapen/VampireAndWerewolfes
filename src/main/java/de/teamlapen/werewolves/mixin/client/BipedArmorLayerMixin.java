package de.teamlapen.werewolves.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.teamlapen.werewolves.core.WerewolfActions;
import de.teamlapen.werewolves.core.WerewolfSkills;
import de.teamlapen.werewolves.player.werewolf.WerewolfPlayer;
import de.teamlapen.werewolves.util.Helper;
import de.teamlapen.werewolves.util.WerewolfSize;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedArmorLayer.class)
public class BipedArmorLayerMixin<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> {

    @Inject(method = "func_241739_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/inventory/EquipmentSlotType;ILnet/minecraft/client/renderer/entity/model/BipedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/BipedArmorLayer;setModelSlotVisible(Lnet/minecraft/client/renderer/entity/model/BipedModel;Lnet/minecraft/inventory/EquipmentSlotType;)V", shift = At.Shift.AFTER))
    private void asd(MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, T entity, EquipmentSlotType equipmentSlotType, int p_241739_5_, A model, CallbackInfo ci){
        if (!(entity instanceof PlayerEntity))return;
        if (!Helper.isWerewolf(((PlayerEntity) entity)))return;
        WerewolfPlayer werewolf = WerewolfPlayer.get(((PlayerEntity) entity));
        if (!Helper.isFormActionActive(werewolf)) return;
        if (werewolf.getSkillHandler().isSkillEnabled(WerewolfSkills.wear_armor) && werewolf.getActionHandler().isActionActive(WerewolfActions.human_form)) return;
        model.setVisible(false);
    }
}
