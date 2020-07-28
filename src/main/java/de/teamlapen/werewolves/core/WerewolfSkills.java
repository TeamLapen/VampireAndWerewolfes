package de.teamlapen.werewolves.core;

import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.player.skills.ActionSkill;
import de.teamlapen.werewolves.player.IWerewolfPlayer;
import de.teamlapen.werewolves.player.SimpleWerewolfSkill;
import de.teamlapen.werewolves.player.werewolf.WerewolfFormUtil;
import de.teamlapen.werewolves.player.werewolf.WerewolfPlayer;
import de.teamlapen.werewolves.player.werewolf.skills.HealthWerewolfSkill;
import de.teamlapen.werewolves.util.REFERENCE;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static de.teamlapen.lib.lib.util.UtilLib.getNull;

@SuppressWarnings("unused")
@ObjectHolder(REFERENCE.MODID)
public class WerewolfSkills {

    public static final ISkill werewolf_form = getNull();
    public static final ISkill night_vision = getNull();
    public static final ISkill bite = getNull();
    public static final ISkill rage = getNull(); //TODO
    public static final ISkill beast_form = getNull();
    public static final ISkill stun_bite = getNull();//TODO
    public static final ISkill better_claws = getNull();//TODO
    public static final ISkill health = getNull();
    public static final ISkill health_reg = getNull();
    public static final ISkill damage = getNull();//TODO
    public static final ISkill resistance = getNull();//TODO
    public static final ISkill health_after_kill = getNull();//TODO
    public static final ISkill howling = getNull();
    public static final ISkill sense = getNull();//TODO
    public static final ISkill survival_form = getNull();
    public static final ISkill speed = getNull();//TODO
    public static final ISkill jump = getNull();//TODO
    public static final ISkill leap = getNull();//TODO
    public static final ISkill fall_damage = getNull();//TODO
    public static final ISkill movement_attacks = getNull();//TODO
    public static final ISkill speed_after_kill = getNull();//TODO
    public static final ISkill sixth_sense = getNull();//TODO
    public static final ISkill hide_name = getNull();//TODO
    public static final ISkill advanced_sense = getNull();//TODO

    static void registerWerewolfSkills(IForgeRegistry<ISkill> registry) {
        registry.register(new SimpleWerewolfSkill(REFERENCE.WEREWOLF_PLAYER_KEY));
        registry.register(new ActionSkill<IWerewolfPlayer>(new ResourceLocation(REFERENCE.MODID, "werewolf_form"), WerewolfActions.werewolf_form).setToggleActions(
                (player) -> ((WerewolfPlayer) player).setForm(WerewolfFormUtil.Form.HUMAN),
                (player) -> ((WerewolfPlayer) player).setForm(WerewolfFormUtil.Form.NONE)));
        registry.register(new SimpleWerewolfSkill.ToggleWerewolfAction("night_vision", (player, value) -> ((WerewolfPlayer) player).getSpecialAttributes().night_vision = value));
        registry.register(new SimpleWerewolfSkill("night_vision").setToggleActions(
                (player) -> ((WerewolfPlayer) player).getSpecialAttributes().night_vision = true,
                (player) -> ((WerewolfPlayer) player).getSpecialAttributes().night_vision = false).setHasDefaultDescription());
        registry.register(new ActionSkill<>(new ResourceLocation(REFERENCE.MODID, "bite"), WerewolfActions.bite));

        //beast tree
        registry.register(new SimpleWerewolfSkill("rage"));//action
        registry.register(new SimpleWerewolfSkill("beast_form").setToggleActions(
                (player) -> ((WerewolfPlayer) player).setForm(WerewolfFormUtil.Form.BEAST),
                (player) -> ((WerewolfPlayer) player).setForm(WerewolfFormUtil.Form.HUMAN)));
        registry.register(new HealthWerewolfSkill());
        registry.register(new SimpleWerewolfSkill("health_reg", true));//skill
        registry.register(new SimpleWerewolfSkill("damage"));//skill
        registry.register(new SimpleWerewolfSkill("resistance"));//skill
        registry.register(new SimpleWerewolfSkill("health_after_kill"));//skill
        registry.register(new SimpleWerewolfSkill("stun_bite"));//skill
        registry.register(new SimpleWerewolfSkill("better_claws"));//skill


        registry.register(new ActionSkill<>(new ResourceLocation(REFERENCE.MODID, "howling"), WerewolfActions.howling));
        registry.register(new SimpleWerewolfSkill("sense"));//action

        //survival tree
        registry.register(new SimpleWerewolfSkill("survival_form").setToggleActions(
                (player) -> ((WerewolfPlayer) player).setForm(WerewolfFormUtil.Form.SURVIVALIST),
                (player) -> ((WerewolfPlayer) player).setForm(WerewolfFormUtil.Form.HUMAN)));
        registry.register(new SimpleWerewolfSkill("speed"));//skill
        registry.register(new SimpleWerewolfSkill("jump"));//skill
        registry.register(new SimpleWerewolfSkill("fall_damage"));//skill
        registry.register(new SimpleWerewolfSkill("leap"));//skill
        registry.register(new SimpleWerewolfSkill("sixth_sense"));//skill
        registry.register(new SimpleWerewolfSkill("speed_after_kill"));//skill
        registry.register(new SimpleWerewolfSkill("movement_attacks"));//skill

        //stealth tree
        registry.register(new SimpleWerewolfSkill("hide_name"));//action
        registry.register(new SimpleWerewolfSkill("advanced_sense"));//skill
    }
}
