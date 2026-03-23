package mesos.cards.characters;

import java.util.Set;
import mesos.cards.TribeDeckCard;
import mesos.enums.CharacterType;
import mesos.enums.Era;
import mesos.enums.PlayerCount;
import mesos.player.Player;

public abstract class CharacterCard extends TribeDeckCard {
    protected CharacterCard(String name, Era era, Set<PlayerCount> allowedPlayerCounts) {
        super(name, era, allowedPlayerCounts);
    }

    public abstract void onPurchase(Player player);

    public abstract CharacterType getType();

    public int getBaseSustenanceDiscount() {
        return 0;
    }

    public int getShamanIcons() {
        return 0;
    }
}
