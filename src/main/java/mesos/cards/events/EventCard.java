package mesos.cards.events;

import java.util.List;
import java.util.Set;
import mesos.cards.TribeDeckCard;
import mesos.enums.EventType;
import mesos.enums.Era;
import mesos.enums.PlayerCount;
import mesos.player.Player;

public abstract class EventCard extends TribeDeckCard {
    private final EventType eventType;

    protected EventCard(String name, Era era, Set<PlayerCount> allowedPlayerCounts, EventType eventType) {
        super(name, era, allowedPlayerCounts);
        this.eventType = eventType;
    }

    public abstract void resolveEvent(List<Player> players);

    public EventType getEventType() {
        return eventType;
    }
}
