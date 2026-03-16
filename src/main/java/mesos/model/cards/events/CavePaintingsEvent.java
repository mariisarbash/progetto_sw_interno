package mesos.model.cards.events;

import mesos.model.enums.CharacterType;
import mesos.model.enums.Era;
import mesos.model.enums.EventType;
import mesos.model.enums.PlayerCount;
import mesos.model.game.GameContext;
import mesos.model.player.Player;

import java.util.Set;

public class CavePaintingsEvent extends EventCard {
    private final int artistThreshold;
    private final int ppPenalty;
    private final int ppRewardPerArtist;

    public CavePaintingsEvent(String name, Era era, Set<PlayerCount> allowedPlayerCounts, int artistThreshold, int ppPenalty, int ppRewardPerArtist) {
        super(name, era, allowedPlayerCounts, EventType.CAVE_PAINTINGS);
        this.artistThreshold = artistThreshold;
        this.ppPenalty = ppPenalty;
        this.ppRewardPerArtist = ppRewardPerArtist;
    }

    @Override
    public void resolveEvent(GameContext context) {
        for (Player player : context.getGame().getPlayers()) {
            int artists = player.getTribe().countCharacters(CharacterType.ARTIST);
            if (artists < artistThreshold) {
                player.removePrestige(ppPenalty);
            } else {
                player.addPrestige(artists * ppRewardPerArtist);
            }
        }
    }
}
