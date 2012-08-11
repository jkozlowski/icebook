/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.exec;

import com.google.common.base.Optional;
import icebook.book.OrderBook;
import icebook.book.OrderBook.Entry;
import icebook.order.IcebergOrder;
import icebook.order.LimitOrder;
import icebook.order.Order;
import icebook.order.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
public final class ExecutionEngine implements Matcher {

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
        checkNotNull(book, "book cannot be null.");
        checkArgument(book.isEmpty(Side.SELL), "book must be empty.");
        checkArgument(book.isEmpty(Side.BUY), "book must be empty.");
        this.book = book;
    }

    /**
     * Inserts this {@code order}. It is the responsibility of the user of this class to make sure that an order does
     * not get inserted twice.
     *
     * @param order {@link Order} to insert.
     *
     * @return all trades that were executed if the {@code order} was aggressive,
     *         or an empty collection if the {@code order was passive.}
     *
     * @throws IllegalStateException if {@code order} is filled.
     */
    public Collection<Trade> insert(@Nonnull final Order order) {
        checkNotNull(order, "order cannot be null.");

        while (book.peek(order.getSide().getOpposite()).isPresent()) {
            final Optional<Trade> trade = order.match(this, book.peek(order.getSide().getOpposite()).get());
            if (!trade.isPresent()) {
                break;
            }
        }

        return Collections.EMPTY_LIST;
    }

    @Override
    public Optional<Trade> match(@Nonnull final IcebergOrder order, @Nullable final Entry entry) {
        return Optional.absent();
    }

    @Override
    public Optional<Trade> match(@Nonnull final LimitOrder order, @Nullable final Entry entry) {
        return Optional.absent();
    }
}
