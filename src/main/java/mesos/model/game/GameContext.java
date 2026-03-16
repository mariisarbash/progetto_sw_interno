package mesos.model.game;

import mesos.model.board.Board;
import mesos.model.enums.Era;
import mesos.model.enums.GamePhase;
import mesos.model.offer.OfferTile;
import mesos.model.player.Player;

public class GameContext {
    private final Game game;
    private final Board board;
    private final int currentRound;
    private final Era currentEra;
    private final GamePhase phase;
    private Player activePlayer;
    private OfferTile selectedOfferTile;

    public GameContext(Game game, Board board, int currentRound, Era currentEra, GamePhase phase, Player activePlayer, OfferTile selectedOfferTile) {
        this.game = game;
        this.board = board;
        this.currentRound = currentRound;
        this.currentEra = currentEra;
        this.phase = phase;
        this.activePlayer = activePlayer;
        this.selectedOfferTile = selectedOfferTile;
    }

    public Game getGame() {
        return game;
    }

    public Board getBoard() {
        return board;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public Era getCurrentEra() {
        return currentEra;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public OfferTile getSelectedOfferTile() {
        return selectedOfferTile;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public void setSelectedOfferTile(OfferTile selectedOfferTile) {
        this.selectedOfferTile = selectedOfferTile;
    }
}
