package mesos.model.cards.characters;

import mesos.model.enums.CharacterType;
import mesos.model.enums.Era;
import mesos.model.enums.PlayerCount;
import mesos.model.game.GameContext;
import mesos.model.player.Player;

import java.util.Set;

public class Hunter extends CharacterCard {
    private final boolean hasImmediateFoodIcon;

    public Hunter(String name, Era era, Set<PlayerCount> allowedPlayerCounts, boolean hasImmediateFoodIcon) {
        super(name, era, allowedPlayerCounts);
        this.hasImmediateFoodIcon = hasImmediateFoodIcon;
    }

    @Override
    public void onPurchase(Player player, GameContext context) {
        if (hasImmediateFoodIcon) {
            player.gainFood(1);
        }
    }

    @Override
    public CharacterType getType() {
        return CharacterType.HUNTER;
    }

    public boolean hasImmediateFoodIcon() {
        return hasImmediateFoodIcon;
    }
}
