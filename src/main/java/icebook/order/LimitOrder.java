/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.order;

import icebook.book.OrderBook.Entry;
import icebook.book.OrderBooks;
import icebook.exec.TimeSource;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Defines a limit order.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class LimitOrder extends AbstractOrder {

    /**
     * {@inheritDoc}
     */
    public LimitOrder(@Nonnegative final long id,
                      @Nonnull final Side side,
                      @Nonnegative final long price,
                      @Nonnegative final long volume) {
        super(id, side, price, volume);
    }

    @Override
    public Entry getEntry(@Nonnull final TimeSource timeSource) {
        checkNotNull(timeSource);
        checkState(!isFilled(), "Cannot getEntry() of a filled order.");
        return OrderBooks.newEntry(getId(), timeSource.getTime(), getSide(), getPrice(), getVolume());
    }
}
