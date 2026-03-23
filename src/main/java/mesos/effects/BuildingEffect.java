package mesos.effects;

import mesos.board.Board;
import mesos.cards.characters.CharacterCard;
import mesos.enums.CharacterType;
import mesos.enums.EventType;
import mesos.order.TurnOrderSpace;
import mesos.player.Player;

public abstract class BuildingEffect {
    public void onAcquire(Player owner) {
    }

    public void onCharacterAdded(Player owner, CharacterCard character) {
    }

    public void onTurnOrderPlaced(Player owner, TurnOrderSpace space) {
    }

    public void onEndOfActionPhase(Player owner, Board board) {
    }

    public int getExtraSustenanceDiscount(Player owner) {
        return 0;
    }

    public int getAdditionalShamanIcons(Player owner) {
        return 0;
    }

    public boolean ignoresShamanicPenalty(Player owner) {
        return false;
    }

    public boolean doublesShamanicReward(Player owner) {
        return false;
    }

    public boolean gainsShamanicRewardOnTie(Player owner) {
        return false;
    }

    public int getExtraFoodPerCharacterForEvent(EventType eventType, CharacterType characterType, Player owner) {
        return 0;
    }

    public int getExtraPrestigePerCharacterForEvent(EventType eventType, CharacterType characterType, Player owner) {
        return 0;
    }

    public int getEndGameBonus(Player owner) {
        return 0;
    }

    public String getDescription() {
        return "";
    }
}
