/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */

package icebook.book;

import com.google.common.base.Optional;
import icebook.book.OrderBook.Entry;
import icebook.order.Side;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests {@link DefaultOrderBook} and {@link OptimisedOrderBook}.
 *
 * @author Jakub D Kozlowski
 * @since 1.0
 */
public class OrderBooksTest {

    private static final String ORDERBOOKS_PROVIDER = "order-books-data-provider";

    @DataProvider(name = ORDERBOOKS_PROVIDER)
    public static OrderBook[][] getOrderBooks() {
        final OrderBook[][] orderBooks = new OrderBook[2][1];
        orderBooks[0][0] = OrderBooks.newDefaultOrderBook();
        orderBooks[1][0] = OrderBooks.newOptimisedOrderBook();
        return orderBooks;
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER)
    public void testConstructor(final OrderBook book) {
        assertThat(book.toImmutableSortedSet(Side.SELL).isEmpty(), is(true));
        assertThat(book.toImmutableSortedSet(Side.BUY).isEmpty(), is(true));
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER, expectedExceptions = NullPointerException.class)
    public void testInsertNullEntry(final OrderBook book) {
        book.insert(null);
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER, expectedExceptions = IllegalArgumentException.class)
    public void testInsertEntryNotOlder(final OrderBook book) {
        final Entry entry = OrderBooks.newEntry(1, 123, Side.SELL, 123, 123);
        final Entry entry2 = OrderBooks.newEntry(1, 123, Side.SELL, 123, 123);
        book.insert(entry);
        book.insert(entry2);
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER)
    public void testInsertSellEntries(final OrderBook book) {
        final Entry entry1 = OrderBooks.newEntry(1, 123, Side.SELL, 123, 123);
        final Entry entry2 = OrderBooks.newEntry(2, 124, Side.SELL, 124, 123);
        book.insert(entry1);
        book.insert(entry2);
        assertThat(book.toImmutableSortedSet(Side.SELL).toArray(new Entry[]{}), is(new Entry[]{entry1, entry2}));
        assertThat(book.peek(Side.SELL).get(), is(entry1));
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER)
    public void testInsertBuyEntries(final OrderBook book) {
        final Entry entry1 = OrderBooks.newEntry(1, 123, Side.BUY, 123, 123);
        final Entry entry2 = OrderBooks.newEntry(2, 124, Side.BUY, 124, 123);
        book.insert(entry1);
        book.insert(entry2);
        assertThat(book.toImmutableSortedSet(Side.BUY).toArray(new Entry[]{}), is(new Entry[]{entry2, entry1}));
        assertThat(book.peek(Side.BUY).get(), is(entry2));
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER)
    public void testPeekEmptyBook(final OrderBook book) {
        assertThat(book.peek(Side.SELL), is(Optional.<Entry>absent()));
        assertThat(book.peek(Side.BUY), is(Optional.<Entry>absent()));
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER)
    public void testPeekNonEmptyBook(final OrderBook book) {
        final Entry buy1 = OrderBooks.newEntry(1, 123, Side.BUY, 123, 123);
        final Entry buy2 = OrderBooks.newEntry(2, 124, Side.BUY, 124, 123);
        book.insert(buy1);
        book.insert(buy2);

        final Entry sell1 = OrderBooks.newEntry(1, 125, Side.SELL, 123, 123);
        final Entry sell2 = OrderBooks.newEntry(2, 126, Side.SELL, 124, 123);
        book.insert(sell1);
        book.insert(sell2);


        assertThat(book.peek(Side.BUY), is(Optional.of(buy2)));
        assertThat(book.peek(Side.SELL), is(Optional.of(sell1)));
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER, expectedExceptions = NullPointerException.class)
    public void testRemoveNullSide(final OrderBook book) {
        book.remove(null);
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER, expectedExceptions = NoSuchElementException.class)
    public void testRemoveEmptySide(final OrderBook book) {
        book.remove(Side.SELL);
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER)
    public void testRemoveSingleElementPresent(final OrderBook book) {
        final Entry sell = OrderBooks.newEntry(1, 125, Side.SELL, 123, 123);
        book.insert(sell);
        assertThat(book.remove(Side.SELL), is(sell));
        assertThat(book.isEmpty(Side.SELL), is(true));
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER)
    public void testRemoveTwoElementsPresent(final OrderBook book) {
        final Entry sell = OrderBooks.newEntry(1, 125, Side.SELL, 123, 123);
        final Entry sell2 = OrderBooks.newEntry(1, 126, Side.SELL, 124, 123);
        book.insert(sell);
        book.insert(sell2);
        assertThat(book.remove(Side.SELL), is(sell));
        assertThat(book.isEmpty(Side.SELL), is(false));
        assertThat(book.remove(Side.SELL), is(sell2));
        assertThat(book.isEmpty(Side.SELL), is(true));
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER, expectedExceptions = NullPointerException.class)
    public void testReplaceHeadNullHead(final OrderBook book) {
        book.replaceHead(null);
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER, expectedExceptions = NoSuchElementException.class)
    public void testReplaceHeadEmptySide(final OrderBook book) {
        book.replaceHead(OrderBooks.newEntry(1, 125, Side.SELL, 123, 123));
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER, expectedExceptions = IllegalArgumentException.class)
    public void testReplaceHeadNewHeadOlderThanOldHead(final OrderBook book) {
        final Entry sell = OrderBooks.newEntry(1, 125, Side.SELL, 123, 123);
        final Entry sell2 = OrderBooks.newEntry(1, 126, Side.SELL, 124, 123);
        book.insert(sell);
        book.insert(sell2);

        book.replaceHead(OrderBooks.newEntry(1, 126, Side.SELL, 123, 123));
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER)
    public void testReplaceHeadSameTimestamp(final OrderBook book) {
        final Entry sell = OrderBooks.newEntry(1, 125, Side.SELL, 123, 123);
        final Entry sell2 = OrderBooks.newEntry(1, 126, Side.SELL, 124, 123);
        book.insert(sell);
        book.insert(sell2);

        final Entry newHead = OrderBooks.newEntry(1, 125, Side.SELL, 123, 122);

        assertThat(book.replaceHead(newHead), is(sell));
        assertThat(book.peek(Side.SELL), is(Optional.of(newHead)));
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER)
    public void testReplaceHeadEarlierTimestamp(final OrderBook book) {
        final Entry sell = OrderBooks.newEntry(1, 125, Side.SELL, 123, 123);
        final Entry sell2 = OrderBooks.newEntry(1, 126, Side.SELL, 124, 123);
        book.insert(sell);
        book.insert(sell2);

        final Entry newHead = OrderBooks.newEntry(1, 124, Side.SELL, 123, 122);

        assertThat(book.replaceHead(newHead), is(sell));
        assertThat(book.peek(Side.SELL), is(Optional.of(newHead)));
    }

    @Test(dataProvider = ORDERBOOKS_PROVIDER)
    public void testIsEmpty(final OrderBook book) {
        assertThat(book.isEmpty(Side.BUY), is(true));
        assertThat(book.isEmpty(Side.SELL), is(true));

        book.insert(OrderBooks.newEntry(1, 123, Side.BUY, 123, 123));
        assertThat(book.isEmpty(Side.BUY), is(false));

        book.insert(OrderBooks.newEntry(2, 124, Side.SELL, 123, 123));
        assertThat(book.isEmpty(Side.SELL), is(false));
    }

}
