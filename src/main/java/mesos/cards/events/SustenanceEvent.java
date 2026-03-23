package mesos.cards.events;

import java.util.List;
import java.util.Set;
import mesos.enums.Era;
import mesos.enums.EventType;
import mesos.enums.PlayerCount;
import mesos.player.Player;

public class SustenanceEvent extends EventCard {
    private final int ppPenaltyPerUnfedCharacter;

    public SustenanceEvent(String name, Era era, Set<PlayerCount> allowedPlayerCounts, int ppPenaltyPerUnfedCharacter) {
        super(name, era, allowedPlayerCounts, EventType.SUSTENANCE);
        this.ppPenaltyPerUnfedCharacter = ppPenaltyPerUnfedCharacter;
    }

    @Override
    public void resolveEvent(List<Player> players) {
        for (Player player : players) {
            int totalCharacters = player.getTribe().getCharacterCards().size();
            int totalDiscount = player.getTribe().calculateSustenanceDiscount()
                    + player.getTribe().getExtraSustenanceDiscount(player);
            int foodNeeded = Math.max(0, totalCharacters - totalDiscount);
            int foodAvailable = player.getFood();
            int unfedCharacters = Math.max(0, foodNeeded - foodAvailable);
            player.payFood(Math.min(foodNeeded, foodAvailable));
            if (unfedCharacters > 0) {
                player.removePrestige(unfedCharacters * ppPenaltyPerUnfedCharacter);
            }
        }
    }

    public int getPpPenaltyPerUnfedCharacter() {
        return ppPenaltyPerUnfedCharacter;
    }
}
