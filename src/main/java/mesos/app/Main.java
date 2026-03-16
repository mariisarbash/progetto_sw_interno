package mesos.app;

import mesos.model.enums.PlayerCount;
import mesos.model.enums.TotemColor;
import mesos.model.game.Game;
import mesos.model.player.Player;
import mesos.model.player.Totem;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Player p1 = new Player("P1", new Totem(TotemColor.RED));
        Player p2 = new Player("P2", new Totem(TotemColor.BLUE));

        Game game = new Game(PlayerCount.TWO, List.of(p1, p2));
        game.startGame();

        System.out.println("Mesos skeleton initialized. Current phase: " + game.getPhase());
    }
}
