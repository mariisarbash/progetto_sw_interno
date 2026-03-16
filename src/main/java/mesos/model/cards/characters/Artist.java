package mesos.model.cards.characters;

import mesos.model.enums.CharacterType;
import mesos.model.enums.Era;
import mesos.model.enums.PlayerCount;

import java.util.Set;

public class Artist extends CharacterCard {
    public Artist(String name, Era era, Set<PlayerCount> allowedPlayerCounts) {
        super(name, era, allowedPlayerCounts);
    }

    @Override
    public CharacterType getType() {
        return CharacterType.ARTIST;
    }
}
