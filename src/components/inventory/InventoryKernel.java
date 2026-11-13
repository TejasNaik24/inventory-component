package components.inventory;

import java.util.Map;

import components.standard.Standard;

/**
 * Inventory kernel component with primary methods.
 *
 * @mathmodel type InventoryKernel is modeled by a finite set of pairs
 *            (name: string of character, quantity: integer) with the
 *            function property, where every name is nonempty and
 *            every quantity is positive
 * @initially <pre>
 * ():
 *  ensures
 *   this = {}
 * </pre>
 */
public interface InventoryKernel extends Standard<Inventory> {

    /**
     * Adds the given quantity of an item to this inventory.
     *
     * @param name
     *            the name of the item
     * @param amount
     *            the quantity to add
     * @updates this
     * @requires |name| > 0 and amount > 0
     * @ensures <pre>
     * quantityOf(name) = #quantityOf(name) + amount  and
     * [every other item's quantity in this is the same as in #this]
     * </pre>
     */
    void add(String name, int amount);

    /**
     * Removes the given quantity of an item from this inventory.
     *
     * @param name
     *            the name of the item
     * @param amount
     *            the quantity to remove
     * @updates this
     * @requires 0 < amount <= quantityOf(name)
     * @ensures <pre>
     * quantityOf(name) = #quantityOf(name) - amount  and
     * [if quantityOf(name) = 0, then name is no longer in this]  and
     * [every other item's quantity in this is the same as in #this]
     * </pre>
     */
    void remove(String name, int amount);

    /**
     * Removes and returns an arbitrary item and its quantity.
     *
     * @return the name and quantity of the item removed
     * @updates this
     * @requires |this| > 0
     * @ensures removeAny is in #this and
     *          this = #this \ {removeAny}
     */
    Map.Pair<String, Integer> removeAny();

    /**
     * Reports the quantity of the specified item.
     *
     * @param name
     *            the name of the item
     * @return the quantity of the item, or zero if it is absent
     * @ensures if there exists q: integer such that (name, q) is in this,
     *          then quantityOf = q, else quantityOf = 0
     */
    int quantityOf(String name);

    /**
     * Reports the number of distinct item types in this inventory.
     *
     * @return the number of distinct item types
     * @ensures size = |this|
     */
    int size();

}