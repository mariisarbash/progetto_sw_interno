package mesos.model.cards.buildings;

import mesos.model.cards.base.Card;
import mesos.model.cards.buildings.effects.BuildingEffectStrategy;
import mesos.model.cards.characters.CharacterCard;
import mesos.model.cards.events.EventCard;
import mesos.model.enums.Era;
import mesos.model.game.GameContext;
import mesos.model.player.Player;

public class BuildingCard extends Card {
    private final int foodCost;
    private final int prestigePoints;
    private final BuildingEffectStrategy effectStrategy;

    public BuildingCard(String name, Era era, int foodCost, int prestigePoints, BuildingEffectStrategy effectStrategy) {
        super(name, era);
        this.foodCost = foodCost;
        this.prestigePoints = prestigePoints;
        this.effectStrategy = effectStrategy;
    }

    public int calculateRealCost(int discount) {
        return Math.max(0, foodCost - Math.max(0, discount));
    }

    public void onAcquire(Player owner, GameContext context) {
        effectStrategy.onAcquire(owner, context);
    }

    public void onEvent(EventCard event, Player owner, GameContext context) {
        effectStrategy.onEvent(owner, event, context);
    }

    public void onCharacterAdded(CharacterCard character, Player owner, GameContext context) {
        effectStrategy.onCharacterAdded(owner, character, context);
    }

    public void onEndOfActionPhase(Player owner, GameContext context) {
        effectStrategy.onEndOfActionPhase(owner, context);
    }

    public int getEndGameBonus(Player owner, GameContext context) {
        return effectStrategy.onGameEnd(owner, context);
    }

    public int getTotalPrestigeAtGameEnd(Player owner, GameContext context) {
        return prestigePoints + getEndGameBonus(owner, context);
    }

    public String getEffectDescription() {
        return effectStrategy.getDescription();
    }

    public int getFoodCost() {
        return foodCost;
    }

    public int getPrestigePoints() {
        return prestigePoints;
    }

    public BuildingEffectStrategy getEffectStrategy() {
        return effectStrategy;
    }
}
