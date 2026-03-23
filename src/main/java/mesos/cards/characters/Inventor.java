package mesos.cards.characters;

import java.util.Set;
import mesos.enums.CharacterType;
import mesos.enums.Era;
import mesos.enums.InventionIcon;
import mesos.enums.PlayerCount;
import mesos.player.Player;

public class Inventor extends CharacterCard {
    private final InventionIcon inventionIcon;

    public Inventor(String name, Era era, Set<PlayerCount> allowedPlayerCounts, InventionIcon inventionIcon) {
        super(name, era, allowedPlayerCounts);
        this.inventionIcon = inventionIcon;
    }

    @Override
    public void onPurchase(Player player) {
    }

    @Override
    public CharacterType getType() {
        return CharacterType.INVENTOR;
    }

    public InventionIcon getInventionIcon() {
        return inventionIcon;
    }
}
