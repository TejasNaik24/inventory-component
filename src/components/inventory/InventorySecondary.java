package components.inventory;

import components.map.Map;

/**
 * Layered implementations of secondary methods for Inventory.
 *
 * @author Tejas Naik
 */
public abstract class InventorySecondary implements Inventory {

    /*
     * Secondary methods ---------------------------------------------------
     */

    @Override
    public final boolean hasItem(String name) {
        assert name != null : "Violation of: name is not null";

        return this.quantityOf(name) > 0;
    }

    @Override
    public final int totalQuantity() {
        Inventory temp = this.newInstance();
        int total = 0;

        while (this.size() > 0) {
            Map.Pair<String, Integer> item = this.removeAny();

            total += item.value();
            temp.add(item.key(), item.value());
        }

        this.transferFrom(temp);

        return total;
    }

    @Override
    public final void restock(Inventory source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";

        Inventory temp = source.newInstance();

        while (source.size() > 0) {
            Map.Pair<String, Integer> item = source.removeAny();

            this.add(item.key(), item.value());
            temp.add(item.key(), item.value());
        }

        source.transferFrom(temp);
    }

    @Override
    public final int lowStock(int threshold) {
        assert threshold > 0 : "Violation of: threshold > 0";

        Inventory temp = this.newInstance();
        int count = 0;

        while (this.size() > 0) {
            Map.Pair<String, Integer> item = this.removeAny();

            if (item.value() < threshold) {
                count++;
            }

            temp.add(item.key(), item.value());
        }

        this.transferFrom(temp);

        return count;
    }

    /*
     * Standard methods ----------------------------------------------------
     */

    @Override
    public final boolean equals(Object obj) {
        boolean equal = true;

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Inventory)) {
            return false;
        }

        Inventory other = (Inventory) obj;

        if (this.size() != other.size()) {
            return false;
        }

        Inventory temp = this.newInstance();

        while (this.size() > 0) {
            Map.Pair<String, Integer> item = this.removeAny();

            if (other.quantityOf(item.key()) != item.value()) {
                equal = false;
            }

            temp.add(item.key(), item.value());
        }

        this.transferFrom(temp);

        return equal;
    }

    @Override
    public final int hashCode() {
        Inventory temp = this.newInstance();
        int hash = 0;

        while (this.size() > 0) {
            Map.Pair<String, Integer> item = this.removeAny();

            hash += item.key().hashCode() ^ item.value().hashCode();

            temp.add(item.key(), item.value());
        }

        this.transferFrom(temp);

        return hash;
    }

    @Override
    public final String toString() {
        Inventory temp = this.newInstance();
        StringBuilder result = new StringBuilder("{");
        boolean first = true;

        while (this.size() > 0) {
            Map.Pair<String, Integer> item = this.removeAny();

            if (!first) {
                result.append(",");
            }

            result.append("(");
            result.append(item.key());
            result.append(",");
            result.append(item.value());
            result.append(")");

            first = false;
            temp.add(item.key(), item.value());
        }

        this.transferFrom(temp);

        result.append("}");
        return result.toString();
    }

}
