package de.teamlapen.werewolves.player.skill;

import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.actions.DefaultAction;

public class ActionSkill<T extends IFactionPlayer<?>> extends de.teamlapen.vampirism.player.skills.ActionSkill<T> {

    public ActionSkill(DefaultAction<T> action) {
        super(action.getRegistryName(), action);
    }

    public ActionSkill(DefaultAction<T> action, boolean customDescription) {
        super(action.getRegistryName(), action, customDescription);
    }
}
