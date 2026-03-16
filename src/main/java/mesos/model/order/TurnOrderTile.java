package mesos.model.order;

import mesos.model.enums.PlayerCount;
import mesos.model.player.Player;
import mesos.model.player.Totem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TurnOrderTile {
    private final List<TurnOrderSpace> spaces;

    public TurnOrderTile() {
        this.spaces = new ArrayList<>();
    }

    public void configureForPlayerCount(PlayerCount playerCount) {
        spaces.clear();
        int size = switch (playerCount) {
            case TWO -> 2;
            case THREE -> 3;
            case FOUR -> 4;
            case FIVE -> 5;
        };
        for (int i = 0; i < size; i++) {
            spaces.add(new TurnOrderSpace(i, Math.max(0, size - 1 - i), i == size - 1));
        }
    }

    public void addTotem(Totem totem, Player player) {
        TurnOrderSpace space = getFirstFreeSpace();
        if (space == null) {
            throw new IllegalStateException("No free turn order spaces available");
        }
        space.assignTotem(totem);
    }

    public List<Player> determineNewOrder() {

        return List.of();
    }

    public void applySpaceEffect(TurnOrderSpace space, Player player) {
        player.gainFood(space.getFoodBonus());
        if (space.isLastSpace()) {
            boolean paid = player.payFood(1);
            if (!paid) {
                player.removePrestige(2);
            }
        }
    }

    public TurnOrderSpace getFirstFreeSpace() {
        return spaces.stream().filter(TurnOrderSpace::isFree).findFirst().orElse(null);
    }

    public List<TurnOrderSpace> getSpaces() {
        return List.copyOf(spaces);
    }
}
