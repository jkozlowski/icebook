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
import javax.annotation.concurrent.Immutable;
import java.util.NoSuchElementException;

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
         * Executes this {@link Entry}. Because {@link Entry} is immutable, a new {@link Entry} instance will be
         * returned, with updated {@link #getVolume()}, and remaining parameters of an {@link Entry} unchanged.
         *
         * @param trade details of the trade.
         *
         * @return executed {@link Entry}.
         *
         * @throws NullPointerException     if {@code trade} is null.
         * @throws IllegalArgumentException if {@code trade} does not refer to this {@link Entry}.
         */
        Entry execute(@Nonnull final Trade trade);

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
     * Inserts an {@link Entry} into this {@link OrderBook}.
     *
     * @param entry entry to insert.
     *
     * @return reference to this {@code entry}.
     *
     * @throws NullPointerException     if {@code order} is null.
     * @throws IllegalArgumentException if the timestamp of {@code entry} is not strictly older than the
     *                                  current oldest {@link Entry}.
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
     * @throws NullPointerException   if {@code side} is null.
     * @throws NoSuchElementException if this {@code side} is empty.
     */
    Entry remove(@Nonnull final Side side);

    /**
     * Removes the first {@link Entry} on the {@code side} of this {@code newHead} and replaces it with {@code
     * newHead}.
     *
     * @param newHead {@link Entry} to put at the head.
     *
     * @return removed {@link Entry}.
     *
     * @throws NullPointerException     if {@code newHead} is null.
     * @throws NoSuchElementException   if side of this {@code newHead} is empty.
     * @throws IllegalArgumentException if {@code newHead}'s timestamp is not {@code <=} to {@code oldHead}'s
     *                                  timestamp.
     */
    Entry replaceHead(@Nonnull final Entry newHead);

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
