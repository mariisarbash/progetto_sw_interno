package mesos.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mesos.board.Board;
import mesos.board.RowRefillResult;
import mesos.cards.BuildingCard;
import mesos.cards.characters.Artist;
import mesos.cards.characters.Builder;
import mesos.cards.characters.CharacterCard;
import mesos.cards.characters.Inventor;
import mesos.cards.events.EventCard;
import mesos.enums.GamePhase;
import mesos.enums.PlayerCount;
import mesos.offer.OfferTile;
import mesos.player.Player;
import mesos.player.Totem;

public class Game {
    private int currentRound;
    private mesos.enums.Era currentEra;
    private final int maxRounds = 10;
    private final PlayerCount playerCount;
    private GamePhase phase;
    private final List<Player> players;
    private final Board board;

    public Game(PlayerCount playerCount, List<Player> players) {
        this.playerCount = playerCount;
        this.players = new ArrayList<>(players);
        this.currentRound = 1;
        this.currentEra = mesos.enums.Era.I;
        this.phase = GamePhase.SETUP;
        this.board = new Board();
    }

    public void setup() {
        board.buildTribeDeck(playerCount);
        board.buildBuildingDecks(playerCount);
        board.configureOfferTrack(playerCount);
        board.configureTurnOrderTile(playerCount);
        initializeTurnOrder();
        board.revealInitialRows(playerCount);
        board.revealInitialBuildings(playerCount);
        assignInitialFood();
        phase = GamePhase.PLACEMENT;
    }

    private void initializeTurnOrder() {
        List<Player> shuffled = new ArrayList<>(players);
        Collections.shuffle(shuffled);
        for (Player player : shuffled) {
            board.getTurnOrderTile().addTotem(player.getTotem(), player);
        }
    }

    public void assignInitialFood() {
        List<Player> order = board.getTurnOrderTile().determineNewOrder();
        if (order.isEmpty()) {
            order = new ArrayList<>(players);
        }
        for (int i = 0; i < order.size(); i++) {
            Player player = order.get(i);
            if (i == 0) {
                player.gainFood(2);
            } else if (i <= 2) {
                player.gainFood(3);
            } else {
                player.gainFood(4);
            }
        }
    }

    public void startGame() {
        if (phase == GamePhase.SETUP) {
            setup();
        }
        phase = GamePhase.PLACEMENT;
    }

    public void placementPhase() {
        phase = GamePhase.PLACEMENT;
        board.getTurnOrderTile().clearSpaces();
    }

    public void actionPhase() {
        phase = GamePhase.ACTION_RESOLUTION;
        for (OfferTile tile : board.getOfferTrack().getTilesInResolutionOrder()) {
            if (tile.getOccupiedBy() == null) {
                continue;
            }
            Player activePlayer = findPlayerByTotem(tile.getOccupiedBy());
            if (activePlayer == null) {
                continue;
            }
            activePlayer.chooseOfferTile(tile);
        }
    }

    public void endOfRound() {
        phase = GamePhase.END_OF_ROUND;
        for (Player player : players) {
            player.getTribe().notifyBuildingsOnEndOfActionPhase(player, board);
        }
        List<EventCard> events = board.sortEventsForResolution(board.getActiveRoundEvents());
        for (EventCard event : events) {
            event.resolveEvent(players);
        }
        RowRefillResult result = board.refillTopRow(playerCount);
        handleRefillResult(result);
        if (phase != GamePhase.GAME_OVER) {
            currentRound++;
            phase = GamePhase.PLACEMENT;
        }
    }

    public void handleRefillResult(RowRefillResult result) {
        if (result == null) {
            return;
        }
        if (result.isEraTransitionTriggered()) {
            currentEra = result.getNewEra();
            board.applyEraTransition(currentEra);
        }
        if (result.isDeckExhausted() || currentRound >= maxRounds) {
            triggerEndGame();
        }
    }

    public void triggerEndGame() {
        phase = GamePhase.FINAL_SCORING;
        resolveEndGameVisibleEvents();
        calculateFinalScore();
        phase = GamePhase.GAME_OVER;
    }

    public void resolveEndGameVisibleEvents() {
        List<EventCard> events = board.sortEventsForResolution(board.getAllVisibleEvents());
        for (EventCard event : events) {
            event.resolveEvent(players);
        }
    }

    public void calculateFinalScore() {
        for (Player player : players) {
            int inventors = player.getTribe().countCharacters(mesos.enums.CharacterType.INVENTOR);
            int distinctIcons = player.getTribe().countDistinctInventionIcons();
            int artistPairs = player.getTribe().countArtistPairs();
            int builderPrestige = player.getTribe().getCharacterCards().stream()
                    .filter(Builder.class::isInstance)
                    .map(Builder.class::cast)
                    .mapToInt(Builder::getPpValue)
                    .sum();
            int buildingPrestige = player.getTribe().getBuildingCards().stream()
                    .mapToInt(b -> b.getTotalPrestigeAtGameEnd(player))
                    .sum();
            player.addPrestige(inventors * distinctIcons);
            player.addPrestige(artistPairs * 10);
            player.addPrestige(builderPrestige);
            player.addPrestige(buildingPrestige);
        }
    }

    private Player findPlayerByTotem(Totem totem) {
        return players.stream().filter(player -> player.getTotem().equals(totem)).findFirst().orElse(null);
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public mesos.enums.Era getCurrentEra() {
        return currentEra;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public PlayerCount getPlayerCount() {
        return playerCount;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public Board getBoard() {
        return board;
    }
}
