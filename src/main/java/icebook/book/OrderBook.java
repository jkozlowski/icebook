/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;
import icebook.exec.Trade;
import icebook.order.Side;

import javax.annotation.Nonnull;

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
    public interface Entry extends Comparable<Entry> {

        /**
         * Gets the id.
         *
         * @return the id.
         */
        long getId();

        /**
         * Gets the timestamp.
         *
         * @return gets the timestamp.
         */
        long getTimestamp();

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

        /**
         * Checks if this {@link Entry} is filled.
         *
         * @return {@code true} if this {@link Entry} is filled, {@code false} otherwise.
         */
        boolean isFilled();

        /**
         * Executes this {@link Entry}.
         *
         * @param trade details of the trade.
         *
         * @throws NullPointerException     if {@code trade} is null.
         * @throws IllegalArgumentException if {@code trade} does not refer to this {@link Entry}.
         */
        void execute(@Nonnull final Trade trade);

        /**
         * <em>Note: this class has a natural ordering that is inconsistent with equals.</em> More specifically,
         * if {@link #compareTo(Entry)}  returns 0, the two {@link Entry}ies prices and timestamps are equal; it does
         * not however indicate that their remaining properties are also equal by the contract of {@link
         * #equals(Object)}.
         *
         * @throws NullPointerException     if {@code other} is null.
         * @throws IllegalArgumentException if {@code other}'s {@link Side} is different.
         */
        @Override
        public int compareTo(@Nonnull final Entry other);
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

    /**
     * Gets the first {@link Entry} on this {@code side}.
     *
     * @param side side of {@link Entry} to get.
     *
     * @return first {@link Entry} on this {@code side}.
     *
     * @throws NullPointerException if {@code side} is null.
     */
    Optional<Entry> peek(@Nonnull final Side side);

    /**
     * Removes the first {@link Entry} on this {@code side}.
     *
     * @param side side of {@link Entry} to remove.
     *
     * @return removed {@link Entry}.
     *
     * @throws NullPointerException  if {@code side} is null.
     * @throws IllegalStateException if this {@code side} is empty.
     */
    Entry remove(@Nonnull final Side side);

    /**
     * Checks if the book is empty on this {@code side}.
     *
     * @param side the side of the book to check.
     *
     * @return {@code true} is this {@code side} of the book is empty, {@code false} otherwise.
     *
     * @throws NullPointerException if {@code side} is null.
     */
    boolean isEmpty(@Nonnull final Side side);

    /**
     * Gets an immutable sorted set of {@link Entry}ies for this {@code side}.
     *
     * @param side side to get.
     *
     * @return {@link ImmutableSortedSet} of {@link Entry}ies for this {@code side}.
     *
     * @throws NullPointerException if {@code side} is null.
     */
    ImmutableSortedSet<Entry> toImmutableSortedSet(@Nonnull final Side side);
}
