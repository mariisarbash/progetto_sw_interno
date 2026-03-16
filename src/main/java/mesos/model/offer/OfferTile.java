package mesos.model.offer;

import mesos.model.cards.base.Card;
import mesos.model.cards.buildings.BuildingCard;
import mesos.model.cards.events.EventCard;
import mesos.model.enums.OfferTileId;
import mesos.model.enums.PlayerCount;
import mesos.model.game.GameContext;
import mesos.model.player.Player;
import mesos.model.player.Totem;

import java.util.HashSet;
import java.util.Set;

public class OfferTile {
    private final OfferTileId id;
    private final Set<PlayerCount> allowedPlayerCounts;
    private final int cardsFromTop;
    private final int cardsFromBottom;
    private final int directFoodBonus;
    private Totem occupiedBy;

    public OfferTile(OfferTileId id, Set<PlayerCount> allowedPlayerCounts, int cardsFromTop, int cardsFromBottom, int directFoodBonus) {
        this.id = id;
        this.allowedPlayerCounts = allowedPlayerCounts != null ? new HashSet<>(allowedPlayerCounts) : new HashSet<>();
        this.cardsFromTop = cardsFromTop;
        this.cardsFromBottom = cardsFromBottom;
        this.directFoodBonus = directFoodBonus;
    }

    public boolean isAvailable() {
        return occupiedBy == null;
    }

    public OfferChoice createChoice() {
        return new OfferChoice(this);
    }

    public boolean validateChoice(OfferChoice choice, GameContext context) {
        if (choice == null || choice.isEmpty()) {
            return false;
        }
        if (choice.countPicksFromRow(mesos.model.enums.VisibleRow.TOP) != cardsFromTop) {
            return false;
        }
        if (choice.countPicksFromRow(mesos.model.enums.VisibleRow.BOTTOM) != cardsFromBottom) {
            return false;
        }

        return true;
    }

    public void executeChoice(OfferChoice choice, GameContext context) {
        Player activePlayer = context.getActivePlayer();
        if (activePlayer == null) {
            throw new IllegalStateException("No active player in context");
        }
        if (!validateChoice(choice, context)) {
            throw new IllegalArgumentException("Invalid offer choice");
        }

        activePlayer.gainFood(directFoodBonus);

        for (CardPick pick : choice.getOrderedPicks()) {
            Card card = context.getBoard().takeCard(pick);
            if (card instanceof EventCard) {
                throw new IllegalStateException("Event cards cannot be directly taken");
            }
            if (card instanceof mesos.model.cards.characters.CharacterCard characterCard) {
                activePlayer.acquireCharacter(characterCard);
                characterCard.onPurchase(activePlayer, context);
            } else if (card instanceof BuildingCard buildingCard) {
                activePlayer.acquireBuilding(buildingCard, context);
            }
        }
    }

    public OfferTileId getId() {
        return id;
    }

    public Set<PlayerCount> getAllowedPlayerCounts() {
        return Set.copyOf(allowedPlayerCounts);
    }

    public int getCardsFromTop() {
        return cardsFromTop;
    }

    public int getCardsFromBottom() {
        return cardsFromBottom;
    }

    public int getDirectFoodBonus() {
        return directFoodBonus;
    }

    public Totem getOccupiedBy() {
        return occupiedBy;
    }

    public void setOccupiedBy(Totem occupiedBy) {
        this.occupiedBy = occupiedBy;
    }
}
