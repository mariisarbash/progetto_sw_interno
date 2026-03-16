package mesos.model.cards.buildings.effects;

import mesos.model.game.GameContext;
import mesos.model.player.Player;

public class SetCompletionFoodEffect extends BuildingEffectStrategy {
    @Override
    public void onAcquire(Player owner, GameContext context) {

    }

    @Override
    public String getDescription() {
        return "Set completion food effect";
    }
}
