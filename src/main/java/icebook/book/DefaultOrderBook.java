/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import javax.annotation.Nonnull;
import java.util.PriorityQueue;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default {@link OrderBook} implementation that uses {@link PriorityQueue}s to store {@link Entry}ies directly.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class DefaultOrderBook implements OrderBook {

    private final Queue<Entry> buys;

    private final Queue<Entry> sells;

    /**
     * Default constructor.
     */
    public DefaultOrderBook() {
        this.buys = new PriorityQueue<>();
        this.sells = new PriorityQueue<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry insert(@Nonnull final Entry entry) {
        checkNotNull(entry);
        getQueue(entry).add(entry);
        return entry;
    }

    /**
     * Gets the appropriate queue for this {@code entry}.
     *
     * @param entry {@link Entry} to get the appropriate queue.
     *
     * @return appropriate queue.
     *
     * @throws NullPointerException if {@code entry} is null.
     */
    private Queue<Entry> getQueue(@Nonnull final Entry entry) {
        checkNotNull(entry, "entry cannot be null.");
        return entry.getSide().isBuy() ? buys : sells;
    }
}
