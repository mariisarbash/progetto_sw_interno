package mesos.model.tribe;

import mesos.model.cards.buildings.BuildingCard;
import mesos.model.cards.characters.Artist;
import mesos.model.cards.characters.Builder;
import mesos.model.cards.characters.CharacterCard;
import mesos.model.cards.characters.Gatherer;
import mesos.model.cards.characters.Inventor;
import mesos.model.cards.characters.Shaman;
import mesos.model.enums.CharacterType;
import mesos.model.enums.InventionIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Tribe {
    private final List<CharacterCard> characterCards;
    private final List<BuildingCard> buildingCards;

    public Tribe() {
        this.characterCards = new ArrayList<>();
        this.buildingCards = new ArrayList<>();
    }

    public void addCharacter(CharacterCard characterCard) {
        characterCards.add(characterCard);
    }

    public void addBuilding(BuildingCard buildingCard) {
        buildingCards.add(buildingCard);
    }

    public int countCharacters(CharacterType type) {
        return (int) characterCards.stream().filter(c -> c.getType() == type).count();
    }

    public int calculateBuildingDiscount() {
        return characterCards.stream()
                .filter(Builder.class::isInstance)
                .map(Builder.class::cast)
                .mapToInt(Builder::getDiscountValue)
                .sum();
    }

    public int calculateSustenanceDiscount() {
        return characterCards.stream()
                .filter(Gatherer.class::isInstance)
                .map(Gatherer.class::cast)
                .mapToInt(Gatherer::getSustenanceDiscount)
                .sum();
    }

    public int getTotalShamanIcons() {
        return characterCards.stream()
                .filter(Shaman.class::isInstance)
                .map(Shaman.class::cast)
                .mapToInt(Shaman::getShamanIcons)
                .sum();
    }

    public int countDistinctInventionIcons() {
        return (int) characterCards.stream()
                .filter(Inventor.class::isInstance)
                .map(Inventor.class::cast)
                .map(Inventor::getInventionIcon)
                .distinct()
                .count();
    }

    public int countCompleteCharacterSets() {
        int min = Integer.MAX_VALUE;
        for (CharacterType type : CharacterType.values()) {
            min = Math.min(min, countCharacters(type));
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    public int countInventorPairs() {
        return characterCards.stream()
                .filter(Inventor.class::isInstance)
                .map(Inventor.class::cast)
                .collect(Collectors.groupingBy(Inventor::getInventionIcon, Collectors.counting()))
                .values()
                .stream()
                .mapToInt(count -> (int) (count / 2))
                .sum();
    }

    public int countArtistPairs() {
        return countCharacters(CharacterType.ARTIST) / 2;
    }

    public List<BuildingCard> getBuildingCards() {
        return List.copyOf(buildingCards);
    }

    public List<CharacterCard> getCharacterCards() {
        return List.copyOf(characterCards);
    }
}
