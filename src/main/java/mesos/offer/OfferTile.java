package mesos.offer;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import mesos.board.Board;
import mesos.board.RowSlot;
import mesos.cards.BuildingCard;
import mesos.cards.Card;
import mesos.cards.characters.CharacterCard;
import mesos.enums.OfferTileId;
import mesos.enums.PlayerCount;
import mesos.enums.VisibleRow;
import mesos.order.TurnOrderSpace;
import mesos.player.Player;
import mesos.player.Totem;

public class OfferTile {
    private final OfferTileId id;
    private final Set<PlayerCount> allowedPlayerCounts;
    private int cardsFromTop;
    private int cardsFromBottom;
    private int directFoodBonus;
    private Totem occupiedBy;

    public OfferTile(OfferTileId id) {
        this.id = id;
        this.allowedPlayerCounts = EnumSet.allOf(PlayerCount.class);
    }

    public boolean isAvailable() {
        return occupiedBy == null;
    }

    public OfferChoice createChoice() {
        return new OfferChoice(this);
    }

    public boolean validateChoice(OfferChoice choice, Player player, Board board) {
        if (choice == null || player == null || board == null) {
            return false;
        }
        if (choice.getOfferTile() != this) {
            return false;
        }
        if (cardsFromTop == 0 && cardsFromBottom == 0) {
            return choice.isEmpty();
        }
        if (choice.isEmpty()) {
            return false;
        }
        if (choice.countPicksFromRow(VisibleRow.TOP) > cardsFromTop) {
            return false;
        }
        if (choice.countPicksFromRow(VisibleRow.BOTTOM) > cardsFromBottom) {
            return false;
        }

        List<RowSlot> simulatedTop = cloneSlots(board.getVisibleRow(VisibleRow.TOP).getSlots());
        List<RowSlot> simulatedBottom = cloneSlots(board.getVisibleRow(VisibleRow.BOTTOM).getSlots());
        int remainingTop = cardsFromTop;
        int remainingBottom = cardsFromBottom;
        int simulatedFood = player.getFood();

        for (CardPick pick : choice.getOrderedPicks()) {
            List<RowSlot> simulatedRow = pick.getRow() == VisibleRow.TOP ? simulatedTop : simulatedBottom;
            if (pick.getRow() == VisibleRow.TOP) {
                if (remainingTop <= 0) return false;
                remainingTop--;
            } else {
                if (remainingBottom <= 0) return false;
                remainingBottom--;
            }

            RowSlot slot = simulatedRow.stream().filter(s -> s.getIndex() == pick.getSlotIndex()).findFirst().orElse(null);
            if (slot == null || !slot.isSelectable()) {
                return false;
            }
            if (slot.containsBuilding() && containsSelectableCharacter(simulatedRow)) {
                return false;
            }
            if (slot.getCard() instanceof BuildingCard buildingCard) {
                int realCost = buildingCard.calculateRealCost(player.getTribe().calculateBuildingDiscount());
                if (simulatedFood < realCost) {
                    return false;
                }
                simulatedFood -= realCost;
            }
            slot.clearCard();
        }
        return true;
    }

    public void executeChoice(OfferChoice choice, Player player, Board board) {
        if (!validateChoice(choice, player, board)) {
            throw new IllegalArgumentException("Invalid offer choice for tile " + id);
        }
        if (directFoodBonus > 0) {
            player.gainFood(directFoodBonus);
        }
        for (CardPick pick : choice.getOrderedPicks()) {
            Card card = board.takeCard(pick);
            if (card instanceof CharacterCard characterCard) {
                player.acquireCharacter(characterCard);
            } else if (card instanceof BuildingCard buildingCard) {
                player.acquireBuilding(buildingCard);
            }
        }
        TurnOrderSpace space = board.getTurnOrderTile().addTotem(player.getTotem(), player);
        board.getTurnOrderTile().applySpaceEffect(space, player);
        player.getTribe().notifyBuildingsOnTurnOrderPlaced(player, space);
        occupiedBy = null;
        player.clearSelectedOfferTile();
    }

    private List<RowSlot> cloneSlots(List<RowSlot> slots) {
        List<RowSlot> copy = new ArrayList<>();
        for (RowSlot slot : slots) {
            copy.add(new RowSlot(slot.getIndex(), slot.getCard()));
        }
        return copy;
    }

    private boolean containsSelectableCharacter(List<RowSlot> row) {
        return row.stream().anyMatch(slot -> slot.containsCharacter() && slot.isSelectable());
    }

    public OfferTileId getId() {
        return id;
    }

    public Set<PlayerCount> getAllowedPlayerCounts() {
        return EnumSet.copyOf(allowedPlayerCounts);
    }

    public void setAllowedPlayerCounts(PlayerCount... playerCounts) {
        allowedPlayerCounts.clear();
        if (playerCounts == null || playerCounts.length == 0) {
            allowedPlayerCounts.addAll(EnumSet.allOf(PlayerCount.class));
        } else {
            for (PlayerCount count : playerCounts) {
                allowedPlayerCounts.add(count);
            }
        }
    }

    public int getCardsFromTop() {
        return cardsFromTop;
    }

    public void setCardsFromTop(int cardsFromTop) {
        this.cardsFromTop = cardsFromTop;
    }

    public int getCardsFromBottom() {
        return cardsFromBottom;
    }

    public void setCardsFromBottom(int cardsFromBottom) {
        this.cardsFromBottom = cardsFromBottom;
    }

    public int getDirectFoodBonus() {
        return directFoodBonus;
    }

    public void setDirectFoodBonus(int directFoodBonus) {
        this.directFoodBonus = directFoodBonus;
    }

    public Totem getOccupiedBy() {
        return occupiedBy;
    }

    public void setOccupiedBy(Totem occupiedBy) {
        this.occupiedBy = occupiedBy;
    }
}
