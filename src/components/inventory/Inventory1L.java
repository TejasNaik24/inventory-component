package components.inventory;

import components.map.Map;
import components.map.Map1L;

/**
 * {@code Inventory} represented as a {@code Map<String, Integer>}.
 *
 * @convention <pre>
 * for all name: string of character, quantity: integer
 *     where ((name, quantity) is in $this.items)
 *   (name /= <> and quantity > 0)
 * </pre>
 * @correspondence this = $this.items
 *
 * @author Tejas Naik
 */
public class Inventory1L extends InventorySecondary {

    /*
     * Private members -----------------------------------------------------
     */

    /**
     * Item names mapped to their quantities.
     */
    private Map<String, Integer> items;

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        this.items = new Map1L<>();
    }

    /*
     * Constructors --------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Inventory1L() {
        this.createNewRep();
    }

    /*
     * Standard methods ----------------------------------------------------
     */

    @Override
    public final Inventory newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(Inventory source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof Inventory1L
                : "Violation of: source is of dynamic type Inventory1L";

        Inventory1L localSource = (Inventory1L) source;

        this.items = localSource.items;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ------------------------------------------------------
     */

    @Override
    public final void add(String name, int amount) {
        assert name != null : "Violation of: name is not null";
        assert name.length() > 0 : "Violation of: name is not empty";
        assert amount > 0 : "Violation of: amount > 0";

        if (this.items.hasKey(name)) {
            int newQuantity = this.items.value(name) + amount;
            this.items.remove(name);
            this.items.add(name, newQuantity);
        } else {
            this.items.add(name, amount);
        }
    }

    @Override
    public final void remove(String name, int amount) {
        assert name != null : "Violation of: name is not null";
        assert amount > 0 : "Violation of: amount > 0";
        assert this.items.hasKey(name)
                : "Violation of: name is in this inventory";
        assert amount <= this.items.value(name)
                : "Violation of: amount <= quantityOf(name)";

        int newQuantity = this.items.value(name) - amount;

        this.items.remove(name);

        if (newQuantity > 0) {
            this.items.add(name, newQuantity);
        }
    }

    @Override
    public final Map.Pair<String, Integer> removeAny() {
        assert this.items.size() > 0
                : "Violation of: this is not empty";

        return this.items.removeAny();
    }

    @Override
    public final int quantityOf(String name) {
        assert name != null : "Violation of: name is not null";

        int quantity = 0;

        if (this.items.hasKey(name)) {
            quantity = this.items.value(name);
        }

        return quantity;
    }

    @Override
    public final int size() {
        return this.items.size();
    }

}
