package mesos.model.cards.events;

import mesos.model.enums.CharacterType;
import mesos.model.enums.Era;
import mesos.model.enums.EventType;
import mesos.model.enums.PlayerCount;
import mesos.model.game.GameContext;
import mesos.model.player.Player;

import java.util.Set;

public class HuntingEvent extends EventCard {
    private final int ppRewardPerHunter;

    public HuntingEvent(String name, Era era, Set<PlayerCount> allowedPlayerCounts, int ppRewardPerHunter) {
        super(name, era, allowedPlayerCounts, EventType.HUNTING);
        this.ppRewardPerHunter = ppRewardPerHunter;
    }

    @Override
    public void resolveEvent(GameContext context) {
        for (Player player : context.getGame().getPlayers()) {
            int hunters = player.getTribe().countCharacters(CharacterType.HUNTER);
            player.addPrestige(hunters * ppRewardPerHunter);
        }
    }
}
