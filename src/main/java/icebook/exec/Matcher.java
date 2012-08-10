/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.exec;

import com.google.common.base.Optional;
import icebook.book.OrderBook.Entry;
import icebook.order.IcebergOrder;
import icebook.order.LimitOrder;
import icebook.order.Order;
import icebook.order.Side;

import javax.annotation.Nonnull;

/**
 * Interface for matching {@link Order}s and {@link Entry}ies.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public interface Matcher {

    /**
     * Matches an {@link IcebergOrder} against this {@code entry}.
     *
     * @param order {@link IcebergOrder} to match.
     * @param entry {@link Entry} to match against.
     *
     * @return either a {@link Trade} if {@code code} and {@code entry} match, or {@link Optional#absent()}.
     *
     * @throws NullPointerException     if any argument is null.
     * @throws IllegalArgumentException if {@code order} and {@code entry} are not on the opposite side.
     */
    Optional<Trade> match(@Nonnull final IcebergOrder order, @Nonnull final Entry entry);

    /**
     * Matches a {@link LimitOrder} against this {@code entry}.
     *
     * @param order {@link LimitOrder} to match.
     * @param entry {@link Entry} to match against.
     *
     * @return either a {@link Trade} if {@code code} and {@code entry} match, or {@link Optional#absent()}.
     *
     * @throws NullPointerException     if any argument is null.
     * @throws IllegalArgumentException if {@code order} and {@code entry} have the same {@link Side}.
     */
    Optional<Trade> match(@Nonnull final LimitOrder order, @Nonnull final Entry entry);
}
