/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.formatter;

import icebook.order.Order;
import icebook.order.Side;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Formatter;
import java.util.Locale;
import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Formats {@link SortedSet}s of {@link Side#BUY} and {@link Side#SELL} {@link Order}s.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public final class Formatters {

    public static final int ID_COLUMN_WIDTH = 10;

    public static final int VOLUME_COLUMN_WIDTH = 10;

    public static final int PRICE_COLUMN_WIDTH = 7;

    public static final int TOTAL_COLUMN_WIDTH = 67;

    public static final int TOP_FORMATTING_MARKERS_WIDTH = 2;

    public static final int TOP_DASH_WIDTH = TOTAL_COLUMN_WIDTH - TOP_FORMATTING_MARKERS_WIDTH;

    public static Appendable format(final @Nonnull Appendable out,
                                    final @Nonnull SortedSet<Order> sellOrders,
                                    final @Nonnull SortedSet<Order> buyOrders) throws IOException {

        checkNotNull(out);
        checkNotNull(sellOrders);
        checkNotNull(buyOrders);

        final Formatter format = new Formatter(out, Locale.ENGLISH);

        out.append("+-----------------------------------------------------------------+\n");
        out.append("| BUY                            | SELL                           |\n");
        out.append("| Id       | Volume      | Price | Price | Volume      | Id       |\n");
        out.append("+----------+-------------+-------+-------+-------------+----------+\n");

        out.append("+-----------------------------------------------------------------+\n");

        return out;
    }

    /**
     * Appends {@code c} to {@code out} the number of times indicated by {@code times}.
     *
     * @param out   {@link Appendable} to append to.
     * @param c     character to append.
     * @param times indicates the number of times to append {@code c} to {@code out}.
     *
     * @return the {@code out} for easy chaining.
     *
     * @throws NullPointerException     if {@code out} is null.
     * @throws IllegalArgumentException if {@code times <= 0}.
     */
    static Appendable appendTimes(final @Nonnull Appendable out,
                                  final char c,
                                  final @Nonnegative int times) throws IOException {

        checkNotNull(out);
        checkArgument(times > 0);

        for (int i = 0; i < times; i++) {
            out.append(c);
        }

        return out;
    }

}
