/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.order;

import com.google.common.base.Optional;
import icebook.book.OrderBook.Entry;
import icebook.exec.Matcher;
import icebook.exec.Trade;

import javax.annotation.Nonnull;

/**
 * Defines an order.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public interface Order {

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
     * Gets an {@link Entry} for the remaining volume of this {@link Order}.
     *
     * @return {@link Entry} for the remaining volume of this {@link Order}.
     *
     * @throws IllegalStateException if this {@link Order} is filled.
     */
    Entry getEntry();

    /**
     * Accepts a {@link Matcher}.
     *
     * @param matcher {@link Matcher} to accept.
     * @param entry   {@link Entry} to match against.
     *
     * @return result of matching.
     *
     * @throws NullPointerException     if any argument is null.
     * @throws IllegalArgumentException if {@code order} and {@code entry} have the same {@link Side}.
     */
    Optional<Trade> match(@Nonnull final Matcher matcher, @Nonnull final Entry entry);
}
