package mesos.tribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mesos.board.Board;
import mesos.cards.BuildingCard;
import mesos.cards.characters.Builder;
import mesos.cards.characters.CharacterCard;
import mesos.cards.characters.Inventor;
import mesos.enums.CharacterType;
import mesos.enums.EventType;
import mesos.order.TurnOrderSpace;
import mesos.player.Player;

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
        return (int) characterCards.stream().filter(card -> card.getType() == type).count();
    }

    public int calculateBuildingDiscount() {
        return characterCards.stream()
                .filter(Builder.class::isInstance)
                .map(Builder.class::cast)
                .mapToInt(Builder::getDiscountValue)
                .sum();
    }

    public int calculateSustenanceDiscount() {
        return characterCards.stream().mapToInt(CharacterCard::getBaseSustenanceDiscount).sum();
    }

    public int getTotalShamanIcons() {
        return characterCards.stream().mapToInt(CharacterCard::getShamanIcons).sum();
    }

    public int countDistinctInventionIcons() {
        Set<?> icons = new HashSet<>(characterCards.stream()
                .filter(Inventor.class::isInstance)
                .map(Inventor.class::cast)
                .map(Inventor::getInventionIcon)
                .toList());
        return icons.size();
    }

    public int countCompleteCharacterSets() {
        int min = Integer.MAX_VALUE;
        for (CharacterType type : CharacterType.values()) {
            min = Math.min(min, countCharacters(type));
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    public int countInventorPairs() {
        Map<Object, Integer> occurrences = new HashMap<>();
        for (CharacterCard card : characterCards) {
            if (card instanceof Inventor inventor) {
                Object icon = inventor.getInventionIcon();
                occurrences.put(icon, occurrences.getOrDefault(icon, 0) + 1);
            }
        }
        return occurrences.values().stream().mapToInt(value -> value / 2).sum();
    }

    public int countArtistPairs() {
        return countCharacters(CharacterType.ARTIST) / 2;
    }

    public void notifyBuildingsOnCharacterAdded(Player owner, CharacterCard character) {
        for (BuildingCard buildingCard : buildingCards) {
            buildingCard.onCharacterAdded(owner, character);
        }
    }

    public void notifyBuildingsOnTurnOrderPlaced(Player owner, TurnOrderSpace space) {
        for (BuildingCard buildingCard : buildingCards) {
            buildingCard.onTurnOrderPlaced(owner, space);
        }
    }

    public void notifyBuildingsOnEndOfActionPhase(Player owner, Board board) {
        for (BuildingCard buildingCard : buildingCards) {
            buildingCard.onEndOfActionPhase(owner, board);
        }
    }

    public int getExtraSustenanceDiscount(Player owner) {
        return buildingCards.stream().mapToInt(building -> building.getExtraSustenanceDiscount(owner)).sum();
    }

    public int getAdditionalShamanIcons(Player owner) {
        return buildingCards.stream().mapToInt(building -> building.getAdditionalShamanIcons(owner)).sum();
    }

    public boolean ignoresShamanicPenalty(Player owner) {
        return buildingCards.stream().anyMatch(building -> building.ignoresShamanicPenalty(owner));
    }

    public boolean doublesShamanicReward(Player owner) {
        return buildingCards.stream().anyMatch(building -> building.doublesShamanicReward(owner));
    }

    public boolean gainsShamanicRewardOnTie(Player owner) {
        return buildingCards.stream().anyMatch(building -> building.gainsShamanicRewardOnTie(owner));
    }

    public int getExtraFoodPerCharacterForEvent(EventType eventType, CharacterType characterType, Player owner) {
        return buildingCards.stream()
                .mapToInt(building -> building.getExtraFoodPerCharacterForEvent(eventType, characterType, owner))
                .sum();
    }

    public int getExtraPrestigePerCharacterForEvent(EventType eventType, CharacterType characterType, Player owner) {
        return buildingCards.stream()
                .mapToInt(building -> building.getExtraPrestigePerCharacterForEvent(eventType, characterType, owner))
                .sum();
    }

    public int getBuildingEndGameBonus(Player owner) {
        return buildingCards.stream().mapToInt(building -> building.getEndGameBonus(owner) + building.getPrintedPrestigePoints()).sum();
    }

    public List<BuildingCard> getBuildingCards() {
        return new ArrayList<>(buildingCards);
    }

    public List<CharacterCard> getCharacterCards() {
        return new ArrayList<>(characterCards);
    }
}
