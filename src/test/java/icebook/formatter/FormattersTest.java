/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.formatter;

import com.google.common.collect.Lists;
import icebook.order.Order;
import icebook.order.Orders;
import icebook.order.Side;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests {@link Formatters}
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class FormattersTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testAppendTimesNullSb() throws IOException {
        Formatters.appendTimes(null, '-', 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAppendTimesWrongTimes() throws IOException {
        Formatters.appendTimes(new StringBuilder(), '-', 0);
    }

    @Test
    public void testAppendTimes() throws IOException {
        assertThat(Formatters.appendTimes(new StringBuilder(), '.', 5).toString(), is("....."));
    }

    @Test
    public void testFormat() throws IOException {
        final List<Order> buyOrdersList = Lists.newArrayList();
        buyOrdersList.add(Orders.newLimitOrder(Side.BUY, 1234567890, 32503, 1234567890));
        buyOrdersList.add(Orders.newLimitOrder(Side.BUY, 1138, 31502, 7500));

        final List<Order> sellOrdersList = Lists.newArrayList();
        sellOrdersList.add(Orders.newLimitOrder(Side.SELL, 1234567891, 32504, 1234567890));
        sellOrdersList.add(Orders.newLimitOrder(Side.SELL, 6808, 32505, 7777));
        sellOrdersList.add(Orders.newLimitOrder(Side.SELL, 42100, 32507, 3000));

        final SortedSet<Order> buyOrders = mock(SortedSet.class);
        when(buyOrders.iterator()).thenReturn(buyOrdersList.iterator());

        final SortedSet<Order> sellOrders = mock(SortedSet.class);
        when(sellOrders.iterator()).thenReturn(sellOrdersList.iterator());

        final String expected
                = "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|1234567890|1,234,567,890| 32,503| 32,504|1,234,567,890|1234567891|\n"
                + "|      1138|        7,500| 31,502| 32,505|        7,777|      6808|\n"
                + "|          |             |       | 32,507|        3,000|     42100|\n"
                + "+-----------------------------------------------------------------+\n";
        assertThat(Formatters.format(new StringBuilder(), sellOrders, buyOrders).toString(), is(expected));
    }
}
