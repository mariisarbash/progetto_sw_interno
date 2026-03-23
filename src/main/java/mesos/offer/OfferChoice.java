package mesos.offer;

import java.util.ArrayList;
import java.util.List;
import mesos.enums.VisibleRow;

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
        return (int) orderedPicks.stream().filter(pick -> pick.getRow() == row).count();
    }

    public List<CardPick> getOrderedPicks() {
        return new ArrayList<>(orderedPicks);
    }

    public boolean isEmpty() {
        return orderedPicks.isEmpty();
    }

    public OfferTile getOfferTile() {
        return offerTile;
    }
}
