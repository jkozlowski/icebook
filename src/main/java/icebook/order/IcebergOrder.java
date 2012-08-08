/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.order;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Defines an iceberg order.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class IcebergOrder implements Order {

    public final Side side;

    public final long id;

    public final long price;

    public final long quantity;

    public final long peakSize;

    public IcebergOrder(final Side side, final long id, final long price, final long quantity, final long peakSize) {
        this.side = checkNotNull(side);
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.peakSize = peakSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IcebergOrder that = (IcebergOrder) o;

        if (id != that.id) return false;
        if (peakSize != that.peakSize) return false;
        if (price != that.price) return false;
        if (quantity != that.quantity) return false;
        if (side != that.side) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = side.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (price ^ (price >>> 32));
        result = 31 * result + (int) (quantity ^ (quantity >>> 32));
        result = 31 * result + (int) (peakSize ^ (peakSize >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final ToStringHelper toString = Objects.toStringHelper(this);
        toString.add("side", side);
        toString.add("id", id);
        toString.add("price", price);
        toString.add("quantity", quantity);
        toString.add("peakSize", peakSize);
        return toString.toString();
    }
}