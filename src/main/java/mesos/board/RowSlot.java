package mesos.board;

import mesos.cards.BuildingCard;
import mesos.cards.Card;
import mesos.cards.characters.CharacterCard;
import mesos.cards.events.EventCard;

public class RowSlot {
    private final int index;
    private Card card;

    public RowSlot(int index, Card card) {
        this.index = index;
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

    public int getIndex() {
        return index;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void clearCard() {
        this.card = null;
    }
}
