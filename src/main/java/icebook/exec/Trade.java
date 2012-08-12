/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.exec;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.primitives.Longs;

import javax.annotation.Nonnegative;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents a trade.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
@Immutable
public final class Trade {

    private final long buyOrderId;

    private final long sellOrderId;

    private final long price;

    private final long quantity;

    private final int hashCode;

    /**
     * Default constructor.
     *
     * @param buyOrderId  id of the buy order.
     * @param sellOrderId id of the sell order.
     * @param price       execution price.
     * @param quantity    quantity executed.
     *
     * @throws IllegalArgumentException if any argument is {@code <= 0}.
     */
    public Trade(@Nonnegative final long buyOrderId,
                 @Nonnegative final long sellOrderId,
                 @Nonnegative final long price,
                 @Nonnegative final long quantity) {
        checkArgument(Longs.min(buyOrderId, sellOrderId, price, quantity) > 0, "all arguments have to be positive.");
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.price = price;
        this.quantity = quantity;
        this.hashCode = Objects.hashCode(this.buyOrderId, this.sellOrderId, this.price, this.quantity);
    }

    /**
     * Gets the buyOrderId.
     *
     * @return the buyOrderId.
     */
    public long getBuyOrderId() {
        return buyOrderId;
    }

    /**
     * Gets the sellOrderId.
     *
     * @return the sellOrderId.
     */
    public long getSellOrderId() {
        return sellOrderId;
    }

    /**
     * Gets the price.
     *
     * @return the price.
     */
    public long getPrice() {
        return price;
    }

    /**
     * Gets the quantity.
     *
     * @return the quantity.
     */
    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trade trade = (Trade) o;

        if (buyOrderId != trade.buyOrderId) return false;
        if (price != trade.price) return false;
        if (quantity != trade.quantity) return false;
        if (sellOrderId != trade.sellOrderId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        final ToStringHelper helper = Objects.toStringHelper(this);
        helper.add("buyOrderId", buyOrderId);
        helper.add("sellOrderId", sellOrderId);
        helper.add("price", price);
        helper.add("quantity", quantity);
        return helper.toString();
    }
}
