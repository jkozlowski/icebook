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

/**
 * Factory for {@link OrderBook}s and {@link Entry}ies.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public final class OrderBooks {

    private OrderBooks() {
    }

    /**
     * Gets a default {@link Entry} implementation.
     *
     * @param id        id of this {@link Entry}.
     * @param timestamp timestamp of this {@link Entry}.
     * @param side      side of this {@link Entry}.
     * @param price     price of this {@link Entry}.
     * @param volume    volume of this {@link Entry}.
     *
     * @throws NullPointerException     if {@code side} is null.
     * @throws IllegalArgumentException if {@code id}, {@code timestamp} or {@code price} is {@code <= 0},
     *                                  or {@code volume} is {@code < 0}.
     */
    public static final Entry newEntry(@Nonnegative final long id,
                                       @Nonnegative final long timestamp,
                                       @Nonnull final Side side,
                                       @Nonnegative final long price,
                                       @Nonnegative final long volume) {
        return new EntryImpl(id, timestamp, side, price, volume);
    }

    /**
     * Gets a default {@link OrderBook} implementation.
     *
     * @return default {@link OrderBook} implementation.
     */
    public static final OrderBook newDefaultOrderBook() {
        return new DefaultOrderBook();
    }
}
