package mesos.model.game;

import mesos.model.board.Board;
import mesos.model.board.RowRefillResult;
import mesos.model.cards.events.EventCard;
import mesos.model.enums.Era;
import mesos.model.enums.GamePhase;
import mesos.model.enums.PlayerCount;
import mesos.model.offer.OfferTile;
import mesos.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private int currentRound;
    private Era currentEra;
    private final int maxRounds = 10;
    private final PlayerCount playerCount;
    private GamePhase phase;
    private final List<Player> players;
    private final Board board;

    public Game(PlayerCount playerCount, List<Player> players) {
        this.playerCount = playerCount;
        this.players = new ArrayList<>(players);
        this.board = new Board();
        this.currentRound = 1;
        this.currentEra = Era.I;
        this.phase = GamePhase.SETUP;
    }

    public void setup() {
        board.buildTribeDeck(playerCount);
        board.buildBuildingDecks(playerCount);
        board.configureOfferTrack(playerCount);
        board.configureTurnOrderTile(playerCount);
        board.revealInitialRows(playerCount);
        board.revealInitialBuildings(playerCount);
        assignInitialFood();
    }

    public void assignInitialFood() {
        for (Player player : players) {
            player.gainFood(12);
        }
    }

    public void startGame() {
        setup();
        phase = GamePhase.PLACEMENT;
    }

    public void placementPhase() {
        phase = GamePhase.PLACEMENT;

    }

    public void actionPhase() {
        phase = GamePhase.ACTION_RESOLUTION;
        List<OfferTile> orderedTiles = board.getOfferTrack().getTilesInResolutionOrder();

        for (OfferTile ignored : orderedTiles) {

        }
    }

    public void endOfRound() {
        phase = GamePhase.END_OF_ROUND;

        List<EventCard> activeEvents = board.sortEventsForResolution(board.getActiveRoundEvents());
        for (EventCard event : activeEvents) {
            event.resolveEvent(buildContext(null));
        }

        board.discardBottomCharactersAndEvents();
        board.moveTopCharactersAndEventsToBottom();
        RowRefillResult result = board.refillTopRow(playerCount);
        handleRefillResult(result);
        currentRound++;
    }

    public void handleRefillResult(RowRefillResult result) {
        if (result.isEraTransitionTriggered() && result.getNewEra() != null) {
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
        List<EventCard> visibleEvents = board.sortEventsForResolution(board.getAllVisibleEvents());
        for (EventCard event : visibleEvents) {
            event.resolveEvent(buildContext(null));
        }
    }

    public void calculateFinalScore() {
        for (Player player : players) {
            int bonus = player.getTribe().getBuildingCards().stream()
                    .mapToInt(building -> building.getEndGameBonus(player, buildContext(player)))
                    .sum();
            player.addPrestige(bonus);
        }

    }

    public GameContext buildContext(Player activePlayer) {
        OfferTile selected = activePlayer != null ? activePlayer.getSelectedOfferTile() : null;
        return new GameContext(this, board, currentRound, currentEra, phase, activePlayer, selected);
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public Era getCurrentEra() {
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
        return List.copyOf(players);
    }

    public Board getBoard() {
        return board;
    }
}
