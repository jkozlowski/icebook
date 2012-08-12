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
     * @param id       id of the order.
     * @param side     side of the order.
     * @param price    price of the order.
     * @param volume volume of the order.
     * @param peakSize peakSize of the order.
     *
     * @return a new iceberg order or {@link Optional#absent()} if {@code side} is null or any {@code long} argument
     *         is {@code <=0}.
     */
    public static Optional<Order>
    newIcebergOrder(@Nonnegative final long id,
                    @Nonnull final Side side,
                    @Nonnegative final long price,
                    @Nonnegative final long volume,
                    @Nonnegative final long peakSize) {

        if (null == side || Longs.min(id, price, volume, peakSize) <= 0 || peakSize > volume) {
            return Optional.absent();
        }

        return Optional.<Order>of(new IcebergOrder(id, side, price, volume, peakSize));
    }

    /**
     * Creates a limit order.
     *
     * @param id       id of the order.
     * @param side     side of the order.
     * @param price    price of the order.
     * @param volume volume of the order.
     *
     * @return a new limit order or {@link Optional#absent()} if {@code side} is null or any {@code long} argument
     *         is {@code <=0}.
     */
    public static Optional<Order>
    newLimitOrder(@Nonnegative final long id,
                  @Nonnull final Side side,
                  @Nonnegative final long price,
                  @Nonnegative final long volume) {

        if (null == side || Longs.min(id, price, volume) <= 0) {
            return Optional.absent();
        }

        return Optional.<Order>of(new LimitOrder(id, side, price, volume));
    }
}
