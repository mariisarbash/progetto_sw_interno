package mesos.model.cards.characters;

import mesos.model.enums.CharacterType;
import mesos.model.enums.Era;
import mesos.model.enums.InventionIcon;
import mesos.model.enums.PlayerCount;

import java.util.Set;

public class Inventor extends CharacterCard {
    private final InventionIcon inventionIcon;

    public Inventor(String name, Era era, Set<PlayerCount> allowedPlayerCounts, InventionIcon inventionIcon) {
        super(name, era, allowedPlayerCounts);
        this.inventionIcon = inventionIcon;
    }

    @Override
    public CharacterType getType() {
        return CharacterType.INVENTOR;
    }

    public InventionIcon getInventionIcon() {
        return inventionIcon;
    }
}
