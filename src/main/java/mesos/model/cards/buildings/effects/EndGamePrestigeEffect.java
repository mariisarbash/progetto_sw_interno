package mesos.model.cards.buildings.effects;

import mesos.model.game.GameContext;
import mesos.model.player.Player;

public class EndGamePrestigeEffect extends BuildingEffectStrategy {
    private final int prestigeBonus;

    public EndGamePrestigeEffect(int prestigeBonus) {
        this.prestigeBonus = prestigeBonus;
    }

    @Override
    public int onGameEnd(Player owner, GameContext context) {
        return prestigeBonus;
    }

    @Override
    public String getDescription() {
        return "End game prestige bonus: " + prestigeBonus;
    }
}
