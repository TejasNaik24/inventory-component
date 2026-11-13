package components.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the secondary methods of the Inventory component.
 *
 * @author Tejas Naik
 */
public class InventoryTest {

    /**
     * Creates an inventory from alternating item names and quantities.
     *
     * @param args
     *            alternating String item names and Integer quantities
     * @return the newly created inventory
     */
    private static Inventory create(Object... args) {
        assert args.length % 2 == 0 : "Violation of: even number of arguments";

        Inventory result = new Inventory1L();

        for (int i = 0; i < args.length; i += 2) {
            String name = (String) args[i];
            int amount = (Integer) args[i + 1];
            result.add(name, amount);
        }

        return result;
    }

    /*
     * hasItem tests -------------------------------------------------------
     */

    /**
     * Tests hasItem on an empty inventory.
     */
    @Test
    public void testHasItemEmpty() {
        Inventory inv = create();

        assertFalse(inv.hasItem("apples"));
        assertEquals(create(), inv);
    }

    /**
     * Tests hasItem with existing and missing items.
     */
    @Test
    public void testHasItemPresentAndAbsent() {
        Inventory inv = create("apples", 8);

        assertTrue(inv.hasItem("apples"));
        assertFalse(inv.hasItem("oranges"));

        assertEquals(create("apples", 8), inv);
    }

    /**
     * Tests hasItem after an item's quantity reaches zero.
     */
    @Test
    public void testHasItemAfterRemoval() {
        Inventory inv = create("apples", 8);

        inv.remove("apples", 8);

        assertFalse(inv.hasItem("apples"));
        assertEquals(create(), inv);
    }

    /**
     * Tests that item names are case-sensitive.
     */
    @Test
    public void testHasItemCaseSensitive() {
        Inventory inv = create("apples", 8);

        assertTrue(inv.hasItem("apples"));
        assertFalse(inv.hasItem("Apples"));

        assertEquals(create("apples", 8), inv);
    }

    /*
     * totalQuantity tests -------------------------------------------------
     */

    /**
     * Tests totalQuantity on an empty inventory.
     */
    @Test
    public void testTotalQuantityEmpty() {
        Inventory inv = create();

        assertEquals(0, inv.totalQuantity());
        assertEquals(create(), inv);
    }

    /**
     * Tests totalQuantity with exactly one item type.
     */
    @Test
    public void testTotalQuantityOneItem() {
        Inventory inv = create("apples", 8);

        assertEquals(8, inv.totalQuantity());
        assertEquals(create("apples", 8), inv);
    }

    /**
     * Tests totalQuantity with multiple item types.
     */
    @Test
    public void testTotalQuantityMultipleItems() {
        Inventory inv = create("apples", 8, "oranges", 4, "bananas", 2);

        assertEquals(14, inv.totalQuantity());
        assertEquals(create("apples", 8, "oranges", 4, "bananas", 2), inv);
    }

    /*
     * restock tests -------------------------------------------------------
     */

    /**
     * Tests restock with an empty source inventory.
     */
    @Test
    public void testRestockEmptySource() {
        Inventory stock = create("apples", 5, "bananas", 3);
        Inventory delivery = create();

        stock.restock(delivery);

        assertEquals(create("apples", 5, "bananas", 3), stock);
        assertEquals(create(), delivery);
    }

    /**
     * Tests restock when the receiving inventory is empty.
     */
    @Test
    public void testRestockEmptyThis() {
        Inventory stock = create();
        Inventory delivery = create("apples", 5, "oranges", 3);

        stock.restock(delivery);

        assertEquals(create("apples", 5, "oranges", 3), stock);
        assertEquals(create("apples", 5, "oranges", 3), delivery);
    }

