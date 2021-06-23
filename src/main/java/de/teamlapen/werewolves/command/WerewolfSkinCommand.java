package de.teamlapen.werewolves.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.teamlapen.lib.lib.util.BasicCommand;
import de.teamlapen.werewolves.command.arguments.WerewolfFormArgument;
import de.teamlapen.werewolves.player.WerewolfForm;
import de.teamlapen.werewolves.player.werewolf.WerewolfPlayer;
import de.teamlapen.werewolves.util.REFERENCE;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;

public class WerewolfSkinCommand extends BasicCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("skin")
                .then(Commands.literal("beast")
                        .then(Commands.argument("type", IntegerArgumentType.integer(0, WerewolfForm.BEAST.getSkinTypes()-1))
                                .executes(context -> {
                                    return setSkin(context, context.getSource().asPlayer(), IntegerArgumentType.getInteger(context, "type"), WerewolfForm.BEAST);
                                })))
                .then(Commands.literal("survival")
                        .then(Commands.argument("type", IntegerArgumentType.integer(0, WerewolfForm.SURVIVALIST.getSkinTypes()-1))
                                .executes(context -> {
                                    return setSkin(context, context.getSource().asPlayer(), IntegerArgumentType.getInteger(context, "type"), WerewolfForm.SURVIVALIST);
                                })))
                .then(Commands.literal("human")
                        .then(Commands.argument("type", IntegerArgumentType.integer(0, WerewolfForm.HUMAN.getSkinTypes()-1))
                                .executes(context -> {
                                    return setSkin(context, context.getSource().asPlayer(), IntegerArgumentType.getInteger(context, "type"), WerewolfForm.HUMAN);
                                })));
    }

    private static int setSkin(CommandContext<CommandSource> context, PlayerEntity playerEntity, int type, WerewolfForm form) {
        WerewolfPlayer.getOpt(playerEntity).ifPresent(w -> w.setSkinType(form, type));
        return 0;
    }
}
