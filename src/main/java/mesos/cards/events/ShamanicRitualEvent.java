package mesos.cards.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mesos.enums.Era;
import mesos.enums.EventType;
import mesos.enums.PlayerCount;
import mesos.player.Player;

public class ShamanicRitualEvent extends EventCard {
    private final int ppReward;
    private final int ppPenalty;

    public ShamanicRitualEvent(String name, Era era, Set<PlayerCount> allowedPlayerCounts, int ppReward, int ppPenalty) {
        super(name, era, allowedPlayerCounts, EventType.SHAMANIC_RITUAL);
        this.ppReward = ppReward;
        this.ppPenalty = ppPenalty;
    }

    @Override
    public void resolveEvent(List<Player> players) {
        if (players.isEmpty()) {
            return;
        }

        Map<Player, Integer> iconsByPlayer = new HashMap<>();
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (Player player : players) {
            int icons = player.getTribe().getTotalShamanIcons() + player.getTribe().getAdditionalShamanIcons(player);
            iconsByPlayer.put(player, icons);
            max = Math.max(max, icons);
            min = Math.min(min, icons);
        }

        final int maxIcons = max;
        long maxCount = iconsByPlayer.values().stream().filter(value -> value == maxIcons).count();

        for (Player player : players) {
            int icons = iconsByPlayer.get(player);
            boolean tiedForMax = (icons == maxIcons && maxCount > 1);

            // Gestione del PREMIO (Maggioranza)
            if (icons == max) {
                int reward = ppReward;
                // Raddoppia SOLO se non c'è pareggio e il giocatore ha l'effetto
                if (!tiedForMax && player.getTribe().doublesShamanicReward(player)) {
                    reward = ppReward * 2;
                }
                player.addPrestige(reward);
            }

            // Gestione della PENALITÀ (Minoranza).
            // Usiamo un "if" separato e non un "else if" per coprire il Pareggio Totale!
            if (icons == min && !player.getTribe().ignoresShamanicPenalty(player)) {
                player.removePrestige(ppPenalty);
            }
        }
    }

    public int getPpReward() {
        return ppReward;
    }

    public int getPpPenalty() {
        return ppPenalty;
    }
}
