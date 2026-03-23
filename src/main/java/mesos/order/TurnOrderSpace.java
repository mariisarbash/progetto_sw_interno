package mesos.order;

import mesos.player.Totem;

public class TurnOrderSpace {
    private final int position;
    private final int foodBonus;
    private final boolean lastSpace;
    private Totem occupiedBy;

    public TurnOrderSpace(int position, int foodBonus, boolean lastSpace) {
        this.position = position;
        this.foodBonus = foodBonus;
        this.lastSpace = lastSpace;
    }

    public boolean isFree() {
        return occupiedBy == null;
    }

    public void assignTotem(Totem totem) {
        this.occupiedBy = totem;
    }

    public void clear() {
        this.occupiedBy = null;
    }

    public int getPosition() {
        return position;
    }

    public int getFoodBonus() {
        return foodBonus;
    }

    public boolean isLastSpace() {
        return lastSpace;
    }

    public Totem getOccupiedBy() {
        return occupiedBy;
    }
}
