/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;
import icebook.order.Side;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Implementation of the {@link OrderBook} that optimises several code paths, but requires more memory.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class OptimisedOrderBook implements OrderBook {

    /**
     * Entry for a single price level in the {@link OrderBook}. {@link PriceLevelEntry} maintains a linked list
     * of {@link Entry}ies, without using comparisons; {@link Entry}ies are simply put at the end of the {@code
     * entries} queue.
     */
    static final class PriceLevelEntry implements Comparable<PriceLevelEntry> {

        private static final Ordering<Long> buyOrdering = Ordering.natural().reverse();

        private final int hashCode;

        public final long priceLevel;

        public final Side side;

        public final PriceLevelEntries entries;

        /**
         * Default constructor.
         *
         * @param firstEntry first {@link Entry} at this price level.
         *
         * @throws NullPointerException if {@code firstEntry} is null.
         */
        public PriceLevelEntry(@Nonnull final Entry firstEntry) {
            checkNotNull(firstEntry, "firstEntry cannot be null");
            this.priceLevel = firstEntry.getPrice();
            this.side = firstEntry.getSide();
            this.entries = new PriceLevelEntries(firstEntry);
            this.hashCode = Objects.hashCode(priceLevel, side);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final PriceLevelEntry that = (PriceLevelEntry) o;

            if (priceLevel != that.priceLevel) return false;
            if (side != that.side) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        /**
         * {@inheritDoc}
         *
         * @throws IllegalArgumentException if {@code other's} side is different.
         */
        @Override
        public int compareTo(@Nonnull final PriceLevelEntry other) {
            checkNotNull(other, "other cannot be null.");
            checkArgument(side.equals(other.side), "other's side must be the same.");
            return side.isSell() ?
                    Longs.compare(priceLevel, other.priceLevel) :
                    buyOrdering.compare(priceLevel, other.priceLevel);
        }
    }

    /**
     * Maintains a sorted list of {@link Entry}ies. This implementation will make basic sanity checks,
     * to make sure that all entries are of the same {@link Side} and price. Similarly,
     * it will not allow to insert an {@link Entry} with a timestamp that is earlier than the current oldest timestamp.
     */
    static final class PriceLevelEntries {

        public final long priceLevel;

        public final Side side;

        private final LinkedList<Entry> entries;

        /**
         * Creates a {@link PriceLevelEntries} for entries that have the same side and price as {@code firstEntry}.
         *
         * @param firstEntry first entry in this {@link PriceLevelEntries}.
         *
         * @throws NullPointerException if {@code firstEntry} is null.
         */
        public PriceLevelEntries(@Nonnull final Entry firstEntry) {
            checkNotNull(firstEntry, "firstEntry cannot be null.");
            this.priceLevel = firstEntry.getPrice();
            this.side = firstEntry.getSide();
            this.entries = Lists.newLinkedList();
            this.entries.addLast(firstEntry);
        }

        /**
         * Inserts this {@code entry} to this {@link PriceLevelEntries}.
         *
         * @param entry {@link Entry} to insert.
         *
         * @throws NullPointerException     if {@code entry} is null.
         * @throws IllegalArgumentException if the timestamp of {@code entry} is not strictly older than the current
         *                                  oldest {@link Entry}.
         * @throws IllegalArgumentException if the price of {@code entry} is not that of the rest of {@link Entry}ies.
         * @throws IllegalArgumentException if the side of {@code entry} is not that of the rest of {@link Entry}ies.
         */
        public void insert(@Nonnull final Entry entry) {
            checkNotNull(entry, "entry cannot be null.");
            checkArgument(entry.getSide().equals(side), "entry's side must be the same.");
            checkArgument(entry.getPrice() == priceLevel, "entry's priceLevel must be the same.");
            checkArgument(entries.isEmpty() || entries.getLast().getTimestamp() < entry.getTimestamp(),
                          "entry must be older than current oldest entry.");
            entries.addLast(entry);
        }

        /**
         * Removes the first {@link Entry} in this {@link PriceLevelEntries}.
         *
         * @return old head {@link Entry} in this {@link PriceLevelEntries}.
         *
         * @throws NoSuchElementException if this {@link PriceLevelEntries} is empty.
         */
        public Entry removeHead() {
            return entries.removeFirst();
        }

        /**
         * Replaces the first {@link Entry} in this {@link PriceLevelEntries} with {@code newHead}.
         *
         * @return old head {@link Entry} in this {@link PriceLevelEntries}.
         *
         * @throws NullPointerException     if {@code newHead} is null.
         * @throws NoSuchElementException   if this {@link PriceLevelEntries} is empty.
         * @throws IllegalArgumentException if {@code newHead}'s timestamp is not {@code <=} to {@code oldHead}'s
         *                                  timestamp.
         */
        public Entry replaceHead(@Nonnull final Entry newHead) {
            checkNotNull(newHead, "newHead cannot be null.");
            final Entry oldHead = entries.remove();
            checkArgument(oldHead.getTimestamp() >= newHead.getTimestamp());
            entries.addFirst(newHead);
            return oldHead;
        }

        /**
         * Checks if this {@link PriceLevelEntries} is empty.
         *
         * @return {@code true} if this {@link PriceLevelEntries} is empty, {@code false} otherwise.
         */
        public boolean isEmpty() {
            return entries.isEmpty();
        }

        /**
         * Gets the first {@link Entry} in this {@link PriceLevelEntries}.
         *
         * @return first {@link Entry} in this {@link PriceLevelEntries} or {@link Optional#absent()} if this
         *         {@link PriceLevelEntries} is empty.
         */
        public Optional<Entry> peek() {
            return Optional.fromNullable(entries.peek());
        }
    }

    private long lastTimestamp = Long.MIN_VALUE;

    private final Queue<PriceLevelEntry> sells;

    private final Queue<PriceLevelEntry> buys;

    private final Map<Long, PriceLevelEntry> sellsLookup;

    private final Map<Long, PriceLevelEntry> buysLookup;

    /**
     * Default constructor.
     */
    public OptimisedOrderBook() {
        this.sells = new PriorityQueue<PriceLevelEntry>();
        this.buys = new PriorityQueue<PriceLevelEntry>();
        this.sellsLookup = Maps.newHashMap();
        this.buysLookup = Maps.newHashMap();
    }

    /**
     * {@inheritDoc}
     *
     * <p>The insertion will be {@code O(1)} in the best case scenario, when we are inserting an entry for which we
     * already have a {@link PriceLevelEntry}; otherwise, the insertion is {@code O(log(n))}.
     * </p>
     */
    @Override
    public Entry insert(@Nonnull final Entry entry) {
        checkNotNull(entry, "entry cannot be null.");
        checkArgument(lastTimestamp < entry.getTimestamp());

        final Map<Long, PriceLevelEntry> lookup = getLookup(entry.getSide());
        final PriceLevelEntry exists = lookup.get(entry.getPrice());
        if (null != exists) {
            exists.entries.insert(entry);
        }
        else {
            final PriceLevelEntry newEntry = new PriceLevelEntry(entry);
            lookup.put(newEntry.priceLevel, newEntry);
            getQueue(newEntry.side).add(newEntry);
        }

        lastTimestamp = entry.getTimestamp();

        return entry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Entry> peek(@Nonnull final Side side) {
        checkNotNull(side, "side cannot be null.");
        final Queue<PriceLevelEntry> queue = getQueue(side);
        if (queue.isEmpty()) {
            return Optional.absent();
        }

        return queue.peek().entries.peek();
    }

    /**
     * {@inheritDoc}
     *
     * <p>The removal will be {@code O(1)} in the best case scenario, when we do not have to remove the {@link
     * PriceLevelEntry}; otherwise, the removal is {@code O(log(n))}.</p>
     */
    @Override
    public Entry remove(@Nonnull final Side side) {
        checkNotNull(side, "side cannot be null.");

        if (isEmpty(side)) {
            throw new NoSuchElementException("this side is empty.");
        }

        final PriceLevelEntry priceLevelEntry = getQueue(side).peek();
        checkNotNull(priceLevelEntry, "ooops, something's wrong.");
        final Entry oldHead = priceLevelEntry.entries.removeHead();
        if (priceLevelEntry.entries.isEmpty()) {
            checkState(getQueue(side).remove() == priceLevelEntry);
            getLookup(side).remove(priceLevelEntry.priceLevel);
        }

        return oldHead;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The head replacement will be {@code O(1)}.</p>
     */
    @Override
    public Entry replaceHead(@Nonnull final Entry newHead) {
        checkNotNull(newHead, "newHead cannot be null.");

        if (isEmpty(newHead.getSide())) {
            throw new NoSuchElementException("this side is empty.");
        }

        final PriceLevelEntry priceLevelEntry = getQueue(newHead.getSide()).peek();
        checkNotNull(priceLevelEntry);

        return priceLevelEntry.entries.replaceHead(newHead);
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
        checkNotNull(side, "side cannot be null.");

        final SortedSet<PriceLevelEntry> priceLevels = Sets.newTreeSet(getQueue(side));

        final LinkedList<Entry> entries = Lists.newLinkedList();
        for (final PriceLevelEntry e : priceLevels) {
            entries.addAll(e.entries.entries);
        }

        return ImmutableSortedSet.copyOf(entries);
    }

    /**
     * Gets the lookup for this {@code side}.
     *
     * @param side side of lookup to get.
     *
     * @return lookup for this {@code side}.
     *
     * @throws NullPointerException if {@code side} is null.
     */
    private Map<Long, PriceLevelEntry> getLookup(@Nonnull final Side side) {
        checkNotNull(side, "side cannot be null.");
        return side.isBuy() ? buysLookup : sellsLookup;
    }

    /**
     * Gets the queue for this {@code side}.
     *
     * @param side side of queue to get.
     *
     * @return queue for this {@code side}.
     *
     * @throws NullPointerException if {@code side} is null.
     */
    private Queue<PriceLevelEntry> getQueue(@Nonnull final Side side) {
        checkNotNull(side, "side cannot be null.");
        return side.isBuy() ? buys : sells;
    }
}
