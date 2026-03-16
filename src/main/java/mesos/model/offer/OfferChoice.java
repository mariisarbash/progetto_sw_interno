package mesos.model.offer;

import mesos.model.enums.VisibleRow;

import java.util.ArrayList;
import java.util.List;

public class OfferChoice {
    private final OfferTile offerTile;
    private final List<CardPick> orderedPicks;

    public OfferChoice(OfferTile offerTile) {
        this.offerTile = offerTile;
        this.orderedPicks = new ArrayList<>();
    }

    public void addPick(CardPick pick) {
        orderedPicks.add(pick);
    }

    public int countPicksFromRow(VisibleRow row) {
        return (int) orderedPicks.stream().filter(p -> p.getRow() == row).count();
    }

    public List<CardPick> getOrderedPicks() {
        return List.copyOf(orderedPicks);
    }

    public boolean isEmpty() {
        return orderedPicks.isEmpty();
    }

    public OfferTile getOfferTile() {
        return offerTile;
    }
}
