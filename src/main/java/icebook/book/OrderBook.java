/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import icebook.order.Side;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Maintains sorted sets of {@link Side#SELL} and {@link Side#BUY} {@link Entry}ies.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public interface OrderBook {

    /**
     * Single entry in the {@link OrderBook}.
     */
    @Immutable
    public interface Entry extends Comparable<Entry> {

        /**
         * Gets the id.
         *
         * @return the id.
         */
        long getId();

        /**
         * Gets the side.
         *
         * @return the side.
         */
        Side getSide();

        /**
         * Gets the price.
         *
         * @return the price.
         */
        long getPrice();

        /**
         * Gets the volume.
         *
         * @return the volume.
         */
        long getVolume();
    }

    /**
     * Inserts an {@link Entry} into this {@link OrderBook}. It is the responsibility of the user of this {@link
     * OrderBook} to ensure that no entry exists already with this {@link Entry#getId()}.
     *
     * @param entry entry to insert.
     *
     * @return reference to this {@code entry}.
     *
     * @throws NullPointerException if {@code order} is null.
     */
    Entry insert(final @Nonnull Entry entry);
}
