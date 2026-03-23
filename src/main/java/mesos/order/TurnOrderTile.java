package mesos.order;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mesos.enums.PlayerCount;
import mesos.player.Player;
import mesos.player.Totem;

public class TurnOrderTile {
    private final List<TurnOrderSpace> spaces;
    private final Map<Totem, Player> playerByTotem;

    public TurnOrderTile() {
        this.spaces = new ArrayList<>();
        this.playerByTotem = new LinkedHashMap<>();
    }

    public void configureForPlayerCount(PlayerCount playerCount) {
        spaces.clear();
        playerByTotem.clear();
        int count = switch (playerCount) {
            case TWO -> 2;
            case THREE -> 3;
            case FOUR -> 4;
            case FIVE -> 5;
        };
        for (int i = 0; i < count; i++) {
            boolean last = i == count - 1;
            int bonus = last ? 0 : 1;
            spaces.add(new TurnOrderSpace(i, bonus, last));
        }
    }

    public TurnOrderSpace addTotem(Totem totem, Player player) {
        TurnOrderSpace firstFreeSpace = getFirstFreeSpace();
        if (firstFreeSpace != null) {
            firstFreeSpace.assignTotem(totem);
            playerByTotem.put(totem, player);
        }
        return firstFreeSpace;
    }

    public List<Player> determineNewOrder() {
        List<Player> ordered = new ArrayList<>();
        for (TurnOrderSpace space : spaces) {
            if (space.getOccupiedBy() != null) {
                Player player = playerByTotem.get(space.getOccupiedBy());
                if (player != null) {
                    ordered.add(player);
                }
            }
        }
        return ordered;
    }

    public void applySpaceEffect(TurnOrderSpace space, Player player) {
        if (space == null || player == null) {
            return;
        }
        if (space.getFoodBonus() > 0) {
            player.gainFood(space.getFoodBonus());
        }
        if (space.isLastSpace() && !player.payFood(1)) {
            player.removePrestige(2);
        }
    }

    public TurnOrderSpace getFirstFreeSpace() {
        return spaces.stream().filter(TurnOrderSpace::isFree).findFirst().orElse(null);
    }

    public List<TurnOrderSpace> getSpaces() {
        return new ArrayList<>(spaces);
    }

    public void clearSpaces() {
        spaces.forEach(TurnOrderSpace::clear);
    }
}
