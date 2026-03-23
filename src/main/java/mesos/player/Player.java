package mesos.player;

import mesos.cards.BuildingCard;
import mesos.cards.characters.CharacterCard;
import mesos.offer.OfferTile;
import mesos.tribe.Tribe;

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
    }

    public boolean payFood(int amount) {
        if (amount <= 0) {
            return true;
        }
        if (food < amount) {
            return false;
        }
        food -= amount;
        return true;
    }

    public void gainFood(int amount) {
        if (amount > 0) {
            food += amount;
        }
    }

    public int getFood() {
        return food;
    }

    public void addPrestige(int amount) {
        if (amount > 0) {
            prestigePoints += amount;
        }
    }

    public void removePrestige(int amount) {
        if (amount > 0) {
            prestigePoints -= amount;
        }
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
        card.onPurchase(this);
        tribe.notifyBuildingsOnCharacterAdded(this, card);
    }

    public void acquireBuilding(BuildingCard card) {
        int discount = tribe.calculateBuildingDiscount();
        int realCost = card.calculateRealCost(discount);
        if (!payFood(realCost)) {
            return;
        }
        tribe.addBuilding(card);
        card.onAcquire(this);
    }

    public String getId() {
        return id;
    }

    public int getPrestigePoints() {
        return prestigePoints;
    }

    public Totem getTotem() {
        return totem;
    }

    public Tribe getTribe() {
        return tribe;
    }
}
