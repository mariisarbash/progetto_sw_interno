package mesos.model.cards.events;

import mesos.model.cards.base.TribeDeckCard;
import mesos.model.enums.Era;
import mesos.model.enums.EventType;
import mesos.model.enums.PlayerCount;
import mesos.model.game.GameContext;

import java.util.Set;

public abstract class EventCard extends TribeDeckCard {
    private final EventType eventType;

    protected EventCard(String name, Era era, Set<PlayerCount> allowedPlayerCounts, EventType eventType) {
        super(name, era, allowedPlayerCounts);
        this.eventType = eventType;
    }

    public abstract void resolveEvent(GameContext context);

    public EventType getEventType() {
        return eventType;
    }
}
