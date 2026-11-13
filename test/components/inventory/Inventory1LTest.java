package components.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Map;

import org.junit.Test;

/**
 * Tests for the kernel and Standard methods of Inventory1L.
 *
 * @author Tejas Naik
 */
public class Inventory1LTest {

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
     * Constructor tests ---------------------------------------------------
     */

    /**
     * Tests the no-argument constructor.
     */
    @Test
    public void testConstructor() {
        Inventory inv = new Inventory1L();

        assertEquals(0, inv.size());
        assertEquals(create(), inv);
    }

    /*
     * add tests -----------------------------------------------------------
     */

    /**
     * Tests adding a new item to an empty inventory.
     */
    @Test
    public void testAddNewItem() {
        Inventory inv = create();

        inv.add("apples", 8);

        assertEquals(8, inv.quantityOf("apples"));
        assertEquals(1, inv.size());
        assertEquals(create("apples", 8), inv);
    }

    /**
     * Tests adding to an item that already exists.
     */
    @Test
    public void testAddExistingItem() {
        Inventory inv = create("apples", 5);

        inv.add("apples", 3);

        assertEquals(8, inv.quantityOf("apples"));
        assertEquals(1, inv.size());
        assertEquals(create("apples", 8), inv);
    }

    /**
     * Tests that item names are case-sensitive when adding.
     */
    @Test
    public void testAddCaseSensitive() {
        Inventory inv = create("Apple", 5);

        inv.add("apple", 3);

        assertEquals(5, inv.quantityOf("Apple"));
        assertEquals(3, inv.quantityOf("apple"));
        assertEquals(2, inv.size());
        assertEquals(create("Apple", 5, "apple", 3), inv);
    }

    /*
     * remove tests --------------------------------------------------------
     */

    /**
     * Tests removing part of an item's quantity.
     */
    @Test
    public void testRemovePartialQuantity() {
        Inventory inv = create("apples", 8);

        inv.remove("apples", 3);

        assertEquals(5, inv.quantityOf("apples"));
        assertEquals(1, inv.size());
        assertEquals(create("apples", 5), inv);
    }

    /**
     * Tests removing an item's entire quantity.
     */
    @Test
    public void testRemoveEntireQuantity() {
        Inventory inv = create("apples", 8, "oranges", 4);

        inv.remove("apples", 8);

        assertEquals(0, inv.quantityOf("apples"));
        assertEquals(1, inv.size());
        assertEquals(create("oranges", 4), inv);
    }

    /*
     * removeAny tests -----------------------------------------------------
     */

    /**
     * Tests removeAny when exactly one item type exists.
     */
    @Test
    public void testRemoveAnyOneItem() {
        Inventory inv = create("apples", 8);

        Map.Pair<String, Integer> removed = inv.removeAny();

        assertEquals("apples", removed.key());
        assertEquals(8, removed.value().intValue());
        assertEquals(0, inv.size());
        assertEquals(create(), inv);
    }

    /**
     * Tests removeAny when multiple item types exist.
     */
    @Test
    public void testRemoveAnyMultipleItems() {
        Inventory inv = create("apples", 8, "oranges", 4);

        Map.Pair<String, Integer> removed = inv.removeAny();

        if (removed.key().equals("apples")) {
            assertEquals(8, removed.value().intValue());
            assertEquals(create("oranges", 4), inv);
        } else {
            assertEquals("oranges", removed.key());
            assertEquals(4, removed.value().intValue());
            assertEquals(create("apples", 8), inv);
        }

        assertEquals(1, inv.size());
    }

    /*
     * quantityOf tests ----------------------------------------------------
     */

    /**
     * Tests quantityOf for an existing item.
     */
    @Test
    public void testQuantityOfPresentItem() {
        Inventory inv = create("apples", 8, "oranges", 4);

        assertEquals(8, inv.quantityOf("apples"));
        assertEquals(4, inv.quantityOf("oranges"));
        assertEquals(create("apples", 8, "oranges", 4), inv);
    }

