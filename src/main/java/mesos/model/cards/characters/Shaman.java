package mesos.model.cards.characters;

import mesos.model.enums.CharacterType;
import mesos.model.enums.Era;
import mesos.model.enums.PlayerCount;

import java.util.Set;

public class Shaman extends CharacterCard {
    private final int shamanIcons;

    public Shaman(String name, Era era, Set<PlayerCount> allowedPlayerCounts, int shamanIcons) {
        super(name, era, allowedPlayerCounts);
        this.shamanIcons = shamanIcons;
    }

    @Override
    public CharacterType getType() {
        return CharacterType.SHAMAN;
    }

    public int getShamanIcons() {
        return shamanIcons;
    }
}
