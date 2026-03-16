package mesos.model.cards.base;

import mesos.model.enums.Era;
import mesos.model.enums.PlayerCount;

import java.util.HashSet;
import java.util.Set;

public abstract class TribeDeckCard extends Card {
    private final Set<PlayerCount> allowedPlayerCounts;

    protected TribeDeckCard(String name, Era era, Set<PlayerCount> allowedPlayerCounts) {
        super(name, era);
        this.allowedPlayerCounts = allowedPlayerCounts != null ? new HashSet<>(allowedPlayerCounts) : new HashSet<>();
    }

    public Set<PlayerCount> getAllowedPlayerCounts() {
        return Set.copyOf(allowedPlayerCounts);
    }
}
