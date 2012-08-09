/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.primitives.Longs;
import icebook.book.OrderBook.Entry;
import icebook.order.Side;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default {@link OrderBook.Entry} implementation.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class EntryImpl implements OrderBook.Entry {

    private final long id;

    private final long timestamp;

    private final Side side;

    private final long price;

    private final long volume;

    private final int hashCode;

    /**
     * Default constructor.
     *
     * @param id        id of this {@link EntryImpl}.
     * @param timestamp timestamp of this {@link EntryImpl}.
     * @param side      side of this {@link EntryImpl}.
     * @param price     price of this {@link EntryImpl}.
     * @param volume    volume of this {@link EntryImpl}.
     *
     * @throws NullPointerException     if {@code side} is null.
     * @throws IllegalArgumentException if any {@code long} argument is {@code <= 0}
     */
    public EntryImpl(@Nonnegative final long id,
                     @Nonnegative final long timestamp,
                     @Nonnull final Side side,
                     @Nonnegative final long price,
                     @Nonnegative final long volume) {
        checkArgument(id > 0, "id cannot be negative.");
        checkArgument(timestamp > 0, "timestamp cannot be negative.");
        checkNotNull(side, "side cannot be null.");
        checkArgument(price > 0, "price cannot be negative.");
        checkArgument(volume > 0, "volume cannot be negative.");

        this.id = id;
        this.timestamp = timestamp;
        this.side = side;
        this.price = price;
        this.volume = volume;
        this.hashCode = Objects.hashCode(id, timestamp, side, price, volume);
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
    @Override
    public long getTimestamp() {
        return timestamp;
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
    public long getPrice() {
        return price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getVolume() {
        return volume;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@Nonnull final Entry other) {

        checkNotNull(other, "other cannot be null");
        checkArgument(other.getSide().equals(this.side), "other's side must be the same.");

        if (this == other) {
            return 0;
        }

        if (Longs.compare(this.getPrice(), other.getPrice()) > 0) {
            return this.side.isBuy() ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }

        if (Longs.compare(this.getPrice(), other.getPrice()) < 0) {
            return this.side.isBuy() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }

        return Longs.compare(this.timestamp, other.getTimestamp());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final EntryImpl entry = (EntryImpl) o;

        if (id != entry.id) return false;
        if (timestamp != entry.timestamp) return false;
        if (price != entry.price) return false;
        if (volume != entry.volume) return false;
        if (side != entry.side) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        final ToStringHelper helper = Objects.toStringHelper(this);
        helper.add("id", id);
        helper.add("timestamp", timestamp);
        helper.add("side", side);
        helper.add("price", price);
        helper.add("volume", volume);
        return helper.toString();
    }
}
