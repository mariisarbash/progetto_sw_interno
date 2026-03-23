package mesos.effects;

import mesos.board.Board;
import mesos.board.RowSlot;
import mesos.cards.BuildingCard;
import mesos.cards.Card;
import mesos.cards.characters.CharacterCard;
import mesos.player.Player;

public class ExtraTopRowPickEffect extends BuildingEffect {
    @Override
    public void onEndOfActionPhase(Player owner, Board board) {
        if (owner == null || board == null) {
            return;
        }
        for (RowSlot slot : board.getVisibleRow(mesos.enums.VisibleRow.TOP).getSelectableSlots()) {
            Card card = slot.getCard();
            if (card instanceof CharacterCard characterCard) {
                board.takeCard(new mesos.offer.CardPick(mesos.enums.VisibleRow.TOP, slot.getIndex()));
                owner.acquireCharacter(characterCard);
                return;
            }
            if (card instanceof BuildingCard buildingCard) {
                int cost = buildingCard.calculateRealCost(owner.getTribe().calculateBuildingDiscount());
                if (owner.getFood() >= cost) {
                    board.takeCard(new mesos.offer.CardPick(mesos.enums.VisibleRow.TOP, slot.getIndex()));
                    owner.acquireBuilding(buildingCard);
                    return;
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return "EXTRA_TOP_ROW_PICK";
    }
}
