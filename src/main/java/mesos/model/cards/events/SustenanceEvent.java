package mesos.model.cards.events;

import mesos.model.enums.CharacterType;
import mesos.model.enums.Era;
import mesos.model.enums.EventType;
import mesos.model.enums.PlayerCount;
import mesos.model.game.GameContext;
import mesos.model.player.Player;

import java.util.Set;

public class SustenanceEvent extends EventCard {
    private final int ppPenaltyPerUnfedCharacter;

    public SustenanceEvent(String name, Era era, Set<PlayerCount> allowedPlayerCounts, int ppPenaltyPerUnfedCharacter) {
        super(name, era, allowedPlayerCounts, EventType.SUSTENANCE);
        this.ppPenaltyPerUnfedCharacter = ppPenaltyPerUnfedCharacter;
    }

    @Override
    public void resolveEvent(GameContext context) {
        for (Player player : context.getGame().getPlayers()) {
            int totalCharacters = player.getTribe().getCharacterCards().size();
            int discount = player.getTribe().calculateSustenanceDiscount();
            int foodNeeded = Math.max(0, totalCharacters - discount);
            boolean fullyPaid = player.payFood(foodNeeded);
            if (!fullyPaid) {
                int missing = Math.max(0, foodNeeded - player.getFood());
                player.payFood(player.getFood());
                player.removePrestige(missing * ppPenaltyPerUnfedCharacter);
            }
        }
    }
}
