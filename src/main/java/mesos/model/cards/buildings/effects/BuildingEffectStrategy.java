package mesos.model.cards.buildings.effects;

import mesos.model.cards.characters.CharacterCard;
import mesos.model.cards.events.EventCard;
import mesos.model.game.GameContext;
import mesos.model.player.Player;

public abstract class BuildingEffectStrategy {
    public void onAcquire(Player owner, GameContext context) {
    }

    public void onEvent(Player owner, EventCard event, GameContext context) {
    }

    public void onCharacterAdded(Player owner, CharacterCard character, GameContext context) {
    }

    public void onEndOfActionPhase(Player owner, GameContext context) {
    }

    public int onGameEnd(Player owner, GameContext context) {
        return 0;
    }

    public String getDescription() {
        return "No description available";
    }
}
