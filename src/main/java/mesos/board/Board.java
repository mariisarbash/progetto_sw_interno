package mesos.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import mesos.cards.Card;
import mesos.cards.BuildingCard;
import mesos.cards.TribeDeckCard;
import mesos.cards.characters.Artist;
import mesos.cards.characters.Builder;
import mesos.cards.characters.Gatherer;
import mesos.cards.characters.Hunter;
import mesos.cards.characters.Inventor;
import mesos.cards.characters.Shaman;
import mesos.cards.events.CavePaintingsEvent;
import mesos.cards.events.EventCard;
import mesos.cards.events.HuntingEvent;
import mesos.cards.events.ShamanicRitualEvent;
import mesos.cards.events.SustenanceEvent;
import mesos.effects.EndGameScoringEffect;
import mesos.effects.ExtraTopRowPickEffect;
import mesos.effects.NoBuildingEffect;
import mesos.effects.PerCharacterEventBonusEffect;
import mesos.effects.ShamanicRitualEffect;
import mesos.effects.SustenanceDiscountEffect;
import mesos.effects.TriggeredFoodRewardEffect;
import mesos.effects.TurnOrderPlacementEffect;
import mesos.enums.CharacterType;
import mesos.enums.EndGameScoringType;
import mesos.enums.Era;
import mesos.enums.EventType;
import mesos.enums.InventionIcon;
import mesos.enums.PlayerCount;
import mesos.enums.TriggeredFoodRewardType;
import mesos.enums.VisibleRow;
import mesos.offer.CardPick;
import mesos.offer.OfferTrack;
import mesos.order.TurnOrderTile;

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
        List<TribeDeckCard> eraI = createEraDeck(Era.I, playerCount, 5);
        List<TribeDeckCard> eraII = createEraDeck(Era.II, playerCount, 5);
        List<TribeDeckCard> eraIII = createEraDeck(Era.III, playerCount, 5);
        Collections.shuffle(eraI);
        Collections.shuffle(eraII);
        Collections.shuffle(eraIII);
        tribeDeck.addAll(eraI);
        tribeDeck.addAll(eraII);
        tribeDeck.addAll(eraIII);
    }

    public void buildBuildingDecks(PlayerCount playerCount) {
        buildingDecks.clear();
        Map<Era, Integer> counts = buildingCountsFor(playerCount);
        for (Era era : Era.values()) {
            List<BuildingCard> pool = createBuildingPool(era);
            Collections.shuffle(pool);
            int count = Math.min(counts.get(era), pool.size());
            buildingDecks.put(era, new ArrayList<>(pool.subList(0, count)));
        }
    }

    public void configureOfferTrack(PlayerCount playerCount) {
        offerTrack.configureForPlayerCount(playerCount);
    }

    public void configureTurnOrderTile(PlayerCount playerCount) {
        turnOrderTile.configureForPlayerCount(playerCount);
    }

    public void revealInitialRows(PlayerCount playerCount) {
        int bottomNeeded = playerCountToInt(playerCount) + 1;
        int topNeeded = playerCountToInt(playerCount) + 4;

        while (bottomRow.countNonBuildingCards() < bottomNeeded && !tribeDeck.isEmpty()) {
            TribeDeckCard card = tribeDeck.remove(0);
            if (card instanceof EventCard) {
                topRow.appendCard(card);
            } else {
                bottomRow.appendCard(card);
            }
        }

        while (topRow.countNonBuildingCards() < topNeeded && !tribeDeck.isEmpty()) {
            topRow.appendCard(tribeDeck.remove(0));
        }
    }

    public void revealInitialBuildings(PlayerCount playerCount) {
        List<BuildingCard> eraOneDeck = buildingDecks.getOrDefault(Era.I, List.of());
        for (BuildingCard buildingCard : new ArrayList<>(eraOneDeck)) {
            topRow.appendCard(buildingCard);
        }
        eraOneDeck.clear();
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
        VisibleRowState row = getVisibleRow(pick.getRow());
        return row.removeCardAt(pick.getSlotIndex());
    }

    public void discardBottomCharactersAndEvents() {
        bottomRow.removeCharactersAndEvents();
    }

    public void moveTopCharactersAndEventsToBottom() {
        List<Card> movable = topRow.extractCharactersAndEvents();
        for (Card card : movable) {
            bottomRow.appendCard(card);
        }
    }

    public RowRefillResult refillTopRow(PlayerCount playerCount) {
        discardBottomCharactersAndEvents();
        moveTopCharactersAndEventsToBottom();

        int requiredTopCards = playerCountToInt(playerCount) + 4;
        Era visibleEra = inferCurrentVisibleEra();
        boolean eraTransitionTriggered = false;
        Era newEra = null;

        while (topRow.countNonBuildingCards() < requiredTopCards && !tribeDeck.isEmpty()) {
            TribeDeckCard drawn = tribeDeck.remove(0);
            topRow.appendCard(drawn);
            if (!eraTransitionTriggered && drawn.getEra().ordinal() > visibleEra.ordinal()) {
                eraTransitionTriggered = true;
                newEra = drawn.getEra();
            }
        }

        boolean deckExhausted = tribeDeck.isEmpty() && topRow.countNonBuildingCards() < requiredTopCards;
        return new RowRefillResult(eraTransitionTriggered, newEra, deckExhausted);
    }

    public void applyEraTransition(Era newEra) {
        if (newEra == Era.III) {
            discardBottomBuildingsForEraIII();
        }
        moveTopBuildingsToBottom();
        revealBuildingsForEra(newEra);
    }

    public void discardBottomBuildingsForEraIII() {
        bottomRow.removeBuildings();
    }

    public void moveTopBuildingsToBottom() {
        List<Card> buildings = topRow.extractBuildings();
        for (Card card : buildings) {
            bottomRow.appendCard(card);
        }
    }

    public void revealBuildingsForEra(Era newEra) {
        List<BuildingCard> deck = buildingDecks.getOrDefault(newEra, List.of());
        for (BuildingCard building : new ArrayList<>(deck)) {
            topRow.appendCard(building);
        }
        deck.clear();
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
        List<EventCard> ordered = new ArrayList<>(events);
        ordered.sort(Comparator.comparingInt(this::eventPriority).thenComparing(e -> e.getEra().ordinal()));
        return ordered;
    }

    private int eventPriority(EventCard eventCard) {
        if (eventCard.getEventType() == EventType.SUSTENANCE) {
            return 99;
        }
        return switch (eventCard.getEventType()) {
            case HUNTING -> 1;
            case SHAMANIC_RITUAL -> 2;
            case CAVE_PAINTINGS -> 3;
            case SUSTENANCE -> 99;
        };
    }

    private Era inferCurrentVisibleEra() {
        return topRow.getSlots().stream()
                .map(RowSlot::getCard)
                .filter(card -> card instanceof TribeDeckCard)
                .map(Card::getEra)
                .findFirst()
                .orElse(Era.I);
    }

    private int playerCountToInt(PlayerCount playerCount) {
        return switch (playerCount) {
            case TWO -> 2;
            case THREE -> 3;
            case FOUR -> 4;
            case FIVE -> 5;
        };
    }

    private Map<Era, Integer> buildingCountsFor(PlayerCount playerCount) {
        Map<Era, Integer> counts = new EnumMap<>(Era.class);
        switch (playerCount) {
            case TWO -> {
                counts.put(Era.I, 1);
                counts.put(Era.II, 2);
                counts.put(Era.III, 3);
            }
            case THREE -> {
                counts.put(Era.I, 2);
                counts.put(Era.II, 2);
                counts.put(Era.III, 4);
            }
            case FOUR -> {
                counts.put(Era.I, 2);
                counts.put(Era.II, 3);
                counts.put(Era.III, 4);
            }
            case FIVE -> {
                counts.put(Era.I, 2);
                counts.put(Era.II, 3);
                counts.put(Era.III, 5);
            }
        }
        return counts;
    }

    private List<TribeDeckCard> createEraDeck(Era era, PlayerCount playerCount, int copiesPerType) {
        Set<PlayerCount> allowed = EnumSet.allOf(PlayerCount.class);
        List<TribeDeckCard> cards = new ArrayList<>();
        for (int i = 0; i < copiesPerType; i++) {
            cards.add(new Hunter("Hunter-" + era + '-' + i, era, allowed, i % 2 == 0));
            cards.add(new Shaman("Shaman-" + era + '-' + i, era, allowed, (i % 3) + 1));
            cards.add(new Artist("Artist-" + era + '-' + i, era, allowed));
            cards.add(new Builder("Builder-" + era + '-' + i, era, allowed, (i % 3) + 1, (i % 2) + 1));
            cards.add(new Inventor("Inventor-" + era + '-' + i, era, allowed, InventionIcon.values()[(era.ordinal() * copiesPerType + i) % InventionIcon.values().length]));
            cards.add(new Gatherer("Gatherer-" + era + '-' + i, era, allowed));
        }
        cards.add(new HuntingEvent("Hunting-" + era, era, allowed, era.ordinal() + 1));
        cards.add(new ShamanicRitualEvent("Shamanic-" + era, era, allowed, (era.ordinal() + 1) * 5, (era.ordinal() + 1) * 2));
        cards.add(new CavePaintingsEvent("Cave-" + era, era, allowed, 2, (era.ordinal() + 1) * 2, era.ordinal() + 1));
        cards.add(new SustenanceEvent("Sustenance-" + era, era, allowed, era.ordinal() + 2));
        return cards.stream()
                .filter(card -> card.getAllowedPlayerCounts().contains(playerCount))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<BuildingCard> createBuildingPool(Era era) {
        List<BuildingCard> pool = new ArrayList<>();
        pool.add(new BuildingCard("Set Food", era, 3, 2, new TriggeredFoodRewardEffect(TriggeredFoodRewardType.COMPLETE_SET, 5)));
        pool.add(new BuildingCard("Pair Food", era, 3, 2, new TriggeredFoodRewardEffect(TriggeredFoodRewardType.MATCHING_INVENTOR_PAIR, 3)));
        pool.add(new BuildingCard("Sustenance Artists", era, 2, 1, new SustenanceDiscountEffect(CharacterType.ARTIST, 1)));
        pool.add(new BuildingCard("Sustenance Inventors", era, 2, 1, new SustenanceDiscountEffect(CharacterType.INVENTOR, 1)));
        pool.add(new BuildingCard("No Shaman Penalty", era, 4, 2, new ShamanicRitualEffect(0, true, false, false)));
        pool.add(new BuildingCard("Extra Shaman Icons", era, 4, 3, new ShamanicRitualEffect(3, false, false, false)));
        pool.add(new BuildingCard("Double Shaman Reward", era, 5, 4, new ShamanicRitualEffect(0, false, true, true)));
        pool.add(new BuildingCard("Hunt Bonus", era, 4, 3, new PerCharacterEventBonusEffect(EventType.HUNTING, CharacterType.HUNTER, 1, 1)));
        pool.add(new BuildingCard("Turn Order Food", era, 3, 2, new TurnOrderPlacementEffect(1)));
        pool.add(new BuildingCard("Extra Top Pick", era, 5, 4, new ExtraTopRowPickEffect()));
        pool.add(new BuildingCard("Double Builders", era, 5, 3, new EndGameScoringEffect(EndGameScoringType.DOUBLE_BUILDERS, 0, null)));
        pool.add(new BuildingCard("Artist Endgame", era, 4, 2, new EndGameScoringEffect(EndGameScoringType.PER_CHARACTER_TYPE, 3, CharacterType.ARTIST)));
        pool.add(new BuildingCard("Flat Endgame", era, 6, 25, new NoBuildingEffect()));
        return pool;
    }
}
