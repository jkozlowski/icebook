/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import com.google.common.base.Optional;
import icebook.book.OrderBook.Entry;
import icebook.order.Side;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link DefaultOrderBook}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class DefaultOrderBookTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void testInsertNullEntry() {
        new DefaultOrderBook().insert(null);
    }

    @Test
    public void testConstructor() {
        final OrderBook book = OrderBooks.newDefaultOrderBook();
        assertThat(book.toSortedSet(Side.SELL).isEmpty(), is(true));
        assertThat(book.toSortedSet(Side.BUY).isEmpty(), is(true));
    }

    @Test
    public void testInsertSellEntries() {
        final OrderBook book = OrderBooks.newDefaultOrderBook();
        final Entry entry1 = OrderBooks.newEntry(1, 123, Side.SELL, 123, 123);
        final Entry entry2 = OrderBooks.newEntry(2, 124, Side.SELL, 124, 123);
        book.insert(entry1);
        book.insert(entry2);
        assertThat(book.toSortedSet(Side.SELL).toArray(new Entry[]{}), is(new Entry[]{entry1, entry2}));
    }

    @Test
    public void testInsertBuyEntries() {
        final OrderBook book = OrderBooks.newDefaultOrderBook();
        final Entry entry1 = OrderBooks.newEntry(1, 123, Side.BUY, 123, 123);
        final Entry entry2 = OrderBooks.newEntry(2, 124, Side.BUY, 124, 123);
        book.insert(entry1);
        book.insert(entry2);
        assertThat(book.toSortedSet(Side.BUY).toArray(new Entry[]{}), is(new Entry[]{entry2, entry1}));
    }

    @Test
    public void testPeekEmptyBook() {
        final OrderBook book = OrderBooks.newDefaultOrderBook();
        assertThat(book.peek(Side.SELL), is(Optional.<Entry>absent()));
        assertThat(book.peek(Side.BUY), is(Optional.<Entry>absent()));
    }

    @Test
    public void testPeekNonEmptyBook() {
        final OrderBook book = OrderBooks.newDefaultOrderBook();
        final Entry buy1 = OrderBooks.newEntry(1, 123, Side.BUY, 123, 123);
        final Entry buy2 = OrderBooks.newEntry(2, 124, Side.BUY, 124, 123);
        book.insert(buy1);
        book.insert(buy2);

        final Entry sell1 = OrderBooks.newEntry(1, 123, Side.SELL, 123, 123);
        final Entry sell2 = OrderBooks.newEntry(2, 124, Side.SELL, 124, 123);
        book.insert(sell1);
        book.insert(sell2);


        assertThat(book.peek(Side.BUY), is(Optional.of(buy2)));
        assertThat(book.peek(Side.SELL), is(Optional.of(sell1)));
    }

    @Test
    public void testIsEmpty() {
        final OrderBook book = OrderBooks.newDefaultOrderBook();
        assertThat(book.isEmpty(Side.BUY), is(true));
        assertThat(book.isEmpty(Side.SELL), is(true));

        book.insert(OrderBooks.newEntry(1, 123, Side.BUY, 123, 123));
        assertThat(book.isEmpty(Side.BUY), is(false));

        book.insert(OrderBooks.newEntry(1, 123, Side.SELL, 123, 123));
        assertThat(book.isEmpty(Side.SELL), is(false));
    }
}
