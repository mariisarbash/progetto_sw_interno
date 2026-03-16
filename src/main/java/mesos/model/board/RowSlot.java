package mesos.model.board;

import mesos.model.cards.base.Card;
import mesos.model.cards.buildings.BuildingCard;
import mesos.model.cards.characters.CharacterCard;
import mesos.model.cards.events.EventCard;

public class RowSlot {
    private final int index;
    private Card card;

    public RowSlot(int index, Card card) {
        this.index = index;
        this.card = card;
    }

    public int getIndex() {
        return index;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public boolean isEmpty() {
        return card == null;
    }

    public boolean containsCharacter() {
        return card instanceof CharacterCard;
    }

    public boolean containsEvent() {
        return card instanceof EventCard;
    }

    public boolean containsBuilding() {
        return card instanceof BuildingCard;
    }

    public boolean isSelectable() {
        return !isEmpty() && !containsEvent();
    }
}
