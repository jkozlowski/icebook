/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.output;

import com.google.common.collect.Lists;
import icebook.book.OrderBook.Entry;
import icebook.book.OrderBooks;
import icebook.exec.Trade;
import icebook.order.Side;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests {@link Appenders}
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class AppendersTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testAppendEntriesNullOut() throws IOException {
        Appenders.append(null, mock(SortedSet.class), mock(SortedSet.class));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testAppendEntriesNullSellOrders() throws IOException {
        Appenders.append(mock(Appendable.class), null, mock(SortedSet.class));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testAppendEntriesNullBuyOrders() throws IOException {
        Appenders.append(mock(Appendable.class), mock(SortedSet.class), null);
    }

    @Test
    public void testAppendEntriesMoreSells() throws IOException {
        final List<Entry> buys = Lists.newArrayList();
        buys.add(OrderBooks.newEntry(1234567890, 123, Side.BUY, 32503, 1234567890));
        buys.add(OrderBooks.newEntry(1138, 123, Side.BUY, 31502, 7500));

        final List<Entry> sells = Lists.newArrayList();
        sells.add(OrderBooks.newEntry(1234567891, 123, Side.SELL, 32504, 1234567890));
        sells.add(OrderBooks.newEntry(6808, 123, Side.SELL, 32505, 7777));
        sells.add(OrderBooks.newEntry(42100, 123, Side.SELL, 32507, 3000));

        final SortedSet<Entry> buyEntries = mock(SortedSet.class);
        when(buyEntries.iterator()).thenReturn(buys.iterator());

        final SortedSet<Entry> sellEntries = mock(SortedSet.class);
        when(sellEntries.iterator()).thenReturn(sells.iterator());

        final String expected
                = "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|1234567890|1,234,567,890| 32,503| 32,504|1,234,567,890|1234567891|\n"
                + "|      1138|        7,500| 31,502| 32,505|        7,777|      6808|\n"
                + "|          |             |       | 32,507|        3,000|     42100|\n"
                + "+-----------------------------------------------------------------+\n";
        assertThat(Appenders.append(new StringBuilder(), sellEntries, buyEntries).toString(), is(expected));
    }

    @Test
    public void testAppendEntriesMoreBuys() throws IOException {
        final List<Entry> buys = Lists.newArrayList();
        buys.add(OrderBooks.newEntry(1234567890, 123, Side.BUY, 32503, 1234567890));
        buys.add(OrderBooks.newEntry(1138, 123, Side.BUY, 31502, 7500));

        final List<Entry> sells = Lists.newArrayList();
        sells.add(OrderBooks.newEntry(1234567891, 123, Side.SELL, 32504, 1234567890));

        final SortedSet<Entry> buyEntries = mock(SortedSet.class);
        when(buyEntries.iterator()).thenReturn(buys.iterator());

        final SortedSet<Entry> sellEntries = mock(SortedSet.class);
        when(sellEntries.iterator()).thenReturn(sells.iterator());

        final String expected
                = "+-----------------------------------------------------------------+\n"
                + "| BUY                            | SELL                           |\n"
                + "| Id       | Volume      | Price | Price | Volume      | Id       |\n"
                + "+----------+-------------+-------+-------+-------------+----------+\n"
                + "|1234567890|1,234,567,890| 32,503| 32,504|1,234,567,890|1234567891|\n"
                + "|      1138|        7,500| 31,502|       |             |          |\n"
                + "+-----------------------------------------------------------------+\n";
        assertThat(Appenders.append(new StringBuilder(), sellEntries, buyEntries).toString(), is(expected));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testAppendTradesNullOut() {
        Appenders.append(null, Collections.EMPTY_LIST);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testAppendTradesNullTrades() {
        Appenders.append(mock(Appendable.class), null);
    }

    @Test
    public void testAppendEmptyTrades() {
        assertThat(Appenders.append(new StringBuilder(), Collections.EMPTY_LIST).toString().isEmpty(), is(true));
    }

    @Test
    public void testAppendTrades() {

        final List<Trade> trades = Lists.newArrayList();
        trades.add(new Trade(100322, 100345, 5103, 7500));
        trades.add(new Trade(100323, 100346, 5102, 2400));
        trades.add(new Trade(100324, 100347, 5103, 500));

        final String expected
                = "100322,100345,5103,7500\n"
                + "100323,100346,5102,2400\n"
                + "100324,100347,5103,500\n";

        assertThat(Appenders.append(new StringBuilder(), trades).toString(), is(expected));
    }
}
