package mesos.model.player;

import mesos.model.cards.buildings.BuildingCard;
import mesos.model.cards.characters.CharacterCard;
import mesos.model.game.GameContext;
import mesos.model.offer.OfferTile;
import mesos.model.tribe.Tribe;

public class Player {
    private final String id;
    private int food;
    private int prestigePoints;
    private final Totem totem;
    private final Tribe tribe;
    private OfferTile selectedOfferTile;

    public Player(String id, Totem totem) {
        this.id = id;
        this.totem = totem;
        this.tribe = new Tribe();
        this.food = 0;
        this.prestigePoints = 0;
    }

    public boolean payFood(int amount) {
        if (food < amount) {
            return false;
        }
        food -= amount;
        return true;
    }

    public void gainFood(int amount) {
        this.food += Math.max(0, amount);
    }

    public int getFood() {
        return food;
    }

    public void addPrestige(int amount) {
        this.prestigePoints += Math.max(0, amount);
    }

    public void removePrestige(int amount) {
        this.prestigePoints = Math.max(0, this.prestigePoints - Math.max(0, amount));
    }

    public void chooseOfferTile(OfferTile tile) {
        this.selectedOfferTile = tile;
    }

    public void clearSelectedOfferTile() {
        this.selectedOfferTile = null;
    }

    public OfferTile getSelectedOfferTile() {
        return selectedOfferTile;
    }

    public void acquireCharacter(CharacterCard card) {
        tribe.addCharacter(card);
    }

    public void acquireBuilding(BuildingCard card, GameContext context) {
        int discount = tribe.calculateBuildingDiscount();
        int realCost = card.calculateRealCost(discount);
        if (!payFood(realCost)) {
            throw new IllegalStateException("Not enough food to buy building");
        }
        tribe.addBuilding(card);
        card.onAcquire(this, context);
    }

    public String getId() {
        return id;
    }

    public Totem getTotem() {
        return totem;
    }

    public Tribe getTribe() {
        return tribe;
    }

    public int getPrestigePoints() {
        return prestigePoints;
    }
}
