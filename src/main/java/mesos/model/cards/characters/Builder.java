package mesos.model.cards.characters;

import mesos.model.enums.CharacterType;
import mesos.model.enums.Era;
import mesos.model.enums.PlayerCount;

import java.util.Set;

public class Builder extends CharacterCard {
    private final int ppValue;
    private final int discountValue;

    public Builder(String name, Era era, Set<PlayerCount> allowedPlayerCounts, int ppValue, int discountValue) {
        super(name, era, allowedPlayerCounts);
        this.ppValue = ppValue;
        this.discountValue = discountValue;
    }

    @Override
    public CharacterType getType() {
        return CharacterType.BUILDER;
    }

    public int getPpValue() {
        return ppValue;
    }

    public int getDiscountValue() {
        return discountValue;
    }
}
