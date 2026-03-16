package mesos.model.cards.characters;

import mesos.model.cards.base.TribeDeckCard;
import mesos.model.enums.CharacterType;
import mesos.model.enums.Era;
import mesos.model.enums.PlayerCount;
import mesos.model.game.GameContext;
import mesos.model.player.Player;

import java.util.Set;

public abstract class CharacterCard extends TribeDeckCard {
    protected CharacterCard(String name, Era era, Set<PlayerCount> allowedPlayerCounts) {
        super(name, era, allowedPlayerCounts);
    }

    public void onPurchase(Player player, GameContext context) {

    }

    public abstract CharacterType getType();
}
