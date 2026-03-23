package mesos.offer;

import mesos.enums.VisibleRow;

public class CardPick {
    private final VisibleRow row;
    private final int slotIndex;

    public CardPick(VisibleRow row, int slotIndex) {
        this.row = row;
        this.slotIndex = slotIndex;
    }

    public VisibleRow getRow() {
        return row;
    }

    public int getSlotIndex() {
        return slotIndex;
    }
}
