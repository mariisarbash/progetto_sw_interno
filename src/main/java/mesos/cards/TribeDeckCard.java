package mesos.cards;

import java.util.EnumSet;
import java.util.Set;
import mesos.enums.Era;
import mesos.enums.PlayerCount;

public abstract class TribeDeckCard extends Card {
    private final Set<PlayerCount> allowedPlayerCounts;

    protected TribeDeckCard(String name, Era era, Set<PlayerCount> allowedPlayerCounts) {
        super(name, era);
        this.allowedPlayerCounts = allowedPlayerCounts == null || allowedPlayerCounts.isEmpty()
                ? EnumSet.allOf(PlayerCount.class)
                : EnumSet.copyOf(allowedPlayerCounts);
    }

    public Set<PlayerCount> getAllowedPlayerCounts() {
        return EnumSet.copyOf(allowedPlayerCounts);
    }
}
