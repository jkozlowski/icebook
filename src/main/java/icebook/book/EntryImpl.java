/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
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

    private final Side side;

    private final long price;

    private final long volume;

    private final int hashCode;

    /**
     * Default constructor.
     *
     * @param id     id of this {@link EntryImpl}.
     * @param side   side of this {@link EntryImpl}.
     * @param price  price of this {@link EntryImpl}.
     * @param volume volume of this {@link EntryImpl}.
     *
     * @throws NullPointerException     if {@code side} is null.
     * @throws IllegalArgumentException if any {@code long} arguments is {@code <= 0}
     */
    public EntryImpl(@Nonnegative final long id, @Nonnull final Side side, @Nonnegative final long price,
                     @Nonnegative final long volume) {
        checkArgument(id > 0, "id cannot be negative.");
        checkNotNull(side, "side cannot be null.");
        checkArgument(price > 0);
        checkArgument(volume > 0);

        this.id = id;
        this.side = side;
        this.price = price;
        this.volume = volume;
        this.hashCode = Objects.hashCode(id, side, price, volume);
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
     *
     * TODO: Implement the comparator.
     */
    @Override
    public int compareTo(Entry o) {
        return 0;
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
        helper.add("side", side);
        helper.add("price", price);
        helper.add("volume", volume);
        return helper.toString();
    }
}
