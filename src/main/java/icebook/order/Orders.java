/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.order;

import com.google.common.base.Optional;
import com.google.common.primitives.Longs;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * Factory for {@link Order}s.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public final class Orders {

    /**
     * Factory class.
     */
    private Orders() {
    }

    /**
     * Creates an iceberg order.
     *
     * @param side     side of the order.
     * @param id       id of the order.
     * @param price    price of the order.
     * @param quantity quantity of the order.
     * @param peakSize peakSize of the order.
     *
     * @return a new iceberg order or {@link Optional#absent()} if {@code side} is null or any {@code long} argument
     *         is {@code <=0}.
     */
    public static Optional<Order>
    newIcebergOrder(@Nonnull final Side side,
                    @Nonnegative final long id,
                    @Nonnegative final long price,
                    @Nonnegative final long quantity,
                    @Nonnegative final long peakSize) {

        if (null == side || Longs.min(id, price, quantity, peakSize) <= 0) {
            return Optional.absent();
        }

        return Optional.<Order>of(new IcebergOrder(side, id, price, quantity, peakSize));
    }

    /**
     * Creates a limit order.
     *
     * @param side     side of the order.
     * @param id       id of the order.
     * @param price    price of the order.
     * @param quantity quantity of the order.
     *
     * @return a new limit order or {@link Optional#absent()} if {@code side} is null or any {@code long} argument
     *         is {@code <=0}.
     */
    public static Optional<Order>
    newLimitOrder(@Nonnull final Side side,
                  @Nonnegative final long id,
                  @Nonnegative final long price,
                  @Nonnegative final long quantity) {

        if (null == side || Longs.min(id, price, quantity) <= 0) {
            return Optional.absent();
        }

        return Optional.<Order>of(new LimitOrder(side, id, price, quantity));
    }
}
