package mesos.effects;

import mesos.cards.characters.Builder;
import mesos.enums.CharacterType;
import mesos.enums.EndGameScoringType;
import mesos.player.Player;

public class EndGameScoringEffect extends BuildingEffect {
    private final EndGameScoringType scoringType;
    private final int pointsPerUnit;
    private final CharacterType targetCharacterType;

    public EndGameScoringEffect(EndGameScoringType scoringType, int pointsPerUnit, CharacterType targetCharacterType) {
        this.scoringType = scoringType;
        this.pointsPerUnit = pointsPerUnit;
        this.targetCharacterType = targetCharacterType;
    }

    @Override
    public int getEndGameBonus(Player owner) {
        return switch (scoringType) {
            case FLAT -> pointsPerUnit;
            case PER_CHARACTER_TYPE -> owner.getTribe().countCharacters(targetCharacterType) * pointsPerUnit;
            case PER_COMPLETE_SET -> owner.getTribe().countCompleteCharacterSets() * pointsPerUnit;
            case DOUBLE_BUILDERS -> owner.getTribe().getCharacterCards().stream()
                    .filter(Builder.class::isInstance)
                    .map(Builder.class::cast)
                    .mapToInt(Builder::getPpValue)
                    .sum();
        };
    }

    @Override
    public String getDescription() {
        return scoringType.name();
    }
}
