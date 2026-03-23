package mesos.effects;

import mesos.order.TurnOrderSpace;
import mesos.player.Player;

public class TurnOrderPlacementEffect extends BuildingEffect {
    private final int extraFoodIfBonusSpace;

    public TurnOrderPlacementEffect(int extraFoodIfBonusSpace) {
        this.extraFoodIfBonusSpace = extraFoodIfBonusSpace;
    }

    @Override
    public void onTurnOrderPlaced(Player owner, TurnOrderSpace space) {
        if (space != null && space.getFoodBonus() > 0 && !space.isLastSpace()) {
            owner.gainFood(extraFoodIfBonusSpace);
        }
    }

    @Override
    public String getDescription() {
        return "TURN_ORDER";
    }
}
