package components.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import components.map.Map;

/**
 * Tests for the kernel and Standard methods of Inventory1L.
 *
 * @author Tejas Naik
 */
public class Inventory1LTest {

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
     * A small quantity to add or remove.
     */
    private static final int FEW = 3;

    /**
     * A medium quantity to add or remove.
     */
    private static final int SOME = 5;

    /**
     * Number of distinct item types in the three-item tests.
     */
    private static final int THREE_TYPES = 3;

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

        inv.add("apples", APPLES);

        assertEquals(APPLES, inv.quantityOf("apples"));
        assertEquals(1, inv.size());
        assertEquals(create("apples", APPLES), inv);
    }

    /**
     * Tests adding to an item that already exists.
     */
    @Test
    public void testAddExistingItem() {
        Inventory inv = create("apples", SOME);

        inv.add("apples", FEW);

        assertEquals(SOME + FEW, inv.quantityOf("apples"));
        assertEquals(1, inv.size());
        assertEquals(create("apples", SOME + FEW), inv);
    }

    /**
     * Tests that item names are case-sensitive when adding.
     */
    @Test
    public void testAddCaseSensitive() {
        Inventory inv = create("Apple", SOME);

        inv.add("apple", FEW);

        assertEquals(SOME, inv.quantityOf("Apple"));
        assertEquals(FEW, inv.quantityOf("apple"));
        assertEquals(2, inv.size());
        assertEquals(create("Apple", SOME, "apple", FEW), inv);
    }

    /*
     * remove tests --------------------------------------------------------
     */

    /**
     * Tests removing part of an item's quantity.
     */
    @Test
    public void testRemovePartialQuantity() {
        Inventory inv = create("apples", APPLES);

        inv.remove("apples", FEW);

        assertEquals(APPLES - FEW, inv.quantityOf("apples"));
        assertEquals(1, inv.size());
        assertEquals(create("apples", APPLES - FEW), inv);
    }

    /**
     * Tests removing an item's entire quantity.
     */
    @Test
    public void testRemoveEntireQuantity() {
        Inventory inv = create("apples", APPLES, "oranges", ORANGES);

        inv.remove("apples", APPLES);

        assertEquals(0, inv.quantityOf("apples"));
        assertEquals(1, inv.size());
        assertEquals(create("oranges", ORANGES), inv);
    }

    /*
     * removeAny tests -----------------------------------------------------
     */

    /**
     * Tests removeAny when exactly one item type exists.
     */
    @Test
    public void testRemoveAnyOneItem() {
        Inventory inv = create("apples", APPLES);

        Map.Pair<String, Integer> removed = inv.removeAny();

        assertEquals("apples", removed.key());
        assertEquals(APPLES, removed.value().intValue());
        assertEquals(0, inv.size());
        assertEquals(create(), inv);
    }

    /**
     * Tests removeAny when multiple item types exist.
     */
    @Test
    public void testRemoveAnyMultipleItems() {
        Inventory inv = create("apples", APPLES, "oranges", ORANGES);

        Map.Pair<String, Integer> removed = inv.removeAny();

        if (removed.key().equals("apples")) {
            assertEquals(APPLES, removed.value().intValue());
            assertEquals(create("oranges", ORANGES), inv);
        } else {
            assertEquals("oranges", removed.key());
            assertEquals(ORANGES, removed.value().intValue());
            assertEquals(create("apples", APPLES), inv);
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
        Inventory inv = create("apples", APPLES, "oranges", ORANGES);

        assertEquals(APPLES, inv.quantityOf("apples"));
        assertEquals(ORANGES, inv.quantityOf("oranges"));
        assertEquals(create("apples", APPLES, "oranges", ORANGES), inv);
    }

    /**
     * Tests quantityOf for an item that is absent.
     */
    @Test
    public void testQuantityOfAbsentItem() {
        Inventory inv = create("apples", APPLES);

        assertEquals(0, inv.quantityOf("oranges"));
        assertEquals(create("apples", APPLES), inv);
    }

    /**
     * Tests quantityOf with different capitalization.
     */
    @Test
    public void testQuantityOfCaseSensitive() {
        Inventory inv = create("apples", APPLES);

        assertEquals(APPLES, inv.quantityOf("apples"));
        assertEquals(0, inv.quantityOf("Apples"));
        assertEquals(create("apples", APPLES), inv);
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
        Inventory inv = create("apples", APPLES, "oranges", ORANGES,
                "bananas", 2);

        assertEquals(THREE_TYPES, inv.size());

        inv.add("apples", SOME);

        assertEquals(THREE_TYPES, inv.size());
        assertEquals(create("apples", APPLES + SOME, "oranges", ORANGES,
                "bananas", 2), inv);
    }

    /**
     * Tests size after an item is completely removed.
     */
    @Test
    public void testSizeAfterRemoval() {
        Inventory inv = create("apples", APPLES, "oranges", ORANGES);

        inv.remove("apples", APPLES);

        assertEquals(1, inv.size());
        assertEquals(create("oranges", ORANGES), inv);
    }

    /*
     * newInstance tests ---------------------------------------------------
     */

    /**
     * Tests that newInstance creates a distinct empty inventory.
     */
    @Test
    public void testNewInstance() {
        Inventory inv = create("apples", APPLES, "oranges", ORANGES);

        Inventory copy = inv.newInstance();

        assertNotSame(inv, copy);
        assertEquals(inv.getClass(), copy.getClass());
        assertEquals(create(), copy);
        assertEquals(create("apples", APPLES, "oranges", ORANGES), inv);
    }

    /*
     * clear tests ---------------------------------------------------------
     */

    /**
     * Tests clearing a nonempty inventory.
     */
    @Test
    public void testClearNonEmpty() {
        Inventory inv = create("apples", APPLES, "oranges", ORANGES);

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
        Inventory source = create("oranges", APPLES, "bananas", 2);

        destination.transferFrom(source);

        assertEquals(create("oranges", APPLES, "bananas", 2), destination);
        assertEquals(create(), source);
    }

    /**
     * Tests transferFrom with an empty source.
     */
    @Test
    public void testTransferFromEmptySource() {
        Inventory destination = create("apples", APPLES);
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
        Inventory source = create("apples", APPLES, "oranges", ORANGES);

        destination.transferFrom(source);

        assertEquals(create("apples", APPLES, "oranges", ORANGES),
                destination);
        assertEquals(create(), source);
    }

    /**
     * Tests that transferred inventories are independent afterward.
     */
    @Test
    public void testTransferFromIndependentAfterTransfer() {
        Inventory destination = create("apples", APPLES);
        Inventory source = create("oranges", ORANGES);

        destination.transferFrom(source);

        source.add("bananas", FEW);

        assertEquals(create("oranges", ORANGES), destination);
        assertEquals(create("bananas", FEW), source);
    }

}
