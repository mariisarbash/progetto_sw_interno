package mesos.board;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import mesos.cards.Card;
import mesos.enums.VisibleRow;

public class VisibleRowState {
    private final VisibleRow row;
    private final List<RowSlot> slots;

    public VisibleRowState(VisibleRow row) {
        this.row = row;
        this.slots = new ArrayList<>();
    }

    public RowSlot getSlot(int index) {
        return slots.stream().filter(slot -> slot.getIndex() == index).findFirst().orElse(null);
    }

    public List<RowSlot> getSlots() {
        return new ArrayList<>(slots);
    }

    public List<RowSlot> getSelectableSlots() {
        return slots.stream().filter(RowSlot::isSelectable).collect(Collectors.toList());
    }

    public Card removeCardAt(int index) {
        RowSlot slot = getSlot(index);
        if (slot == null) {
            return null;
        }
        Card card = slot.getCard();
        slot.clearCard();
        return card;
    }

    public void appendCard(Card card) {
        if (card == null) {
            return;
        }
        for (RowSlot slot : slots) {
            if (slot.isEmpty()) {
                slot.setCard(card);
                return;
            }
        }
        int nextIndex = slots.size();
        slots.add(new RowSlot(nextIndex, card));
    }

    public void removeCharactersAndEvents() {
        for (RowSlot slot : slots) {
            if (slot.containsCharacter() || slot.containsEvent()) {
                slot.clearCard();
            }
        }
    }

    public List<Card> extractCharactersAndEvents() {
        List<Card> result = new ArrayList<>();
        for (RowSlot slot : slots) {
            if (slot.containsCharacter() || slot.containsEvent()) {
                result.add(slot.getCard());
                slot.clearCard();
            }
        }
        return result;
    }

    public void removeBuildings() {
        for (RowSlot slot : slots) {
            if (slot.containsBuilding()) {
                slot.clearCard();
            }
        }
    }

    public List<Card> extractBuildings() {
        List<Card> result = new ArrayList<>();
        for (RowSlot slot : slots) {
            if (slot.containsBuilding()) {
                result.add(slot.getCard());
                slot.clearCard();
            }
        }
        return result;
    }

    public int countNonBuildingCards() {
        int count = 0;
        for (RowSlot slot : slots) {
            if (!slot.isEmpty() && !slot.containsBuilding()) {
                count++;
            }
        }
        return count;
    }

    public VisibleRow getRow() {
        return row;
    }
}
