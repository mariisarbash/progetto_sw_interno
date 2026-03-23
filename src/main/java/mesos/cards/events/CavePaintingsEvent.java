package mesos.cards.events;

import java.util.List;
import java.util.Set;
import mesos.enums.CharacterType;
import mesos.enums.Era;
import mesos.enums.EventType;
import mesos.enums.PlayerCount;
import mesos.player.Player;

public class CavePaintingsEvent extends EventCard {
    private final int artistThreshold;
    private final int ppPenalty;
    private final int ppRewardPerArtist;

    public CavePaintingsEvent(String name, Era era, Set<PlayerCount> allowedPlayerCounts,
                              int artistThreshold, int ppPenalty, int ppRewardPerArtist) {
        super(name, era, allowedPlayerCounts, EventType.CAVE_PAINTINGS);
        this.artistThreshold = artistThreshold;
        this.ppPenalty = ppPenalty;
        this.ppRewardPerArtist = ppRewardPerArtist;
    }

    @Override
    public void resolveEvent(List<Player> players) {
        for (Player player : players) {
            int artists = player.getTribe().countCharacters(CharacterType.ARTIST);
            if (artists >= artistThreshold) {
                player.addPrestige(artists * ppRewardPerArtist);
            } else {
                player.removePrestige(ppPenalty);
            }
            int extraFood = player.getTribe().getExtraFoodPerCharacterForEvent(getEventType(), CharacterType.ARTIST, player);
            player.gainFood(artists * extraFood);
        }
    }

    public int getArtistThreshold() {
        return artistThreshold;
    }

    public int getPpPenalty() {
        return ppPenalty;
    }

    public int getPpRewardPerArtist() {
        return ppRewardPerArtist;
    }
}
