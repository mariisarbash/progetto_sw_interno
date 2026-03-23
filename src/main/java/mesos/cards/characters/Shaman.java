package mesos.cards.characters;

import java.util.Set;
import mesos.enums.CharacterType;
import mesos.enums.Era;
import mesos.enums.PlayerCount;
import mesos.player.Player;

public class Shaman extends CharacterCard {
    private final int shamanIcons;

    public Shaman(String name, Era era, Set<PlayerCount> allowedPlayerCounts, int shamanIcons) {
        super(name, era, allowedPlayerCounts);
        this.shamanIcons = shamanIcons;
    }

    @Override
    public void onPurchase(Player player) {
    }

    @Override
    public CharacterType getType() {
        return CharacterType.SHAMAN;
    }

    @Override
    public int getShamanIcons() {
        return shamanIcons;
    }
}
