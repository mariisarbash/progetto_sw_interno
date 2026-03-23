package mesos.effects;

import mesos.enums.CharacterType;
import mesos.player.Player;

public class SustenanceDiscountEffect extends BuildingEffect {
    private final CharacterType discountedCharacterType;
    private final int discountPerCharacter;

    public SustenanceDiscountEffect(CharacterType discountedCharacterType, int discountPerCharacter) {
        this.discountedCharacterType = discountedCharacterType;
        this.discountPerCharacter = discountPerCharacter;
    }

    @Override
    public int getExtraSustenanceDiscount(Player owner) {
        return owner.getTribe().countCharacters(discountedCharacterType) * discountPerCharacter;
    }

    @Override
    public String getDescription() {
        return discountedCharacterType.name();
    }
}
