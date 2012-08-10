/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.exec;

import icebook.book.OrderBook;
import icebook.order.Order;
import icebook.order.Side;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Executes {@link Order}s.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public final class ExecutionEngine {

    private final OrderBook book;

    /**
     * Default constructor.
     *
     * @param book {@link OrderBook} that this {@link ExecutionEngine} will use.
     *
     * @throws NullPointerException     if {@code book} is null.
     * @throws IllegalArgumentException if {@code book} is not empty.
     */
    public ExecutionEngine(@Nonnull final OrderBook book) {
        checkNotNull(book);
        checkArgument(book.isEmpty(Side.SELL));
        checkArgument(book.isEmpty(Side.BUY));
        this.book = book;
    }

    /**
     * Inserts this {@code order}.
     *
     * @param order {@link Order} to insert.
     *
     * @return all trades that were executed if the {@code order} was aggressive,
     *         or an empty collection if the {@code order was passive.}
     */
    public Collection<Trade> insert(@Nonnull final Order order) {
        checkNotNull(order);
        return Collections.EMPTY_LIST;
    }
}
