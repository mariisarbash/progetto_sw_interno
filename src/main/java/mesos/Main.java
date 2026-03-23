package mesos;

import java.util.List;
import mesos.enums.PlayerCount;
import mesos.enums.TotemColor;
import mesos.game.Game;
import mesos.player.Player;
import mesos.player.Totem;

public class Main {
    public static void main(String[] args) {
        List<Player> players = List.of(
                new Player("P1", new Totem(TotemColor.RED)),
                new Player("P2", new Totem(TotemColor.BLUE))
        );
        Game game = new Game(PlayerCount.TWO, players);
        game.startGame();
    }
}
