package mesos.player;

import java.util.Objects;
import mesos.enums.TotemColor;

public class Totem {
    private final TotemColor color;

    public Totem(TotemColor color) {
        this.color = color;
    }

    public TotemColor getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Totem totem)) {
            return false;
        }
        return color == totem.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
