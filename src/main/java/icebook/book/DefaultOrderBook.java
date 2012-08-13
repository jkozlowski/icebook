/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;
import icebook.order.Side;

import javax.annotation.Nonnull;
import java.util.PriorityQueue;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default {@link OrderBook} implementation that uses {@link PriorityQueue}s to store {@link Entry}ies.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class DefaultOrderBook implements OrderBook {

    private long lastTimestamp = Long.MIN_VALUE;

    private final Queue<Entry> buys;

    private final Queue<Entry> sells;

    /**
     * Default constructor.
     */
    public DefaultOrderBook() {
        this.buys = new PriorityQueue<Entry>();
        this.sells = new PriorityQueue<Entry>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry insert(@Nonnull final Entry entry) {
        checkNotNull(entry, "entry cannot be null.");
        checkArgument(lastTimestamp < entry.getTimestamp());
        getQueue(entry).add(entry);
        lastTimestamp = entry.getTimestamp();
        return entry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Entry> peek(@Nonnull final Side side) {
        checkNotNull(side, "side cannot be null.");
        return Optional.fromNullable(getQueue(side).peek());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry remove(@Nonnull final Side side) {
        checkNotNull(side, "side cannot be null.");
        return getQueue(side).remove();
    }

    @Override
    public Entry replaceHead(@Nonnull final Entry newHead) {
        checkNotNull(newHead, "newHead cannot be null.");
        final Entry oldHead = getQueue(newHead.getSide()).remove();
        checkArgument(oldHead.getTimestamp() >= newHead.getTimestamp());
        getQueue(newHead.getSide()).add(newHead);
        return oldHead;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty(@Nonnull final Side side) {
        checkNotNull(side, "side cannot be null.");
        return getQueue(side).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutableSortedSet<Entry> toImmutableSortedSet(@Nonnull final Side side) {
        return ImmutableSortedSet.copyOf(getQueue(side));
    }

    /**
     * Gets the queue for this {@code entry}.
     *
     * @param entry {@link Entry} to get the queue for.
     *
     * @return the queue for this {@link Entry}.
     *
     * @throws NullPointerException if {@code entry} is null.
     */
    private Queue<Entry> getQueue(@Nonnull final Entry entry) {
        checkNotNull(entry, "entry cannot be null.");
        return getQueue(entry.getSide());
    }

    /**
     * Gets the queue for this {@code side}.
     *
     * @param side {@link Side} to get the queue for.
     *
     * @return the queue for this {@code side}.
     *
     * @throws NullPointerException if {@code side} is null.
     */
    private Queue<Entry> getQueue(@Nonnull final Side side) {
        checkNotNull(side, "side cannot be null.");
        return side.isBuy() ? buys : sells;
    }
}
