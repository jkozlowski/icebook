/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.formatter;

import icebook.order.Order;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.SortedSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
        final SortedSet<Order> orders = Mockito.mock(SortedSet.class);
        assertThat(Formatters.format(new StringBuilder(), orders, orders).toString(),
                   is("+-----------------------------------------------------------------+"));
    }
}
