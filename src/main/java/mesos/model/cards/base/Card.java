package mesos.model.cards.base;

import mesos.model.enums.Era;

public abstract class Card {
    private final String name;
    private final Era era;

    protected Card(String name, Era era) {
        this.name = name;
        this.era = era;
    }

    public String getName() {
        return name;
    }

    public Era getEra() {
        return era;
    }
}
