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

    /*
     * Test quantities -----------------------------------------------------
     */

    /**
     * Quantity of apples used in most tests.
     */
    private static final int APPLES = 8;

    /**
     * Quantity of oranges used in most tests.
     */
    private static final int ORANGES = 4;

    /**
     * A small quantity used in several tests.
     */
    private static final int SMALL = 2;

    /**
     * A small quantity to add or remove.
     */
    private static final int FEW = 3;

    /**
     * A medium quantity to add or remove.
     */
    private static final int SOME = 5;

    /**
     * A larger quantity used in stock tests.
     */
    private static final int SIX = 6;

    /**
     * A quantity used to test different values.
     */
    private static final int SEVEN = 7;

    /**
     * A quantity used in restock tests.
     */
    private static final int TEN = 10;

    /**
     * Number of item types in two-item tests.
     */
    private static final int TWO_TYPES = 2;

    /**
     * Number of item types in three-item tests.
     */
    private static final int THREE_TYPES = 3;

    /**
     * Number of arguments representing one item and its quantity.
     */
    private static final int ARGUMENTS_PER_ITEM = 2;

    /*
     * Test thresholds -----------------------------------------------------
     */

    /**
     * Threshold used in standard low-stock tests.
     */
    private static final int LOW_THRESHOLD = 5;

    /**
     * A higher low-stock threshold.
     */
    private static final int HIGH_THRESHOLD = 6;

    /**
     * Threshold used when all items should be considered low.
     */
    private static final int ALL_LOW_THRESHOLD = 4;

    /*
     * Test helpers --------------------------------------------------------
     */

    /**
     * Creates an inventory from alternating item names and quantities.
     *
     * @param args
     *            alternating String item names and Integer quantities
     * @return the newly created inventory
     */
    private static Inventory create(Object... args) {
        assert args.length % ARGUMENTS_PER_ITEM == 0
                : "Violation of: even number of arguments";

        Inventory result = new Inventory1L();

        for (int i = 0; i < args.length; i += ARGUMENTS_PER_ITEM) {
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
        Inventory inv = create("apples", APPLES);

        assertTrue(inv.hasItem("apples"));
        assertFalse(inv.hasItem("oranges"));

        assertEquals(create("apples", APPLES), inv);
    }

    /**
     * Tests hasItem after an item's quantity reaches zero.
     */
    @Test
    public void testHasItemAfterRemoval() {
        Inventory inv = create("apples", APPLES);

        inv.remove("apples", APPLES);

        assertFalse(inv.hasItem("apples"));
        assertEquals(create(), inv);
    }

    /**
     * Tests that item names are case-sensitive.
     */
    @Test
    public void testHasItemCaseSensitive() {
        Inventory inv = create("apples", APPLES);

        assertTrue(inv.hasItem("apples"));
        assertFalse(inv.hasItem("Apples"));

        assertEquals(create("apples", APPLES), inv);
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
        Inventory inv = create("apples", APPLES);

        assertEquals(APPLES, inv.totalQuantity());
        assertEquals(create("apples", APPLES), inv);
    }

    /**
     * Tests totalQuantity with multiple item types.
     */
    @Test
    public void testTotalQuantityMultipleItems() {
        Inventory inv = create("apples", APPLES, "oranges", ORANGES,
                "bananas", SMALL);

        assertEquals(APPLES + ORANGES + SMALL, inv.totalQuantity());
        assertEquals(create("apples", APPLES, "oranges", ORANGES,
                "bananas", SMALL), inv);
    }

    /*
     * restock tests -------------------------------------------------------
     */

    /**
     * Tests restock with an empty source inventory.
     */
    @Test
    public void testRestockEmptySource() {
        Inventory stock = create("apples", SOME, "bananas", FEW);
        Inventory delivery = create();

        stock.restock(delivery);

        assertEquals(create("apples", SOME, "bananas", FEW), stock);
        assertEquals(create(), delivery);
    }

    /**
     * Tests restock when the receiving inventory is empty.
     */
    @Test
    public void testRestockEmptyThis() {
        Inventory stock = create();
        Inventory delivery = create("apples", SOME, "oranges", FEW);

        stock.restock(delivery);

        assertEquals(create("apples", SOME, "oranges", FEW), stock);
        assertEquals(create("apples", SOME, "oranges", FEW), delivery);
    }

    /**
     * Tests restock when the source contains only new item types.
     */
    @Test
    public void testRestockOnlyNewItems() {
        Inventory stock = create("apples", SOME);
        Inventory delivery = create("oranges", FEW, "bananas", SMALL);

        stock.restock(delivery);

        assertEquals(create("apples", SOME, "oranges", FEW,
                "bananas", SMALL), stock);
        assertEquals(create("oranges", FEW, "bananas", SMALL), delivery);
    }

    /**
     * Tests restock when all source item types already exist.
     */
    @Test
    public void testRestockOnlyOverlappingItems() {
        Inventory stock = create("apples", SOME, "oranges", ORANGES);
        Inventory delivery = create("apples", FEW, "oranges", SMALL);

        stock.restock(delivery);

        assertEquals(create("apples", SOME + FEW, "oranges",
                ORANGES + SMALL), stock);
        assertEquals(create("apples", FEW, "oranges", SMALL), delivery);
    }

    /**
     * Tests restock with both new and overlapping item types.
     */
    @Test
    public void testRestockMixedItems() {
        Inventory stock = create("apples", SOME, "bananas", FEW);
        Inventory delivery = create("apples", ORANGES, "oranges", SIX);

        stock.restock(delivery);

        assertEquals(create("apples", SOME + ORANGES, "bananas", FEW,
                "oranges", SIX), stock);
        assertEquals(create("apples", ORANGES, "oranges", SIX), delivery);
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

        assertEquals(0, inv.lowStock(LOW_THRESHOLD));
        assertEquals(create(), inv);
    }

    /**
     * Tests that an item exactly at the threshold is not counted.
     */
    @Test
    public void testLowStockAtThreshold() {
        Inventory inv = create("apples", LOW_THRESHOLD);

        assertEquals(0, inv.lowStock(LOW_THRESHOLD));
        assertEquals(create("apples", LOW_THRESHOLD), inv);
    }

    /**
     * Tests lowStock with a threshold of one.
     */
    @Test
    public void testLowStockThresholdOne() {
        Inventory inv = create("apples", 1, "oranges", TEN);

        assertEquals(0, inv.lowStock(1));
        assertEquals(create("apples", 1, "oranges", TEN), inv);
    }

    /**
     * Tests lowStock when every item is below the threshold.
     */
    @Test
    public void testLowStockAllItemsLow() {
        Inventory inv = create("apples", 1, "oranges", SMALL,
                "bananas", FEW);

        assertEquals(THREE_TYPES, inv.lowStock(ALL_LOW_THRESHOLD));
        assertEquals(create("apples", 1, "oranges", SMALL,
                "bananas", FEW), inv);
    }

    /**
     * Tests lowStock with a mixture of low and sufficiently stocked items.
     */
    @Test
    public void testLowStockMixedItems() {
        Inventory inv = create("apples", SMALL, "oranges", SOME,
                "bananas", APPLES, "grapes", FEW + 1);

        assertEquals(TWO_TYPES, inv.lowStock(LOW_THRESHOLD));
        assertEquals(THREE_TYPES, inv.lowStock(HIGH_THRESHOLD));

        assertEquals(create("apples", SMALL, "oranges", SOME,
                "bananas", APPLES, "grapes", FEW + 1), inv);
    }

    /*
     * equals tests --------------------------------------------------------
     */

    /**
     * Tests equals with the same object.
     */
    @Test
    public void testEqualsSameObject() {
        Inventory first = create("apples", APPLES);

        assertTrue(first.equals(first));
        assertEquals(create("apples", APPLES), first);
    }

    /**
     * Tests equals with the same contents in different insertion orders.
     */
    @Test
    public void testEqualsSameContentsDifferentOrder() {
        Inventory first = create("apples", APPLES, "oranges", ORANGES);
        Inventory second = create("oranges", ORANGES, "apples", APPLES);

        assertTrue(first.equals(second));
        assertTrue(second.equals(first));

        assertEquals(create("apples", APPLES, "oranges", ORANGES), first);
        assertEquals(create("oranges", ORANGES, "apples", APPLES), second);
    }

    /**
     * Tests equals with different quantities.
     */
    @Test
    public void testEqualsDifferentQuantities() {
        Inventory first = create("apples", APPLES, "oranges", ORANGES);
        Inventory second = create("apples", SEVEN, "oranges", ORANGES);

        assertFalse(first.equals(second));

        assertEquals(create("apples", APPLES, "oranges", ORANGES), first);
    }

    /**
     * Tests equals with different item names.
     */
    @Test
    public void testEqualsDifferentItems() {
        Inventory first = create("apples", APPLES);
        Inventory second = create("bananas", APPLES);

        assertFalse(first.equals(second));

        assertEquals(create("apples", APPLES), first);
    }

    /**
     * Tests equals with null.
     */
    @Test
    public void testEqualsNull() {
        Inventory first = create("apples", APPLES);

        assertFalse(first.equals(null));

        assertEquals(create("apples", APPLES), first);
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
        Inventory nonEmpty = create("apples", APPLES);

        assertFalse(empty.equals(nonEmpty));
        assertFalse(nonEmpty.equals(empty));

        assertEquals(create(), empty);
        assertEquals(create("apples", APPLES), nonEmpty);
    }

    /**
     * Tests equals when one inventory is a subset of another.
     */
    @Test
    public void testEqualsSubset() {
        Inventory smaller = create("apples", APPLES);
        Inventory larger = create("apples", APPLES, "oranges", ORANGES);

        assertFalse(smaller.equals(larger));
        assertFalse(larger.equals(smaller));

        assertEquals(create("apples", APPLES), smaller);
        assertEquals(create("apples", APPLES, "oranges", ORANGES), larger);
    }

    /**
     * Tests equals with an object that is not an Inventory.
     */
    @Test
    public void testEqualsDifferentObjectType() {
        Inventory inv = create("apples", APPLES);

        Object other = "apples";
        assertFalse(inv.equals(other));

        assertEquals(create("apples", APPLES), inv);
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
        Inventory inv = create("apples", APPLES, "oranges", ORANGES);

        int hash = inv.hashCode();

        assertEquals(hash, inv.hashCode());
        assertEquals(create("apples", APPLES, "oranges", ORANGES), inv);
    }

    /**
     * Tests that equal inventories have the same hashCode.
     */
    @Test
    public void testHashCodeEqualInventories() {
        Inventory first = create("apples", APPLES, "oranges", ORANGES);
        Inventory second = create("oranges", ORANGES, "apples", APPLES);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());

        assertEquals(create("apples", APPLES, "oranges", ORANGES), first);
        assertEquals(create("oranges", ORANGES, "apples", APPLES), second);
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
        Inventory inv = create("apples", APPLES);

        assertEquals("{(apples,8)}", inv.toString());
        assertEquals(create("apples", APPLES), inv);
    }

    /**
     * Tests toString with multiple item types.
     */
    @Test
    public void testToStringMultipleItems() {
        Inventory inv = create("apples", APPLES, "oranges", ORANGES);

        String result = inv.toString();

        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
        assertTrue(result.contains("(apples,8)"));
        assertTrue(result.contains("(oranges,4)"));

        assertEquals(create("apples", APPLES, "oranges", ORANGES), inv);
    }

}
