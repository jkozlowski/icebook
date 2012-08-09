/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.output;

import icebook.order.LimitOrder;
import icebook.order.Order;
import icebook.order.Side;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.SortedSet;

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
     * Appends to {@code out} a formatted representation of {@code sellOrders} and {@code buyOrders},
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
     * @param out        {@link Appendable} to append to.
     * @param sellOrders {@link Side#SELL} {@link Order}s to render.
     * @param buyOrders  {@link Side#BUY} {@link Order}s to render.
     *
     * @return a reference to {@code out}.
     *
     * @throws IOException If an I/O error occurs.
     */
    public static Appendable append(final @Nonnull Appendable out,
                                    final @Nonnull SortedSet<Order> sellOrders,
                                    final @Nonnull SortedSet<Order> buyOrders) throws IOException {

        checkNotNull(out);
        checkNotNull(sellOrders);
        checkNotNull(buyOrders);

        final Formatter format = new Formatter(out, Locale.ENGLISH);

        // No actual formatting, because this way the header is much more readable in source code.
        format.format("+-----------------------------------------------------------------+%n");
        format.format("| BUY                            | SELL                           |%n");
        format.format("| Id       | Volume      | Price | Price | Volume      | Id       |%n");
        format.format("+----------+-------------+-------+-------+-------------+----------+%n");

        final Iterator<Order> buyOrdersIterator = buyOrders.iterator();
        final Iterator<Order> sellOrdersIterator = sellOrders.iterator();
        while (sellOrdersIterator.hasNext() || buyOrdersIterator.hasNext()) {

            if (buyOrdersIterator.hasNext()) {
                final LimitOrder buyOrder = (LimitOrder) buyOrdersIterator.next();
                // I used magic values, so that the append string is easier to read.
                format.format("|%10d|%,13d|%,7d|", buyOrder.id, buyOrder.quantity, buyOrder.price);
            }
            else {
                // Again, this is easier to read.
                format.format("|          |             |       |");
            }

            if (sellOrdersIterator.hasNext()) {
                final LimitOrder sellOrder = (LimitOrder) sellOrdersIterator.next();
                // Likewise.
                format.format("%,7d|%,13d|%10d|", sellOrder.price, sellOrder.quantity, sellOrder.id);
            }
            else {
                // Likewise.
                format.format("          |             |       |");
            }

            format.format("%n");
        }

        out.append("+-----------------------------------------------------------------+\n");

        return out;
    }
}
