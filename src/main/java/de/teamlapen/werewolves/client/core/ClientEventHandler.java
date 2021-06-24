package de.teamlapen.werewolves.client.core;

import de.teamlapen.vampirism.api.entity.player.actions.IActionHandler;
import de.teamlapen.vampirism.client.gui.VampirismScreen;
import de.teamlapen.werewolves.client.gui.WerewolfPlayerAppearanceScreen;
import de.teamlapen.werewolves.core.WerewolfActions;
import de.teamlapen.werewolves.mixin.client.ScreenAccessor;
import de.teamlapen.werewolves.player.IWerewolfPlayer;
import de.teamlapen.werewolves.player.werewolf.WerewolfPlayer;
import de.teamlapen.werewolves.util.Helper;
import de.teamlapen.werewolves.util.REFERENCE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;


@OnlyIn(Dist.CLIENT)
public class ClientEventHandler {
    private int zoomTime = 0;
    private double zoomAmount = 0;
    private double zoomModifier = 0;


    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) event.getPlayer();
        if (shouldRenderWerewolfForm(player)) {
            event.setCanceled(WEntityRenderer.render.render(WerewolfPlayer.get(player), MathHelper.lerp(event.getPartialRenderTick(), player.prevRotationYaw, player.rotationYaw), event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), event.getLight()));
        }
    }

    @SubscribeEvent
    public void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) event.getPlayer();
        if (shouldRenderWerewolfForm(player)) {
            WEntityRenderer.render.renderPost(event.getRenderer().getEntityModel(), WerewolfPlayer.get(player), MathHelper.lerp(event.getPartialRenderTick(), player.prevRotationYaw, player.rotationYaw), event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), event.getLight());
        }
    }

    private boolean shouldRenderWerewolfForm(AbstractClientPlayerEntity player) {
        return Helper.isWerewolf(player) && (WerewolfPlayer.getOpt(player).map(w -> w.getForm().isTransformed()).orElse(false) || (Minecraft.getInstance().currentScreen instanceof WerewolfPlayerAppearanceScreen && ((WerewolfPlayerAppearanceScreen) Minecraft.getInstance().currentScreen).isRenderForm()));
    }

    @SubscribeEvent
    public void onFOVModifier(EntityViewRenderEvent.FOVModifier event) {
        if (this.zoomTime > 0) {
            event.setFOV(event.getFOV() - this.zoomModifier);
            this.zoomModifier -= this.zoomAmount;
            --this.zoomTime;
        }
    }


    @SubscribeEvent
    public void onGuiInitPost(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof VampirismScreen) {
            if (Helper.isWerewolf(Minecraft.getInstance().player)) {
                ResourceLocation BACKGROUND = new ResourceLocation(REFERENCE.VMODID, "textures/gui/vampirism_menu.png");
                ((ScreenAccessor) event.getGui()).invokeAddButton_werewolves(new ImageButton(((VampirismScreen) event.getGui()).getGuiLeft() + 47, ((VampirismScreen) event.getGui()).getGuiTop() + 90, 20, 20, 20, 205, 20, BACKGROUND, 256, 256, (context) -> {
                    Minecraft.getInstance().displayGuiScreen(new WerewolfPlayerAppearanceScreen(event.getGui()));
                }, (button1, matrixStack, mouseX, mouseY) -> {
                    event.getGui().renderTooltip(matrixStack, new TranslationTextComponent("gui.vampirism.vampirism_menu.appearance_menu"), mouseX, mouseY);
                }, StringTextComponent.EMPTY));
            }
        }
    }

    @SubscribeEvent
    public void onRenderNamePlate(RenderNameplateEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            if (Helper.isWerewolf((PlayerEntity) event.getEntity())) {
                WerewolfPlayer werewolf = WerewolfPlayer.get(((PlayerEntity) event.getEntity()));
                IActionHandler<IWerewolfPlayer> d = werewolf.getActionHandler();
                if (d.isActionActive(WerewolfActions.hide_name) && Helper.isFormActionActive(werewolf)) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    public void onZoomPressed() {
        this.zoomTime = 20;
        this.zoomAmount = Minecraft.getInstance().gameSettings.fov / 4 / this.zoomTime;
        this.zoomModifier = Minecraft.getInstance().gameSettings.fov - Minecraft.getInstance().gameSettings.fov / 4;
    }
}
