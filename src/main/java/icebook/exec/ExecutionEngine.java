/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.exec;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Longs;
import icebook.book.OrderBook;
import icebook.book.OrderBook.Entry;
import icebook.order.Order;
import icebook.order.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Executes {@link Order}s.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public final class ExecutionEngine {

    private final TimeSource timeSource;

    private final Map<Long, Order> orders;

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
        this.timeSource = new AtomicLongTimeSource();
        this.book = book;
        this.orders = Maps.newHashMap();
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
     * @throws IllegalArgumentException if {@code order} is filled.
     */
    public Collection<Trade> insert(@Nonnull final Order order) {
        checkNotNull(order, "order cannot be null.");
        checkArgument(!order.isFilled());

        final Collection<Trade> trades = Lists.newLinkedList();

        while (!order.isFilled() && !book.isEmpty(order.getSide().getOpposite())) {
            final Entry entry = book.peek(order.getSide().getOpposite()).get();

            final Optional<Trade> trade = match(order, entry);
            if (!trade.isPresent()) {
                break;
            }

            trades.add(trade.get());

            order.execute(trade.get());

            final Order entryOrder = orders.get(entry.getId());
            checkState(null != entryOrder);

            book.remove(entry.getSide());
            final Entry newEntry = entry.execute(trade.get());
            if (!newEntry.isFilled()) {
                book.insert(newEntry);
            }

            entryOrder.execute(trade.get());
            if (entryOrder.isFilled()) {
                orders.remove(entryOrder.getId());
            }
            else if (newEntry.isFilled()) {
                book.insert(entryOrder.getEntry(timeSource));
            }

        }

        if (!order.isFilled()) {
            orders.put(order.getId(), order);
            book.insert(order.getEntry(timeSource));
        }

        return trades;
    }

    /**
     * Matches an {@link Order} against this {@code entry}.
     *
     * @param newOrder {@link Order} to match.
     * @param entry    {@link Entry} to match against.
     *
     * @return either a {@link Trade} if {@code code} and {@code entry} match, or {@link Optional#absent()}.
     *
     * @throws NullPointerException     if any argument is null.
     * @throws IllegalArgumentException if {@code newOrder} and {@code entry} are not on the opposite side.
     */
    public Optional<Trade> match(@Nonnull final Order newOrder, @Nullable final Entry entry) {
        checkNotNull(newOrder, "newOrder cannot be null.");
        checkNotNull(entry, "entry cannot be null.");
        checkArgument(newOrder.getSide().getOpposite().equals(entry.getSide()),
                      "newOrder and entry must be of opposite sides.");

        if (newOrder.getSide().isBuy() && Longs.compare(newOrder.getPrice(), entry.getPrice()) >= 0) {
            return Optional.of(new Trade(newOrder.getId(), entry.getId(), entry.getPrice(),
                                         Longs.min(newOrder.getVolume(), entry.getVolume())));
        }

        if (newOrder.getSide().isSell() && Longs.compare(newOrder.getPrice(), entry.getPrice()) <= 0) {
            return Optional.of(new Trade(entry.getId(), newOrder.getId(), entry.getPrice(),
                                         Longs.min(newOrder.getVolume(), entry.getVolume())));
        }

        return Optional.absent();
    }

    /**
     * Gets an immutable sorted set of {@link Entry}ies for this {@code side}.
     *
     * @param side side to get.
     *
     * @return {@link ImmutableSortedSet} of {@link Entry}ies for this {@code side}.
     *
     * @throws NullPointerException if {@code side} is null.
     */
    public ImmutableSortedSet<Entry> toImmutableSortedSet(@Nonnull final Side side) {
        checkNotNull(side, "side cannot be null.");
        return book.toImmutableSortedSet(side);
    }
}
