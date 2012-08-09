/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

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

    @Override
    public int compareTo(Entry o) {
        return 0;
    }
}