    /**
     * Tests restock when the source contains only new item types.
     */
    @Test
    public void testRestockOnlyNewItems() {
        Inventory stock = create("apples", 5);
        Inventory delivery = create("oranges", 3, "bananas", 2);

        stock.restock(delivery);

        assertEquals(create("apples", 5, "oranges", 3, "bananas", 2), stock);
        assertEquals(create("oranges", 3, "bananas", 2), delivery);
    }

    /**
     * Tests restock when all source item types already exist.
     */
    @Test
    public void testRestockOnlyOverlappingItems() {
        Inventory stock = create("apples", 5, "oranges", 4);
        Inventory delivery = create("apples", 3, "oranges", 2);

        stock.restock(delivery);

        assertEquals(create("apples", 8, "oranges", 6), stock);
        assertEquals(create("apples", 3, "oranges", 2), delivery);
    }

    /**
     * Tests restock with both new and overlapping item types.
     */
    @Test
    public void testRestockMixedItems() {
        Inventory stock = create("apples", 5, "bananas", 3);
        Inventory delivery = create("apples", 4, "oranges", 6);

        stock.restock(delivery);

        assertEquals(create("apples", 9, "bananas", 3, "oranges", 6), stock);
        assertEquals(create("apples", 4, "oranges", 6), delivery);
    }

    /*
     * lowStock tests ------------------------------------------------------
     */

    /**
     * Tests lowStock on an empty inventory.
     */
    @Test
    public void testLowStockEmpty() {
        Inventory inv = create();

        assertEquals(0, inv.lowStock(5));
        assertEquals(create(), inv);
    }

    /**
     * Tests that an item exactly at the threshold is not counted.
     */
    @Test
    public void testLowStockAtThreshold() {
        Inventory inv = create("apples", 5);

        assertEquals(0, inv.lowStock(5));
        assertEquals(create("apples", 5), inv);
    }

    /**
     * Tests lowStock with a threshold of one.
     */
    @Test
    public void testLowStockThresholdOne() {
        Inventory inv = create("apples", 1, "oranges", 10);

        assertEquals(0, inv.lowStock(1));
        assertEquals(create("apples", 1, "oranges", 10), inv);
    }

    /**
     * Tests lowStock when every item is below the threshold.
     */
    @Test
    public void testLowStockAllItemsLow() {
        Inventory inv = create("apples", 1, "oranges", 2, "bananas", 3);

        assertEquals(3, inv.lowStock(4));
        assertEquals(create("apples", 1, "oranges", 2, "bananas", 3), inv);
    }

    /**
     * Tests lowStock with a mixture of low and sufficiently stocked items.
     */
    @Test
    public void testLowStockMixedItems() {
        Inventory inv = create("apples", 2, "oranges", 5, "bananas", 8,
                "grapes", 4);

        assertEquals(2, inv.lowStock(5));
        assertEquals(3, inv.lowStock(6));

        assertEquals(create("apples", 2, "oranges", 5, "bananas", 8,
                "grapes", 4), inv);
    }

    /*
     * equals tests --------------------------------------------------------
     */

    /**
     * Tests equals with the same object.
     */
    @Test
    public void testEqualsSameObject() {
        Inventory first = create("apples", 8);

        assertTrue(first.equals(first));
        assertEquals(create("apples", 8), first);
    }

    /**
     * Tests equals with the same contents in different insertion orders.
     */
    @Test
    public void testEqualsSameContentsDifferentOrder() {
        Inventory first = create("apples", 8, "oranges", 4);
        Inventory second = create("oranges", 4, "apples", 8);

        assertTrue(first.equals(second));
        assertTrue(second.equals(first));

        assertEquals(create("apples", 8, "oranges", 4), first);
        assertEquals(create("oranges", 4, "apples", 8), second);
    }

    /**
     * Tests equals with different quantities.
     */
    @Test
    public void testEqualsDifferentQuantities() {
        Inventory first = create("apples", 8, "oranges", 4);
        Inventory second = create("apples", 7, "oranges", 4);

        assertFalse(first.equals(second));

        assertEquals(create("apples", 8, "oranges", 4), first);
    }

