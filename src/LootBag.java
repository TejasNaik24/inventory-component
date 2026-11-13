import components.inventory.Inventory;
import components.inventory.Inventory1L;

/**
 * Represents a player's loot bag in a game.
 *
 * @author Tejas Naik
 */
public final class LootBag {

    /*
     * Private members -----------------------------------------------------
     */

    /**
     * Inventory containing the items in the loot bag.
     */
    private Inventory contents;

    /**
     * Maximum number of individual items the bag can hold.
     */
    private int capacity;

    /*
     * Constructors --------------------------------------------------------
     */

    /**
     * Creates an empty loot bag with the specified capacity.
     *
     * @param capacity
     *            the maximum number of individual items the bag can hold
     * @requires capacity > 0
     */
    public LootBag(int capacity) {
        assert capacity > 0 : "Violation of: capacity > 0";

        this.contents = new Inventory1L();
        this.capacity = capacity;
    }

    /*
     * Public methods ------------------------------------------------------
     */

    /**
     * Adds items to the bag if there is enough available space.
     *
     * @param item
     *            the name of the item
     * @param amount
     *            the quantity to pick up
     * @return true if the items were added, false otherwise
     * @updates this
     * @requires |item| > 0 and amount > 0
     * @ensures if amount <= #spaceLeft(), then the items are added,
     *          otherwise the bag remains unchanged
     */
    public boolean pickUp(String item, int amount) {
        assert item != null : "Violation of: item is not null";
        assert item.length() > 0 : "Violation of: item is nonempty";
        assert amount > 0 : "Violation of: amount > 0";

        boolean pickedUp = false;

        if (amount <= this.spaceLeft()) {
            this.contents.add(item, amount);
            pickedUp = true;
        }

        return pickedUp;
    }

    /**
     * Uses one item from the bag if at least one is available.
     *
     * @param item
     *            the name of the item to use
     * @return true if an item was used, false otherwise
     * @updates this
     * @requires |item| > 0
     * @ensures if the item is available, one is removed and true is returned;
     *          otherwise the bag remains unchanged and false is returned
     */
    public boolean use(String item) {
        assert item != null : "Violation of: item is not null";
        assert item.length() > 0 : "Violation of: item is nonempty";

        boolean used = false;

        if (this.contents.hasItem(item)) {
            this.contents.remove(item, 1);
            used = true;
        }

        return used;
    }

    /**
     * Takes all items from a chest if they fit in the bag.
     *
     * @param chest
     *            the inventory containing the chest's items
     * @return the number of individual items gained, or zero if they
     *         do not fit
     * @updates this, chest
     * @ensures <pre>
     * if the chest's contents fit in the available space, then
     *   the bag gains all the chest's items and the chest becomes empty;
     * otherwise
     *   both the bag and chest remain unchanged
     * </pre>
     */
    public int lootChest(Inventory chest) {
        assert chest != null : "Violation of: chest is not null";

        int gained = 0;
        int chestQuantity = chest.totalQuantity();

        if (chestQuantity <= this.spaceLeft()) {
            gained = chestQuantity;
            this.contents.restock(chest);
            chest.clear();
        }

        return gained;
    }

    /**
     * Reports the number of additional individual items the bag can hold.
     *
     * @return the remaining capacity
     */
    public int spaceLeft() {
        return this.capacity - this.contents.totalQuantity();
    }

    /**
     * Returns a string representation of the loot bag.
     *
     * @return the bag's current contents and capacity
     */
    @Override
    public String toString() {
        return "LootBag " + this.contents.totalQuantity() + "/"
                + this.capacity + " " + this.contents;
    }

    /*
     * Main method ---------------------------------------------------------
     */

    /**
     * Demonstrates a player's loot bag operations.
     *
     * @param args
     *            command-line arguments (not used)
     */
    public static void main(String[] args) {

        LootBag bag = new LootBag(20);

        System.out.println("=== Player Loot Bag ===");
        System.out.println("Created a loot bag with capacity 20.");
        System.out.println(bag);

        /*
         * Pick up items.
         */
        System.out.println("\n=== Picking Up Items ===");

        System.out.println("Picked up 3 potions: "
                + bag.pickUp("potions", 3));
        System.out.println("Picked up 10 arrows: "
                + bag.pickUp("arrows", 10));
        System.out.println("Picked up 5 gold: "
                + bag.pickUp("gold", 5));

        System.out.println(bag);

        /*
         * Attempt to pick up more items than the bag can hold.
         */
        System.out.println("\n=== Full Bag Check ===");

        System.out.println("Tried to pick up 5 gems: "
                + bag.pickUp("gems", 5));
        System.out.println(bag);

        /*
         * Use potions until none remain.
         */
        System.out.println("\n=== Using Potions ===");

        System.out.println("Used a potion: " + bag.use("potions"));
        System.out.println("Used a potion: " + bag.use("potions"));
        System.out.println("Used a potion: " + bag.use("potions"));
        System.out.println("Tried another potion: "
                + bag.use("potions"));

        System.out.println(bag);

        /*
         * Find and successfully loot a chest.
         */
        System.out.println("\n=== Looting a Chest ===");

        Inventory chest = new Inventory1L();
        chest.add("shields", 2);
        chest.add("coins", 3);

        System.out.println("Found a chest:");
        System.out.println(chest);

        int gained = bag.lootChest(chest);

        System.out.println("Items gained from chest: " + gained);
        System.out.println("Loot bag after opening chest:");
        System.out.println(bag);

        System.out.println("Chest after looting:");
        System.out.println(chest);

        /*
         * Attempt to loot another chest that does not fit.
         */
        System.out.println("\n=== Second Chest ===");

        Inventory secondChest = new Inventory1L();
        secondChest.add("diamonds", 2);

        System.out.println("Found another chest:");
        System.out.println(secondChest);

        int secondGained = bag.lootChest(secondChest);

        System.out.println("Items gained from second chest: "
                + secondGained);
        System.out.println("Bag after second chest attempt:");
        System.out.println(bag);

        System.out.println("Second chest after attempt:");
        System.out.println(secondChest);

        /*
         * Final bag status.
         */
        System.out.println("\n=== Final Status ===");
        System.out.println("Space remaining: " + bag.spaceLeft());
        System.out.println(bag);
    }

}