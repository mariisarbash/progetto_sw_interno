package mesos.effects;

import mesos.player.Player;

public class ShamanicRitualEffect extends BuildingEffect {
    private final int extraShamanIcons;
    private final boolean ignorePenaltyIfLowest;
    private final boolean doubleRewardIfHighest;
    private final boolean rewardAlsoOnTie;

    public ShamanicRitualEffect(int extraShamanIcons, boolean ignorePenaltyIfLowest,
                                boolean doubleRewardIfHighest, boolean rewardAlsoOnTie) {
        this.extraShamanIcons = extraShamanIcons;
        this.ignorePenaltyIfLowest = ignorePenaltyIfLowest;
        this.doubleRewardIfHighest = doubleRewardIfHighest;
        this.rewardAlsoOnTie = rewardAlsoOnTie;
    }

    @Override
    public int getAdditionalShamanIcons(Player owner) {
        return extraShamanIcons;
    }

    @Override
    public boolean ignoresShamanicPenalty(Player owner) {
        return ignorePenaltyIfLowest;
    }

    @Override
    public boolean doublesShamanicReward(Player owner) {
        return doubleRewardIfHighest;
    }

    @Override
    public boolean gainsShamanicRewardOnTie(Player owner) {
        return rewardAlsoOnTie;
    }

    @Override
    public String getDescription() {
        return "SHAMANIC_RITUAL";
    }
}
