package mesos.cards.characters;

import java.util.Set;
import mesos.enums.CharacterType;
import mesos.enums.Era;
import mesos.enums.PlayerCount;
import mesos.player.Player;

public class Hunter extends CharacterCard {
    private final boolean hasImmediateFoodIcon;

    public Hunter(String name, Era era, Set<PlayerCount> allowedPlayerCounts, boolean hasImmediateFoodIcon) {
        super(name, era, allowedPlayerCounts);
        this.hasImmediateFoodIcon = hasImmediateFoodIcon;
    }

    @Override
    public void onPurchase(Player player) {
        if (hasImmediateFoodIcon) {
            int hunterCount = player.getTribe().countCharacters(mesos.enums.CharacterType.HUNTER);
            player.gainFood(hunterCount);
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
