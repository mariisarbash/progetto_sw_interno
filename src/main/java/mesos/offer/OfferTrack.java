package mesos.offer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import mesos.enums.OfferTileId;
import mesos.enums.PlayerCount;
import mesos.player.Player;

public class OfferTrack {
    private final List<OfferTile> tiles;

    public OfferTrack() {
        this.tiles = new ArrayList<>();
    }

    public void configureForPlayerCount(PlayerCount playerCount) {
        tiles.clear();
        addTile(playerCount, OfferTileId.A, 0, 0, 3, PlayerCount.FIVE);
        addTile(playerCount, OfferTileId.B, 0, 1, 0, PlayerCount.TWO, PlayerCount.THREE, PlayerCount.FOUR, PlayerCount.FIVE);
        addTile(playerCount, OfferTileId.C, 1, 0, 0, PlayerCount.TWO, PlayerCount.THREE, PlayerCount.FOUR, PlayerCount.FIVE);
        addTile(playerCount, OfferTileId.D, 1, 1, 0, PlayerCount.TWO, PlayerCount.THREE, PlayerCount.FOUR, PlayerCount.FIVE);
        addTile(playerCount, OfferTileId.E, 2, 0, 0, PlayerCount.THREE, PlayerCount.FOUR, PlayerCount.FIVE);
        addTile(playerCount, OfferTileId.F, 0, 2, 0, PlayerCount.FOUR, PlayerCount.FIVE);
        addTile(playerCount, OfferTileId.G, 1, 2, 0, PlayerCount.FIVE);
    }

    private void addTile(PlayerCount current, OfferTileId id, int top, int bottom, int food, PlayerCount... allowed) {
        OfferTile tile = new OfferTile(id);
        tile.setCardsFromTop(top);
        tile.setCardsFromBottom(bottom);
        tile.setDirectFoodBonus(food);
        tile.setAllowedPlayerCounts(allowed);
        if (tile.getAllowedPlayerCounts().contains(current)) {
            tiles.add(tile);
        }
    }

    public void placeTotem(Player player, OfferTile tile) {
        if (tile != null && tile.isAvailable()) {
            tile.setOccupiedBy(player.getTotem());
        }
    }

    public List<OfferTile> getTilesInResolutionOrder() {
        return tiles.stream().sorted(Comparator.comparing(OfferTile::getId)).toList();
    }

    public List<OfferTile> getTiles() {
        return new ArrayList<>(tiles);
    }
}
