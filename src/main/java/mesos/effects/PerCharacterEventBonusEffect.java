package mesos.effects;

import mesos.enums.CharacterType;
import mesos.enums.EventType;
import mesos.player.Player;

public class PerCharacterEventBonusEffect extends BuildingEffect {
    private final EventType targetEventType;
    private final CharacterType targetCharacterType;
    private final int extraFoodPerCharacter;
    private final int extraPrestigePerCharacter;

    public PerCharacterEventBonusEffect(EventType targetEventType, CharacterType targetCharacterType,
                                        int extraFoodPerCharacter, int extraPrestigePerCharacter) {
        this.targetEventType = targetEventType;
        this.targetCharacterType = targetCharacterType;
        this.extraFoodPerCharacter = extraFoodPerCharacter;
        this.extraPrestigePerCharacter = extraPrestigePerCharacter;
    }

    @Override
    public int getExtraFoodPerCharacterForEvent(EventType eventType, CharacterType characterType, Player owner) {
        if (eventType == targetEventType && characterType == targetCharacterType) {
            return extraFoodPerCharacter;
        }
        return 0;
    }

    @Override
    public int getExtraPrestigePerCharacterForEvent(EventType eventType, CharacterType characterType, Player owner) {
        if (eventType == targetEventType && characterType == targetCharacterType) {
            return extraPrestigePerCharacter;
        }
        return 0;
    }

    @Override
    public String getDescription() {
        return targetEventType.name();
    }
}
