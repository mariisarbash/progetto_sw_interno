package mesos.model.offer;

import mesos.model.board.Board;
import mesos.model.cards.base.Card;
import mesos.model.enums.VisibleRow;

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

    public Card resolve(Board board) {
        return board.getVisibleRow(row).removeCardAt(slotIndex);
    }
}
