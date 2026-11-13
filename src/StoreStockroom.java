import components.inventory.Inventory;
import components.inventory.Inventory1L;

/**
 * A client program demonstrating how a store manages its inventory
 * throughout a business day.
 *
 * @author Tejas Naik
 */
public final class StoreStockroom {

    /**
     * Private constructor to prevent instantiation.
     */
    private StoreStockroom() {
    }

    /**
     * Sells the requested quantity of an item if enough is in stock.
     *
     * @param stock
     *            the store inventory
     * @param item
     *            the item to sell
     * @param amount
     *            the quantity to sell
     * @updates stock
     * @requires amount > 0
     * @ensures sells the items if quantityOf(item) >= amount,
     *          otherwise stock remains unchanged
     */
    private static void sell(Inventory stock, String item, int amount) {
        if (stock.quantityOf(item) >= amount) {
            stock.remove(item, amount);
            System.out.println("Sold " + amount + " " + item + ".");
        } else {
            System.out.println("Not enough " + item + " to sell " + amount
                    + ".");
        }
    }

    /**
     * Runs a demonstration of store inventory operations.
     *
     * @param args
     *            command-line arguments (not used)
     */
    public static void main(String[] args) {

        /*
         * Open the store and receive the morning shipment.
         */
        Inventory stock = new Inventory1L();

        System.out.println("=== Store Stockroom ===");
        System.out.println("Store opened.");

        stock.add("milk", 10);
        stock.add("eggs", 12);
        stock.add("bread", 4);
        stock.add("apples", 20);

        System.out.println("\nMorning shipment received:");
        System.out.println(stock);

        /*
         * Process customer purchases.
         */
        System.out.println("\n=== Customer Purchases ===");

        sell(stock, "milk", 3);
        sell(stock, "apples", 5);
        sell(stock, "eggs", 12);
        sell(stock, "bread", 6);

        System.out.println("Still have eggs: " + stock.hasItem("eggs"));

        System.out.println("\nStock after customer purchases:");
        System.out.println(stock);

        /*
         * Check which items need reordering before delivery.
         */
        System.out.println("\n=== Reorder Check ===");
        System.out.println("Items to reorder (fewer than 5): "
                + stock.lowStock(5));

        /*
         * Receive and restock a supplier delivery.
         */
        System.out.println("\n=== Supplier Delivery ===");

        Inventory delivery = new Inventory1L();
        delivery.add("milk", 6);
        delivery.add("bread", 8);
        delivery.add("bananas", 15);

        System.out.println("Incoming delivery:");
        System.out.println(delivery);

        stock.restock(delivery);

        System.out.println("Store inventory after restocking:");
        System.out.println(stock);

        System.out.println("Supplier delivery after restocking:");
        System.out.println(delivery);

        /*
         * Print the end-of-day inventory report.
         */
        System.out.println("\n=== End-of-Day Report ===");
        System.out.println("Distinct items: " + stock.size());
        System.out.println("Total units: " + stock.totalQuantity());
        System.out.println("Items to reorder (fewer than 5): "
                + stock.lowStock(5));

        System.out.println("\nStore closed.");
    }

}