/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.order;

/**
 * Side of order.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public enum Side {

    BUY, SELL;

    /**
     * Checks if this {@link Side} is {@link Side#SELL}.
     *
     * @return {@code true} if this {@link Side} is {@link Side#SELL}, {@code false} otherwise.
     */
    public boolean isSell() {
        return SELL == this;
    }

    /**
     * Checks if this {@link Side} is {@link Side#BUY}.
     *
     * @return {@code true} if this {@link Side} is {@link Side#BUY}, {@code false} otherwise.
     */
    public boolean isBuy() {
        return BUY == this;
    }
}

