package mesos.cards;

import mesos.board.Board;
import mesos.cards.characters.CharacterCard;
import mesos.effects.BuildingEffect;
import mesos.effects.NoBuildingEffect;
import mesos.enums.CharacterType;
import mesos.enums.Era;
import mesos.enums.EventType;
import mesos.order.TurnOrderSpace;
import mesos.player.Player;

public class BuildingCard extends Card {
    private final int foodCost;
    private final int printedPrestigePoints;
    private final BuildingEffect effect;

    public BuildingCard(String name, Era era, int foodCost, int printedPrestigePoints, BuildingEffect effect) {
        super(name, era);
        this.foodCost = foodCost;
        this.printedPrestigePoints = printedPrestigePoints;
        this.effect = effect == null ? new NoBuildingEffect() : effect;
    }

    public int calculateRealCost(int discount) {
        return Math.max(0, foodCost - discount);
    }

    public void onAcquire(Player owner) {
        effect.onAcquire(owner);
    }

    public void onCharacterAdded(Player owner, CharacterCard character) {
        effect.onCharacterAdded(owner, character);
    }

    public void onTurnOrderPlaced(Player owner, TurnOrderSpace space) {
        effect.onTurnOrderPlaced(owner, space);
    }

    public void onEndOfActionPhase(Player owner, Board board) {
        effect.onEndOfActionPhase(owner, board);
    }

    public int getExtraSustenanceDiscount(Player owner) {
        return effect.getExtraSustenanceDiscount(owner);
    }

    public int getAdditionalShamanIcons(Player owner) {
        return effect.getAdditionalShamanIcons(owner);
    }

    public boolean ignoresShamanicPenalty(Player owner) {
        return effect.ignoresShamanicPenalty(owner);
    }

    public boolean doublesShamanicReward(Player owner) {
        return effect.doublesShamanicReward(owner);
    }

    public boolean gainsShamanicRewardOnTie(Player owner) {
        return effect.gainsShamanicRewardOnTie(owner);
    }

    public int getExtraFoodPerCharacterForEvent(EventType eventType, CharacterType characterType, Player owner) {
        return effect.getExtraFoodPerCharacterForEvent(eventType, characterType, owner);
    }

    public int getExtraPrestigePerCharacterForEvent(EventType eventType, CharacterType characterType, Player owner) {
        return effect.getExtraPrestigePerCharacterForEvent(eventType, characterType, owner);
    }

    public int getEndGameBonus(Player owner) {
        return effect.getEndGameBonus(owner);
    }

    public int getTotalPrestigeAtGameEnd(Player owner) {
        return printedPrestigePoints + getEndGameBonus(owner);
    }

    public String getEffectDescription() {
        return effect.getDescription();
    }

    public int getFoodCost() {
        return foodCost;
    }

    public int getPrintedPrestigePoints() {
        return printedPrestigePoints;
    }

    public BuildingEffect getEffect() {
        return effect;
    }
}
