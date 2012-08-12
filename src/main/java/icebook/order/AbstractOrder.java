/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.order;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.primitives.Longs;
import icebook.exec.Trade;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Defines properties of an order with a {@link Side}, price and volume.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
abstract class AbstractOrder implements Order {

    private final Side side;

    private final long id;

    private final long price;

    private long volume;

    /**
     * Default constructor.
     *
     * @param side   {@link Side} of this order.
     * @param id     id of this order.
     * @param price  price of this order.
     * @param volume current volume of this order.
     *
     * @throws NullPointerException     if {@code side} is null.
     * @throws IllegalArgumentException if any {@code long} argument is {@code <= 0}.
     */
    public AbstractOrder(@Nonnegative final long id,
                         @Nonnull final Side side,
                         @Nonnegative final long price,
                         @Nonnegative final long volume) {

        checkNotNull(side);
        checkArgument(Longs.min(id, price, volume) > 0);
        this.side = side;
        this.id = id;
        this.price = price;
        this.volume = volume;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public long getPrice() {
        return price;
    }

    /**
     * {@inheritDoc}
     */
    public long getVolume() {
        return volume;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Side getSide() {
        return side;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFilled() {
        return 0 == volume;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@Nonnull final Trade trade) {
        checkNotNull(trade);
        if (side.isBuy()) {
            checkArgument(trade.getBuyOrderId() == id);
        }
        else {
            checkArgument(trade.getSellOrderId() == id);
        }

        checkArgument(volume >= trade.getQuantity());

        volume -= trade.getQuantity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AbstractOrder that = (AbstractOrder) o;

        if (id != that.id) return false;
        if (price != that.price) return false;
        if (volume != that.volume) return false;
        if (side != that.side) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(side, id, price, volume);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final ToStringHelper toString = Objects.toStringHelper(this);
        toString.add("side", side);
        toString.add("id", id);
        toString.add("price", price);
        toString.add("volume", volume);
        return toString.toString();
    }
}
