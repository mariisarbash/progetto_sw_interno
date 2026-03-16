package mesos.model.board;

import mesos.model.cards.base.Card;
import mesos.model.cards.base.TribeDeckCard;
import mesos.model.cards.buildings.BuildingCard;
import mesos.model.cards.events.EventCard;
import mesos.model.enums.Era;
import mesos.model.enums.PlayerCount;
import mesos.model.enums.VisibleRow;
import mesos.model.offer.CardPick;
import mesos.model.offer.OfferTrack;
import mesos.model.order.TurnOrderTile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Board {
    private final List<TribeDeckCard> tribeDeck;
    private final Map<Era, List<BuildingCard>> buildingDecks;
    private final VisibleRowState topRow;
    private final VisibleRowState bottomRow;
    private final OfferTrack offerTrack;
    private final TurnOrderTile turnOrderTile;

    public Board() {
        this.tribeDeck = new ArrayList<>();
        this.buildingDecks = new EnumMap<>(Era.class);
        this.topRow = new VisibleRowState(VisibleRow.TOP);
        this.bottomRow = new VisibleRowState(VisibleRow.BOTTOM);
        this.offerTrack = new OfferTrack();
        this.turnOrderTile = new TurnOrderTile();
    }

    public void buildTribeDeck(PlayerCount playerCount) {
        tribeDeck.clear();

    }

    public void buildBuildingDecks(PlayerCount playerCount) {
        buildingDecks.clear();
        buildingDecks.put(Era.I, new ArrayList<>());
        buildingDecks.put(Era.II, new ArrayList<>());
        buildingDecks.put(Era.III, new ArrayList<>());

    }

    public void configureOfferTrack(PlayerCount playerCount) {
        offerTrack.configureForPlayerCount(playerCount);
    }

    public void configureTurnOrderTile(PlayerCount playerCount) {
        turnOrderTile.configureForPlayerCount(playerCount);
    }

    public void revealInitialRows(PlayerCount playerCount) {
        topRow.clear();
        bottomRow.clear();

    }

    public void revealInitialBuildings(PlayerCount playerCount) {

    }

    public VisibleRowState getVisibleRow(VisibleRow row) {
        return row == VisibleRow.TOP ? topRow : bottomRow;
    }

    public List<RowSlot> getSelectableSlots(VisibleRow row) {
        return getVisibleRow(row).getSelectableSlots();
    }

    public OfferTrack getOfferTrack() {
        return offerTrack;
    }

    public TurnOrderTile getTurnOrderTile() {
        return turnOrderTile;
    }

    public Card takeCard(CardPick pick) {
        return pick.resolve(this);
    }

    public void discardBottomCharactersAndEvents() {
        for (RowSlot slot : bottomRow.getSlots()) {
            if (slot.containsCharacter() || slot.containsEvent()) {
                slot.setCard(null);
            }
        }
    }

    public void moveTopCharactersAndEventsToBottom() {
        List<RowSlot> topSlots = topRow.getSlots();
        List<RowSlot> bottomSlots = bottomRow.getSlots();
        int size = Math.min(topSlots.size(), bottomSlots.size());

        for (int i = 0; i < size; i++) {
            RowSlot topSlot = topSlots.get(i);
            RowSlot bottomSlot = bottomSlots.get(i);
            if ((topSlot.containsCharacter() || topSlot.containsEvent()) && bottomSlot.isEmpty()) {
                bottomSlot.setCard(topSlot.getCard());
                topSlot.setCard(null);
            }
        }
    }

    public RowRefillResult refillTopRow(PlayerCount playerCount) {

        return new RowRefillResult(false, null, tribeDeck.isEmpty());
    }

    public void applyEraTransition(Era newEra) {
        if (newEra == Era.III) {
            discardBottomBuildingsForEraIII();
        }
        moveTopBuildingsToBottom();
        revealBuildingsForEra(newEra);
    }

    public void discardBottomBuildingsForEraIII() {
        for (RowSlot slot : bottomRow.getSlots()) {
            if (slot.containsBuilding()) {
                slot.setCard(null);
            }
        }
    }

    public void moveTopBuildingsToBottom() {
        List<RowSlot> topSlots = topRow.getSlots();
        List<RowSlot> bottomSlots = bottomRow.getSlots();
        int size = Math.min(topSlots.size(), bottomSlots.size());

        for (int i = 0; i < size; i++) {
            RowSlot topSlot = topSlots.get(i);
            RowSlot bottomSlot = bottomSlots.get(i);
            if (topSlot.containsBuilding() && bottomSlot.isEmpty()) {
                bottomSlot.setCard(topSlot.getCard());
                topSlot.setCard(null);
            }
        }
    }

    public void revealBuildingsForEra(Era newEra) {

    }

    public List<EventCard> getActiveRoundEvents() {
        return bottomRow.getSlots().stream()
                .map(RowSlot::getCard)
                .filter(EventCard.class::isInstance)
                .map(EventCard.class::cast)
                .collect(Collectors.toList());
    }

    public List<EventCard> getAllVisibleEvents() {
        List<EventCard> events = new ArrayList<>();
        events.addAll(topRow.getSlots().stream()
                .map(RowSlot::getCard)
                .filter(EventCard.class::isInstance)
                .map(EventCard.class::cast)
                .toList());
        events.addAll(bottomRow.getSlots().stream()
                .map(RowSlot::getCard)
                .filter(EventCard.class::isInstance)
                .map(EventCard.class::cast)
                .toList());
        return events;
    }

    public List<EventCard> sortEventsForResolution(List<EventCard> events) {
        return events.stream()
                .sorted(Comparator.comparingInt(event -> event.getEventType().ordinal()))
                .collect(Collectors.toList());
    }
}
