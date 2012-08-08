/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.formatter;

import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.Ranges;
import icebook.order.Order;
import icebook.order.Side;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
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

    public static String format(final SortedSet<Order> sellOrders, final SortedSet<Order> buyOrders) {

        final StringBuilder sb = new StringBuilder();
        final Formatter format = new Formatter(sb, Locale.ENGLISH);

        return null;
    }

    /**
     * Appends {@code c} to {@code sb} the number of times indicated by {@code times}.
     *
     * @param sb    {@link StringBuilder} to append to.
     * @param c     character to append.
     * @param times indicates the number of times to append {@code c} to {@code sb}.
     *
     * @return the {@code sb} for easy chaining.
     *
     * @throws NullPointerException     if {@code sb} is null.
     * @throws IllegalArgumentException if {@code times <= 0}.
     */
    static StringBuilder appendTimes(final @Nonnull StringBuilder sb, final char c, final @Nonnegative int times) {

        checkNotNull(sb);
        checkArgument(times > 0);

        for (int counter : Ranges.closed(1, times).asSet(DiscreteDomains.integers())) {
            sb.append(c);
        }

        return sb;
    }

}