    /**
     * Tests equals with different item names.
     */
    @Test
    public void testEqualsDifferentItems() {
        Inventory first = create("apples", 8);
        Inventory second = create("bananas", 8);

        assertFalse(first.equals(second));

        assertEquals(create("apples", 8), first);
    }

    /**
     * Tests equals with null.
     */
    @Test
    public void testEqualsNull() {
        Inventory first = create("apples", 8);

        assertFalse(first.equals(null));

        assertEquals(create("apples", 8), first);
    }

    /**
     * Tests equals with two empty inventories.
     */
    @Test
    public void testEqualsEmptyInventories() {
        Inventory first = create();
        Inventory second = create();

        assertTrue(first.equals(second));
        assertEquals(create(), first);
    }

    /**
     * Tests equals with an empty and a nonempty inventory.
     */
    @Test
    public void testEqualsEmptyAndNonEmpty() {
        Inventory empty = create();
        Inventory nonEmpty = create("apples", 8);

        assertFalse(empty.equals(nonEmpty));
        assertFalse(nonEmpty.equals(empty));

        assertEquals(create(), empty);
        assertEquals(create("apples", 8), nonEmpty);
    }

    /**
     * Tests equals when one inventory is a subset of another.
     */
    @Test
    public void testEqualsSubset() {
        Inventory smaller = create("apples", 8);
        Inventory larger = create("apples", 8, "oranges", 4);

        assertFalse(smaller.equals(larger));
        assertFalse(larger.equals(smaller));

        assertEquals(create("apples", 8), smaller);
        assertEquals(create("apples", 8, "oranges", 4), larger);
    }

    /**
     * Tests equals with an object that is not an Inventory.
     */
    @Test
    public void testEqualsDifferentObjectType() {
        Inventory inv = create("apples", 8);

        assertFalse(inv.equals("apples"));

        assertEquals(create("apples", 8), inv);
    }

    /*
     * hashCode tests ------------------------------------------------------
     */

    /**
     * Tests hashCode on an empty inventory.
     */
    @Test
    public void testHashCodeEmpty() {
        Inventory inv = create();

        int hash = inv.hashCode();

        assertEquals(hash, inv.hashCode());
        assertEquals(create(), inv);
    }

    /**
     * Tests that hashCode is stable for the same object.
     */
    @Test
    public void testHashCodeStable() {
        Inventory inv = create("apples", 8, "oranges", 4);

        int hash = inv.hashCode();

        assertEquals(hash, inv.hashCode());
        assertEquals(create("apples", 8, "oranges", 4), inv);
    }

    /**
     * Tests that equal inventories have the same hashCode.
     */
    @Test
    public void testHashCodeEqualInventories() {
        Inventory first = create("apples", 8, "oranges", 4);
        Inventory second = create("oranges", 4, "apples", 8);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());

        assertEquals(create("apples", 8, "oranges", 4), first);
        assertEquals(create("oranges", 4, "apples", 8), second);
    }

    /*
     * toString tests ------------------------------------------------------
     */

    /**
     * Tests toString on an empty inventory.
     */
    @Test
    public void testToStringEmpty() {
        Inventory inv = create();

        assertEquals("{}", inv.toString());
        assertEquals(create(), inv);
    }

    /**
     * Tests toString with exactly one item type.
     */
    @Test
    public void testToStringOneItem() {
        Inventory inv = create("apples", 8);

        assertEquals("{(apples,8)}", inv.toString());
        assertEquals(create("apples", 8), inv);
    }

    /**
     * Tests toString with multiple item types.
     */
    @Test
    public void testToStringMultipleItems() {
        Inventory inv = create("apples", 8, "oranges", 4);

        String result = inv.toString();

        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
        assertTrue(result.contains("(apples,8)"));
        assertTrue(result.contains("(oranges,4)"));

        assertEquals(create("apples", 8, "oranges", 4), inv);
    }

}
