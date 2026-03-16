package mesos.model.player;

import mesos.model.enums.TotemColor;

public class Totem {
    private final TotemColor color;

    public Totem(TotemColor color) {
        this.color = color;
    }

    public TotemColor getColor() {
        return color;
    }
}
