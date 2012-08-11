/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.order;

import icebook.book.OrderBook.Entry;
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
     * Gets the price.
     *
     * @return the price.
     */
    long getPrice();

    /**
     * Gets the remaining volume.
     *
     * @return the remaining volume.
     */
    long getVolume();

    /**
     * Checks if this {@link Order} is filled.
     *
     * @return {@code true} if this {@link Order} is filled, {@code false} otherwise.
     */
    boolean isFilled();

    /**
     * Executes this {@link Order}.
     *
     * @param trade details of the trade.
     *
     * @throws NullPointerException     if {@code trade} is null.
     * @throws IllegalArgumentException if {@code trade} does not refer to this {@link Order}.
     */
    void execute(@Nonnull final Trade trade);

    /**
     * Gets an {@link Entry} for the remaining volume of this {@link Order}.
     *
     * @return {@link Entry} for the remaining volume of this {@link Order}.
     *
     * @throws IllegalStateException if this {@link Order} is filled.
     */
    Entry getEntry();
}
