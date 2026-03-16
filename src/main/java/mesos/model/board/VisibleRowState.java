package mesos.model.board;

import mesos.model.cards.base.Card;
import mesos.model.enums.VisibleRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VisibleRowState {
    private final VisibleRow row;
    private final List<RowSlot> slots;

    public VisibleRowState(VisibleRow row) {
        this.row = row;
        this.slots = new ArrayList<>();
    }

    public VisibleRow getRow() {
        return row;
    }

    public RowSlot getSlot(int index) {
        return slots.get(index);
    }

    public List<RowSlot> getSlots() {
        return List.copyOf(slots);
    }

    public List<RowSlot> getSelectableSlots() {
        return slots.stream().filter(RowSlot::isSelectable).collect(Collectors.toList());
    }

    public Card removeCardAt(int index) {
        RowSlot slot = slots.get(index);
        Card removed = slot.getCard();
        slot.setCard(null);
        return removed;
    }

    public void appendCard(Card card) {
        slots.add(new RowSlot(slots.size(), card));
    }

    public void clear() {
        slots.clear();
    }
}
