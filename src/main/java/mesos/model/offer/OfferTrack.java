package mesos.model.offer;

import mesos.model.enums.OfferTileId;
import mesos.model.enums.PlayerCount;
import mesos.model.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class OfferTrack {
    private final List<OfferTile> tiles;

    public OfferTrack() {
        this.tiles = new ArrayList<>();
    }

    public void configureForPlayerCount(PlayerCount playerCount) {
        tiles.clear();

        tiles.add(new OfferTile(OfferTileId.A, EnumSet.of(PlayerCount.FIVE), 0, 0, 3));
        tiles.add(new OfferTile(OfferTileId.B, EnumSet.allOf(PlayerCount.class), 1, 0, 0));
        tiles.add(new OfferTile(OfferTileId.C, EnumSet.allOf(PlayerCount.class), 0, 1, 0));
        tiles.add(new OfferTile(OfferTileId.D, EnumSet.allOf(PlayerCount.class), 1, 1, 0));
    }

    public void placeTotem(Player player, OfferTile tile) {
        if (!tile.isAvailable()) {
            throw new IllegalStateException("Offer tile already occupied");
        }
        tile.setOccupiedBy(player.getTotem());
        player.chooseOfferTile(tile);
    }

    public List<OfferTile> getTilesInResolutionOrder() {
        return tiles.stream().sorted(Comparator.comparing(OfferTile::getId)).toList();
    }

    public List<OfferTile> getTiles() {
        return List.copyOf(tiles);
    }
}
