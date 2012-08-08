/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.order;

/**
 * Factory for {@link Order}s.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public final class Orders {

    /**
     * Factory class.
     */
    private Orders() {
    }

    /**
     *
     *
     * @return
     */
    public static Order newIcebergOrder(final Side side, final long id, final long price, final long quantity, final long peakSize) {
        return new IcebergOrder(side, id, price, quantity, peakSize);
    }

    /**
     *
     *
     * @return
     */
    public static Order newLimitOrder(final Side side, final long id, final long price, final long quantity) {
        return new LimitOrder(side, id, price, quantity);
    }
}
