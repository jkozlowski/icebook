/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.output;

import com.google.common.base.Equivalence;
import com.google.common.base.Equivalence.Wrapper;
import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import icebook.book.OrderBook.Entry;
import icebook.exec.Trade;
import icebook.order.Side;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Appenders that append formatted output to {@link Appendable}s.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public final class Appenders {

    private Appenders() {
    }

    /**
     * Appends to {@code out} a formatted representation of {@code sells} and {@code buys},
     * e.g. the following list of orders:
     *
     * <h2>BUY[Id, Volume, Price]</h2>
     * <ol>
     * <li>1234567890, 1234567890, 32503</li>
     * <li>1138, 7500, 31502</li>
     * </ol>
     *
     * <h2>SELL[Id, Volume, Price]</h2>
     * <ol>
     * <li>1234567891, 1234567890, 32504</li>
     * <li>6808, 7777, 32505</li>
     * <li>42100, 3000, 32507</li>
     * </ol>
     *
     * will be rendered as:
     *
     * <pre>
     * +-----------------------------------------------------------------+
     * | BUY                            | SELL                           |
     * | Id       | Volume      | Price | Price | Volume      | Id       |
     * +----------+-------------+-------+-------+-------------+----------+
     * |1234567890|1,234,567,890| 32,503| 32,504|1,234,567,890|1234567891|
     * |      1138|        7,500| 31,502| 32,505|        7,777|      6808|
     * |          |             |       | 32,507|        3,000|     42100|
     * +-----------------------------------------------------------------+
     * </pre>
     *
     * <p>If both {@code sells} and {@code buys} are empty, this method will append an empty table to {@code out}.
     * <em>This method will explicitly flush {@code out}</em>.</p>
     *
     * @param out   {@link Appendable} to append to.
     * @param sells {@link Side#SELL} {@link Entry}ies to render.
     * @param buys  {@link Side#BUY} {@link Entry}ies to render.
     *
     * @return a reference to {@code out}.
     *
     * @throws NullPointerException if any argument is null.
     */
    public static Appendable append(@Nonnull final Appendable out,
                                    @Nonnull final SortedSet<Entry> sells,
                                    @Nonnull final SortedSet<Entry> buys) {

        checkNotNull(out, "out cannot be null.");
        checkNotNull(sells, "sells cannot be null.");
        checkNotNull(buys, "buys cannot be null.");

        final Formatter format = new Formatter(out, Locale.ENGLISH);

        // No actual formatting, because this way the header is much more readable in source code.
        format.format("+-----------------------------------------------------------------+%n");
        format.format("| BUY                            | SELL                           |%n");
        format.format("| Id       | Volume      | Price | Price | Volume      | Id       |%n");
        format.format("+----------+-------------+-------+-------+-------------+----------+%n");

        final Iterator<Entry> buysIterator = buys.iterator();
        final Iterator<Entry> sellsIterator = sells.iterator();
        while (sellsIterator.hasNext() || buysIterator.hasNext()) {

            if (buysIterator.hasNext()) {
                final Entry buyEntry = buysIterator.next();
                // I used magic values, so that the append string is easier to read.
                format.format("|%10d|%,13d|%,7d|", buyEntry.getId(), buyEntry.getVolume(), buyEntry.getPrice());
            }
            else {
                // Again, this is easier to read.
                format.format("|          |             |       |");
            }

            if (sellsIterator.hasNext()) {
                final Entry sellEntry = sellsIterator.next();
                // Likewise.
                format.format("%,7d|%,13d|%10d|", sellEntry.getPrice(), sellEntry.getVolume(), sellEntry.getId());
            }
            else {
                // Likewise.
                format.format("       |             |          |");
            }

            format.format("%n");
        }

        format.format("+-----------------------------------------------------------------+%n");
        format.flush();

        return out;
    }

    /**
     * Appends to {@code out} a formatted representation of {@code trades}, where the trade message will be contained
     * on a single line with nothing else present on the same line, and be in comma separated format with the following
     * fields:
     *
     * <table>
     * <tr>
     * <th>Field Index</th>
     * <th>Description</th>
     * </tr>
     * <tr>
     * <td>0</td>
     * <td>buy order id matched</td>
     * </tr>
     * <tr>
     * <td>1</td>
     * <td>sell order id matched</td>
     * </tr>
     * <tr>
     * <td>2</td>
     * <td>price in pence</td>
     * </tr>
     * <tr>
     * <td>3</td>
     * <td>quantity</td>
     * </tr>
     * </table>
     *
     * e.g. <pre>100322,100345,5103,7500</pre>
     *
     * <p>If {@code trades} is empty, this method will not append anything to {@code out}. <em>This method will
     * explicitly flush {@code out}</em>.</p>
     *
     * <p>If {@code trades} contains multiple trades between the same id's, those will be collapsed into a single
     * trade.</p>
     *
     * @param out    {@link Appendable} to append to.
     * @param trades {@link Trade}s to render.
     *
     * @return a reference to {@code out}.
     *
     * @throws NullPointerException if any argument is null.
     */
    public static Appendable append(@Nonnull final Appendable out, @Nonnull final Collection<Trade> trades) {

        checkNotNull(out, "out cannot be null.");
        checkNotNull(trades, "trades cannot be null.");

        final Formatter format = new Formatter(out, Locale.ENGLISH);

        // Collapse trades.
        final BiMap<Long, Wrapper<Trade>> tradesBiMap = HashBiMap.create();
        long counter = 0;
        for (final Trade t : trades) {

            final Wrapper<Trade> wrappedT = idEquivalence.wrap(t);
            final Long savedCounter = tradesBiMap.inverse().get(wrappedT);

            if (null != savedCounter) {
                final Trade savedTrade = tradesBiMap.get(savedCounter).get();
                tradesBiMap.remove(savedCounter);
                tradesBiMap.put(counter, merge(t, savedTrade));
            }
            else {
                tradesBiMap.put(counter, wrappedT);
            }

            counter++;
        }

        // Sort the trades.
        final SortedMap<Long, Wrapper<Trade>> sortedTrades = new TreeMap<Long, Wrapper<Trade>>(tradesBiMap);

        for (final Map.Entry<Long, Wrapper<Trade>> e : sortedTrades.entrySet()) {
            final Trade t = e.getValue().get();
            format.format("%d,%d,%d,%d%n", t.getBuyOrderId(), t.getSellOrderId(), t.getPrice(), t.getQuantity());
        }

        format.flush();

        return out;
    }

    /**
     * Gets a merged {@link Trade}, i.e. a {@link Trade} with same {@link Trade#getBuyOrderId()},
     * {@link Trade#getSellOrderId()} and {@link Trade#getPrice()}, but quantity defined as {@code t1.getQuantity() +
     * t2.getQuantity()}.
     *
     * @param t1 first {@link Trade} to merge.
     * @param t2 second {@link Trade} to merge.
     *
     * @return merged {@link Trade}, wrapped in {@link Wrapper}, defined by {@link #idEquivalence}.
     */
    static Wrapper<Trade> merge(@Nonnull final Trade t1, @Nonnull final Trade t2) {
        checkNotNull(t1, "t1 cannot be null.");
        checkNotNull(t2, "t2 cannot be null.");
        checkArgument(t1.getBuyOrderId() == t2.getBuyOrderId(), "buyOrderIds must be equal.");
        checkArgument(t1.getSellOrderId() == t2.getSellOrderId(), "sellOrderIds must be equal.");
        checkArgument(t1.getPrice() == t2.getPrice(), "prices must be equal.");
        return idEquivalence.wrap(new Trade(t1.getBuyOrderId(), t1.getSellOrderId(), t1.getPrice(),
                                            t1.getQuantity() + t2.getQuantity()));
    }

    /**
     * Defines an equivalence for {@link Trade}s that declares two {@link Trade}s equivalent,
     * if they refer to the same exact {@link Trade#getBuyOrderId()} and {@link Trade#getSellOrderId()}.
     */
    private static final Equivalence<Trade> idEquivalence = new Equivalence<Trade>() {

        /**
         * {@inheritDoc}
         */
        @Override
        protected boolean doEquivalent(@Nonnull final Trade a, @Nonnull final Trade b) {
            return a.getBuyOrderId() == b.getBuyOrderId() && a.getSellOrderId() == b.getSellOrderId();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected int doHash(@Nonnull final Trade trade) {
            return Objects.hashCode(trade.getBuyOrderId(), trade.getSellOrderId());
        }
    };
}
