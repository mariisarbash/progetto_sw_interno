package mesos.effects;

import mesos.cards.characters.CharacterCard;
import mesos.enums.TriggeredFoodRewardType;
import mesos.player.Player;

public class TriggeredFoodRewardEffect extends BuildingEffect {
    private final TriggeredFoodRewardType triggerType;
    private final int foodReward;
    private int rewardedOccurrences;

    public TriggeredFoodRewardEffect(TriggeredFoodRewardType triggerType, int foodReward) {
        this.triggerType = triggerType;
        this.foodReward = foodReward;
        this.rewardedOccurrences = 0;
    }

    @Override
    public void onCharacterAdded(Player owner, CharacterCard character) {
        int currentOccurrences = switch (triggerType) {
            case COMPLETE_SET -> owner.getTribe().countCompleteCharacterSets();
            case MATCHING_INVENTOR_PAIR -> owner.getTribe().countInventorPairs();
        };
        int newOccurrences = Math.max(0, currentOccurrences - rewardedOccurrences);
        if (newOccurrences > 0) {
            owner.gainFood(newOccurrences * foodReward);
            rewardedOccurrences = currentOccurrences;
        }
    }

    @Override
    public String getDescription() {
        return triggerType.name();
    }
}
