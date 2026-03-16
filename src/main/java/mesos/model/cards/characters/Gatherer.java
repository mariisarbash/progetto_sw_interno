package mesos.model.cards.characters;

import mesos.model.enums.CharacterType;
import mesos.model.enums.Era;
import mesos.model.enums.PlayerCount;

import java.util.Set;

public class Gatherer extends CharacterCard {
    private final int sustenanceDiscount;

    public Gatherer(String name, Era era, Set<PlayerCount> allowedPlayerCounts) {
        super(name, era, allowedPlayerCounts);
        this.sustenanceDiscount = 3;
    }

    @Override
    public CharacterType getType() {
        return CharacterType.GATHERER;
    }

    public int getSustenanceDiscount() {
        return sustenanceDiscount;
    }
}