    /**
     * Tests quantityOf for an item that is absent.
     */
    @Test
    public void testQuantityOfAbsentItem() {
        Inventory inv = create("apples", 8);

        assertEquals(0, inv.quantityOf("oranges"));
        assertEquals(create("apples", 8), inv);
    }

    /**
     * Tests quantityOf with different capitalization.
     */
    @Test
    public void testQuantityOfCaseSensitive() {
        Inventory inv = create("apples", 8);

        assertEquals(8, inv.quantityOf("apples"));
        assertEquals(0, inv.quantityOf("Apples"));
        assertEquals(create("apples", 8), inv);
    }

    /*
     * size tests ----------------------------------------------------------
     */

    /**
     * Tests size on an empty inventory.
     */
    @Test
    public void testSizeEmpty() {
        Inventory inv = create();

        assertEquals(0, inv.size());
        assertEquals(create(), inv);
    }

    /**
     * Tests size with multiple distinct item types.
     */
    @Test
    public void testSizeDistinctItems() {
        Inventory inv = create("apples", 8, "oranges", 4, "bananas", 2);

        assertEquals(3, inv.size());

        inv.add("apples", 5);

        assertEquals(3, inv.size());
        assertEquals(create("apples", 13, "oranges", 4, "bananas", 2),
                inv);
    }

    /**
     * Tests size after an item is completely removed.
     */
    @Test
    public void testSizeAfterRemoval() {
        Inventory inv = create("apples", 8, "oranges", 4);

        inv.remove("apples", 8);

        assertEquals(1, inv.size());
        assertEquals(create("oranges", 4), inv);
    }

    /*
     * newInstance tests ---------------------------------------------------
     */

    /**
     * Tests that newInstance creates a distinct empty inventory.
     */
    @Test
    public void testNewInstance() {
        Inventory inv = create("apples", 8, "oranges", 4);

        Inventory copy = inv.newInstance();

        assertNotSame(inv, copy);
        assertEquals(inv.getClass(), copy.getClass());
        assertEquals(create(), copy);
        assertEquals(create("apples", 8, "oranges", 4), inv);
    }

    /*
     * clear tests ---------------------------------------------------------
     */

    /**
     * Tests clearing a nonempty inventory.
     */
    @Test
    public void testClearNonEmpty() {
        Inventory inv = create("apples", 8, "oranges", 4);

        inv.clear();

        assertEquals(0, inv.size());
        assertEquals(0, inv.totalQuantity());
        assertEquals(create(), inv);
    }

    /**
     * Tests clearing an already empty inventory.
     */
    @Test
    public void testClearEmpty() {
        Inventory inv = create();

        inv.clear();

        assertEquals(0, inv.size());
        assertEquals(create(), inv);
    }

    /*
     * transferFrom tests --------------------------------------------------
     */

    /**
     * Tests transferring the contents of one inventory into another.
     */
    @Test
    public void testTransferFrom() {
        Inventory destination = create("apples", 2);
        Inventory source = create("oranges", 8, "bananas", 4);

        destination.transferFrom(source);

        assertEquals(create("oranges", 8, "bananas", 4), destination);
        assertEquals(create(), source);
    }

    /**
     * Tests transferFrom with an empty source.
     */
    @Test
    public void testTransferFromEmptySource() {
        Inventory destination = create("apples", 8);
        Inventory source = create();

        destination.transferFrom(source);

        assertEquals(create(), destination);
        assertEquals(create(), source);
    }

    /**
     * Tests transferFrom with an empty destination.
     */
    @Test
    public void testTransferFromEmptyDestination() {
        Inventory destination = create();
        Inventory source = create("apples", 8, "oranges", 4);

        destination.transferFrom(source);

        assertEquals(create("apples", 8, "oranges", 4), destination);
        assertEquals(create(), source);
    }

    /**
     * Tests that transferred inventories are independent afterward.
     */
    @Test
    public void testTransferFromIndependentAfterTransfer() {
        Inventory destination = create("apples", 8);
        Inventory source = create("oranges", 4);

        destination.transferFrom(source);

        source.add("bananas", 3);

        assertEquals(create("oranges", 4), destination);
        assertEquals(create("bananas", 3), source);
    }

}