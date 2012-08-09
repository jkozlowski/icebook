/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.order;

import com.google.common.base.Optional;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link Orders}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class OrdersTest {

    @Test
    public void testNewIcebergOrderInvalidParameters() {
        assertThat(Orders.newIcebergOrder(null, 1, 1, 1, 1), is(Optional.<Order>absent()));
        assertThat(Orders.newIcebergOrder(Side.BUY, 0, 1, 1, 1), is(Optional.<Order>absent()));
        assertThat(Orders.newIcebergOrder(Side.BUY, 1, 0, 1, 1), is(Optional.<Order>absent()));
        assertThat(Orders.newIcebergOrder(Side.BUY, 1, 1, 0, 1), is(Optional.<Order>absent()));
        assertThat(Orders.newIcebergOrder(Side.BUY, 1, 1, 1, 0), is(Optional.<Order>absent()));
    }

    @Test
    public void testNewLimitOrderInvalidParameters() {
        assertThat(Orders.newLimitOrder(null, 1, 1, 1), is(Optional.<Order>absent()));
        assertThat(Orders.newLimitOrder(Side.BUY, 0, 1, 1), is(Optional.<Order>absent()));
        assertThat(Orders.newLimitOrder(Side.BUY, 1, 0, 1), is(Optional.<Order>absent()));
        assertThat(Orders.newLimitOrder(Side.BUY, 1, 1, 0), is(Optional.<Order>absent()));
    }
}
