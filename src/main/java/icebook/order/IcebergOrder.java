/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.order;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.primitives.Longs;
import icebook.book.OrderBook.Entry;
import icebook.book.OrderBooks;
import icebook.exec.TimeSource;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Defines an iceberg order.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
final class IcebergOrder extends AbstractOrder {

    private final long peakSize;

    /**
     * Default constructor.
     *
     * @param side     {@link Side} of this order.
     * @param id       id of this order.
     * @param price    price of this order.
     * @param volume   current volume of this order.
     * @param peakSize size of the visible peak.
     *
     * @throws NullPointerException     if {@code side} is null.
     * @throws IllegalArgumentException if any {@code long} argument is {@code <= 0}, or {@code peakSize > volume}.
     */
    public IcebergOrder(@Nonnegative final long id,
                        @Nonnull final Side side,
                        @Nonnegative final long price,
                        @Nonnegative final long volume,
                        @Nonnegative final long peakSize) {
        super(id, side, price, volume);
        checkArgument(peakSize <= volume);
        this.peakSize = peakSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry getEntry(@Nonnull final TimeSource timeSource) {
        checkNotNull(timeSource);
        checkState(!isFilled(), "Cannot getEntry() of a filled order.");
        return OrderBooks.newEntry(getId(),
                                   timeSource.getTime(),
                                   getSide(), getPrice(),
                                   Longs.min(getVolume(), peakSize));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IcebergOrder that = (IcebergOrder) o;

        if (getId() != that.getId()) return false;
        if (peakSize != that.peakSize) return false;
        if (getPrice() != that.getPrice()) return false;
        if (getVolume() != that.getVolume()) return false;
        if (getSide() != that.getSide()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getSide(), getId(), getPrice(), getVolume(), peakSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final ToStringHelper toString = Objects.toStringHelper(this);
        toString.add("side", getSide());
        toString.add("id", getId());
        toString.add("price", getPrice());
        toString.add("volume", getVolume());
        toString.add("peakSize", peakSize);
        return toString.toString();
    }
}
