/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.order;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import icebook.book.OrderBook.Entry;
import icebook.book.OrderBooks;
import icebook.exec.TimeSource;
import icebook.exec.Trade;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Defines a limit order.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class LimitOrder implements Order {

    private final Side side;

    private final long id;

    private final long price;

    private long volume;

    public LimitOrder(final Side side, final long id, final long price, final long volume) {
        this.side = checkNotNull(side);
        this.id = id;
        this.price = price;
        this.volume = volume;
    }

    @Override
    public long getId() {
        return id;
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
     * Gets the volume.
     *
     * @return the volume.
     */
    public long getVolume() {
        return volume;
    }

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

    @Override
    public Entry getEntry(@Nonnull final TimeSource timeSource) {
        checkNotNull(timeSource);
        checkState(!isFilled(), "Cannot getEntry() of a filled order.");
        return OrderBooks.newEntry(id, timeSource.getTime(), side, price, volume);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final LimitOrder that = (LimitOrder) o;

        if (id != that.id) return false;
        if (price != that.price) return false;
        if (volume != that.volume) return false;
        if (side != that.side) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = side.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (price ^ (price >>> 32));
        result = 31 * result + (int) (volume ^ (volume >>> 32));
        return result;
    }

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
