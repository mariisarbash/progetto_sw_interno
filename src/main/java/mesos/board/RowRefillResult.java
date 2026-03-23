package mesos.board;

import mesos.enums.Era;

public class RowRefillResult {
    private final boolean eraTransitionTriggered;
    private final Era newEra;
    private final boolean deckExhausted;

    public RowRefillResult(boolean eraTransitionTriggered, Era newEra, boolean deckExhausted) {
        this.eraTransitionTriggered = eraTransitionTriggered;
        this.newEra = newEra;
        this.deckExhausted = deckExhausted;
    }

    public boolean isEraTransitionTriggered() {
        return eraTransitionTriggered;
    }

    public Era getNewEra() {
        return newEra;
    }

    public boolean isDeckExhausted() {
        return deckExhausted;
    }
}
