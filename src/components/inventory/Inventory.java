package components.inventory;

/**
 * {@code InventoryKernel} enhanced with secondary methods.
 */
public interface Inventory extends InventoryKernel {

    /**
     * Reports whether the specified item is in this inventory.
     *
     * @param name
     *            the name of the item
     * @return true if the item is in this inventory, false otherwise
     * @ensures hasItem = (quantityOf(name) > 0)
     */
    boolean hasItem(String name);

    /**
     * Reports the total quantity of all items in this inventory.
     *
     * @return the sum of the quantities of all items
     * @ensures totalQuantity is the sum of the quantities of all pairs in this
     */
    int totalQuantity();

    /**
     * Adds the contents of another inventory to this inventory.
     *
     * @param source
     *            the inventory whose contents are added
     * @updates this
     * @requires source /= this
     * @ensures <pre>
     * [for every item name,
     *  quantityOf(name) = #quantityOf(name) + source.quantityOf(name)]
     * </pre>
     */
    void restock(Inventory source);

    /**
     * Counts the item types whose quantities are below the given threshold.
     *
     * @param threshold
     *            the quantity threshold
     * @return the number of item types with quantities below threshold
     * @requires threshold > 0
     * @ensures lowStock is the number of distinct item types whose quantity
     *          in this inventory is strictly less than threshold
     */
    int lowStock(int threshold);

}
