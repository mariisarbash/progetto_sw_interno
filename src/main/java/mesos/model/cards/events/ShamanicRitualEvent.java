package mesos.model.cards.events;

import mesos.model.enums.Era;
import mesos.model.enums.EventType;
import mesos.model.enums.PlayerCount;
import mesos.model.game.GameContext;
import mesos.model.player.Player;

import java.util.Set;

public class ShamanicRitualEvent extends EventCard {
    private final int ppReward;
    private final int ppPenalty;

    public ShamanicRitualEvent(String name, Era era, Set<PlayerCount> allowedPlayerCounts, int ppReward, int ppPenalty) {
        super(name, era, allowedPlayerCounts, EventType.SHAMANIC_RITUAL);
        this.ppReward = ppReward;
        this.ppPenalty = ppPenalty;
    }

    @Override
    public void resolveEvent(GameContext context) {
        for (Player player : context.getGame().getPlayers()) {
            if (player.getTribe().getTotalShamanIcons() > 0) {
                player.addPrestige(ppReward);
            } else {
                player.removePrestige(ppPenalty);
            }
        }
    }
}
